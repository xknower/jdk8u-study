package jdk.jfr.internal.tool;

import java.io.PrintWriter;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import jdk.jfr.AnnotationElement;
import jdk.jfr.DataAmount;
import jdk.jfr.Frequency;
import jdk.jfr.MemoryAddress;
import jdk.jfr.Percentage;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedClass;
import jdk.jfr.consumer.RecordedClassLoader;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedFrame;
import jdk.jfr.consumer.RecordedMethod;
import jdk.jfr.consumer.RecordedObject;
import jdk.jfr.consumer.RecordedStackTrace;
import jdk.jfr.consumer.RecordedThread;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

/**
 * Print events in a human-readable format.
 *
 * This class is also used by {@link RecordedObject#toString()}
 */
public final class PrettyWriter extends EventPrintWriter {
    private static final String TYPE_OLD_OBJECT = Type.TYPES_PREFIX + "OldObject";
    private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private final static Long ZERO = 0L;
    private boolean showIds;
    private RecordedEvent currentEvent;

    public PrettyWriter(PrintWriter destination) {
        super(destination);
    }

    @Override
    protected void print(List<RecordedEvent> events) {
        for (RecordedEvent e : events) {
            print(e);
            flush(false);
        }
    }

    public void printType(Type t) {
        if (showIds) {
            print("// id: ");
            println(String.valueOf(t.getId()));
        }
        int commentIndex = t.getName().length() + 10;
        String typeName = t.getName();
        int index = typeName.lastIndexOf(".");
        if (index != -1) {
            println("@Name(\"" + typeName + "\")");
        }
        printAnnotations(commentIndex, t.getAnnotationElements());
        print("class " + typeName.substring(index + 1));
        String superType = t.getSuperType();
        if (superType != null) {
            print(" extends " + superType);
        }
        println(" {");
        indent();
        boolean first = true;
        for (ValueDescriptor v : t.getFields()) {
            printField(commentIndex, v, first);
            first = false;
        }
        retract();
        println("}");
        println();
    }

    private void printField(int commentIndex, ValueDescriptor v, boolean first) {
        if (!first) {
            println();
        }
        printAnnotations(commentIndex, v.getAnnotationElements());
        printIndent();
        Type vType = PrivateAccess.getInstance().getType(v);
        if (Type.SUPER_TYPE_SETTING.equals(vType.getSuperType())) {
            print("static ");
        }
        print(makeSimpleType(v.getTypeName()));
        if (v.isArray()) {
            print("[]");
        }
        print(" ");
        print(v.getName());
        print(";");
        printCommentRef(commentIndex, v.getTypeId());
    }

    private void printCommentRef(int commentIndex, long typeId) {
        if (showIds) {
            int column = getColumn();
            if (column > commentIndex) {
                print("  ");
            } else {
                while (column < commentIndex) {
                    print(" ");
                    column++;
                }
            }
            println(" // id=" + typeId);
        } else {
            println();
        }
    }

    private void printAnnotations(int commentIndex, List<AnnotationElement> annotations) {
        for (AnnotationElement a : annotations) {
            printIndent();
            print("@");
            print(makeSimpleType(a.getTypeName()));
            List<ValueDescriptor> vs = a.getValueDescriptors();
            if (!vs.isEmpty()) {
                printAnnotation(a);
                printCommentRef(commentIndex, a.getTypeId());
            } else {
                println();
            }
        }
    }

    private void printAnnotation(AnnotationElement a) {
        StringJoiner sj = new StringJoiner(", ", "(", ")");
        List<ValueDescriptor> vs = a.getValueDescriptors();
        for (ValueDescriptor v : vs) {
            Object o = a.getValue(v.getName());
            if (vs.size() == 1 && v.getName().equals("value")) {
                sj.add(textify(o));
            } else {
                sj.add(v.getName() + "=" + textify(o));
            }
        }
        print(sj.toString());
    }

    private String textify(Object o) {
        if (o.getClass().isArray()) {
            Object[] array = (Object[]) o;
            if (array.length == 1) {
                return quoteIfNeeded(array[0]);
            }
            StringJoiner s = new StringJoiner(", ", "{", "}");
            for (Object ob : array) {
                s.add(quoteIfNeeded(ob));
            }
            return s.toString();
        } else {
            return quoteIfNeeded(o);
        }
    }

