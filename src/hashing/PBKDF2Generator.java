/*
 * This is used to hash (primarily) passwords and other text/files utilizing the PBKDF2 algorithm. PBKDF2 is a modern, 
 * high-security algorithm and is recommended by NIST for its effectiveness.
 * 
 * Classes used are (in the import statements):
 * - MessageDigest
 * - NoSuchAlgorithmException
 * - SecretKeyFactory
 * - PBEKeySpec
 * 
 * The SecurityPractices program will use this for hashing and effective checksums for TRS operations. 
 * 
 * */

package hashing;

import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;


public class PBKDF2Generator {

	/*-------------------------------------Creating the PBKDF2------------------------------------------*/
	public static String generatePBKDF2(String input) throws Exception {
			try {
					
				byte[] salt = generateSalt();
					
				PBEKeySpec spec = new PBEKeySpec(input.toCharArray(), salt, 65536, 128); //(password, salt, iteration count, key length)
				SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				
				byte[] derived = factory.generateSecret(spec).getEncoded();
				
				for (int i = 0; i < derived.length; i++) {
				System.out.print(derived[i] + 128);
				}//end for loop
				System.out.println();
				
				return String.valueOf(derived);
				
			} catch ( NoSuchAlgorithmException e) { throw new Exception("PBKDF2 Error: ", e); }
			
	}//end PBKDF2ToHex method
	
	/*----------------------Generating the Salt for each PBKDF2 iteration--------------------*/
	public static byte[] generateSalt() {
		
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
		
	}//end generateSalt method

	
}//end PBKDF2Generator class
