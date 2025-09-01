package it.creativeraccoon.password.hashing.sdk.verifiers;

/**
 *
 * Interface that specifies the behavior of all password verifiers
 * @author ZeroBrushV2
 */
public interface PasswordVerifier {

	/**
	 * Metodo che verifica se la password inserita dall'utente e' identica a quella salvata nel DB
	 * 
	 * @param userPassword password inserita da UI dall'utente per effettuare il login
	 * @param hashedPassword stringa rappresenta la password con la quale l'utente e' stato registrato nella piattaforma 
	 * @return true se le due password sono identiche, altrimenti false
	 */
	boolean verify(String userPassword, String hashedPassword);
}
