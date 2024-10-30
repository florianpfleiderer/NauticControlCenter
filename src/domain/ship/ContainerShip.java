package domain.ship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.ICargo;
import domain.LoadException;
import domain.UnloadException;
import domain.harbor.Harbor;
import logging.ILogger;
import logging.LoggerFactory;
import logging.LoggingMode;
import observer.IObserver;

public class ContainerShip<CARGO extends ICargo> implements IShip<CARGO> {

	private String name;
	private List<CARGO> cargo = new ArrayList<>();
	private int maxStorageSpace = 10;
	private ILogger logger = null;

	private Harbor<?> currentLocation;
	private Set<IObserver> observers = new HashSet<>();

	public ContainerShip(String shipName, Harbor<?> currentLocation) {
		name = shipName;
		this.currentLocation = currentLocation;
		this.logger = LoggerFactory.INSTANCE.getLogger(LoggingMode.FILE_LOG);
	}

	public ContainerShip(long id, Harbor<?> currentLocation) {
		name = "Nautilus " + id;
		this.currentLocation = currentLocation;
		this.logger = LoggerFactory.INSTANCE.getLogger(LoggingMode.FILE_LOG);
	}

	public Harbor<?> getCurrentLocation() {
		return currentLocation;
	}

	@SuppressWarnings("unchecked")
	public boolean travelTo(Harbor<?> newHarbor) {

		if (currentLocation.equals(newHarbor) == true) {
			// already there
			
			logger.warn("Ship - Already in destination harbor");
			return false;
		}
		// add cargo, don't care about destination location

		//unchecked cast - caused by wildcard and generics. cannot be
		// resolved by type checking!

		currentLocation.loadShip((IShip<ICargo>) this);
		// travel
		logger.info("Ship - Travel from   {" + currentLocation + "}   to   {" + newHarbor + " }");
		currentLocation = newHarbor;
		// remove all Cargo for specifiy harbor

		// unchecked cast - caused by wildcard and generics. can not be
		// resolved by type checking!
		currentLocation.unloadShip((IShip<ICargo>) this);
		return true;
	}

	public boolean loadCargo(CARGO newCargo) throws LoadException {
		if (newCargo != null && isStorageSpaceFull() == false) {
			logger.info("Ship - Load Cargo: " + newCargo);
			cargo.add(newCargo);
			return true;
		}
		notifyObserverAction();
		logger.warn("Ship - No free cargo space on ship: " + name + "  used [" + cargo.size() + "/ "
				+ maxStorageSpace + "]");
		throw new LoadException(
				"Ship - No free cargo space on ship [" + cargo.size() + "| " + maxStorageSpace + "]: " + name);
	}

	public List<ICargo> unloadCargo(Harbor<?> harbor) throws UnloadException {
		if (cargo.size() == 0) {
			throw new UnloadException("Nothing to unload from ship: " + name);
		}
		List<ICargo> unloadList = new ArrayList<>();
		for (ICargo element : cargo) {
			if (element.getDestinationLocation().equals(harbor)) {
				unloadList.add(element);
				logger.info("Ship - Unload Cargo: " + element);

			}
		}
		cargo.removeAll(unloadList);
		notifyObserverAction();
		return unloadList;
	}

	public int getFreeStorageSpace() {
		return maxStorageSpace - cargo.size();
	}

	@Override
	public String getIdentifier() {
		return name;
	}

	@Override
	public boolean isStorageSpaceEmpty() {
		return cargo.isEmpty();
	}

	@Override
	public boolean isStorageSpaceFull() {
		if (getFreeStorageSpace() > 0)
			return false;
		return true;

	}

	@Override
	public void registerObserver(IObserver obs) {
		observers.add(obs);

	}

	@Override
	public void unregisterObserver(IObserver obs) {
		observers.add(obs);

	}

	@SuppressWarnings("unchecked")
	private void notifyObserverAction() {
		for (IObserver obs : observers) {
			obs.notifyObserver((IShip<ICargo>) this);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [ID: " + getIdentifier() + "] [Storage Size: " + cargo.size()
				+ "] [Location: " + currentLocation.getIdentifier() + "]";
	}

	protected void setMaximumStorageSpace(int num) {
		// Cant resize it to this value since the new space is less than the
		// currently used space.
		if (cargo.size() <= num) {
			maxStorageSpace = num;
			return;
		}

		throw new IllegalArgumentException("");
	}

}
