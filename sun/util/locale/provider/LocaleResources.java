package sun.util.locale.provider;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.calendar.ZoneInfo;
import sun.util.resources.LocaleData;
import sun.util.resources.OpenListResourceBundle;
import sun.util.resources.ParallelListResourceBundle;
import sun.util.resources.TimeZoneNamesBundle;

/**
 * Central accessor to locale-dependent resources for JRE/CLDR provider adapters.
 *
 * @author Masayoshi Okutsu
 * @author Naoto Sato
 */
public class LocaleResources {

    private final Locale locale;
    private final LocaleData localeData;
    private final LocaleProviderAdapter.Type type;

    // Resource cache
    private ConcurrentMap<String, ResourceReference> cache = new ConcurrentHashMap<>();
    private ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();

    // cache key prefixes
    private static final String BREAK_ITERATOR_INFO = "BII.";
    private static final String CALENDAR_DATA = "CALD.";
    private static final String COLLATION_DATA_CACHEKEY = "COLD";
    private static final String DECIMAL_FORMAT_SYMBOLS_DATA_CACHEKEY = "DFSD";
    private static final String CURRENCY_NAMES = "CN.";
    private static final String LOCALE_NAMES = "LN.";
    private static final String TIME_ZONE_NAMES = "TZN.";
    private static final String ZONE_IDS_CACHEKEY = "ZID";
    private static final String CALENDAR_NAMES = "CALN.";
    private static final String NUMBER_PATTERNS_CACHEKEY = "NP";
    private static final String DATE_TIME_PATTERN = "DTP.";

    // null singleton cache value
    private static final Object NULLOBJECT = new Object();

    LocaleResources(ResourceBundleBasedAdapter adapter, Locale locale) {
        this.locale = locale;
        this.localeData = adapter.getLocaleData();
        type = ((LocaleProviderAdapter)adapter).getAdapterType();
    }

    private void removeEmptyReferences() {
        Object ref;
        while ((ref = referenceQueue.poll()) != null) {
            cache.remove(((ResourceReference)ref).getCacheKey());
        }
    }

    Object getBreakIteratorInfo(String key) {
        Object biInfo;
        String cacheKey = BREAK_ITERATOR_INFO + key;

        removeEmptyReferences();
        ResourceReference data = cache.get(cacheKey);
        if (data == null || ((biInfo = data.get()) == null)) {
           biInfo = localeData.getBreakIteratorInfo(locale).getObject(key);
           cache.put(cacheKey, new ResourceReference(cacheKey, biInfo, referenceQueue));
       }

       return biInfo;
    }

    int getCalendarData(String key) {
        Integer caldata;
        String cacheKey = CALENDAR_DATA  + key;

        removeEmptyReferences();

        ResourceReference data = cache.get(cacheKey);
        if (data == null || ((caldata = (Integer) data.get()) == null)) {
            ResourceBundle rb = localeData.getCalendarData(locale);
            if (rb.containsKey(key)) {
                caldata = Integer.parseInt(rb.getString(key));
            } else {
                caldata = 0;
            }

            cache.put(cacheKey,
                      new ResourceReference(cacheKey, (Object) caldata, referenceQueue));
        }

        return caldata;
    }

    public String getCollationData() {
        String key = "Rule";
        String coldata = "";

        removeEmptyReferences();
        ResourceReference data = cache.get(COLLATION_DATA_CACHEKEY);
        if (data == null || ((coldata = (String) data.get()) == null)) {
            ResourceBundle rb = localeData.getCollationData(locale);
            if (rb.containsKey(key)) {
                coldata = rb.getString(key);
            }
            cache.put(COLLATION_DATA_CACHEKEY,
                      new ResourceReference(COLLATION_DATA_CACHEKEY, (Object) coldata, referenceQueue));
        }

        return coldata;
    }

