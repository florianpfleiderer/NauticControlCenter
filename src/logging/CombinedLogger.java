package logging;

public enum CombinedLogger implements ILogger {

	INSTANCE;

	private ILogger fileLogger;
	private ILogger consoleLogger;

	private CombinedLogger() {
		fileLogger = LoggerFactory.INSTANCE.getLogger(LoggingMode.FILE_LOG);
		consoleLogger = LoggerFactory.INSTANCE.getLogger(LoggingMode.CONSOLE_LOG);
	}

	@Override
	public void debug(String message) {
		fileLogger.debug(message);
		consoleLogger.debug(message);

	}

	@Override
	public void debug(String message, Object obj) {
		fileLogger.debug(message, obj);
		consoleLogger.debug(message, obj);

	}

	@Override
	public void info(String message) {
		fileLogger.info(message);
		consoleLogger.info(message);

	}

	@Override
	public void warn(String message) {
		fileLogger.warn(message);
		consoleLogger.warn(message);

	}

	@Override
	public void error(String message) {
		fileLogger.warn(message);
		consoleLogger.warn(message);

	}

	@Override
	public void fatal(String message) {
		fileLogger.fatal(message);
		consoleLogger.fatal(message);

	}

	@Override
	public void modifyLogLevel(LogLevel newLevel) {
		consoleLogger.modifyLogLevel(newLevel);
		fileLogger.modifyLogLevel(newLevel);
	}

	public void modifyConsoleLogLevel(LogLevel newLevel) {
		consoleLogger.modifyLogLevel(newLevel);
	}

	public void modifyFileLogLevel(LogLevel newLevel) {
		fileLogger.modifyLogLevel(newLevel);
	}

}
