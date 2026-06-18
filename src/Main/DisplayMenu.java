/*
 * this is the help menu for the CLI program. It will list all the flags and helpful information you 
 * could need for executing the program properly.
 * 
 * */

package Main;


public class DisplayMenu {

	public static void displayHelp() {
		
		
		String helpText = """
				Below is a useful list of command arguments to execute your desired capability:
				
				-----Hashing------
				-M (or -MD5): Convert text into an MD5 hash and output it in either hex or base64 with the --hex and --base64 flags. (Default is hex)
				-P (or -PBKDF2): Convert text into a PBKDF2 hash and output it in either ehx or base64 with the --hex and --base64 flags. (Default is hex)
				-s (or -salt): add this flag to your -M, -b, or -P command to prepend your text with randomly generated salt before hashing for more secure storage.
				-h (or -help): Display this message.
				-C (or -Checksum): Use this command along to verify a stored checksum with a file or string of your choice.
					Checksums are essential for ensuring integrity and non-repudiation of a file or text.
				
				-----Host & Port Discovery-----
				-ph (or -pinghost): Perform IPv4 & IPv6 discovery on a host via ICMP.
				-p4 (or -pingIPv4): Perform host disocvery via IPv4 address.
				-p6 (or -pingIPv6): Perform host discovery via IPv6 address.
				-S (or -SYN): Perform a SYN stealth scan on a target domain or IP address to determine open ports. Add the domain/IP after the flag.
				-A (or -SYNACK): Perform a SYN/ACK (loud) port scan on a target IP or domain. add the domain/IP after the flag. 
				-D (or -DNS): Write this before a specific domain or IP to perform DNS reolution on it.
				
				""";
		

		System.out.println(helpText);
		
		
	}//end displayHelp method
	
	public static void endExecutionPrompt() {
		
		String endPrompt = """
				
				---------------------------------------------------
				| Please enter another command or ctrl+c to quit. |
				---------------------------------------------------
				""";
		
		System.out.println(endPrompt);
		
	}// end endExecutionPrompt method
	
	
}//end HelpMenu class

