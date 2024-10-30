package domain;

import domain.harbor.Harbor;
import domain.ship.IShip;
import logging.ILogger;
import logging.LoggerFactory;
import logging.LoggingMode;
import observer.IObserver;

public class Owner implements IObserver, IIdentifiable {

	private String ownerName;
	private ILogger logger = null;

	public Owner(long id) {
		ownerName = "Owner " + id;
		this.logger = LoggerFactory.INSTANCE.getLogger(LoggingMode.DEFAULT);
	}

	public Owner(String name) {
		this.ownerName = name;
		this.logger = LoggerFactory.INSTANCE.getLogger(LoggingMode.DEFAULT);
	}

	@Override
	public void notifyObserver(ICargo cargo) {
		logger.debug(this + " - cargo action:");

	}

	@Override
	public void notifyObserver(Harbor<?> harbor) {
		logger.debug(this + " - harbor action:");

	}

	@Override
	public void notifyObserver(IShip<ICargo> subject) {
		logger.debug(this + " - ship action:");
	}

	@Override
	public void notifyExtraHarborFee(IShip<ICargo> ship) {
		logger.debug(this + " - ship action: Extra fee for " + ship.getClass().getSimpleName() + " ID:"
				+ ship.getIdentifier());

	}

	@Override
	public String getIdentifier() {
		return ownerName;
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + " [ID: " + getIdentifier() + "]";
	}

}
