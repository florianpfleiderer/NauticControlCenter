package logging;

public class MainClass {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println(LogLevel.DEBUG.includesLogLevel(LogLevel.ERROR));
		
		ILogger consoleLogger = LoggerFactory.INSTANCE.getLogger(LoggingMode.CONSOLE_LOG);
		consoleLogger.info("hallo");
		
		LoggerFactory.INSTANCE.modifyLogLevel(LogLevel.ERROR);
		consoleLogger.info("Log Level should not be displayed");
		
		ILogger cominedLogger = LoggerFactory.INSTANCE.getLogger(LoggingMode.CONSOLE_FILE_LOG);
		cominedLogger.modifyLogLevel(LogLevel.DEBUG);
		cominedLogger.error("Message");

	}

}
