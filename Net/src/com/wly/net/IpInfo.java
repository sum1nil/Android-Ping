package com.wly.net;

public class IpInfo
{
	private String ip;
	private String hostName;
	private String response;
	private int ttlLeft;
	private int hops;
	
	public  IpInfo(String ip, String hostName, String response, int ttlLeft, int hops) {
			this.ip = ip;
			this.hostName = hostName;
			this.response = response;
			this.ttlLeft = ttlLeft;
			this.hops = hops;
	}
	
	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		sb.append(response + "\n\n");
		sb.append("IP Address: " + ip + "\n");
		sb.append("Hostname: " + hostName + "\n");
		sb.append("TTL Left: " + ttlLeft + "\n");
		sb.append("Number of hops: " + hops + "\n");
		
		return sb.toString();
		
	}
}
