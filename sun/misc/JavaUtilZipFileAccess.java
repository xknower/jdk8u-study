package sun.misc;

import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public interface JavaUtilZipFileAccess {
    public boolean startsWithLocHeader(ZipFile zip);
    public int getManifestNum(JarFile zip);
}

