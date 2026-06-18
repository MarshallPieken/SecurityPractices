/*
 * 
 * Target range is going to come in as a number, whitespace, a hyphen, whitespace, and another (larger) number.
 * For example: 22-80, 26-65535
 * the program needs to recognize these numbers and put them into the correct method to be scanned and output for the user.
 * 
 * 
 * */


//each switch statement option can help choose which method to utilize.
package portscanner;

import java.lang.StringBuilder;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;


public class PortScan {

/*****************************Parameters****************************************/
	private static String substr1;
	private static String substr2;
	private static int bottomPort;
	private static int topPort;
	private static int[] portList;

	
/*****************************Constructor***********************************/
//	public SYNScan() {
//		this.substr1 = "";
//		this.substr2 = "";
//		this.portNumber = 0;
//		this.bottomPort = 0;
//		this.topPort = 0;
//		this.portList = new int[0];
//	}//end constructor
	
/******************Specify ports to scan from given input (e.g., "23-80")****************/
	public static int[] portParser(String input) {
		input.trim(); //get rid of potential whitespace
		
		//1) acquire substring until "-" and substring after "-"
		for (int i = 0; i < input.length(); i++) {
				
				if (input.charAt(i) == '-') { 
					
					substr1 = input.substring(0, i); 
					substr2 = input.substring(i + 1);
					break;
					
					}//end if
		}//end for loop
		
		
		//2) convert both to int
		if (substr1 != null && substr2 != null) {
			
			bottomPort = Integer.valueOf(substr1);
			topPort = Integer.valueOf(substr2);
		} 
		
		if (bottomPort < 1 || topPort > 65535 || bottomPort > topPort) {
			throw new IllegalArgumentException("Invalid port range: " + input);
		}
		
		//convert them into an integer array to be belt-fed into the port scanner methods below
		int[] portList = new int[topPort - bottomPort + 1];
		
		for (int i = 0; i < portList.length; i++) {
				portList[i] = bottomPort + i;
		}//end outer for loop

		return portList;
		
	}//end portParser method
	
	
/*****************************Guess each port's service***********************************/
		private static String guessService(int port) {
			Map<Integer, String> common = new HashMap<>();
			common.put(21, "ftp");
			common.put(22, "ssh");
			common.put(23, "telnet");
			common.put(25, "smtp");
			common.put(53, "dns");
			common.put(80, "http");
			common.put(110, "pop3");
			common.put(139, "netbios");
			common.put(143, "imap");
			common.put(443, "https");
			common.put(445, "smb");
			common.put(3389, "rdp");
			return common.getOrDefault(port, "");
	}//end guessPortService method
	
/******************Scan ports on a specified IPv4*****************/	
	public static StringBuilder findPorts(String target, String portRange) throws IOException {
		
		StringBuilder outputPortList = new StringBuilder();
		outputPortList.append(String.format("%-8s %-7s %-10s%n", "PORT", "STATE", "SERVICE")); //begin columns for output
		
		int[] portList = portParser(portRange);
		AtomicInteger openPorts = new AtomicInteger(0);
		AtomicInteger unresponsivePorts = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(10);
		CompletionService<String> completeThread = new ExecutorCompletionService<>(executor);
		
		for (int i: portList) { //iterate through the loop to print out whether each port is open 		

			completeThread.submit(() -> {
				try (Socket socket = new Socket()) {

					socket.connect(new InetSocketAddress(target, i), 500);
					openPorts.incrementAndGet();
					return String.format("%-8s %-10s %-10s%n", i + "/tcp", "OPEN", guessService(i));
				
				} catch (SocketTimeoutException e) { 
					unresponsivePorts.incrementAndGet(); 
					return String.format("%-8s %-10s %-10s%n", i + "/tcp", "FILTERED", "timeout");

				} catch (ConnectException e) {
					unresponsivePorts.incrementAndGet(); return null;
				} catch (IOException e) {
					unresponsivePorts.incrementAndGet(); return null;
				}/* end try/catch/catch/catch */ 

			}); //end completeThread.submit()
							
		}//end for loop
		
		for (int i = 0; i < portList.length; i++) {

			try {
				String line = completeThread.take().get(); 
				
				if (line != null) { outputPortList.append(line); }//end if
				
			} catch (ExecutionException e) { unresponsivePorts.incrementAndGet(); }
			  catch (InterruptedException e) { unresponsivePorts.incrementAndGet(); }//end try/catch/catch
			
		}//end for loop
		
		
		int open = openPorts.get();
		int unresponsive = unresponsivePorts.get();
		int total = portList.length;
		int closed = total - open - unresponsive;
		
		
		executor.shutdown();
		outputPortList.append(String.format("%nOpen: %d | Closed: %d | Unresponsive: %d | Total: %d",
				open, closed, unresponsive, total));
		
		return outputPortList;
				
	}//end findPorts method
	

}//end class SYNScan
