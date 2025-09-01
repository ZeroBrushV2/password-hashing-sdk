import it.creativeraccoon.password.hashing.sdk.exceptions.PasswordHasherInstantiationException;
import it.creativeraccoon.password.hashing.sdk.factories.PasswordHashersFactory;
import it.creativeraccoon.password.hashing.sdk.factories.PasswordVerifiersFactory;
import it.creativeraccoon.password.hashing.sdk.hashers.PasswordHasher;
import it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordAlgorithms;
import it.creativeraccoon.password.hashing.sdk.verifiers.PasswordVerifier;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
* Class to be used to evaluate the effectiveness of the settings to be used. <br> <br>
 * In case of changes in the setting it is necessary to test to verify that the settings do not go to vary how long the hashing of a password is carried out
 * <br> <br>
 * For security reasons make sure that the hashing time does not varied from the one already current and that falls within the set parameters
 *
 * 
 * @author ZeroBrushV2
 *
 */
@Slf4j
public class PasswordEfficiencyTest {

    /**
	 * map that must be reflected in the properties file used
	 */
	private static final Map<HashingConfigurationProperties, String> properties =
			new HashMap<HashingConfigurationProperties, String>(){
				private static final long serialVersionUID = 4888112377608235890L;

			{
				put(HashingConfigurationProperties.ALGORITHM, PasswordAlgorithms.ARGON2.getLabel());
				put(HashingConfigurationProperties.ITERATIONS, "180");
				put(HashingConfigurationProperties.MEMORY_COST, "8000");
				put(HashingConfigurationProperties.THREADS, "4");
				put(HashingConfigurationProperties.SALT_SIZE, "16");
			}
		};
	
	/**
	 * Minimum execution time of the hashing method (in milliseconds)
	 */
	private static final long minTimeExecution = 1000;
	/**
	 * Maximum execution time of the hashing method (in milliseconds)
	 */
	private static final long maxTimeExecution = 1500;

    @Test
	public void passwordEfficiencyTest() throws PasswordHasherInstantiationException {
		log.info("Hashing password efficiency test start...");
		
		String hashingResult = passwordHashingTest();
		passwordVerifyingTest("testpassword", hashingResult, true);
	}
	
	private String passwordHashingTest() throws PasswordHasherInstantiationException {
		PasswordHasher passwordHasher = PasswordHashersFactory.getInstance(properties);
		
		long startTimeHashing = System.currentTimeMillis();
		String hashedPassword = passwordHasher.hash("testpassword");
		long endTimeHashing = System.currentTimeMillis();
		long elapsedTimeHashing = endTimeHashing - startTimeHashing;
		
		log.info("Hashing completed in {} ms", elapsedTimeHashing);
		executionTimeInRangeTest(elapsedTimeHashing);
		
		return hashedPassword;
	}
	
	private static void executionTimeInRangeTest(long elapsedTime) {
		boolean isValid = elapsedTime >= minTimeExecution && elapsedTime <= maxTimeExecution;
		String result = isValid ? "VALID" : "NOT VALID";
		log.info("Time range tests, to be carried out by {} ms and {} ms: {} ({} ms)", minTimeExecution, maxTimeExecution, result, elapsedTime);
	}
	
	private static void passwordVerifyingTest(String insertPassword, String hashedPassword, Boolean expectedResult) throws PasswordHasherInstantiationException {
		long startTimeVerifying = System.currentTimeMillis();
		PasswordVerifier passwordVerifier = PasswordVerifiersFactory.getInstance(hashedPassword);
		boolean areMatching = passwordVerifier.verify(insertPassword, hashedPassword);
		long endTimeVerifying = System.currentTimeMillis();
		
		long elapsedTimeVerifying = endTimeVerifying - startTimeVerifying;
		log.info("Password verification completed in {} ms", elapsedTimeVerifying);
		log.info("Passwords match: {}, expected result: {}", Boolean.toString(areMatching).toUpperCase(), expectedResult.toString().toUpperCase());
	}

}
