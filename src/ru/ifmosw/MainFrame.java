package ru.ifmosw;


import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MainFrame extends JFrame{
	private Container root;
	static Random rr;
	private static GuiMap mapgui;
	static private boolean isStClk, isFnClk;
	static public Color pix[][];
	static public int avto[][];
	static public Point pred[][];
	static public int dist[];
	
	static final int X[] = {-1, 0, 0, 1};
	static final int Y[] = {0, -1, 1, 0};
	
	public static boolean try_move(int x, int y) {
		if(x < 0 || y < 0 || x >= 1321 || y >= 553) return false;
		int cRed, cBlue, cGreen;
		cRed = pix[x][y].getRed();
		cGreen = pix[x][y].getGreen();
		cBlue = pix[x][y].getBlue();
		return (cRed + cBlue + cGreen >= 600 && cRed + cBlue + cGreen <= 680);	
	}
	public static void bfs() {
		
		for(int i = 0; i < 1000000; i++)
			dist[i] = Integer.MAX_VALUE / 2;
		Queue <Point> q = new LinkedList<Point>();
		q.add(mapgui.spPoint);
		dist[mapgui.spPoint.y * 1321 + mapgui.spPoint.x] = 0;
		while(!q.isEmpty()) {
			Point st = q.peek();
			q.poll();
			for(int i = 0; i < 4; i++) {
				int toX = st.x + X[i];
				int toY = st.y + Y[i];
				if(try_move(toX, toY) && dist[toY * 1321 + toX] > dist[st.y * 1321 + st.x] + 1 + mapgui.colors[st.x][st.y]) {
					q.add(new Point(toX, toY));
					dist[toY * 1321 + toX] = dist[st.y * 1321 + st.x] + 1 + mapgui.colors[st.x][st.y];
					pred[toX][toY] = st;
				}
			}
		}
		System.out.println(dist[mapgui.stPoint.y * 1321 + mapgui.stPoint.x]);
	}
	public static void updateTraffic() {
		for(int i = 0; i < 1321; i++)
			for(int j = 0; j < 553; j++)
				if(try_move(i, j))
					avto[i][j] = rr.nextInt() % 2;
	}
	
	public MainFrame() throws Exception {
		super("MAPv1");
		root = getContentPane();
        mapgui = new GuiMap();
        rr = new Random();
        updateTraffic();
        mapgui.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Point cc = new Point(e.getX(), e.getY());
				if(cc.y < 40 && cc.y >= 0) {
					if(mapgui.isGoing == false) {
						if(cc.x < 440 && cc.x >= 0) {
							isStClk = true;
							isFnClk = false;
						}
						if(cc.x > 440 && cc.x < 880) {
							isStClk = false;
							isFnClk = true;
						}
						if(cc.x > 880 && mapgui.stPoint.x != -1 && mapgui.spPoint.x != -1) {
							isStClk = false;
							isFnClk = false;
							bfs();
							mapgui.isGoing = true;
						}
					} else {
						if(cc.x > 880) {
							isStClk = false;
							isFnClk = false;
							mapgui.isGoing = false;
							mapgui.stPoint = new Point(-1, -1);
							mapgui.spPoint = new Point(-1, -1);
						}
					}
				} else {
					if(isStClk == true && try_move(cc.x, cc.y)) {
						mapgui.stPoint = cc;
						mapgui.cnsStPoint = cc;
					}
					if(isFnClk == true && try_move(cc.x, cc.y)) {
						mapgui.spPoint = cc;
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
        root.add(mapgui);
        this.setSize(1327, 581); //1327, 581
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        new Thread(new GuiThread()).start();
	}
	public static void main(String[] args) {
		isStClk = false;
		isFnClk = false;
		dist = new int[1000000];
		pix = new Color[1321][553];
		pred = new Point[1321][553];
		avto = new int[1321][553];
		
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					new MainFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
    }
    class GuiThread implements Runnable{
        @Override
        public void run() {
        	while(mapgui.OnExec) {
        		mapgui.repaint();
        	}
        }
    }
	
}
