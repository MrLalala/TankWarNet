package com.Tanknet.Gan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TankServer {

	public static final int TCP_PORT = 8888;
	private static ServerSocket ss;
	public static void main(String[] args) {
		try {
			ss = new ServerSocket(TCP_PORT);
			while(true){
				Socket s = ss.accept();
				System.out.println("A tankClient is Connect: "+s.getInetAddress()+"\t:"+s.getPort());
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

}
