package de.suzufa.screwbox.core.utils;

import static de.suzufa.screwbox.core.utils.Resources.classPathElements;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public final class Reflections {

    private static final String SEPARATOR = FileSystems.getDefault().getSeparator();

    private Reflections() {
    }

    /**
     * Returns a list of all {@link Class}es in the given Package.
     */
    public static List<Class<?>> findClassesInPackage(final String packageName) {
        requireNonNull(packageName, "packageName must not be null");
        final List<Class<?>> clazzes = new ArrayList<>();
        final Pattern classNamePattern = Pattern.compile(".*" + packageName + ".*\\.class");
        for (final String resourceName : getResources(classNamePattern)) {
            final String[] splittedResource = resourceName.split(SEPARATOR);
            final String className = splittedResource[splittedResource.length - 1];
            String packagen = packageName + resourceName
                    .split(packageName.replace(".", SEPARATOR))[1]
                    .replace(SEPARATOR, ".")
                    .replace(className, "");
            packagen = packagen.substring(0, packagen.length() - 1);
            clazzes.add(getClass(className, packagen));
        }
        return clazzes;
    }

    private static Class<?> getClass(final String className, final String packageName) {
        try {
            final String name = packageName + "." + className.substring(0, className.lastIndexOf('.'));
            return Class.forName(name);
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("could not find classes", e);
        }
    }

    private static List<String> getResources(final Pattern pattern) {
        final List<String> matchingResources = new ArrayList<>();
        for (final String element : classPathElements()) {
            for (final var resource : getResources(element)) {
                if (pattern.matcher(resource).matches()) {
                    matchingResources.add(resource);
                }
            }
        }
        return matchingResources;
    }

    private static List<String> getResources(final String element) {
        final File file = new File(element);
        return file.isDirectory()
                ? getResourcesFromDirectory(file)
                : getResourcesFromJarFile(file);

    }

    private static List<String> getResourcesFromJarFile(final File file) {
        final ArrayList<String> retval = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(file)) {
            final var entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                retval.add(entries.nextElement().getName());
            }
            return retval;
        } catch (final IOException e) {
            throw new IllegalStateException("could not load resrouces from jar file", e);
        }
    }

    private static List<String> getResourcesFromDirectory(final File directory) {
        final List<String> resources = new ArrayList<>();
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                resources.addAll(getResourcesFromDirectory(file));
            } else {
                try {
                    resources.add(file.getCanonicalPath());
                } catch (final IOException e) {
                    throw new IllegalStateException("could not read resources from directory", e);
                }
            }
        }
        return resources;
    }
}