package domain.ship;

import java.util.List;

import domain.ICargo;
import domain.IIdentifiable;
import domain.IStorageSpace;
import domain.LoadException;
import domain.UnloadException;
import domain.harbor.Harbor;
import observer.ISubject;

public interface IShip<CARGO extends ICargo> extends IIdentifiable, IStorageSpace, ISubject {
	
	public boolean loadCargo(CARGO newCargo) throws LoadException;
	public List<ICargo> unloadCargo(Harbor<?> harbor) throws UnloadException;
	public boolean travelTo(Harbor<?> newHarbor);
	

}