    private String quoteIfNeeded(Object o) {
        if (o instanceof String) {
            return "\"" + o + "\"";
        } else {
            return String.valueOf(o);
        }
    }

    private String makeSimpleType(String typeName) {
        int index = typeName.lastIndexOf(".");
        return typeName.substring(index + 1);
    }

    public void print(RecordedEvent event) {
        currentEvent = event;
        print(event.getEventType().getName(), " ");
        println("{");
        indent();
        for (ValueDescriptor v : event.getFields()) {
            String name = v.getName();
            if (!isZeroDuration(event, name) && !isLateField(name)) {
                printFieldValue(event, v);
            }
        }
        if (event.getThread() != null) {
            printIndent();
            print(EVENT_THREAD_FIELD + " = ");
            printThread(event.getThread(), "");
        }
        if (event.getStackTrace() != null) {
            printIndent();
            print(STACK_TRACE_FIELD + " = ");
            printStackTrace(event.getStackTrace());
        }
        retract();
        printIndent();
        println("}");
        println();
    }

    private boolean isZeroDuration(RecordedEvent event, String name) {
        return name.equals("duration") && ZERO.equals(event.getValue("duration"));
    }

    private void printStackTrace(RecordedStackTrace stackTrace) {
        println("[");
        List<RecordedFrame> frames = stackTrace.getFrames();
        indent();
        int i = 0;
        while (i < frames.size() && i < getStackDepth()) {
            RecordedFrame frame = frames.get(i);
            if (frame.isJavaFrame()) {
                printIndent();
                printValue(frame, null, "");
                println();
                i++;
            }
        }
        if (stackTrace.isTruncated() || i == getStackDepth()) {
            printIndent();
            println("...");
        }
        retract();
        printIndent();
        println("]");
    }

    public void print(RecordedObject struct, String postFix) {
        println("{");
        indent();
        for (ValueDescriptor v : struct.getFields()) {
            printFieldValue(struct, v);
        }
        retract();
        printIndent();
        println("}" + postFix);
    }

    private void printFieldValue(RecordedObject struct, ValueDescriptor v) {
        printIndent();
        print(v.getName(), " = ");
        printValue(getValue(struct, v), v, "");
    }

    private void printArray(Object[] array) {
        println("[");
        indent();
        for (int i = 0; i < array.length; i++) {
            printIndent();
            printValue(array[i], null, i + 1 < array.length ? ", " : "");
        }
        retract();
        printIndent();
        println("]");
    }

    private void printValue(Object value, ValueDescriptor field, String postFix) {
        if (value == null) {
            println("N/A" + postFix);
            return;
        }
        if (value instanceof RecordedObject) {
            if (value instanceof RecordedThread) {
                printThread((RecordedThread) value, postFix);
                return;
            }
            if (value instanceof RecordedClass) {
                printClass((RecordedClass) value, postFix);
                return;
            }
            if (value instanceof RecordedClassLoader) {
                printClassLoader((RecordedClassLoader) value, postFix);
                return;
            }
            if (value instanceof RecordedFrame) {
                RecordedFrame frame = (RecordedFrame) value;
                if (frame.isJavaFrame()) {
                    printJavaFrame((RecordedFrame) value, postFix);
                    return;
                }
            }
            if (value instanceof RecordedMethod) {
                println(formatMethod((RecordedMethod) value));
                return;
            }
            if (field.getTypeName().equals(TYPE_OLD_OBJECT)) {
                printOldObject((RecordedObject) value);
                return;
            }
             print((RecordedObject) value, postFix);
            return;
        }
        if (value.getClass().isArray()) {
            printArray((Object[]) value);
            return;
        }

        if (value instanceof Double) {
            Double d = (Double) value;
            if (Double.isNaN(d) || d == Double.NEGATIVE_INFINITY) {
                println("N/A");
                return;
            }
        }
        if (value instanceof Float) {
            Float f = (Float) value;
            if (Float.isNaN(f) || f == Float.NEGATIVE_INFINITY) {
                println("N/A");
                return;
            }
        }
        if (value instanceof Long) {
            Long l = (Long) value;
            if (l == Long.MIN_VALUE) {
                println("N/A");
                return;
            }
        }
        if (value instanceof Integer) {
            Integer i = (Integer) value;
            if (i == Integer.MIN_VALUE) {
                println("N/A");
                return;
            }
        }

        if (field.getContentType() != null) {
            if (printFormatted(field, value)) {
                return;
            }
        }

        String text = String.valueOf(value);
        if (value instanceof String) {
            text = "\"" + text + "\"";
        }
        println(text);
    }

