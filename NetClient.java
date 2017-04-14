package com.Tanknet.Gan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class NetClient {
	
	private static int  UDP_PORT_START = 2333;
	
	private int udpPort;
	
	private TankClient tc;
	
	public NetClient(TankClient tc){
		this.tc = tc;
	}
	
	public void connect(String IP,int port){
		Socket s = null;
		try {
			s = new Socket(IP,port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			tc.myTank.id = dis.readInt();
System.out.println("Server: "+ s.getLocalPort());
System.out.println("Server give me a ID: "+ tc.myTank.id);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( s != null ){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
