package mipt.hw;

import mipt.hw.Ship.ShipCapacity;
import mipt.hw.Ship.ShipType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CommonTest {
    @Test
    public void OneShip() {
        Observer observer = new Observer();

        long breadShipsCount = 1;

        observer.setPriers(breadShipsCount, 0, 0);

        observer.LaunchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);

        observer.awaitProcess();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }

    @Test
    public void AllTypesShips() {
        Observer observer = new Observer();

        long breadShipsCount = 1;
        long bananaShipsCount = 1;
        long clothesShipsCount = 1;

        observer.setPriers(breadShipsCount, bananaShipsCount, clothesShipsCount);

        observer.LaunchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);
        observer.LaunchShips(ShipType.BANANA, ShipCapacity.SMALL, bananaShipsCount);
        observer.LaunchShips(ShipType.CLOTHES, ShipCapacity.SMALL, clothesShipsCount);

        observer.awaitProcess();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }

    @Test
    public void AllCapacitiesOfShips() {
        Observer observer = new Observer();

        long small = 1;
        long medium = 1;
        long large = 1;
        long breadShipsCount = small + medium + large;

        observer.setPriers(breadShipsCount, 0, 0);

        observer.LaunchShips(ShipType.BREAD, ShipCapacity.SMALL, small);
        observer.LaunchShips(ShipType.BREAD, ShipCapacity.MEDIUM, medium);
        observer.LaunchShips(ShipType.BREAD, ShipCapacity.LARGE, large);

        observer.awaitProcess();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }

    @Test
    public void MoreThenFiveShips() {
        Observer observer = new Observer();

        long breadShipsCount = 10;
        long bananaShipsCount = 8;
        long clothesShipsCount = 6;

        observer.setPriers(breadShipsCount, bananaShipsCount, clothesShipsCount);

        observer.LaunchShips(ShipType.BREAD, ShipCapacity.SMALL, breadShipsCount);
        observer.LaunchShips(ShipType.BANANA, ShipCapacity.SMALL, bananaShipsCount);
        observer.LaunchShips(ShipType.CLOTHES, ShipCapacity.SMALL, clothesShipsCount);

        observer.awaitProcess();

        for (Pier pier: observer.piers.values()) {
            Assertions.assertEquals(0, pier.getLeftShipsToHandle());
        }
    }
}