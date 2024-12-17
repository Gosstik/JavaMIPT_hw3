package mipt.hw.Ship;

public enum ShipCapacity {
    SMALL(10),
    MEDIUM(50),
    LARGE(100);

    private final long value;

    ShipCapacity(long capacity) {
        this.value = capacity;
    }

    public long getValue() {
        return value;
    }
}
