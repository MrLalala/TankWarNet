package com.Tanknet.Gan;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

	public static final int TCP_PORT = 8888;
	
	private static ServerSocket ss;
	
	List<Client> clients = new ArrayList<Client>();
	
	public void start(){
		try {
			ss = new ServerSocket(TCP_PORT);
			while(true){
				Socket s = ss.accept();
				System.out.println("A tankClient is Connect: "+s.getInetAddress()+"\t:"+s.getPort());
				String ip = s.getLocalAddress().getHostAddress();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int port = dis.readInt();
				clients.add(new Client(ip,port));
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new TankServer().start();
	}
	
	private class Client{
		private String IP;
		private int port;
		public Client(String IP, int port) {
			this.IP = IP;
			this.port = port;
		}
		
		
	}

}
