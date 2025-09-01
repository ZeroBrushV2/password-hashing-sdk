package it.creativeraccoon.password.hashing.sdk.hashers;

import org.apache.commons.lang3.tuple.Pair;

import java.security.SecureRandom;

/**
 * 
 * Interface that specifies the behavior of all password hasher
 * @author ZeroBrushV2
 */
public interface PasswordHasher {

	/**
	 * Method that allows you to carry out the User's password hashing
	 * 
	 * 
	 * @param userPassword password inserted by UI or by a toolkit to record a user to the platform
	 * @return a string that represents the protected password and that also includes all ownership with which it was generated
	 */
	public String hash(String userPassword);
	

	/**
	 * Generates an alphanumeric random password using {@link SecureRandom}. <br>
	 * Length may not be inserted (the default value will be used); <br><br>
	 * 
	 * The random generation can be customized for each hasher through the override of this method.
	 * 
	 * @param length Length of the password to be generated, default 16.
	 * @return Pair, the string present in the pair Left is the light password (to be sent/showing the user concerned)
	 * While the string in the pair right is the password on which the hashing was carried out (to be saved in the database).
	 */
	public default Pair<String, String> generateRandomPassword(Integer length) {
		final String chars = "!@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	     int len = length == null ? 16 : length;
	     SecureRandom random = new SecureRandom();
	     StringBuilder sb = new StringBuilder(); 
	 
	     for (int i = 0; i < len; i++){
	         int randomIndex = random.nextInt(chars.length());
	         sb.append(chars.charAt(randomIndex));
	     }
	        
	     String passwordGenerata = sb.toString();
	     
		 String passwordHashed = hash(passwordGenerata);
		 return Pair.of(passwordGenerata, passwordHashed);
	}

}
