import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class ShatteredPixelCheat {

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

                boolean isHero = "com/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero".equals(className);
                boolean isChar = "com/shatteredpixel/shatteredpixeldungeon/actors/Char".equals(className);
                if (isHero || isChar) {
                    ClassReader cr = new ClassReader(classfileBuffer);
                    ClassWriter cw = new ClassWriter(cr, PARSING_FLAGS);
                    cr.accept(new CheatClassVisitor(cw, isHero), PARSING_OPTIONS);
                    return cw.toByteArray();
                } else {
                    return null;
                }
            }
        });
    }

    private static class CheatClassVisitor extends ClassVisitor {

        private final boolean isHero;

        protected CheatClassVisitor(ClassVisitor cv, boolean isHero) {
            super(ASM_VERSION, cv);
            this.isHero = isHero;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return ("damage".equals(name)) ? new DamageMethodVisitor(mv, isHero) : mv;
        }
    }

    private static class DamageMethodVisitor extends MethodVisitor {

        private final boolean isHero;

        protected DamageMethodVisitor(MethodVisitor mv, boolean isHero) {
            super(ASM_VERSION, mv);
            this.isHero = isHero;
        }

        @Override
        public void visitCode() {
            mv.visitCode();

            if (isHero) {
                // set damage parameter to zero
                mv.visitLdcInsn(0); // iconst_0
                mv.visitVarInsn(Opcodes.ISTORE, 1); // istore_1
            } else {
                // amplify damage parameter by factor of 42
                mv.visitLdcInsn(42);
                mv.visitVarInsn(Opcodes.ILOAD, 1);
                mv.visitInsn(Opcodes.IMUL);
                mv.visitVarInsn(Opcodes.ISTORE, 1);
            }


        }
    }

}
