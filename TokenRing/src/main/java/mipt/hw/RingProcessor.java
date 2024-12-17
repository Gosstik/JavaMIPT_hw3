package mipt.hw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * В конструкторе кольцо инициализируется, то есть создаются все узлы и данные на узлах.
 * В методе {@link RingProcessor#startProcessing()} запускается работа кольца - данные начинают
 * обрабатываться по часовой стрелке. Также производится логгирование в {@link RingLogger}.
 * Вся работа должна быть потокобезопасной и с обработкой всех возможных исключений. Если необходимо,
 * разрешается создавать собственные классы исключений.
 */
public class RingProcessor {
    public RingProcessor(int nodesAmount, int dataAmount, File logs) {
        assert nodesAmount > 1 && dataAmount > 0;

        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;

        RingLogger.addConsoleHandler();
        RingLogger.addFileHandler(logs);

        init();
    }

    public void startProcessing() {
        logStartProcessing();
        for (Node node: nodeList) {
            pool.execute(node);
        }
    }

    public void waitIdle() {
        pool.shutdown();
        try {
            boolean terminated = pool.awaitTermination(
                    Math.max(2000, dataAmount * Node.getOneProcessTime() * 1000), TimeUnit.MILLISECONDS);
            if (!terminated) {
                throw new RuntimeException("Timeout for waitIdle exceeded.");
            }
        } catch (InterruptedException e) {
            InterruptedExceptionHandler.handle();
        }
    }

    public double averageNetworkDelay() {
        return coreNode.timeNetworkDelayList
                .stream()
                .mapToDouble(val -> val)
                .average().orElse(0);
    }

    public double averageBufferDelay() {
        return coreNode.allData.stream()
                .flatMap(node -> node.timeBufferDelayList.stream())
                .mapToDouble(val -> val)
                .average()
                .orElse(0);
    }

    public void logAverage() {
        long networkDelay = TimeUnit.NANOSECONDS.toMillis((long)averageNetworkDelay());
        long bufferDelay = TimeUnit.NANOSECONDS.toMillis((long)averageBufferDelay());
        RingLogger.info("""
                Average information.
                Network delay: %d milliseconds
                Buffer delay: %d milliseconds
                """.formatted(networkDelay, bufferDelay));
    }

    public List<DataPackage> getAllData() {
        return coreNode.allData;
    }

    private void init() {
        initDataPackageFormatString();

        nodeList = new ArrayList<>();
        ThreadLocalRandom gen = ThreadLocalRandom.current();
        final int coreId = getRandomNodeId(gen);

        for (int i = 1; i <= nodesAmount; ++i) {
            Node cur = createNode(i, coreId, gen);
            nodeList.add(cur);
            if (i == coreId) {
                coreNode = cur;
                coreNode.setTotalDataPackagesLeft(nodesAmount * dataAmount);
            }
        }

        nodeList.get(nodeList.size() - 1).setForwardingNodes(coreNode, nodeList.get(0));
        for (int i = 0; i < nodesAmount - 1; ++i) {
            nodeList.get(i).setForwardingNodes(coreNode, nodeList.get(i + 1));
        }
    }

    private int getRandomNodeId(ThreadLocalRandom gen) {
        return gen.nextInt(1, nodesAmount + 1);
    }

    private Node createNode(int nodeId, int coreId, ThreadLocalRandom gen) {
        Node node = new Node(nodeId, coreId);
        for (int i = 1; i <= dataAmount; ++i) {
            int destId = getRandomNodeId(gen);
            DataPackage data = new DataPackage(destId,
                    dataFormatString.formatted(nodeId, destId, i));
            node.addData(data);
        }
        return node;
    }

    private void initDataPackageFormatString() {
        int nodesNumberMaxDigits = digitsCount(nodesAmount);
        int dataNumberMaxDigits = digitsCount(dataAmount);
        dataFormatString =
                "{startNodeId: %" + nodesNumberMaxDigits + "d; " +
                        "destId: %" + nodesNumberMaxDigits + "d; " +
                        "startOrderInList: %" + dataNumberMaxDigits + "d}.";
    }

    private int digitsCount(int val) {
        if (val == 0) {
            return 1;
        }
        int res = 0;
        while (val != 0) {
            ++res;
            val /= 10;
        }
        return res;
    }

    private void logStartProcessing() {
        RingLogger.info("""
                Start RingProcessing.
                Number of nodes: %d.
                Number of DataPackage's per Node: %d.
                coreNode: %d.
                """.formatted(nodesAmount, dataAmount, coreNode.getId()));
    }

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final int nodesAmount;
    private final int dataAmount;
    private String dataFormatString;
    private List<Node> nodeList;
    private Node coreNode;
}
