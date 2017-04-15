package com.Tanknet.Gan;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankNewMsg implements Msg{
	Tank t;
	TankClient tc;
	private int msgType = Msg.TANK_NEW_MSG;
	public TankNewMsg(Tank t){
		this.t= t;
	}

	public TankNewMsg(TankClient tc){
		this.tc = tc;
	}
	
	public TankNewMsg() {

	}
	
	public void send(DatagramSocket ds,String ip,int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(t.id);
			dos.writeInt(t.x);
			dos.writeInt(t.y);
			dos.writeInt(t.dir.ordinal());
			dos.writeBoolean(t.isGood());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(
				buf,buf.length,new InetSocketAddress(ip,udpPort));
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
			int x = dis.readInt();
			int y = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			boolean good = dis.readBoolean();
System.out.println("ID"+id+"\tX:"+x+"\tY:"+y+"\tDir:"+dir.toString()+"\tFlag:"+good);
			Tank t = new Tank(x,y,good,dir,tc);
			t.id = id;
			tc.tanks.add(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
