package com.Tanknet.Gan;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;


//import java.util.UUID;

public class TankClient extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	Tank myTank = new Tank(50, 50, true, Dir.STOP, this);

	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	Image offScreenImage = null;

	NetClient nc = new NetClient(this);
	
	ConnDialog cDlg = new ConnDialog();

	public void paint(Graphics g) {
		g.drawString("missiles count:" + missiles.size(), 10, 50);
		g.drawString("explodes count:" + explodes.size(), 10, 70);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			// m.hitTanks(tanks);
			if(m.hitTank(myTank)){
				TankDeadMsg msg = new TankDeadMsg(myTank.id);
				nc.send(msg);
			}
			m.draw(g);
			// if(!m.isLive()) missiles.remove(m);
			// else m.draw(g);
		}

		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}

		for (int i = 0; i < tanks.size(); i++) {
//System.out.println("have a tank");
			Tank t = tanks.get(i);
			t.draw(g);
		}

		myTank.draw(g);
	}

	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void lauchFrame() {

		// for(int i=0; i<10; i++) {
		// tanks.add(new Tank(50 + 40*(i+1), 50, false, Direction.D, this));
		// }

		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);

		this.addKeyListener(new KeyMonitor());

		setVisible(true);

		//nc.connect("127.0.0.1", TankServer.TCP_PORT);
		
		new Thread(new PaintThread()).start();
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}

	private class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			
				myTank.keyReleased(e);
				
		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_F1)
				cDlg.setVisible(true);
			else
				myTank.keyPressed(e);
		}

	}
	
	
	class ConnDialog extends Dialog{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Button b = new Button("OK");
		TextField tIP = new TextField("127.0.0.1",12);
		TextField tPort = new TextField(""+TankServer.TCP_PORT,4);
		TextField tUPort = new TextField("2233",4);
		public ConnDialog(){
			super(TankClient.this,true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tIP);
			this.add(new Label("Server Port:"));
			this.add(tPort);
			this.add(new Label("My UDP Port"));
			this.add(tUPort);
			this.add(b);
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					String IP = tIP.getText();
					int port = Integer.parseInt(tPort.getText().trim());
					int uPort = Integer.parseInt(tUPort.getText().trim());
					nc.udpPort = uPort;
					nc.connect(IP, port);
					setVisible(false);
				}
				
			});
			this.pack();
			this.setLocation(300,300);
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					setVisible(false);
				}
			});
		}
	}
}
