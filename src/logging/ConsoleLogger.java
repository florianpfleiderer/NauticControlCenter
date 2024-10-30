package logging;

public class ConsoleLogger extends AbstractLogger implements ILogger {

	public ConsoleLogger() {
		// default logLevel defines which messages should be logged by default
		// Could be also be set using a configuration file
	}

	public ConsoleLogger(LogLevel level) {
		logLevel = level;
	}

	// A method for which inherited definition is defined here - ILogger.modifyLogLevel,
	// AbstractLogger.modifyLogLevel or one for both ? Why is this possible ?
	@Override
	public void modifyLogLevel(LogLevel newLevel) {
		this.logLevel = newLevel;

	}

	@Override
	public void debug(String message) {
		if (logLevel.includesLogLevel(LogLevel.DEBUG)) {
			writeToConsole(LogLevel.DEBUG.toString() + message);
		}
	}

	@Override
	public void debug(String message, Object obj) {
		if (logLevel.includesLogLevel(LogLevel.DEBUG)) {
			writeToConsole(LogLevel.DEBUG.toString() + message + " Object: " + obj);
		}

	}

	@Override
	public void info(String message) {
		if (logLevel.includesLogLevel(LogLevel.INFO)) {
			writeToConsole(LogLevel.INFO.toString() + message);
		}

	}

	@Override
	public void warn(String message) {
		if (logLevel.includesLogLevel(LogLevel.WARN)) {
			writeToConsole(LogLevel.WARN.toString() + message);
		}

	}

	@Override
	public void error(String message) {
		if (logLevel.includesLogLevel(LogLevel.ERROR)) {
			writeToConsole(LogLevel.ERROR.toString() + message);
		}

	}

	@Override
	public void fatal(String message) {
		if (logLevel.includesLogLevel(LogLevel.FATAL)) {
			writeToConsole(LogLevel.FATAL.toString() + message);
		}

	}

	private void writeToConsole(String message) {
		// provide a single point for changes - extra information could be added
		// for all log levels.
		System.out.println(message);

	}

}
