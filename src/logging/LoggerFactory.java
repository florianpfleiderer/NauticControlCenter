package logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public enum LoggerFactory {

	// Singelton using enum
	INSTANCE;

	private String loggingFileName = "nauticLogFile.log";
	private String configFileName = "logger.properties";
	private LogLevel defaultLogLevel = LogLevel.DEBUG;

	private ILogger consoleLogger = null;
	private ILogger fileLogger = null;

	private LoggerFactory() {
		consoleLogger = new ConsoleLogger(defaultLogLevel);
		consoleLogger.modifyLogLevel(this.getLogLevelConfiguration(LoggingMode.CONSOLE_LOG));

		ILogger fileLogger = FileLogger.getInstance();
		fileLogger.modifyLogLevel(this.getLogLevelConfiguration(LoggingMode.CONSOLE_LOG));
		this.fileLogger = fileLogger;

	}

	public ILogger getLogger(LoggingMode mode) {
		ILogger logger = null;
		switch (mode) {

		case FILE_LOG:
			// get the file logger
			logger = FileLogger.getInstance();
			((FileLogger) logger).openFile(loggingFileName);
			
			break;

		case CONSOLE_FILE_LOG:
			return CombinedLogger.INSTANCE;

		default:
			// may be changed to be selected according to the configuration file.
			logger = FileLogger.getInstance();
			((FileLogger) logger).openFile(loggingFileName);
			break;
		}
		return logger;
	}

	public void modifyLogLevel(LogLevel newLevel) {
		defaultLogLevel = newLevel;
		// Modify LogLevel of all loggers.
		consoleLogger.modifyLogLevel(newLevel);
		fileLogger.modifyLogLevel(newLevel);
	}
	

	private LogLevel getLogLevelConfiguration(LoggingMode mode) {

		String searchKey = mode.toString() + "_LEVEL";

		Properties prop = new Properties();
		InputStream inputStream = null;

		// if exported as executable jar the properties file will be searched
		// relative to location of the executable jar file (= in the parent
		// folder).
		// if executed from source code this will search relative to
		// CLASSNAME.class folder.
		Class<?> referenceClass = this.getClass();
		URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();

		File file = new File(new File(url.getPath()).getParent(), configFileName);

		// TODO: Remove sysout
		System.out.println("Parent Folder: " + new File(url.getPath()).getParent());
		System.out.println(file.getAbsolutePath());

		if (file != null && file.isFile()) {
			try {
				inputStream = new FileInputStream(file);
				System.out.println("Logger configuration file found. External file: " + file.getAbsolutePath());
			} catch (FileNotFoundException e) {
				// Should not happen since already tested if it is a file and
				// this also checks the existence of the file.
				e.printStackTrace();
			}
		}

		// if the program is compiled and executed from source code the path of
		// the properties file will be searched relative to the class location.
		// this is only a fallback option
		if (inputStream == null) {
			InputStream resourceStream = LoggerFactory.class.getResourceAsStream(configFileName);
			if (resourceStream != null) {
				inputStream = resourceStream;
				System.out.println("Logger configuration is loaded from ressource.");
			}
		}

		// if no configuration file could be found than use the default log
		// level.
		if (inputStream == null) {
			System.err.println("Error loading the logger configuration. Use default values for logger configuration.");
			return defaultLogLevel;
		}

		// try to read the configuration from file
		try {
			prop.load(inputStream);
			String propertiesValue = prop.getProperty(searchKey);
			if (propertiesValue != null) {

				try {
					return LogLevel.valueOf(propertiesValue);
				} catch (IllegalArgumentException | NullPointerException ex) {
					// also set the log level to default log level of this
					// method.
					// nothing to do since the default level is returned at the
					// end of the method.
				}
			}

		} catch (FileNotFoundException exFile) {

			System.err.println("Fatal Error opening the logger configuration file. File: " + configFileName);

		} catch (IOException | IllegalArgumentException exLoadProperties) {

			System.err.println("Fatal Error reading from logger configuration file. File: " + configFileName);

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO: handle exception
				}
			}
		}
		// default log level if any problems occurred.
		return defaultLogLevel;

	}
	
	private String readEntryFromConfigurationFile(String key) {
		

		Properties prop = new Properties();
		InputStream inputStream = null;

		// if exported as executable jar the properties file will be searched
		// relative to location of the executable jar file (= in the parent
		// folder).
		// if executed from source code this will search relative to
		// CLASSNAME.class folder.
		Class<?> referenceClass = this.getClass();
		URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();

		File file = new File(new File(url.getPath()).getParent(), configFileName);

		// TODO: Remove sysout
		System.out.println("Parent Folder: " + new File(url.getPath()).getParent());
		System.out.println(file.getAbsolutePath());

		if (file != null && file.isFile()) {
			try {
				inputStream = new FileInputStream(file);
				System.out.println("Logger configuration file found. External file: " + file.getAbsolutePath());
			} catch (FileNotFoundException e) {
				// Should not happen since already tested if it is a file and
				// this also checks the existence of the file.
				e.printStackTrace();
			}
		}

		// if the program is compiled and executed from source code the path of
		// the properties file will be searched relative to the class location.
		// this is only a fallback option
		if (inputStream == null) {
			InputStream resourceStream = LoggerFactory.class.getResourceAsStream(configFileName);
			if (resourceStream != null) {
				inputStream = resourceStream;
				System.out.println("Logger configuration is loaded from ressource.");
			}
		}

		// if no configuration file could be found than use the default log
		// level.
		if (inputStream == null) {
			System.err.println("Error loading the logger configuration. Use default values for logger configuration.");
			return "";
		}

		// try to read the configuration from file
		try {
			prop.load(inputStream);
			String propertiesValue = prop.getProperty(key);
			return propertiesValue;

		} catch (FileNotFoundException exFile) {

			System.err.println("Fatal Error opening the logger configuration file. File: " + configFileName);

		} catch (IOException | IllegalArgumentException exLoadProperties) {

			System.err.println("Fatal Error reading from logger configuration file. File: " + configFileName);

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// nothing to do since the default level is returned at the
					// end of the method.
				}
			}
		}
		// default if any problems occurred.
		return "";
	}

}
