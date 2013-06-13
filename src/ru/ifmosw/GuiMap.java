package ru.ifmosw;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.*;

import sun.org.mozilla.javascript.internal.ast.TryStatement;

public class GuiMap extends JPanel {
	public boolean OnExec;
	static public boolean isGoing;
	BufferedImage backmap, startPanel, stopPanel, pStart, pFinish, copyback;
	static public Point stPoint, spPoint, cnsStPoint; 
	static private BufferedImage pointAnim[];
	static private int nCadr;
	static public int colors[][];
	static private long nowT, lastT, globalNowTime, globalLastTime;
	
	public GuiMap() throws Exception {
		colors = new int[1321][553];
		pointAnim = new BufferedImage[5];
		for(int i = 0; i < 5; i++) 
			pointAnim[i] = ResourceLoader.getImage("point" + i + ".png");
		backmap = ResourceLoader.getImage("map.jpg"); 
		copyback = ResourceLoader.getImage("map.jpg");// 1321, 553
		startPanel = ResourceLoader.getImage("startpanel.png");
		stopPanel = ResourceLoader.getImage("stoppanel.png");
		pStart = ResourceLoader.getImage("pstart.png");
		pFinish = ResourceLoader.getImage("pfinish.png");
		for(int i = 0; i < 1321; i++)
			for(int j = 0; j < 553; j++)
				MainFrame.pix[i][j] = new Color(backmap.getRGB(i, j));
		isGoing = false;
		OnExec = true;
		nCadr = 0;
		stPoint = new Point(-1, -1);
		spPoint = new Point(-1, -1);
		lastT = 0;
		globalLastTime = 0;
	}
	@Override
	protected void paintComponent(Graphics g) {
		globalNowTime = System.currentTimeMillis();
		if(globalNowTime - globalLastTime > 20000) {
			MainFrame.updateTraffic();
			backmap = copyback;
			int dx, dX, dy, dY;
			for(int i = 0; i < 1321; i++) {
				for(int j = 0; j < 553; j++) {
					if(MainFrame.try_move(i, j)) {
						dx = Math.max(0, i - 9);
						dX = Math.min(1321, i + 10);
						dy = Math.max(0, j - 9);
						dY = Math.min(553, j + 10);
						int cnt = 0;
						for(int u = dx; u < dX; u++)
							for(int v = dy; v < dY; v++)
								cnt += MainFrame.avto[u][v];
						if(cnt < 2) {
							colors[i][j] = 0;
							backmap.setRGB(i, j, Color.GREEN.getRGB());
						}
						if(cnt > 1 && cnt <= 7) {
							colors[i][j] = 15;
							backmap.setRGB(i, j, Color.YELLOW.getRGB());
						}
						if(cnt > 7) {
							colors[i][j] = 35;
							backmap.setRGB(i, j, Color.RED.getRGB());
						}
					}
				}
			}
			if(isGoing == true) MainFrame.bfs();
			globalLastTime = globalNowTime;
		}
		g.drawImage(backmap, 0, 0, null);
		if(stPoint.x != -1) {
			g.drawImage(pStart, cnsStPoint.x - 17, cnsStPoint.y - 35, null);
		}
		if(spPoint.x != -1) {
			g.drawImage(pFinish, spPoint.x - 17, spPoint.y - 35, null);
		}
		if(isGoing == true) {
			nowT = System.currentTimeMillis();
			g.drawImage(stopPanel, 0, 0, null);
			Point k = stPoint;
			if(MainFrame.dist[k.y * 1321 + k.x] != Integer.MAX_VALUE / 2) {
				g.setColor(Color.BLUE);
				while(!k.equals(spPoint)) {
					g.drawOval(k.x, k.y, 3, 3);
					k = MainFrame.pred[k.x][k.y];
				}
				g.drawImage(pointAnim[nCadr], stPoint.x - 5, stPoint.y - 5 , stPoint.x + 9, stPoint.y + 9, 0, 0, 248, 248, null);
				if(nowT - lastT > colors[stPoint.x][stPoint.y] + 20) {
					nCadr = (nCadr + 1) % 5;
					if(!stPoint.equals(spPoint))
						stPoint = MainFrame.pred[stPoint.x][stPoint.y];
					lastT = nowT;
				}
			}
		} else {
			g.drawImage(startPanel, 0, 0, null);
		}
	}
}
