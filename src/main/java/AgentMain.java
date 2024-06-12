import java.lang.instrument.Instrumentation;

public class AgentMain {

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        System.out.println("agent premain");
        instrumentation = inst;
    }
}
