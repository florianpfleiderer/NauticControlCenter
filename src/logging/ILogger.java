package logging;

public interface ILogger {
	
	public void debug(String message);
	public void debug(String message, Object obj9);
	public void info(String message);
	public void warn(String message);
	public void error(String message);
	public void fatal(String message);
	
	public void modifyLogLevel(LogLevel newLevel);

}
