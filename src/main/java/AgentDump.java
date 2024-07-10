import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.Comparator;

public class AgentDump {

    public static void register(Instrumentation inst) {
        final Path dumpFolder = getDumpFolder();
        if (dumpFolder == null) {
            return;
        }

        System.err.println("dump bytecode in " + dumpFolder);

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader cl,
                                    String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {

                // writes class bytecode in .class file
                dumpClass(dumpFolder, cl, className, classfileBuffer);

                return null; // do not modify any class
            }
        });
    }

    private static void dumpClass(Path folder, ClassLoader cl, String className, byte[] bytecode) {
        String clClassName = cl.getClass().getName();
        if (clClassName.endsWith("DelegatingClassLoader")) {
            // ignore internal classloader
            return;
        }
        Path clFolderPath = folder.resolve(normalizePath(cl.toString()));
        try {
            Files.createDirectories(clFolderPath);
            Path filePath = clFolderPath.resolve(normalizePath(className) + ".class");
            Files.write(filePath, bytecode);
        } catch (IOException e) {
            throw new IllegalStateException("unable to dump class file");
        }
    }

    private static String normalizePath(String s) {
        return s.replaceAll("/", "_")
                .replaceAll("\\$", "_")
                .replaceAll("@", "_");
    }

    private static Path getDumpFolder() {
        String dumpPathConfig = System.getProperty("smith.dump_path");
        if (dumpPathConfig == null) {
            return null;
        }

        Path dumpPath = Paths.get(dumpPathConfig);
        if (Files.exists(dumpPath)) {
            // recursively delete
            try {
                Files.walk(dumpPath).sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // silently ignored
                    }
                });
            } catch (IOException e) {
                throw new IllegalStateException("unable to delete temp folder");
            }
        }
        return dumpPath;
    }
}
