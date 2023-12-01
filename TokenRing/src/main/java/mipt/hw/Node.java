package mipt.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {

    public Node(int nodeId, int coreId) {
        this.nodeId = nodeId;

        if (nodeId == coreId) {
            allData = new ArrayList<>();
            timeNetworkDelayList = new ArrayList<>();
        }
        else {
            allData = null;
            timeNetworkDelayList = null;
        }
    }

    public void setForwardingNodes(Node core, Node next) {
        coreNode = core;
        nextNode = next;
    }

    public void setTotalDataPackagesLeft(int totalDataPackages) {
        totalDataPackagesLeft = totalDataPackages;
    }

    public long getId() {
        return nodeId;
    }

    public void addData(DataPackage dataPackage) {
        synchronized (buffer) {
            buffer.add(dataPackage);
            dataPackage.addedToBuffer();
            buffer.notify();
        }
    }

    public void addToAllData(DataPackage dataPackage) {
        assert this == coreNode;

        synchronized (allData) {
            allData.add(dataPackage);
            timeNetworkDelayList.add(System.nanoTime() - dataPackage.getStartTime());
            --totalDataPackagesLeft;

            if (totalDataPackagesLeft == 0) {
                finishProcessing();
            }
        }
    }

    public static long getOneProcessTime() {
        return TIME_TO_PROCESS_MILLIS;
    }

    @Override
    public void run() {
        while (!processingFinished) {
            waitForAddData();
            List<DataPackage> localList = pollDataFromBuffer();
            handleDataPackages();
            forwardDataPackages(localList);
        }
    }

    private void waitForAddData() {
        synchronized (buffer) {
            while (buffer.isEmpty()) {
                if (processingFinished) {
                    return;
                }
                try {
                    buffer.wait();
                } catch (InterruptedException e) {
                    InterruptedExceptionHandler.handle();
                }
            }
        }
    }

    private List<DataPackage> pollDataFromBuffer() {
        List<DataPackage> localList = new ArrayList<>();
        synchronized (buffer) {
            int countLocal = Math.min(buffer.size(), SIMULTANEOUS_PROCESS_COUNT);
            for (int i = 0; i < countLocal; ++i) {
                DataPackage data = buffer.poll();
                assert data != null;

                data.polledFromBuffer();
                localList.add(data);
            }
        }
        return localList;
    }

    private static void handleDataPackages() {
        try {
            Thread.sleep(TIME_TO_PROCESS_MILLIS);
        } catch (InterruptedException e) {
            InterruptedExceptionHandler.handle();
        }
    }

    private void forwardDataPackages(List<DataPackage> localList) {
        for (DataPackage data: localList) {
            if (data.getDestinationNode() == nodeId) {
                coreNode.addToAllData(data);
                RingLogger.info("""
                    Forwarding data to coreNode.
                    Sender: %d
                    Receiver: %d
                    Data: %s
                    """.formatted(nodeId, nextNode.nodeId, data.getData()));
            } else {
                nextNode.addData(data);
                RingLogger.info("""
                    Forwarding data to nextNode.
                    Sender: %d
                    Receiver: %d
                    DataPackage: %s
                    """.formatted(nodeId, nextNode.nodeId, data.getData()));
            }
        }
    }

    private void finishProcessing() {
        assert this == coreNode;

        Node curNode = this;
        while (!curNode.processingFinished) {
            curNode.processingFinished = true;
            synchronized (curNode.buffer) {
                curNode.buffer.notify();
            }
            curNode = curNode.nextNode;
        }
    }

    private static final long TIME_TO_PROCESS_MILLIS = 1;
    private static final int SIMULTANEOUS_PROCESS_COUNT = 3;

    private final BlockingQueue<DataPackage> buffer = new LinkedBlockingQueue<>();
    private final int nodeId;

    private Node coreNode;
    private Node nextNode;
    private int totalDataPackagesLeft = 0; // for coreNode
    private volatile boolean processingFinished = false;

    public final List<DataPackage> allData;
    public final List<Long> timeNetworkDelayList;
}