    private void printOldObject(RecordedObject object) {
        println(" [");
        indent();
        printIndent();
        try {
            printReferenceChain(object);
        } catch (IllegalArgumentException iae) {
           // Could not find a field
           // Not possible to validate fields beforehand using RecordedObject#hasField
           // since nested objects, for example object.referrer.array.index, requires
           // an actual array object (which may be null).
        }
        retract();
        printIndent();
        println("]");
    }

    private void printReferenceChain(RecordedObject object) {
        printObject(object, currentEvent.getLong("arrayElements"));
        for (RecordedObject ref = object.getValue("referrer"); ref != null; ref = object.getValue("referrer")) {
            long skip = ref.getLong("skip");
            if (skip > 0) {
                printIndent();
                println("...");
            }
            String objectHolder = "";
            long size = Long.MIN_VALUE;
            RecordedObject array = ref.getValue("array");
            if (array != null) {
                long index = array.getLong("index");
                size = array.getLong("size");
                objectHolder = "[" + index + "]";
            }
            RecordedObject field = ref.getValue("field");
            if (field != null) {
                objectHolder = field.getString("name");
            }
            printIndent();
            print(objectHolder);
            print(" : ");
            object = ref.getValue("object");
            if (object != null) {
                printObject(object, size);
            }
        }
    }

    void printObject(RecordedObject object, long arraySize) {
        RecordedClass clazz = object.getClass("type");
        if (clazz != null) {
            String className = clazz.getName();
            if (className!= null && className.startsWith("[")) {
                className = decodeDescriptors(className, arraySize > 0 ? Long.toString(arraySize) : "").get(0);
            }
            print(className);
            String description = object.getString("description");
            if (description != null) {
                print(" ");
                print(description);
            }
        }
        println();
    }

    private void printClassLoader(RecordedClassLoader cl, String postFix) {
        // Purposely not printing class loader name to avoid cluttered output
        RecordedClass clazz = cl.getType();
        print(clazz == null ? "null" : clazz.getName());
        if (clazz != null) {
            print(" (");
            print("id = ");
            print(String.valueOf(cl.getId()));
            println(")");
        }
    }

    private void printJavaFrame(RecordedFrame f, String postFix) {
        print(formatMethod(f.getMethod()));
        int line = f.getLineNumber();
        if (line >= 0) {
            print(" line: " + line);
        }
        print(postFix);
    }

