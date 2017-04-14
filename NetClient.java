package com.Tanknet.Gan;

import java.io.IOException;
import java.net.Socket;

public class NetClient {
	public void connect(String IP,int port){
		try {
			Socket s = new Socket(IP,port);
System.out.println("Server: "+ s.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