    public Object[] getDecimalFormatSymbolsData() {
        Object[] dfsdata;

        removeEmptyReferences();
        ResourceReference data = cache.get(DECIMAL_FORMAT_SYMBOLS_DATA_CACHEKEY);
        if (data == null || ((dfsdata = (Object[]) data.get()) == null)) {
            // Note that only dfsdata[0] is prepared here in this method. Other
            // elements are provided by the caller, yet they are cached here.
            ResourceBundle rb = localeData.getNumberFormatData(locale);
            dfsdata = new Object[3];

            // NumberElements look up. First, try the Unicode extension
            String numElemKey;
            String numberType = locale.getUnicodeLocaleType("nu");
            if (numberType != null) {
                numElemKey = numberType + ".NumberElements";
                if (rb.containsKey(numElemKey)) {
                    dfsdata[0] = rb.getStringArray(numElemKey);
                }
            }

            // Next, try DefaultNumberingSystem value
            if (dfsdata[0] == null && rb.containsKey("DefaultNumberingSystem")) {
                numElemKey = rb.getString("DefaultNumberingSystem") + ".NumberElements";
                if (rb.containsKey(numElemKey)) {
                    dfsdata[0] = rb.getStringArray(numElemKey);
                }
            }

            // Last resort. No need to check the availability.
            // Just let it throw MissingResourceException when needed.
            if (dfsdata[0] == null) {
                dfsdata[0] = rb.getStringArray("NumberElements");
            }

            cache.put(DECIMAL_FORMAT_SYMBOLS_DATA_CACHEKEY,
                      new ResourceReference(DECIMAL_FORMAT_SYMBOLS_DATA_CACHEKEY, (Object) dfsdata, referenceQueue));
        }

        return dfsdata;
    }

    public String getCurrencyName(String key) {
        Object currencyName = null;
        String cacheKey = CURRENCY_NAMES + key;

        removeEmptyReferences();
        ResourceReference data = cache.get(cacheKey);

        if (data != null && ((currencyName = data.get()) != null)) {
            if (currencyName.equals(NULLOBJECT)) {
                currencyName = null;
            }

            return (String) currencyName;
        }

        OpenListResourceBundle olrb = localeData.getCurrencyNames(locale);

        if (olrb.containsKey(key)) {
            currencyName = olrb.getObject(key);
            cache.put(cacheKey,
                      new ResourceReference(cacheKey, currencyName, referenceQueue));
        }

        return (String) currencyName;
    }

    public String getLocaleName(String key) {
        Object localeName = null;
        String cacheKey = LOCALE_NAMES + key;

        removeEmptyReferences();
        ResourceReference data = cache.get(cacheKey);

        if (data != null && ((localeName = data.get()) != null)) {
            if (localeName.equals(NULLOBJECT)) {
                localeName = null;
            }

            return (String) localeName;
        }

        OpenListResourceBundle olrb = localeData.getLocaleNames(locale);

        if (olrb.containsKey(key)) {
            localeName = olrb.getObject(key);
            cache.put(cacheKey,
                      new ResourceReference(cacheKey, localeName, referenceQueue));
        }

        return (String) localeName;
    }

    String[] getTimeZoneNames(String key) {
        String[] names = null;
        String cacheKey = TIME_ZONE_NAMES + '.' + key;

        removeEmptyReferences();
        ResourceReference data = cache.get(cacheKey);

        if (Objects.isNull(data) || Objects.isNull((names = (String[]) data.get()))) {
            TimeZoneNamesBundle tznb = localeData.getTimeZoneNames(locale);
            if (tznb.containsKey(key)) {
                names = tznb.getStringArray(key);
                cache.put(cacheKey,
                          new ResourceReference(cacheKey, (Object) names, referenceQueue));
            }
        }

        return names;
    }

