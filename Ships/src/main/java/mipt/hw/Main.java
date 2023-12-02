package mipt.hw;

import mipt.hw.Ship.ShipCapacity;
import mipt.hw.Ship.ShipType;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        InitLogger();

        CommonLogger.info("""
        Main start
        Available number of cores: %d
        """.formatted(Runtime.getRuntime().availableProcessors()));

        Observer observer = new Observer();

        long breadShipsCount = 10;
        long bananaShipsCount = 10;
        long clothesShipsCount = 2;

        observer.setPriers(breadShipsCount, bananaShipsCount, clothesShipsCount);

        observer.launchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);
        observer.launchShips(ShipType.BANANA, ShipCapacity.SMALL, bananaShipsCount);
        observer.launchShips(ShipType.CLOTHES, ShipCapacity.SMALL, clothesShipsCount);

        observer.waitIdle();
    }

    public static void InitLogger() {
//        CommonLogger.addConsoleHandler();

        String logFilename = CommonLogger.getLogsDirName() + "/CommonLogger.log";
        CommonLogger.addFileHandler(new File(logFilename));
    }
}
