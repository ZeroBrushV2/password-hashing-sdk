package it.creativeraccoon.password.hashing.sdk.verifiers;


import it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties;

import java.util.Map;

/**
 * Basic class that will have to be extended by all the hashers.
 * @author ZeroBrushV2
 */
public abstract class PasswordVerifierBase implements PasswordVerifier {
	
	protected static final int DEFAULT_OUTPUTLEN = 32;
	
	public PasswordVerifierBase() {}
	
	abstract byte[] hash(Map<PasswordProperties, String> map, String userPassword);

}