    @SuppressWarnings("unchecked")
    Set<String> getZoneIDs() {
        Set<String> zoneIDs = null;

        removeEmptyReferences();
        ResourceReference data = cache.get(ZONE_IDS_CACHEKEY);
        if (data == null || ((zoneIDs = (Set<String>) data.get()) == null)) {
            TimeZoneNamesBundle rb = localeData.getTimeZoneNames(locale);
            zoneIDs = rb.keySet();
            cache.put(ZONE_IDS_CACHEKEY,
                      new ResourceReference(ZONE_IDS_CACHEKEY, (Object) zoneIDs, referenceQueue));
        }

        return zoneIDs;
    }

    // zoneStrings are cached separately in TimeZoneNameUtility.
    String[][] getZoneStrings() {
        TimeZoneNamesBundle rb = localeData.getTimeZoneNames(locale);
        Set<String> keyset = getZoneIDs();
        // Use a LinkedHashSet to preseve the order
        Set<String[]> value = new LinkedHashSet<>();
        for (String key : keyset) {
            value.add(rb.getStringArray(key));
        }

        // Add aliases data for CLDR
        if (type == LocaleProviderAdapter.Type.CLDR) {
            // Note: TimeZoneNamesBundle creates a String[] on each getStringArray call.
            Map<String, String> aliases = ZoneInfo.getAliasTable();
            for (String alias : aliases.keySet()) {
                if (!keyset.contains(alias)) {
                    String tzid = aliases.get(alias);
                    if (keyset.contains(tzid)) {
                        String[] val = rb.getStringArray(tzid);
                        val[0] = alias;
                        value.add(val);
                    }
                }
            }
        }
        return value.toArray(new String[0][]);
    }

    String[] getCalendarNames(String key) {
        String[] names = null;
        String cacheKey = CALENDAR_NAMES + key;

        removeEmptyReferences();
        ResourceReference data = cache.get(cacheKey);

        if (data == null || ((names = (String[]) data.get()) == null)) {
            ResourceBundle rb = localeData.getDateFormatData(locale);
            if (rb.containsKey(key)) {
                names = rb.getStringArray(key);
                cache.put(cacheKey,
                          new ResourceReference(cacheKey, (Object) names, referenceQueue));
            }
        }

        return names;
    }

    String[] getJavaTimeNames(String key) {
        String[] names = null;
        String cacheKey = CALENDAR_NAMES + key;

        removeEmptyReferences();
        ResourceReference data = cache.get(cacheKey);

        if (data == null || ((names = (String[]) data.get()) == null)) {
            ResourceBundle rb = getJavaTimeFormatData();
            if (rb.containsKey(key)) {
                names = rb.getStringArray(key);
                cache.put(cacheKey,
                          new ResourceReference(cacheKey, (Object) names, referenceQueue));
            }
        }

        return names;
    }

    public String getDateTimePattern(int timeStyle, int dateStyle, Calendar cal) {
        if (cal == null) {
            cal = Calendar.getInstance(locale);
        }
        return getDateTimePattern(null, timeStyle, dateStyle, cal.getCalendarType());
    }

    /**
     * Returns a date-time format pattern
     * @param timeStyle style of time; one of FULL, LONG, MEDIUM, SHORT in DateFormat,
     *                  or -1 if not required
     * @param dateStyle style of time; one of FULL, LONG, MEDIUM, SHORT in DateFormat,
     *                  or -1 if not required
     * @param calType   the calendar type for the pattern
     * @return the pattern string
     */
    public String getJavaTimeDateTimePattern(int timeStyle, int dateStyle, String calType) {
        calType = CalendarDataUtility.normalizeCalendarType(calType);
        String pattern;
        pattern = getDateTimePattern("java.time.", timeStyle, dateStyle, calType);
        if (pattern == null) {
            pattern = getDateTimePattern(null, timeStyle, dateStyle, calType);
        }
        return pattern;
    }

