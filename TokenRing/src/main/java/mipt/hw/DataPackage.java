package mipt.hw;

import java.util.ArrayList;
import java.util.List;

public class DataPackage {
    DataPackage(int destinationNode, String data) {
        this.destinationNode = destinationNode;
        this.data = data;
        startTime = System.nanoTime();
        timeBufferDelayList = new ArrayList<>();
    }

    public int getDestinationNode() {
        return destinationNode;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getData() {
        return data;
    }

    public void addedToBuffer() {
        startTimeInBuffer = System.nanoTime();
    }

    public void polledFromBuffer() {
        timeBufferDelayList.add(System.nanoTime() - startTimeInBuffer);
    }

    private final int destinationNode;
    private final String data;
    private final long startTime;
    private long startTimeInBuffer;

    public final List<Long> timeBufferDelayList;
}
