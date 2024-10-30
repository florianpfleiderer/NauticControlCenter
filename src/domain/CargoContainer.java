package domain;

import domain.harbor.Harbor;

public class CargoContainer<DESTINATION extends Harbor<?>> implements ICargo, IIdentifiable {

	private String identifier;
	private DESTINATION destination;
	
	public CargoContainer(String name) {
		identifier = name;
	}
	
	public CargoContainer(long number) {
		identifier = "Cargo " + number;
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}

	public void setDestination(DESTINATION destination) {
		this.destination = destination;
	}
	
	@Override
	public DESTINATION getDestinationLocation() {
		return destination;
	}

	@Override 
	public String toString() {
		return this.getClass().getSimpleName() + " [ID: " + getIdentifier() + "]  [Location: " + getDestinationLocation().getIdentifier() + "]";
	}
}
