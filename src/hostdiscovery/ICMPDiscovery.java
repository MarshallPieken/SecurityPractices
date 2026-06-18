/*
 * ICMPDiscovery is meant to identify hosts on a network based on IPs. 
 * input is verified automatically in the InetAddress classes.
 * 
 * 
 * functionality for entering a domain and it obtaining the IP before identifying hosts will also be included.
 * 
 * */
package hostdiscovery;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ICMPDiscovery {

	
		//acquire IP addresses attached to specified domain name host
	public static InetAddress acquireIPsFromHost(String hostName) throws UnknownHostException {

		InetAddress ipv4Address = InetAddress.getByName(hostName);
		
		return ipv4Address; 
		
	}//end acquireIPsFromHost method
	
		
		//show host based on IP address
	public static String acquireHostFromIPv4(InetAddress targetIP) {
		
		String host = targetIP.getHostName();
		
		if (host.equals(targetIP.getHostAddress())) {
			//reverse DNS failed
			return null;
			
		} else { return host; }
		
	}// end acquireHostFromIPv4 method
	
	
	public static String acquireHostFromIPv6(InetAddress targetIP) {
		
		String host = targetIP.getHostName();
		
		if (host.equals(targetIP.getHostAddress())) {
			//reverse DNS failed
			return null;
			
		} else { return host; }
		
	}//end acquireHostFromIPv6 method

		
		
}//end ICMPDiscovery class
