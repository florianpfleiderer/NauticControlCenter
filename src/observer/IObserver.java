package observer;

import domain.ICargo;
import domain.harbor.Harbor;
import domain.ship.IShip;

public interface IObserver {
	
	public void notifyObserver(ICargo cargo);
	public void notifyObserver(Harbor<?> harbor);
	public void notifyObserver(IShip<ICargo> ship);
	public void notifyExtraHarborFee(IShip<ICargo> ship);
}
