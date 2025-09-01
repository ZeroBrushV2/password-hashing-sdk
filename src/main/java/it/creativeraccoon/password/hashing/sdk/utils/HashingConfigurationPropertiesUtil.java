package it.creativeraccoon.password.hashing.sdk.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 *
 * Utils class to read the settings from the "Security.properties" file.
 * @author ZeroBrushV2
 */
public class HashingConfigurationPropertiesUtil {

	private static final String propertiesFileName = "security.properties";
	private static final String defaultPropertiesFileName = "security-default.properties";
	private static final Logger logger = LoggerFactory.getLogger(HashingConfigurationPropertiesUtil.class);

	private Properties prop;

	private Collection<it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties> getProperties() {
		return Arrays.asList(it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties.values());
	}
	
	/**
	 * Method that returns a non-modifiable map of the default ownership present in the Security-Dephanex file. Properties
	 * 
	 * @return map of default properties
	 */
	public Map<it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties, String> getDefaultProperties() {
		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(defaultPropertiesFileName)) {
			
			Map<it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties, String> properties = new HashMap<>();
			prop = new Properties();
	
			if (input == null) {
				throw new IllegalArgumentException("The file '" + propertiesFileName + "' not found");
			}
	
			//load a properties file from class path, inside static method
			prop.load(input);
	
			if (properties.keySet().stream().anyMatch(n -> StringUtils.isEmpty(n.getPropertyName()) || ! getProperties().contains(n)))
				throw new IllegalArgumentException("Il nome di almeno una proprieta' non e' valido");
	
			for (it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties property : it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties.values()) {
	
				if (StringUtils.isBlank(prop.getProperty(property.getPropertyName()))) 
					throw new IllegalArgumentException("Property '" + property + "' not found in " + propertiesFileName);
				
				properties.put(property, prop.getProperty(property.getPropertyName()));
	
			}

			return Collections.unmodifiableMap(properties);
		} catch (Throwable ex) {
			throw new IllegalArgumentException("Impossibile recuperare il file di proprieta' di default");
		}
	}
	
	public Map<it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties, String> readProperties() {
		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
	
			Map<it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties, String> properties = new HashMap<>();
			prop = new Properties();
	
			if (input == null) {
				throw new IllegalArgumentException("The file '" + propertiesFileName + "' not found");
			}
	
			//load a properties file from class path, inside static method
			prop.load(input);
	
			if (properties.keySet().stream().anyMatch(n -> StringUtils.isEmpty(n.getPropertyName()) || ! getProperties().contains(n)))
				throw new IllegalArgumentException("Il nome di almeno una proprieta' non e' valido");
			
			for (it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties property : it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties.values()) {
	
				if (StringUtils.isBlank(prop.getProperty(property.getPropertyName())))
						throw new IllegalArgumentException("Proprietà '" + property + "' non trovata in " + propertiesFileName);

				properties.put(property, prop.getProperty(property.getPropertyName()));
			}

			return Collections.unmodifiableMap(properties);
		} catch (Throwable ex) {
			logger.warn("Could not load user defined '" + propertiesFileName + "'. Falling back to default properties. Error: " + ex.getMessage());
			return getDefaultProperties();
		}
	}

}
