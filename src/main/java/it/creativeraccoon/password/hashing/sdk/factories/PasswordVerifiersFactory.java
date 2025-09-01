package it.creativeraccoon.password.hashing.sdk.factories;

import it.creativeraccoon.password.hashing.sdk.exceptions.PasswordHasherInstantiationException;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordAlgorithms;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordEncodingUtils;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties;
import it.creativeraccoon.password.hashing.sdk.verifiers.Argon2PasswordVerifier;
import it.creativeraccoon.password.hashing.sdk.verifiers.PasswordVerifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Factory returning a check method to be used
 * @author ZeroBrushV2
 */
public abstract class PasswordVerifiersFactory {

	private static final Logger logger = LoggerFactory.getLogger(PasswordVerifiersFactory.class);

	private static final Map<PasswordAlgorithms, Class<?>> instances =
			new HashMap<PasswordAlgorithms, Class<?>>() {
				private static final long serialVersionUID = -5927720697138126285L;

				{
					put(PasswordAlgorithms.ARGON2, Argon2PasswordVerifier.class);
				}
			
	};
	
	/**
	 * Method that restores the request of the verifier based on the string that represents the hashing of a user's password
	 * 
	 * @param hashedPassword string that represents the hashed password
	 * @return Instance of a PasswordVerifier
	 * @throws PasswordHasherInstantiationException Error during the instantiation of the verifier
	 */
	public static PasswordVerifier getInstance(String hashedPassword) throws PasswordHasherInstantiationException {
		
		if (StringUtils.isEmpty(hashedPassword))
			throw new IllegalArgumentException("The password cannot be empty");
		
		try {
			Map<PasswordProperties, String> passwordProperties = PasswordEncodingUtils.decode(hashedPassword);
			String hashingAlgorytm = passwordProperties.get(PasswordProperties.ALGORITHM);
			PasswordAlgorithms hasherType = PasswordAlgorithms.getByLabel(hashingAlgorytm);
			
			Class<?> hasherClazz = instances.get(hasherType);
		
			return (PasswordVerifier) hasherClazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			logger.error(e.getMessage(), e);
			throw new PasswordHasherInstantiationException("Error during the initialization of the verifier", e);
		}
	}
}
