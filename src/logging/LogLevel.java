package logging;

public enum LogLevel {
	DEBUG(0, "Debug: "),
	INFO(1, "Info: "),
	WARN(2, "Warning: "),
	ERROR(3, "Error: "),
	FATAL(4, "Fatal Error: ");
	
	private int numericLevel;
	private String levelString;
	
	private LogLevel(int num, String name) {
		numericLevel = num;
		levelString = name;
	}
	
	public int getLogLevelAsNumeric() {
		return numericLevel;
	}
	
	public boolean includesLogLevel(LogLevel compareLevel) {
		return (compareTo(compareLevel) <= 0 ) ? true : false;
	}
	
	public String toString() {
		return levelString;
	}

}
