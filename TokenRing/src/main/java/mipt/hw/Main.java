package mipt.hw;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        String logFilename = RingLogger.getLogsDirName() + "/RingLogger.log";
        RingProcessor processor = new RingProcessor(10, 3, new File(logFilename));

        processor.startProcessing();
        processor.waitIdle();

        processor.logAverage();
    }
}
