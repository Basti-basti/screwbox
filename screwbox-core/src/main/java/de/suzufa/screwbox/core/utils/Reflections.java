package de.suzufa.screwbox.core.utils;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Reflections {

    private static final String CLASSPATH = System.getProperty("java.class.path", ".");

    private Reflections() {
    }

    /**
     * Returns a list of all {@link Class}es in the given Package.
     */
    public static List<Class<?>> findClassesInPackage(String packageName) {
        requireNonNull(packageName, "packageName must not be null");
        List<Class<?>> clazzes = new ArrayList<>();
        Pattern classNamePattern = Pattern.compile(".*" + packageName + ".*\\.class");
        for (String resourceName : getResources(classNamePattern)) {
            String className = resourceName.split("/")[resourceName.split("/").length - 1];
            String packagen = packageName
                    + resourceName.split(packageName.replace(".", "/"))[1].replace("/", ".").replace(
                            className, "");
            packagen = packagen.substring(0, packagen.length() - 1);
            clazzes.add(getClass(className, packagen));
        }
        return clazzes;
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            String name = packageName + "." + className.substring(0, className.lastIndexOf('.'));
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("could not find classes", e);
        }
    }

    private static Collection<String> getResources(final Pattern pattern) {
        final ArrayList<String> retval = new ArrayList<>();
        final String[] classPathElements = CLASSPATH.split(System.getProperty("path.separator"));
        for (final String element : classPathElements) {
            retval.addAll(getResources(element, pattern));
        }
        return retval;
    }

    private static Collection<String> getResources(final String element, final Pattern pattern) {
        final File file = new File(element);
        return file.isDirectory()
                ? getResourcesFromDirectory(file, pattern)
                : getResourcesFromJarFile(file, pattern);
    }

    private static Collection<String> getResourcesFromJarFile(final File file, final Pattern pattern) {
        final ArrayList<String> retval = new ArrayList<>();
        try (ZipFile zf = getZipFile(file)) {
            final var entries = zf.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry ze = entries.nextElement();
                final String fileName = ze.getName();
                if (pattern.matcher(fileName).matches()) {
                    retval.add(fileName);
                }
            }
            return retval;
        } catch (final IOException e) {
            throw new IllegalStateException("could not load resrouces from jar file", e);
        }
    }

    private static ZipFile getZipFile(final File file) {
        try {
            return new ZipFile(file);
        } catch (final IOException e) {
            throw new IllegalStateException("could not open zip file", e);
        }
    }

    private static Collection<String> getResourcesFromDirectory(final File directory, final Pattern pattern) {
        final List<String> retval = new ArrayList<>();
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                retval.addAll(getResourcesFromDirectory(file, pattern));
            } else {
                try {
                    final String fileName = file.getCanonicalPath();
                    if (pattern.matcher(fileName).matches()) {
                        retval.add(fileName);
                    }
                } catch (final IOException e) {
                    throw new IllegalStateException("could not read resources from directory", e);
                }
            }
        }
        return retval;
    }
}