package com.Tanknet.Gan;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileDeadMsg implements Msg {

	int msgType = Msg.MISSILE_DEAD_MSG;
	TankClient tc;
	int id;
	int tankID;
	public MissileDeadMsg(int tankID,int id){
		this.id = id;
		this.tankID = tankID;
	}
	
	public MissileDeadMsg(TankClient tc){
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(tankID); 
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
			int tankID = dis.readInt();
			int id = dis.readInt();
			for (int i = 0; i < tc.missiles.size(); i++){
				Missile m = tc.missiles.get(i);
				if(m.tankID == tankID && m.id == id){
					m.live = false;
					tc.explodes.add(new Explode(m.x,m.y,this.tc));
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
