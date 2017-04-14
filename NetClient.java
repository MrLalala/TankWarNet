package com.Tanknet.Gan;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class NetClient {
	
	private static int  UDP_PORT_START = 2333;
	
	private int udpPort;
	
	public NetClient(){
		udpPort = UDP_PORT_START++;
	}
	
	public void connect(String IP,int port){
		try {
			Socket s = new Socket(IP,port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			s.close();
System.out.println("Server: "+ s.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
