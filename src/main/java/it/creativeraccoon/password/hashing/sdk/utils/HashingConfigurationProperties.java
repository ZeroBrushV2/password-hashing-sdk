package it.creativeraccoon.password.hashing.sdk.utils;

/**
 * 
 * enum used to map the information present in the owned files
 * @author ZeroBrushV2
 */
public enum HashingConfigurationProperties {

	ALGORITHM("security.hashing.algorithm"),
	/**
	 * MemoryCost is intended as the quantity of RAM (in KB) that can be used by the machine for generating a single hash
	 */
	MEMORY_COST("security.hashing.memory"),
	/**
	 * The iTerazions is intended as the quantity of iterations (hashing cycles) to be made to make the hash safe from bruteForce attacks
	 */
	ITERATIONS("security.hashing.iterations"),
	/**
	 * Threads indicate the actual quantity of threads that can be used for the generation of a single hash
	 */
	THREADS("security.hashing.threads"),
	/**
	 * The Salt_Size is intended as the size of the Byte[] of the generated Salt
	 */
	SALT_SIZE("security.hashing.salt.size");

	private String propertyName;
	
	HashingConfigurationProperties(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

}