    private String getDateTimePattern(String prefix, int timeStyle, int dateStyle, String calType) {
        String pattern;
        String timePattern = null;
        String datePattern = null;

        if (timeStyle >= 0) {
            if (prefix != null) {
                timePattern = getDateTimePattern(prefix, "TimePatterns", timeStyle, calType);
            }
            if (timePattern == null) {
                timePattern = getDateTimePattern(null, "TimePatterns", timeStyle, calType);
            }
        }
        if (dateStyle >= 0) {
            if (prefix != null) {
                datePattern = getDateTimePattern(prefix, "DatePatterns", dateStyle, calType);
            }
            if (datePattern == null) {
                datePattern = getDateTimePattern(null, "DatePatterns", dateStyle, calType);
            }
        }
        if (timeStyle >= 0) {
            if (dateStyle >= 0) {
                String dateTimePattern = null;
                if (prefix != null) {
                    dateTimePattern = getDateTimePattern(prefix, "DateTimePatterns", 0, calType);
                }
                if (dateTimePattern == null) {
                    dateTimePattern = getDateTimePattern(null, "DateTimePatterns", 0, calType);
                }
                switch (dateTimePattern) {
                case "{1} {0}":
                    pattern = datePattern + " " + timePattern;
                    break;
                case "{0} {1}":
                    pattern = timePattern + " " + datePattern;
                    break;
                default:
                    pattern = MessageFormat.format(dateTimePattern, timePattern, datePattern);
                    break;
                }
            } else {
                pattern = timePattern;
            }
        } else if (dateStyle >= 0) {
            pattern = datePattern;
        } else {
            throw new IllegalArgumentException("No date or time style specified");
        }
        return pattern;
    }

    public String[] getNumberPatterns() {
        String[] numberPatterns = null;

        removeEmptyReferences();
        ResourceReference data = cache.get(NUMBER_PATTERNS_CACHEKEY);

        if (data == null || ((numberPatterns = (String[]) data.get()) == null)) {
            ResourceBundle resource = localeData.getNumberFormatData(locale);
            numberPatterns = resource.getStringArray("NumberPatterns");
            cache.put(NUMBER_PATTERNS_CACHEKEY,
                      new ResourceReference(NUMBER_PATTERNS_CACHEKEY, (Object) numberPatterns, referenceQueue));
        }

        return numberPatterns;
    }

    /**
     * Returns the FormatData resource bundle of this LocaleResources.
     * The FormatData should be used only for accessing extra
     * resources required by JSR 310.
     */
    public ResourceBundle getJavaTimeFormatData() {
        ResourceBundle rb = localeData.getDateFormatData(locale);
        if (rb instanceof ParallelListResourceBundle) {
            localeData.setSupplementary((ParallelListResourceBundle) rb);
        }
        return rb;
    }

    private String getDateTimePattern(String prefix, String key, int styleIndex, String calendarType) {
        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        if (!"gregory".equals(calendarType)) {
            sb.append(calendarType).append('.');
        }
        sb.append(key);
        String resourceKey = sb.toString();
        String cacheKey = sb.insert(0, DATE_TIME_PATTERN).toString();

        removeEmptyReferences();
        ResourceReference data = cache.get(cacheKey);
        Object value = NULLOBJECT;

        if (data == null || ((value = data.get()) == null)) {
            ResourceBundle r = (prefix != null) ? getJavaTimeFormatData() : localeData.getDateFormatData(locale);
            if (r.containsKey(resourceKey)) {
                value = r.getStringArray(resourceKey);
            } else {
                assert !resourceKey.equals(key);
                if (r.containsKey(key)) {
                    value = r.getStringArray(key);
                }
            }
            cache.put(cacheKey,
                      new ResourceReference(cacheKey, value, referenceQueue));
        }
        if (value == NULLOBJECT) {
            assert prefix != null;
            return null;
        }
        return ((String[])value)[styleIndex];
    }

    private static class ResourceReference extends SoftReference<Object> {
        private final String cacheKey;

        ResourceReference(String cacheKey, Object o, ReferenceQueue<Object> q) {
            super(o, q);
            this.cacheKey = cacheKey;
        }

        String getCacheKey() {
            return cacheKey;
        }
    }
}