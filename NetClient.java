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
	
	int udpPort;
	
	private TankClient tc;
	
	DatagramSocket ds = null;
	
	String IP;
	
	public NetClient(TankClient tc){
		this.tc = tc;
	}
	
	public void connect(String IP,int port){
		this.IP = IP;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Socket s = null;
		try {
			s = new Socket(IP,port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			tc.myTank.id = dis.readInt();
			if(tc.myTank.id%2 == 0)
				tc.myTank.good = true;
			else
				tc.myTank.good = false;
System.out.println("Server: "+ s.getLocalPort());
System.out.println("Server give me a ID: "+ tc.myTank.id);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( s != null ){
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		new Thread(new UDPRecvThread()).start();
	}
	
	public void send(Msg msg){
		msg.send(ds,IP,TankServer.UDP_PORT);
	}
	
	private class UDPRecvThread implements Runnable{
		byte[] buff = new byte[1024];
		public void run() {
			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buff, buff.length);
				try {
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
			int type = 0;
			try {
				type = dis.readInt();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				System.out.println("NetClient dis error");
			}
			Msg msg = null;
			switch(type){
			case Msg.TANK_NEW_MSG:
				msg = new TankNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_MOVE_MSG:
				msg = new TankMoveMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_NEW_MSG:
				msg = new MissileNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_DEAD_MSG:
				msg = new TankDeadMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_DEAD_MSG:
				msg = new MissileDeadMsg(NetClient.this.tc);
				msg.parse(dis);
			default:
				System.out.println("NetClient have some errors");
			}
			
		}
	}
}
