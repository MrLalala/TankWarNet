package com.Tanknet.Gan;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;




public class NetClient {
	
	private static int  UDP_PORT_START = 2333;
	
	private int udpPort;
	
	private TankClient tc;
	
	DatagramSocket ds = null;
	
	public NetClient(TankClient tc){
		udpPort = UDP_PORT_START ++;
		try {
			ds = new DatagramSocket(2353);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
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
		
		TankMsg msg = new TankMsg(tc.myTank);
		send(msg);
		new Thread(new UDPRecvThread()).start();
	}
	
	public void send(TankMsg msg){
		msg.send(ds,"127.0.0.1",TankServer.UDP_PORT);
	}
	
	private class UDPRecvThread implements Runnable{
		byte[] buff = new byte[1024];
		public void run() {
			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buff, buff.length);
				try {
					//将收到的 数据通过 dp进行接收
					ds.receive(dp);
					parse(dp);
System.out.println("A Packet is received from server");
				} catch (IOException e) {
					System.out.println("have some questions");
				}
			}
		}

		private void parse(DatagramPacket dp) {
			DataInputStream dis = new DataInputStream(
					new ByteArrayInputStream(buff,0,dp.getLength()));
			TankMsg msg = new TankMsg(tc);
			msg.parse(dis);
		}
	}
}
