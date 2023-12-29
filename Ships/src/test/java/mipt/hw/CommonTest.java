package mipt.hw;

import mipt.hw.Ship.ShipCapacity;
import mipt.hw.Ship.ShipType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommonTest {

    public static void InitLogger() {
        CommonLogger.removeHandlers();

        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        final String methodName = ste[2].getMethodName();

        String logFilename = CommonLogger.getLogsDirName() + "/CommonLogger_" + methodName + ".log";
        CommonLogger.addFileHandler(new File(logFilename));
    }

    @Test
    @Order(1)
    public void OneShip() {
        InitLogger();

        Observer observer = new Observer();

        long breadShipsCount = 1;

        observer.setPriers(breadShipsCount, 0, 0);

        observer.launchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);

        observer.waitIdle();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }

    @Test
    @Order(2)
    public void AllTypesShips() {
        InitLogger();

        Observer observer = new Observer();

        long breadShipsCount = 1;
        long bananaShipsCount = 1;
        long clothesShipsCount = 1;

        observer.setPriers(breadShipsCount, bananaShipsCount, clothesShipsCount);

        observer.launchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);
        observer.launchShips(ShipType.BANANA, ShipCapacity.SMALL, bananaShipsCount);
        observer.launchShips(ShipType.CLOTHES, ShipCapacity.SMALL, clothesShipsCount);

        observer.waitIdle();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }

    @Test
    @Order(3)
    public void AllCapacitiesOfShips() {
        InitLogger();

        Observer observer = new Observer();

        long small = 1;
        long medium = 1;
        long large = 1;
        long breadShipsCount = small + medium + large;

        observer.setPriers(breadShipsCount, 0, 0);

        observer.launchShips(ShipType.BREAD, ShipCapacity.SMALL, small);
        observer.launchShips(ShipType.BREAD, ShipCapacity.MEDIUM, medium);
        observer.launchShips(ShipType.BREAD, ShipCapacity.LARGE, large);

        observer.waitIdle();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }

    @Test
    @Order(4)
    public void MoreThenFiveShips() {
        InitLogger();

        Observer observer = new Observer();

        long breadShipsCount = 10;
        long bananaShipsCount = 8;
        long clothesShipsCount = 6;

        observer.setPriers(breadShipsCount, bananaShipsCount, clothesShipsCount);

        observer.launchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);
        observer.launchShips(ShipType.BANANA, ShipCapacity.SMALL, bananaShipsCount);
        observer.launchShips(ShipType.CLOTHES, ShipCapacity.SMALL, clothesShipsCount);

        observer.waitIdle();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }

    @Test
    @Order(5)
    public void Stress() {
        InitLogger();

        Observer observer = new Observer();

        long breadShipsCount = 22;
        long bananaShipsCount = 22;
        long clothesShipsCount = 22;

        observer.setPriers(breadShipsCount, bananaShipsCount, clothesShipsCount);

        observer.launchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);
        observer.launchShips(ShipType.BANANA, ShipCapacity.SMALL, bananaShipsCount);
        observer.launchShips(ShipType.CLOTHES, ShipCapacity.SMALL, clothesShipsCount);

        observer.waitIdle();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }
}