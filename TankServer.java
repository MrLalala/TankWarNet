package com.Tanknet.Gan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

	
	private static int ID = 100;
	public static final int TCP_PORT = 8888;
	public static final int UDP_PORT = 5555;
	
	
	private static ServerSocket ss;
	
	List<Client> clients = new ArrayList<Client>();
	
	public void start(){
		new Thread(new UDPSendThread()).start();
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (IOException e) {
			System.out.println("连接失败，请重启客户端");
			
			try {
				ss.close();
			} catch (IOException e1) {
				System.exit(0);
			}
		}
		
		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				String ip = s.getLocalAddress().getHostAddress();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int port = dis.readInt();
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);
System.out.println("A tankClient is Connect: " + s.getInetAddress() + ":" + s.getPort()+"----UDP----:"+port);
				clients.add(new Client(ip, port));
			} catch (IOException e) {
				System.out.println("连接失败，请重启服务端");
				System.exit(0);
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new TankServer().start();
	}
	
	private class Client{
		
		String IP;
		int port;

		public Client(String IP, int port) {
			this.IP = IP;
			this.port = port;
		}
	}
	
	private class UDPSendThread implements Runnable{
		
		byte[] buff = new byte[1024];
		
		public void run(){
			while(true){
				DatagramSocket ds = null;
				try {
					ds = new DatagramSocket(UDP_PORT);
				} catch (SocketException e) {
					System.out.println("连接失败");
					System.exit(0);
				} 
System.out.println("UDP Server started at port : "+UDP_PORT);
				while(ds != null){
					DatagramPacket dp = new DatagramPacket(buff,buff.length);
					try {
						ds.receive(dp);
						for(int i = 0;i<clients.size();i++){
							Client c = clients.get(i);
							dp.setSocketAddress(new InetSocketAddress(c.IP,c.port));							
							ds.send(dp);
						}
System.out.println("A Packet is received");
					} catch (IOException e) {
						System.out.println("have some questions");
					}
				}
			}
		}
	}
}
