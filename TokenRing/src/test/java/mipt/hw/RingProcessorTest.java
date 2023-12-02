package mipt.hw;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RingProcessorTest {
    static {
        RingLogger.disableLogging();
    }

    public static String InitLogger() {
        RingLogger.removeHandlers();

        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        final String methodName = ste[3].getMethodName();

        return RingLogger.getLogsDirName() + "/RingLogger_" + methodName + ".log";
    }

    public static void RunTest(int nodesAmount, int dataAmount) {
        String logFilename = InitLogger();
        RingProcessor processor = new RingProcessor(nodesAmount, dataAmount, new File(logFilename));

        processor.startProcessing();
        processor.waitIdle();

        processor.logAverage();

        assertEquals(nodesAmount * dataAmount, processor.getAllData().size());
        assertTrue(processor.averageNetworkDelay() > 0);
        assertTrue(processor.averageBufferDelay() > 0);
    }

    @Test
    @Order(1)
    public void JustWorks() {
        RunTest(2, 1);
    }

    @Test
    @Order(2)
    public void SomeNodes() {
        RunTest(5, 3);
    }

    @Test
    @Order(3)
    public void ExceedSimultaneousDataAmount() {
        RunTest(3, 5);
    }

    @Test
    @Order(4)
    public void SomeNodesAndData() {
        RunTest(7, 7);
    }

    @Test
    @Order(5)
    public void ManyNodes() {
        RunTest(14, 4);
    }

    @Test
    @Order(6)
    public void ManyData() {
        RunTest(4, 14);
    }

    @Test
    @Order(7)
    public void Stress() {
        RunTest(30, 30);
    }
}
