package it.creativeraccoon.password.hashing.sdk.verifiers;

import it.creativeraccoon.password.hashing.sdk.utils.PasswordEncodingUtils;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 
* Hashing implementations using the implementation of Argon2 BouncyCastle. <br>
 * This implementation of Argon2 provides for the forcing of some properties for security reasons:
 * <ul>
 * <li>
 * A variation of Argon2 called "ID" is used, which turns out to be the most suitable for password hashing.
 * </li>
 * </ul>
 * 
 * @author ZeroBrushV2
 *
 */
public class Argon2PasswordVerifier extends it.creativeraccoon.password.hashing.sdk.verifiers.PasswordVerifierBase {

	private static final Logger logger = LoggerFactory.getLogger(Argon2PasswordVerifier.class);

	private Argon2Parameters.Builder initArgon(Map<PasswordProperties, String> map) {
		
		 return new Argon2Parameters.Builder(
				 	Argon2Parameters.ARGON2_id)
	                .withVersion(Integer.parseInt(map.get(PasswordProperties.VERSION)))
	                .withIterations(Integer.parseInt(map.get(PasswordProperties.ITERATIONS)))
	                .withMemoryAsKB(Integer.parseInt(map.get(PasswordProperties.MEMORY_COST)))
	                .withParallelism(Integer.parseInt(map.get(PasswordProperties.THREADS)))
	                .withSalt(Base64.decode(map.get(PasswordProperties.SALT)));
	}
	
	@Override
	public boolean verify(String userPassword, String hashedPassword) {
		return isPasswordMatching(userPassword, hashedPassword);
	}
	
	private boolean isPasswordMatching(String userPassword, String hashedPassword) {
		
		Map<PasswordProperties, String> map = PasswordEncodingUtils.decode(hashedPassword);
		byte[] result = hash(map, userPassword);
		
		logger. info("Checking if password is matching...");
		Boolean isMatching = areEqual( result, Base64.decode(map.get(PasswordProperties.PASSWORD)));
		logger.info("Password is matching: " + isMatching);
		
		return isMatching;
	}
	
	@Override
	byte[] hash(Map<PasswordProperties, String> map, String userPassword) {
		logger.info("hashing password using know properties...");
		
		char[] password = userPassword.toCharArray();
		Argon2Parameters.Builder builder = initArgon(map);
		Argon2BytesGenerator gen = new Argon2BytesGenerator();

        gen.init(builder.build());

        byte[] result = new byte[DEFAULT_OUTPUTLEN];

        gen.generateBytes(password, result, 0, result.length);
        logger.info("hashing completed!");
        
        
        return result;
	}
	
    private boolean areEqual( byte[] a, byte[] b) {
        return Arrays.areEqual(a, b);
    }

}
