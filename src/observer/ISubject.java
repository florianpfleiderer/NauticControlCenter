package observer;

public interface ISubject {
	
	public void registerObserver(IObserver obs);
	public void unregisterObserver(IObserver obs);

}
