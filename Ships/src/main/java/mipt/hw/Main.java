package mipt.hw;

import mipt.hw.Ship.ShipCapacity;
import mipt.hw.Ship.ShipGenerator;
import mipt.hw.Ship.ShipType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        CustomLogger.info("Main start");
        CustomLogger.info("Available number of cores: " + Runtime.getRuntime().availableProcessors());

        Observer observer = new Observer();

        long breadShipsCount = 10;
        long bananaShipsCount = 10;
        long clothesShipsCount = 2;

        observer.setPriers(breadShipsCount, bananaShipsCount, clothesShipsCount);

        observer.LaunchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);
        observer.LaunchShips(ShipType.BANANA, ShipCapacity.SMALL, bananaShipsCount);
        observer.LaunchShips(ShipType.CLOTHES, ShipCapacity.SMALL, clothesShipsCount);

        observer.awaitProcess();
    }
}