    private String formatMethod(RecordedMethod m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getType().getName());
        sb.append(".");
        sb.append(m.getName());
        sb.append("(");
        StringJoiner sj = new StringJoiner(", ");
        String md = m.getDescriptor().replace("/", ".");
        String parameter = md.substring(1, md.lastIndexOf(")"));
        for (String qualifiedName : decodeDescriptors(parameter, "")) {
            String typeName = qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
            sj.add(typeName);
        }
        sb.append(sj);
        sb.append(")");
        return sb.toString();
    }

    private void printClass(RecordedClass clazz, String postFix) {
        RecordedClassLoader classLoader = clazz.getClassLoader();
        String classLoaderName = "null";
        if (classLoader != null) {
            if (classLoader.getName() != null) {
                classLoaderName = classLoader.getName();
            } else {
                classLoaderName = classLoader.getType().getName();
            }
        }
        String className = clazz.getName();
        if (className.startsWith("[")) {
            className = decodeDescriptors(className, "").get(0);
        }
        println(className + " (classLoader = " + classLoaderName + ")" + postFix);
    }

    List<String> decodeDescriptors(String descriptor, String arraySize) {
        List<String> descriptors = new ArrayList<>();
        for (int index = 0; index < descriptor.length(); index++) {
            String arrayBrackets = "";
            while (descriptor.charAt(index) == '[') {
                arrayBrackets = arrayBrackets +  "[" + arraySize + "]" ;
                arraySize = "";
                index++;
            }
            char c = descriptor.charAt(index);
            String type;
            switch (c) {
            case 'L':
                int endIndex = descriptor.indexOf(';', index);
                type = descriptor.substring(index + 1, endIndex);
                index = endIndex;
                break;
            case 'I':
                type = "int";
                break;
            case 'J':
                type = "long";
                break;
            case 'Z':
                type = "boolean";
                break;
            case 'D':
                type = "double";
                break;
            case 'F':
                type = "float";
                break;
            case 'S':
                type = "short";
                break;
            case 'C':
                type = "char";
                break;
            case 'B':
                type = "byte";
                break;
            default:
                type = "<unknown-descriptor-type>";
            }
            descriptors.add(type + arrayBrackets);
        }
        return descriptors;
    }

    private void printThread(RecordedThread thread, String postFix) {
        long javaThreadId = thread.getJavaThreadId();
        if (javaThreadId > 0) {
            println("\"" + thread.getJavaName() + "\" (javaThreadId = " + thread.getJavaThreadId() + ")" + postFix);
        } else {
            println("\"" + thread.getOSName() + "\" (osThreadId = " + thread.getOSThreadId() + ")" + postFix);
        }
    }

    private boolean printFormatted(ValueDescriptor field, Object value) {
        if (value instanceof Duration) {
            Duration d = (Duration) value;
            if (d.getSeconds() == Long.MIN_VALUE && d.getNano() == 0)  {
                println("N/A");
                return true;
            }
            double s = d.getNano() / 1000_000_000.0 + (int) (d.getSeconds() % 60);
            if (s < 1.0) {
                if (s < 0.001) {
                    println(String.format("%.3f", s * 1_000_000) + " us");
                } else {
                    println(String.format("%.3f", s * 1_000) + " ms");
                }
            } else {
                if (s < 1000.0) {
                    println(String.format("%.3f", s) + " s");
                } else {
                    println(String.format("%.0f", s) + " s");
                }
            }
            return true;
        }
        if (value instanceof OffsetDateTime) {
            OffsetDateTime odt = (OffsetDateTime) value;
            if (odt.equals(OffsetDateTime.MIN))  {
                println("N/A");
                return true;
            }
            println(TIME_FORMAT.format(odt));
            return true;
        }
        Percentage percentage = field.getAnnotation(Percentage.class);
        if (percentage != null) {
            if (value instanceof Number) {
                double d = ((Number) value).doubleValue();
                println(String.format("%.2f", d * 100) + "%");
                return true;
            }
        }
        DataAmount dataAmount = field.getAnnotation(DataAmount.class);
        if (dataAmount != null) {
            if (value instanceof Number) {
                Number n = (Number) value;
                long amount = n.longValue();
                if (field.getAnnotation(Frequency.class) != null) {
                    if (dataAmount.value().equals(DataAmount.BYTES)) {
                        println(Utils.formatBytesPerSecond(amount));
                        return true;
                    }
                    if (dataAmount.value().equals(DataAmount.BITS)) {
                        println(Utils.formatBitsPerSecond(amount));
                        return true;
                    }
                } else {
                    if (dataAmount.value().equals(DataAmount.BYTES)) {
                        println(Utils.formatBytes(amount));
                        return true;
                    }
                    if (dataAmount.value().equals(DataAmount.BITS)) {
                        println(Utils.formatBits(amount));
                        return true;
                    }
                }
            }
        }
        MemoryAddress memoryAddress = field.getAnnotation(MemoryAddress.class);
        if (memoryAddress != null) {
            if (value instanceof Number) {
                long d = ((Number) value).longValue();
                println(String.format("0x%08X", d));
                return true;
            }
        }
        Frequency frequency = field.getAnnotation(Frequency.class);
        if (frequency != null) {
            if (value instanceof Number) {
                println(value + " Hz");
                return true;
            }
        }

        return false;
    }

    public void setShowIds(boolean showIds) {
        this.showIds = showIds;
    }
}
