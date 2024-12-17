package mipt.hw;

public class InterruptedExceptionHandler {
    public static void handle() {
        final String threadName = Thread.currentThread().getName();

        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        final String methodName = ste[2].getMethodName();

        RingLogger.warning("Thread \"%s\" interrupted in method \"%s\".".formatted(threadName, methodName));
        Thread.currentThread().interrupt();
    }
}
