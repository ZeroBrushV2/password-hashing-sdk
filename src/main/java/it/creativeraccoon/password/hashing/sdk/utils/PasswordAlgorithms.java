package it.creativeraccoon.password.hashing.sdk.utils;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Enum which represents all the algorithms managed by the Hashing libraryh
 * @author ZeroBrushV2
 */
public enum PasswordAlgorithms {
	ARGON2("argon2");
	
	private String label;
	
	PasswordAlgorithms(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static PasswordAlgorithms getByLabel(String label) {
		List<PasswordAlgorithms> hashers = Arrays.asList(PasswordAlgorithms.values());
		PasswordAlgorithms hasher = hashers.stream().filter(type -> type.label.contentEquals(label)).findFirst().orElse(null);
		return hasher;
	}
	
	
}
