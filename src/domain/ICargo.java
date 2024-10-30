package domain;

import domain.harbor.Harbor;

public interface ICargo extends IIdentifiable {
	
	public abstract Harbor<?> getDestinationLocation();

}
