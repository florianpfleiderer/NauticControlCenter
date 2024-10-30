package domain.harbor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import domain.ICargo;
import domain.ILocation;
import domain.IStorageSpace;
import domain.LoadException;
import domain.UnloadException;
import domain.ship.IShip;
import domain.ship.SmallContainerShip;
import logging.ILogger;
import logging.LoggerFactory;
import logging.LoggingMode;
import observer.IObserver;
import observer.ISubject;

public class Harbor<CARGO extends ICargo> implements ILocation, IStorageSpace, ISubject {

	private String identifier;
	private List<CARGO> cargoStorage = new ArrayList<>();
	private Set<IObserver> observers = new HashSet<>();
	private ILogger logger = null;

	public Harbor(String name) {
		identifier = name;
		this.logger = LoggerFactory.INSTANCE.getLogger(LoggingMode.DEFAULT);
	}

	public Harbor(long number) {
		identifier = "Port Royal " + number;
		this.logger = LoggerFactory.INSTANCE.getLogger(LoggingMode.DEFAULT);
	}

	public String getHarborName() {
		return identifier;
	}

	@Override
	public String getIdentifier() {
		return getHarborName();
	}

	public void addCargo(CARGO container) {
		cargoStorage.add(container);
	}

	public void loadShip(IShip<ICargo> ship) {
		// In principle exceptions should not be used for this.
		// Only for demonstration purpose! Use Exceptions for real errors only.
		
		logger.info("Harbor - Load ship in harbor: " + this);
		logger.info("Harbor - Load Ship with cargo: " + ship);
		try {
			
			Iterator<CARGO> itrCargo = (Iterator<CARGO>) cargoStorage.iterator();
			while (itrCargo.hasNext()){
				ICargo cargo = itrCargo.next();
				ship.loadCargo(cargo);
				itrCargo.remove();
			}
		} catch (LoadException exception) {
			logger.warn("Harbor - Stop loading ship (no free space): " + this);
		}
		
		notifyObserverAction();
		
	}

	public void unloadShip(IShip<ICargo> ship) {
		// In principle exceptions should not be used for this.
		// Only for demonstration purpose! Use Exceptions for real errors only.
		
		logger.info("Harbor - Unload ship in harbor: " + this);
		logger.info("Harbor - Unload Ship and deliver cargo: " + ship);
		try {
			
			// If the ship is a SmallContainerShip an extra fee is raised by the owner of the harbor.
			if (ship instanceof SmallContainerShip) {
				for(IObserver obs : observers) {
					obs.notifyExtraHarborFee(ship);
				}
			}
			
			// Cargo is send directly to the recipient - not stored in
			// cargoStorage!
			ship.unloadCargo(this);			

		} catch (UnloadException e) {
			logger.warn("Harbor - NO cargo to unload");
		}

	}

	@Override
	public boolean isStorageSpaceEmpty() {
		return cargoStorage.isEmpty();
	}

	@Override
	public boolean isStorageSpaceFull() {
		// No maximum space
		return false;
	}

	@Override
	public void registerObserver(IObserver obs) {
		observers.add(obs);
	}

	@Override
	public void unregisterObserver(IObserver obs) {
		observers.remove(obs);

	}
	
	private void notifyObserverAction() {
		for (IObserver obs : observers){
			obs.notifyObserver(this);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [ID: " + getIdentifier() + "] [Storage Size: " + cargoStorage.size() + "]";
	}
}
