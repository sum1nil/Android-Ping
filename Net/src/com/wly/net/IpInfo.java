package com.wly.net;

public class IpInfo
{
	public String ip;
	public String hostName;
	public String response;
	public int ttlLeft;
	public int hops;
	
	public  IpInfo(String ip, String hostName, String response, int ttlLeft, int hops) {
			this.ip = ip;
			this.hostName = hostName;
			this.response = response;
			this.ttlLeft = ttlLeft;
			this.hops = hops;
	}
	
	public String ToString() { 
		StringBuilder sb = new StringBuilder();
		sb.append(response + "\n\n");
		sb.append("IP Address: " + ip + "\n");
		sb.append("Hostname: " + hostName + "\n");
		sb.append("TTL Left: " + ttlLeft + "\n");
		sb.append("Number of hops: " + hops + "\n");
		
		return sb.toString();
		
	}
}
