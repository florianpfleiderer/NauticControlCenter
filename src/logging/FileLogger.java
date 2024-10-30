package logging;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileLogger implements ILogger {

	private BufferedWriter bufferedWriter = null;
	private String filename = "_messages.log";
	private LogLevel logLevel = LogLevel.DEBUG;
	private static FileLogger instance = null;

	private FileLogger() {

	}

	public static FileLogger getInstance() {
		if (instance == null) {
			instance = new FileLogger();
		}
		return instance;
	}

	private void writeToFile(String line) {
		String timeLog = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		try {
			bufferedWriter.write(timeLog + " - " + line);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public boolean openFile(String filepath) {
		String filename = this.filename;
		if (filepath != null && filepath.isEmpty() == false) {
			filename = filepath;
		}
		if (bufferedWriter == null) {
			// try to open file
			try {
				FileOutputStream outputStream = new FileOutputStream(filename);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
				bufferedWriter = new BufferedWriter(outputStreamWriter);
			} catch (IOException e) {
				System.err.println("File could not be opened: " + filepath);
				return false;
			}
			
		}
		return true;
	}

	public boolean isInitialized() {
		if (this.bufferedWriter == null) {
			return false;
		}
		return true;
	}

	@Override
	public void modifyLogLevel(LogLevel newLevel) {
		logLevel = newLevel;
	}

	@Override
	public void debug(String message) {
		if (isInitialized() && logLevel.includesLogLevel(LogLevel.DEBUG)) {
			this.writeToFile(LogLevel.DEBUG.toString() + message);
		}
	}

	@Override
	public void debug(String message, Object obj) {
		if (isInitialized() && logLevel.includesLogLevel(LogLevel.DEBUG)) {
			this.writeToFile(LogLevel.DEBUG.toString() + message + " Object: " + obj);
		}
	}

	@Override
	public void info(String message) {
		if (isInitialized() && logLevel.includesLogLevel(LogLevel.INFO)) {
			this.writeToFile(LogLevel.INFO.toString() + message);
		}
	}

	@Override
	public void warn(String message) {
		if (isInitialized() && logLevel.includesLogLevel(LogLevel.WARN)) {
			this.writeToFile(LogLevel.WARN.toString() + message);
		}
	}

	@Override
	public void error(String message) {
		if (isInitialized() && logLevel.includesLogLevel(LogLevel.ERROR)) {
			this.writeToFile(LogLevel.WARN.toString() + message);
		}
	}

	@Override
	public void fatal(String message) {
		if (isInitialized() && logLevel.includesLogLevel(LogLevel.FATAL)) {
			this.writeToFile(message);
		}
	}
}
