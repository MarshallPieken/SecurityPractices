/*
 * 
 * main
 */

package Main;	

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class Main {

	public static void main(String[] args) throws Exception {
		
		//print out system launch screen:
		System.out.flush();
		System.out.println("""

========================================================================				
░░░░      ░░░        ░░░      ░░░        ░░░      ░░░░      ░░░  ░░░░░░░
▒▒▒  ▒▒▒▒▒▒▒▒  ▒▒▒▒▒▒▒▒  ▒▒▒▒  ▒▒▒▒▒  ▒▒▒▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒  ▒▒  ▒▒▒▒▒▒▒
▓▓▓▓      ▓▓▓      ▓▓▓▓  ▓▓▓▓▓▓▓▓▓▓▓  ▓▓▓▓▓  ▓▓▓▓  ▓▓  ▓▓▓▓  ▓▓  ▓▓▓▓▓▓▓
█████████  ██  ████████  ████  █████  █████  ████  ██  ████  ██  ███████
████      ███        ███      ██████  ██████      ████      ███        █
========================================================================
					
use -help to display the help menu if needed.
use ctrl+c to exit the program.
														
			""");
		
		//Scanner for input, outside the methods for security:
		Scanner input = new Scanner(System.in);
		
		//place the switch statement below in a loop for program continuation after method execution
		boolean running = true;
		do { //begin main program loop
			String cmd = input.nextLine();//read in the user input inside the loop

		//use switch statement to call each separate command and command/flag combination
		switch(cmd) {
		
		/*----------------------------------------display help menu----------------------------------------------*/
		case "-h":
		case "-help":
			DisplayMenu.displayHelp();
			
			DisplayMenu.endExecutionPrompt();
			break;
		
		/*----------------------------------------Create MD5 hash and output in hex----------------------------------------------*/
		case "-M":
		case "-MD5":
		case "-M --hex":
		case "-MD5 --hex":
			System.out.println("Please enter the text to be hashed into hex: ");
			String hexMD5ToBeHashed = input.nextLine();
			System.out.println(hashing.MD5Generator.generateMD5Hex(hexMD5ToBeHashed));
			
			DisplayMenu.endExecutionPrompt();
			break;
		
		/*------------------------------------Create MD5 hash and output in base64-----------------------------------------------*/
		case "-M --base64":
		case "-MD5 --base64":
			System.out.println("Please enter the text to be hashed into base64: ");
			String b64MD5ToBeHashed = input.nextLine();
			System.out.println(hashing.MD5Generator.generateMD5Base64(b64MD5ToBeHashed));
			
			DisplayMenu.endExecutionPrompt();
			break;
			
		/*----------------------------------------Create PBKDF2 hash----------------------------------------------*/	
		case "-P":
		case "-PBKDF2":
			System.out.println("Enter your passcode to be encrypted to PBKDF2:  ");
			String pbkdf2ToBeHashed = input.nextLine();
			System.out.println(hashing.PBKDF2Generator.generatePBKDF2(pbkdf2ToBeHashed));
			
			DisplayMenu.endExecutionPrompt();
			break;
			
		/*-----------------------------------ping a target host via domain---------------------------------------------------*/
		case "-ph":
		case "-pinghost":
			System.out.println("Enter your target domain: ");
			String ipAddress = input.nextLine();
			System.out.println(hostdiscovery.ICMPDiscovery.acquireIPsFromHost(ipAddress));
			
			DisplayMenu.endExecutionPrompt();
			break;
		
			
		/*----------------------------------------ping a target host via IPv4 address----------------------------------------------*/
		case "-p4":
		case "-pingIPv4":
			System.out.println("Enter your target IPv4 address: ");
			String ipv4String = input.nextLine();
			InetAddress ipv4Address = InetAddress.getByName(ipv4String);
			System.out.println(hostdiscovery.ICMPDiscovery.acquireHostFromIPv4(ipv4Address));
			
			DisplayMenu.endExecutionPrompt();
			break;
		
		/*----------------------------------------ping a target host via IPv6 address----------------------------------------------*/
		case "-p6":
		case "-pingIPv6":
			System.out.println("Enter your target IPv6 address: ");
			String ipv6String = input.nextLine();
			InetAddress ipv6Address = InetAddress.getByName(ipv6String);
			System.out.println(hostdiscovery.ICMPDiscovery.acquireHostFromIPv6(ipv6Address));
			
			DisplayMenu.endExecutionPrompt();
			break;
			
		/*----------------------------------------perform SYN stealth scan on target----------------------------------------------*/
		case "-A":
		case "-SYNACK":
			System.out.println("Enter your target (in the form of a domain, IPv4, or IPv6): ");
			String target = input.nextLine();
			
			System.out.println("Enter your port range with the bottom and top separated by a hypen (e.g., 22-80): ");
			String portRange = input.nextLine();
			
			/**************make the scan its own ExecutorService thread and show the user it's scanning*******************************/
			ExecutorService scanExecutor = Executors.newSingleThreadExecutor();
			Future<StringBuilder> scanFuture = scanExecutor.submit(() -> portscanner.PortScan.findPorts(target, portRange));
			
			ScheduledExecutorService spinner = Executors.newSingleThreadScheduledExecutor();
			
			spinner.scheduleAtFixedRate(new Runnable() {
				int dots = 0;
				
				@Override
				public void run() {
					dots = (dots + 1) % 4; //0..3
					
					System.out.print("\rScanning" + ".".repeat(dots) + "  ");
					System.out.flush();
				}//end run
			}, 0, 300, TimeUnit.MILLISECONDS); // end Spinner.scheduleAtFixedRate(new Runnable)
			
			StringBuilder result = scanFuture.get();
			
			spinner.shutdown();
			scanExecutor.shutdownNow();
			
			System.out.println("SCAN COMPLETE                  ");
			System.out.println("======================================================");
			System.out.println(result);
			System.out.println("======================================================");
			
			System.out.printf("\nPrint the results out to a file? (y/n)\n");
			String decision = input.nextLine();
			
			
			switch (decision) { //nested switch statement
				
			case "y":
			case "Y":
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Enter the filepath to write out your results: ");
					String filePath = br.readLine();
					
					//creating a FileWriter object to write in the file
					FileWriter fWriter = new FileWriter(filePath);
					fWriter.write(result.toString());
					
					fWriter.close();
					
				} catch (IOException e) { System.out.println(e.getMessage()); }
				
				break;
				
			case "n":
			case "N":
				break;
				
			default:
				System.out.println("Seems like you may have made a spelling error. "); 

				break;
			} // end nested switch statement
			
			DisplayMenu.endExecutionPrompt();
			
			break;
			
//		/*----------------------------------------Exit program----------------------------------------------*/	
//			case "exit":
//				exit = true; //end while loop, exit program
//				break;

		/*----------------------------------------Default to help menu----------------------------------------------*/
		default:
			System.out.println("Unknown command.");
			DisplayMenu.displayHelp();
			DisplayMenu.endExecutionPrompt();
			break; 
		
		}//end switch statement for class main
		
		} while (running);
		
		
		input.close();

	}//end main method
}//end Main class
