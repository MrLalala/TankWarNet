package com.Tanknet.Gan;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankDeadMsg implements Msg {

	int msgType = Msg.TANK_DEAD_MSG;
	TankClient tc;
	int id;
	public TankDeadMsg(int id){
		this.id = id;
	}
	
	public TankDeadMsg(TankClient tc){
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buff = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buff,buff.length,new InetSocketAddress(IP,udpPort));
		try {
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if(id == tc.myTank.id)
				return;
			for (int i = 0; i < tc.tanks.size(); i++){
				Tank t = tc.tanks.get(i);
				if(t.id == id){
					t.setLive(false);
//					exist = true;
					break;
				}
				//其他处理
			}
				
		} catch (IOException e) {
			System.out.println("TankMoveMsg error");
		}
	}
}
