package ru.ifmosw;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GuiMap extends JPanel {
	public boolean OnExec;
	static public boolean isGoing;
	BufferedImage backmap, startPanel, stopPanel, pStart, pFinish;
	static public Point stPoint, spPoint, cnsStPoint; 
	static private BufferedImage pointAnim[];
	static private int nCadr;
	static private long nowT, lastT;
	
	public GuiMap() throws Exception {
		pointAnim = new BufferedImage[5];
		for(int i = 0; i < 5; i++) 
			pointAnim[i] = ResourceLoader.getImage("point" + i + ".png");
		backmap = ResourceLoader.getImage("map.jpg"); // 1321, 553
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
	}
	@Override
	protected void paintComponent(Graphics g) {
		nowT = System.currentTimeMillis();
		g.drawImage(backmap, 0, 0, null);
		if(stPoint.x != -1) {
			g.drawImage(pStart, cnsStPoint.x - 17, cnsStPoint.y - 35, null);
		}
		if(spPoint.x != -1) {
			g.drawImage(pFinish, spPoint.x - 17, spPoint.y - 35, null);
		}
		if(isGoing == true) {
			
			g.drawImage(stopPanel, 0, 0, null);
			Point k = stPoint;
			
			if(MainFrame.dist[k.y * 1321 + k.x] != Integer.MAX_VALUE / 2) {
				g.setColor(Color.RED);
				while(!k.equals(spPoint)) {
					g.drawOval(k.x, k.y, 3, 3);
					k = MainFrame.pred[k.x][k.y];
				}
				g.drawImage(pointAnim[nCadr], stPoint.x - 5, stPoint.y - 5 , stPoint.x + 9, stPoint.y + 9, 0, 0, 248, 248, null);
				if(nowT - lastT > 20) {
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
