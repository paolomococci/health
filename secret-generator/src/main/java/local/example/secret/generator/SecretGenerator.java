/**
 * The program creates a 256-bit HMAC-SHA-256 secret key 
 * using a secure random number generator, 
 * encodes that key in Base64, 
 * and prints the encoded string. 
 * This is useful when you need a random key 
 * for authentication or encryption purposes 
 * and want it in a portable textual format.
 */

// Declares that this class belongs to the package `local.example.secret.generator`.
// It is the namespace that organizes classes and interfaces.
package local.example.secret.generator;

// `KeyGenerator` is used to create a cryptographic key.
import javax.crypto.KeyGenerator;
// `SecretKey` represents the key that will be generated.
import javax.crypto.SecretKey;
// JsonMapper is a Jackson helper that makes it easy to read/write JSON using a fully-featured, type-safe API.
import tools.jackson.databind.json.JsonMapper;
// ObjectNode represents a JSON object node, a mutable map of key/value pairs that you can build up programmatically before serializing.
import tools.jackson.databind.node.ObjectNode;
// `SecureRandom` provides a cryptographically strong random number generator.
import java.security.SecureRandom;
// `Base64` is a utility class for encoding and decoding base-64 data.
import java.util.Base64;

/**
 * Declares a public class named `SecretGenerator`.
 * The class is accessible from any other class.
 */
public class SecretGenerator {

    /**
     * The entry point of the program:
     * - `public` - accessible from anywhere.
     * - `static` - belongs to the class, not to an instance.
     * - `void` - no return value.
     * - `String[] args` - array of command-line arguments.
     * - `throws Exception` - declares that the method may throw any exception.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // Calls the static method `getInstance` with the algorithm name `"HmacSHA256"`.
        // Returns a `KeyGenerator` object that can create keys suitable for HMAC with
        // SHA-256.
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        // Sets the key size to 256 bits.
        // Provides a new `SecureRandom` instance to supply randomness during key
        // generation.
        keyGen.init(256, new SecureRandom());
        // Generates a cryptographic secret key according to the configured algorithm
        // and size.
        // The resulting `SecretKey` object holds the raw key bytes.
        SecretKey key = keyGen.generateKey();

        // `key.getEncoded()` returns the raw byte array of the key.
        // `Base64.getEncoder()` obtains a Base64 encoder.
        // `encodeToString` converts the byte array into a readable Base64 string.
        // The string is stored in the variable `base64`.
        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());

        // Build a simple JSON object that contains a single field.
        JsonMapper mapper = JsonMapper.builder().build();
        // Create an empty ObjectNode and add the secretKey property with the Base64 value.
        ObjectNode jsonObjNode = mapper.createObjectNode().put("secretKey", base64);
        // Convert the ObjectNode into a JSON string.
        String jsonString = mapper.writeValueAsString(jsonObjNode);

        // Print the resulting JSON string (Base64-encoded key) to the console.
        System.out.println(jsonString);
    }
}
