package logging;

public class LoggerTestClass {

	public static void main(String[] args) {
		System.out.println("XX" + System.getProperty("user.home"));
		ILogger logger = LoggerFactory.INSTANCE.getLogger(LoggingMode.CONSOLE_LOG);
		logger.info("Test Meldung");
		logger.debug("Debug Message");

	}

}
