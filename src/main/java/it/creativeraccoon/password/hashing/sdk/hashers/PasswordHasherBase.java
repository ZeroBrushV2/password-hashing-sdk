package it.creativeraccoon.password.hashing.sdk.hashers;

import it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties;
import org.bouncycastle.util.encoders.Base64;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic class that will have to be extended by all the hashers.
 * @author ZeroBrushV2
 */
public abstract class PasswordHasherBase implements PasswordHasher {

	protected static final int DEFAULT_OUTPUTLEN = 32;
	protected String algorithm;
	protected Integer version;
	protected Integer memoryCost;
	protected Integer iterations;
	protected Integer threads;
	protected Integer saltSize;
	
	public PasswordHasherBase(Map<HashingConfigurationProperties, String> securityProperties) {
		this.memoryCost = Integer.valueOf(securityProperties.get(HashingConfigurationProperties.MEMORY_COST));
		this.iterations = Integer.valueOf(securityProperties.get(HashingConfigurationProperties.ITERATIONS));
		this.threads = Integer.valueOf(securityProperties.get(HashingConfigurationProperties.THREADS));
		this.saltSize = Integer.valueOf(securityProperties.get(HashingConfigurationProperties.SALT_SIZE));
	}
	
	public PasswordHasherBase() {}
	
	/**
	 * Method that allows you to convert the password and Salt in a map that represents the properties with which the password hasshing was generated
	 * 
	 * @param hashedPassword The byte array that represents the password
	 * @param salt Board of the Salt Array used for the password generation
	 * @return Map that represents the owners of the password previously generated
	 */

    Map<PasswordProperties, String> generateMap(byte[] hashedPassword, byte[] salt) {
    	Map<PasswordProperties, String> result = new HashMap<PasswordProperties, String>();
    	result.put(PasswordProperties.ALGORITHM, algorithm);
    	result.put(PasswordProperties.VERSION, String.valueOf(version));
    	result.put(PasswordProperties.ITERATIONS, iterations.toString());
    	result.put(PasswordProperties.THREADS, threads.toString());
    	result.put(PasswordProperties.MEMORY_COST, memoryCost.toString());
    	result.put(PasswordProperties.PASSWORD, Base64.toBase64String(hashedPassword));
    	result.put(PasswordProperties.SALT, Base64.toBase64String(salt));
    	result.put(PasswordProperties.SALT_SIZE, String.valueOf(salt.length));
    	return result;
    }
	
	protected static byte[] generateRandomSalt(Integer saltSize) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[saltSize];
        secureRandom.nextBytes(salt);
        return salt;
	}
}
