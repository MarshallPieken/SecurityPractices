/*
 * 
 * this file creates an MD5 hash for a given string or file, in either hex or base64 format. 
 * Base64 is primarily used for storage in plaintext format as it's shorter, 
 * while hex is primarily used for checksums. Unless handling massive amounts of hashed data, there 
 * is no specific advantage to storing a value in hex versus base64 formats.
 * 
 * the main external class used here is MessageDigest, in which we created an instance and called its MD5 functionality below.
 * 
 */

package hashing;

import java.security.*;
import java.nio.charset.StandardCharsets;

public class MD5Generator {
	
	
	/*---------------------Generate a hex MD5 hash--------------------------*/
	public static String generateMD5Hex(String input) {
		
		try {
			//create a MessageDigest instance
			MessageDigest msd = MessageDigest.getInstance("MD5");
			
			//update the string into bytes
			byte[] hash = msd.digest(input.getBytes(StandardCharsets.UTF_8));
			
			//convert the hash into hex format
			StringBuilder hex = new StringBuilder();
			for (byte b : hash) {
				hex.append(String.format("%02x", b));
			}//end for loop

			return hex.toString();
			
		} 
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Error: Excpetion thrown:", e);
		}//end try/catch
		
		
	}//end generateMD5 method
	
		
	/*---------------Generate a Base64 MD5 hash-----------------------*/
	public static String generateMD5Base64(String input) {
		
		try {
			//create a MessageDigesst instance
			MessageDigest msd = MessageDigest.getInstance("MD5");
			
			//update the string into bytes
			byte[] hash = msd.digest(input.getBytes(StandardCharsets.UTF_8));
			
			//convert the hash into hex format
			StringBuilder base64 = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
				base64.append(String.format("%02x", b));
			}//end for loop
			
			return base64.toString();
			
		} 
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Error: Exception thrown: ", e);
		}//end try/catch
		

	} //end generateMD5Base64 method
	
	

}//end class
