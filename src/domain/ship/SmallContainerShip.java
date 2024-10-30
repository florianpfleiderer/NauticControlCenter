package domain.ship;

import domain.ICargo;
import domain.harbor.Harbor;

public class SmallContainerShip extends ContainerShip<ICargo> {
	
	private int maxStorageSpace = 5;

	public SmallContainerShip(String shipName, Harbor<?> currentLocation) {
		super(shipName, currentLocation);
		super.setMaximumStorageSpace(maxStorageSpace);
	}

	public SmallContainerShip(long id, Harbor<?> currentLocation) {
		super(id, currentLocation);
		super.setMaximumStorageSpace(maxStorageSpace);
	}

}
