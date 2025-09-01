package it.creativeraccoon.password.hashing.sdk.exceptions;

/**
 * Custom exception that identifies an error during the instantiated of a Hasher
 * 
 * @author ZeroBrushV2
 *
 */
public class PasswordHasherInstantiationException extends Exception {

	private static final long serialVersionUID = -8554575744431910462L;

	public PasswordHasherInstantiationException(String message) {
        super(message);
	}
	
	public PasswordHasherInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
