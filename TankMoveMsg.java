package com.Tanknet.Gan;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankMoveMsg implements Msg{
	
	int msgType = Msg.TANK_MOVE_MSG;
	int id;
	int x,y;
	Dir dir;
	Dir ptDir;
	private TankClient tc;
	
	public TankMoveMsg(int id, int x, int y, Dir dir, Dir ptDir) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.ptDir = ptDir;
	}


	public TankMoveMsg(TankClient tc) {
		// TODO 自动生成的构造函数存根
		this.tc = tc;
	}


	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
			dos.writeInt(ptDir.ordinal());
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buff = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buff,buff.length,new InetSocketAddress(IP,udpPort));
		try {
			ds.send(dp);
		} catch (Exception e) {
			System.out.println("Move发送UDP数据包出错");
		}
	}


	public void parse(DataInputStream dis) {
		try{
			int id = dis.readInt();
			if(tc.myTank.id == id)
				return;
			int x = dis.readInt();
			int y = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			Dir ptDir = Dir.values()[dis.readInt()];
//			boolean exist = false;
			for (int i = 0; i < tc.tanks.size(); i++){
				Tank t = tc.tanks.get(i);
				if(t.id == id){
					t.dir = dir;
					t.ptDir = ptDir;
					t.x = x;
					t.y = y;
//					exist = true;
					break;
				}
				//其他处理
			}
		} catch (IOException e){
			System.out.println("TankMoveMsg error");
		}
	}
}
