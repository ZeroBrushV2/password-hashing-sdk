package it.creativeraccoon.password.hashing.sdk.utils;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UTILS class for the decode and encode the string that leads the user's "password" field
 * Use patterns to generate/read a string representing the password and owners on how it has been generated.
 * <br> <br>
 * This procedure is carried out to ensure that each password is unique and that it does not depend on the settings inserted.
 *
 * @author ZeroBrushV2
 */
@Getter
public abstract class PasswordEncodingUtils {

    private static final String ALGORITHM = "$a=";
    private static final String VERSION = "$v=";
    private static final String MEMORY_COST = "$m=";
    private static final String ITERATIONS = "$i=";
    private static final String THREADS = "$t=";
    private static final String PASSWORD = "$";
    private static final String base64Pattern = "(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?+";
    private static final String hashPattern = "^\\$a=(?<algorithm>\\S+),(?:\\$v=(?<version>\\d+),)\\$m=(?<memory>\\d+),\\$i=(?<iterations>\\d+),\\$t=(?<threads>\\d+)\\$(?<password>%s)$";

    /**
     *
     * Method useful for extracting the owners of a password and insert them in a map
     *
     * @param hashedPassword The user's password saved in the DB
     * @return A map indicating the ownership with which the password has been generated.
     *
     */
    public static Map<it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties, String> decode(String hashedPassword) {
        Pattern pattern = Pattern.compile(StringUtils.replace(hashPattern, "%s", base64Pattern));
        Matcher matcher = pattern.matcher(hashedPassword);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid hash '" + hashedPassword + "'");
        }

        String[] decodedSaltedPassword = splitSaltedPassword(matcher.group("password"));

        String salt = decodedSaltedPassword[0];
        String password = decodedSaltedPassword[1];

        Map<it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties, String> map = new HashMap<>();

        map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.ALGORITHM, matcher.group("algorithm"));
        if (matcher.group("version") != null)
            map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.VERSION, matcher.group("version"));
        map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.MEMORY_COST, matcher.group("memory"));
        map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.ITERATIONS, matcher.group("iterations"));
        map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.THREADS, matcher.group("threads"));
        map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.SALT, salt);
        map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.SALT_SIZE, String.valueOf(Base64.decode(salt).length));

        map.put(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.PASSWORD, password);

        return map;
    }

    /**
     * Method that allows you to create a string that represents the password hashing and the ownership with which it was generated
     *
     * @param values the ownership with which the password was generated
     * @return a string that represents the pattern {@link PasswordEncodingUtils#hashPattern}, the string contains both the secure password and all the characteristics with which it was created ({@link it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties}),
     */
    public static String encode(Map<it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties, String> values) {

        String encodedSaltedPassword = concatSaltAndPassword(values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.SALT), values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.PASSWORD));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(ALGORITHM.concat(values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.ALGORITHM))).append(",")
                .append(values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.VERSION) == null ? StringUtils.EMPTY : VERSION.concat(values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.VERSION))).append(",")
                .append(MEMORY_COST.concat(values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.MEMORY_COST))).append(",")
                .append(ITERATIONS.concat(values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.ITERATIONS))).append(",")
                .append(THREADS.concat(values.get(it.creativeraccoon.password.hashing.sdk.utils.PasswordProperties.THREADS)))
                .append(PASSWORD.concat(encodedSaltedPassword));

        return stringBuilder.toString();
    }


    private static String concatSaltAndPassword(String salt, String password) {
        String saltedPassword = salt.concat("$").concat(password);
        return Base64.toBase64String(saltedPassword.getBytes());
    }

    private static String[] splitSaltedPassword(String encodedPassword) {
        byte[] saltedPasswordBytes = Base64.decode(encodedPassword);

        String saltedPasswordString = new String(saltedPasswordBytes);
        return StringUtils.split(saltedPasswordString, "$");
    }
}
