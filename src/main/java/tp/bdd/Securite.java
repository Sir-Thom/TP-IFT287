package tp.bdd;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Service de cryptage de chaîne de caractères. À utiliser pour la gestion des mots de passe
 * Code inspiré de https://www.geeksforgeeks.org/sha-256-hash-in-java/
 *
 * <pre>
 * Frédéric Bergeron
 * Université de Sherbrooke
 * Version 1.0 - 14 novembre 2019
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 *
 * Pré-condition
 *   l'algorithme SHA-256 doit être connu de java
 *
 * Post-condition
 *   le programme crypte la chaîne donnée selon l'algorithme SHA-256
 * </pre>
 */
public class Securite {


    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}

