import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class ShatteredPixelCheatASM {

    private static final int ASM_VERSION = Opcodes.ASM9;
    private static final int PARSING_OPTIONS = 0;
    private static final int PARSING_FLAGS = 0;

    public static void register(Instrumentation inst) {

        String cheatOption = System.getProperty("smith.cheat");
        if (!Boolean.parseBoolean(cheatOption)) {
            return;
        }

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                if ("com/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero".equals(className)) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    ClassWriter cw = new ClassWriter(cr, PARSING_FLAGS);
                    cr.accept(new CheatClassVisitor(cw), PARSING_OPTIONS);
                    return cw.toByteArray();
                } else {
                    return null;
                }
            }
        });
    }

    private static class CheatClassVisitor extends ClassVisitor {

        protected CheatClassVisitor(ClassVisitor cv) {
            super(ASM_VERSION, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return ("damage".equals(name)) ? new CheatMethodVisitor(mv) : mv;
        }
    }

    private static class CheatMethodVisitor extends MethodVisitor {

        protected CheatMethodVisitor(MethodVisitor mv) {
            super(ASM_VERSION, mv);
        }

        @Override
        public void visitCode() {
            mv.visitCode();

            // set damage parameter to zero
            mv.visitLdcInsn(0); // iconst_0
            mv.visitVarInsn(Opcodes.ISTORE, 1); // istore_1
        }
    }

}
