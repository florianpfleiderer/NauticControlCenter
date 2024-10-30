package logging;

public abstract class AbstractLogger {
	
	protected LogLevel logLevel = LogLevel.DEBUG;
	
	public abstract void modifyLogLevel(LogLevel newLevel);

}
