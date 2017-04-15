package com.Tanknet.Gan;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class MissileNewMsg implements Msg {

	int msgType = Msg.MISSILE_NEW_MSG;
	TankClient tc;
	Missile m;
	
	
	public MissileNewMsg(TankClient tc){
		this.tc = tc;
	}
	public MissileNewMsg(Missile m){
		this.m = m;
	}
	
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(m.tankID);
			dos.writeInt(m.x);
			dos.writeInt(m.y);
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.good);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf  = baos.toByteArray();
		
		DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort));
		try {
			ds.send(dp);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if(id == tc.myTank.id)
				return;
			int x = dis.readInt();
			int y = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			// System.out.println("ID:"+id+"\tX:"+x+"\tY:"+y+"\tDir:"+dir.toString()+"\tFlag:"+good);
			Missile m = new Missile(id,x,y,good,dir,this.tc);
			tc.missiles.add(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

