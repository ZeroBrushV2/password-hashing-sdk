package it.creativeraccoon.password.hashing.sdk.hashers;

import it.creativeraccoon.password.hashing.sdk.utils.HashingConfigurationProperties;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordAlgorithms;
import it.creativeraccoon.password.hashing.sdk.utils.PasswordEncodingUtils;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Hashing implementations using the implementation of Argon2 BouncyCastle. <br>
 * This implementation of Argon2 provides for the forcing of some properties for security reasons:
 * <ul>
 * <li>
 * A variation of Argon2 called "ID" is used, which appears to be the most suitable for password hashing.
 * </li>
 * <li>
 * Bouncycastle includes versions of the algorithm, at the moment the most recent is version 13.
 * </li>
 * </ul>
 *
 * @author ZeroBrushV2
 */
public class Argon2PasswordHasher extends PasswordHasherBase {

	private static final Logger logger = LoggerFactory.getLogger(Argon2PasswordHasher.class);

	public Argon2PasswordHasher(Map<HashingConfigurationProperties, String> securityProperties) {
		super(securityProperties);
		this.algorithm = PasswordAlgorithms.ARGON2.getLabel();
		this.version = Argon2Parameters.ARGON2_VERSION_13;
	}
	
	public Argon2PasswordHasher() {
		this.algorithm = PasswordAlgorithms.ARGON2.getLabel();
		this.version = Argon2Parameters.ARGON2_VERSION_13;
		this.memoryCost = 8000;
		this.iterations = 180;
		this.threads = 4;
		this.saltSize = 16;
	}

	
	@Override
	public String hash(String userPassword) {
		logger.info("hashing password...");
		char[] password = userPassword.toCharArray();
		
		byte[] salt = generateRandomSalt(saltSize);
		
		Argon2Parameters.Builder builder = initArgon(salt);
		Argon2BytesGenerator gen = new Argon2BytesGenerator();

        gen.init(builder.build());

        byte[] hashedPassword = new byte[DEFAULT_OUTPUTLEN];
        gen.generateBytes(password, hashedPassword, 0, hashedPassword.length);
        
        return PasswordEncodingUtils.encode(generateMap(hashedPassword, salt));
	}

	private Argon2Parameters.Builder initArgon(byte[] salt) {
		//The argon2parameters.argon2_id version is used as the most suitable for password hashing
		 return new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
	                .withVersion(version)
	                .withIterations(iterations)
	                .withMemoryAsKB(memoryCost)
	                .withParallelism(threads)
	                .withSalt(salt);
	}

}
