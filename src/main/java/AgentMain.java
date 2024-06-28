import java.lang.instrument.Instrumentation;

public class AgentMain {

    public static void premain(String args, Instrumentation inst) {
        AgentDump.register(inst);
        ShatteredPixelCheatASM.register(inst);
    }
}
