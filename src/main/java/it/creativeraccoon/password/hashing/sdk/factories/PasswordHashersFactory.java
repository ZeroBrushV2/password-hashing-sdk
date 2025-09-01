package it.creativeraccoon.password.hashing.sdk.factories;

import it.creativeraccoon.password.hashing.sdk.exceptions.PasswordHasherInstantiationException;
import it.creativeraccoon.password.hashing.sdk.hashers.Argon2PasswordHasher;
import it.creativeraccoon.password.hashing.sdk.hashers.PasswordHasher;
import it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties;
import it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationPropertiesUtil;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordAlgorithms;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Factory that returns an application for the current hashing method set in the Security file.properties
 * @author ZeroBrushV2
 */
public abstract class PasswordHashersFactory {

	private static final Logger logger = LoggerFactory.getLogger(PasswordHashersFactory.class);
	/**
	 * Map containing all types of hashing algorithms with related classes
	 * When a new Hasher is created, it must be added to the Segeunte Map.
	 */
	private static final Map<PasswordAlgorithms, Class<?>> instances =
		new HashMap<PasswordAlgorithms, Class<?>>() {
			private static final long serialVersionUID = -5927720697138126285L;

			{
				put(PasswordAlgorithms.ARGON2, Argon2PasswordHasher.class);
			}
		
		};
 
	/**
	 * 
	 * Method that allows you to obtain a default Hasher instance indicated in the Security-Default file. Properties
	 * 
	 * @return Default Hasher instance
	 * @throws PasswordHasherInstantiationException Error during the initialization of the Hasher
	 */
	public static PasswordHasher getDefaultInstance() throws PasswordHasherInstantiationException {
		
		try {
			HashingConfigurationPropertiesUtil propsUtils = new HashingConfigurationPropertiesUtil();
			Map<HashingConfigurationProperties, String> defaultProperties = propsUtils.getDefaultProperties();
			
			String hashingAlgorytm = defaultProperties.get(HashingConfigurationProperties.ALGORITHM);
			PasswordAlgorithms hasherType = PasswordAlgorithms.getByLabel(hashingAlgorytm);
			
			
			return getInstance(hasherType, defaultProperties);
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new PasswordHasherInstantiationException("Error during the initialization of the default Hasher", t);
		}
	}
	
	
	/**
	 * Method that allows you to instantiate a certain type of Hasher
	 * 
	 * @param passwordHasher type of hasher to be initialized
	 * @return instance of the hasher requested
	 * @throws PasswordHasherInstantiationException Error during the initialization of the Hasher
	 */
	public static PasswordHasher getInstance(@NonNull PasswordAlgorithms passwordHasher) throws PasswordHasherInstantiationException {
		Class<?> hasherClazz = instances.get(passwordHasher);
		
		try {
			return (PasswordHasher) hasherClazz.getConstructor().newInstance();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new PasswordHasherInstantiationException(String.format("Error during the initialization of the Hasher '%s'", passwordHasher.name()), t);
		}
	}
	
	/**
	 * Method that returns an instance of the Hasher that reflects the properties present in the map as a parameter
	 * 
	 * @param hashingProperties complete map of ownership from which to instantiate the hasher
	 * @return Instance of the hasher requested
	 * @throws PasswordHasherInstantiationException Error during the initialization of the Hasher
	 */
	public static PasswordHasher getInstance(@NonNull Map<HashingConfigurationProperties, String> hashingProperties) throws PasswordHasherInstantiationException {
		
		String hashingAlgorytm = hashingProperties.get(HashingConfigurationProperties.ALGORITHM);
		
		try {
			PasswordAlgorithms hasherType = PasswordAlgorithms.getByLabel(hashingAlgorytm);

			return getInstance(hasherType, hashingProperties);
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new PasswordHasherInstantiationException(String.format("Error during the initialization of the Hasher '%s'", hashingAlgorytm), t);
		}
	}
	
	/**
	 * Method that allows you to instantiate a certain type of hasher according to the specified properties
	 * 
	 * @param passwordHasher type of hasher to be initialized
	 * @param hashingProperties properties with which to initialize the hasher
	 * @return request of the requested Hasher
	 * @throws PasswordHasherInstantiationException Error during the initialization of the Hasher
	 */
	public static PasswordHasher getInstance(PasswordAlgorithms passwordHasher, Map<HashingConfigurationProperties, String> hashingProperties) throws PasswordHasherInstantiationException {
		if (passwordHasher == null)
			throw new IllegalArgumentException("Hasher cannot be empty");

		if (hashingProperties == null || hashingProperties.isEmpty())
			throw new IllegalArgumentException("The map of properties for the hasher cannot be empty");
		
		Class<?> hasherClazz = instances.get(passwordHasher);
		
		try {
			return (PasswordHasher) hasherClazz.getConstructor(Map.class).newInstance(hashingProperties);
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new PasswordHasherInstantiationException(String.format("Error during the initialization of the Hasher '%s'", passwordHasher.name()), t);
		}
	}

}
