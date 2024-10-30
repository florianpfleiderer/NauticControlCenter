package utils;

import java.util.Random;

public enum RandomNumberGenerator {
	INSTANCE;
	
	private Random generator = new Random(System.currentTimeMillis());
	
	public int generateRandom(int start, int end) {
		return (int) Math.abs(start + ((end-start) * generator.nextDouble())  );
	}

}
