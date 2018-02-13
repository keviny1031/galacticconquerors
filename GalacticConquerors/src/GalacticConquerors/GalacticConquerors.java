package GalacticConquerors;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class GalacticConquerors extends JFrame implements ActionListener {

	GamePanel game;
	Alien enemy;
	Timer myTimer;

	public GalacticConquerors() {
		super("Galactic Conquerors");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(686, 678);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		game = new GamePanel();// creating the panel
		add(game);// adding the panel
		setVisible(true);
		myTimer = new Timer(10, this);// trigger 10 times per second
		myTimer.start();
	}

	public void actionPerformed(ActionEvent evt) {
		if (game != null) {
			game.move();
			game.repaint();
		}
	}

	public static void main(String[] arguments) {
		GalacticConquerors frame = new GalacticConquerors();
	}
}// *************************************************************

class GamePanel extends JPanel implements KeyListener, ActionListener {
	private ArrayList<ArrayList<Alien>> enemyColumns = new ArrayList<ArrayList<Alien>>(5), enemyRows = new ArrayList<ArrayList<Alien>>(6);
	private ArrayList<UFO> ufoList = new ArrayList<UFO>(1);
	private String screen = "game";
	private int shipx, shipy, bulletx, bullety, score = 0, level = 90;
	private double shipFrame = 0.0;
	private boolean[] keys;
	private boolean shot = false, validShot = true, ufo = false;
	private Font myFont = new Font("Rocket Propelled", Font.PLAIN, 35);
	private Font titleFont = new Font("Digital tech", Font.PLAIN, 25);
	private Font memeFont = new Font("Comic Sans MS", Font.PLAIN, 50);
	private Image[] ship = new Image[6];
	private Image back, bullet, nextScreen;
	private boolean drawn = false;
	private Timer tracker;

	public GamePanel() {
		setSize(686, 688);
		initEnemy();
		for (int i = 0; i < 6; i++) {
			ship[i] = new ImageIcon("src\\GalacticConquerors\\sprites\\ship" + i + ".png").getImage();
		}
		nextScreen = new ImageIcon("src\\GalacticConquerors\\sprites\\black.png").getImage();
		back = new ImageIcon("src\\GalacticConquerors\\sprites\\background.png").getImage();
		bullet = new ImageIcon("src\\GalacticConquerors\\sprites\\bullet.png").getImage().getScaledInstance(8, 34,
				Image.SCALE_DEFAULT);
		keys = new boolean[KeyEvent.KEY_LAST + 1];
		shipx = 320;
		shipy = 600;
		tracker = new Timer(15000, this);
		tracker.start();
		addKeyListener(this);
	}

	public void initEnemy() {
		enemyColumns.clear();
		for (int i = 0; i < 5; i++) {
			enemyColumns.add(new ArrayList<Alien>());
		}
		enemyColumns.get(0).addAll(Arrays.asList(new Alien(5, 140, level), new Alien(5, 185, level),new Alien(5, 230, level), new Alien(5, 275, level), new Alien(5, 320, level), new Alien (5, 365, level)));
		enemyColumns.get(1).addAll(Arrays.asList(new Alien(50, 140, level), new Alien(50, 185, level),new Alien(50, 230, level), new Alien(50, 275, level), new Alien(50, 320, level), new Alien(50, 365, level)));
		enemyColumns.get(2).addAll(Arrays.asList(new Alien(95, 140, level), new Alien(95, 185, level),new Alien(95, 230, level), new Alien(95, 275, level), new Alien(95, 320, level), new Alien(95, 365, level)));
		enemyColumns.get(3).addAll(Arrays.asList(new Alien(140, 140, level), new Alien(140, 185, level),new Alien(140, 230, level), new Alien(140, 275, level), new Alien(140, 320, level), new Alien(140, 365, level)));
		enemyColumns.get(4).addAll(Arrays.asList(new Alien(185, 140, level), new Alien(185, 185, level),new Alien(185, 230, level), new Alien(185, 275, level), new Alien(185, 320, level), new Alien(185, 365, level)));
		
		enemyRows.clear();
		for (int i = 0; i < 6; i++) {
			enemyRows.add(new ArrayList<Alien>());
		}
/*		enemyRows.get(0).addAll(Arrays.asList(new Alien(5, 140, level), new Alien(5, 185, level),new Alien(5, 230, level), new Alien(5, 275, level), new Alien(5, 320, level), new Alien (5, 365, level)));
		enemyRows.get(1).addAll(Arrays.asList(new Alien(50, 140, level), new Alien(50, 185, level),new Alien(50, 230, level), new Alien(50, 275, level), new Alien(50, 320, level), new Alien(50, 365, level)));
		enemyRows.get(2).addAll(Arrays.asList(new Alien(95, 140, level), new Alien(95, 185, level),new Alien(95, 230, level), new Alien(95, 275, level), new Alien(95, 320, level), new Alien(95, 365, level)));
		enemyRows.get(3).addAll(Arrays.asList(new Alien(140, 140, level), new Alien(140, 185, level),new Alien(140, 230, level), new Alien(140, 275, level), new Alien(320, 280, level), new Alien(140, 365, level)));
		enemyRows.get(4).addAll(Arrays.asList(new Alien(185, 140, level), new Alien(185, 185, level),new Alien(185, 230, level), new Alien(185, 275, level), new Alien(325, 280, level), new Alien(185, 365, level)));*/
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				enemyRows.get(i).add(enemyColumns.get(j).get(i));
			}
		}
	}

	public void displayShip(Graphics g) {
		g.drawImage(ship[(int) Math.floor(shipFrame)], shipx, shipy, this);
		if (shipFrame + 0.15 >= 5.75) {
			shipFrame = 0;
		} else {
			shipFrame += 0.15;
		}
	}

	public void displayAlien(Graphics g) {
		for (int i = 0; i < enemyColumns.size(); i++) {
			for (int j = 0; j < enemyColumns.get(i).size(); j++) {
				if (enemyColumns.get(i).get(j).getType() == 1) {
				g.drawImage(enemyColumns.get(i).get(j).getIcon()[(int) Math.floor(enemyColumns.get(i).get(j).getFrame())],
						(int) enemyColumns.get(i).get(j).getX() + 2, enemyColumns.get(i).get(j).getY() + 8, this);
				}
				else {
					g.drawImage(enemyColumns.get(i).get(j).getIcon()[(int) Math.floor(enemyColumns.get(i).get(j).getFrame())],
							(int) enemyColumns.get(i).get(j).getX(), enemyColumns.get(i).get(j).getY(), this);
						
				}
				//g.drawRect((int)enemyColumns.get(i).get(j).getX(), (int)enemyColumns.get(i).get(j).getY(),45,45);
				enemyColumns.get(i).get(j).move(enemyColumns, level);
			}
		}
	}
	
	public void displayUFO(Graphics g) {
		for (int i = 0; i < 4; i++) {
			g.drawImage(ufoList.get(0).getIcon()[(int) Math.floor(ufoList.get(0).getFrame())], (int) ufoList.get(0).getX(), ufoList.get(0).getY(), this);
			//g.drawRect((int)ufoList.get(0).getX(), (int)ufoList.get(0).getY(),100,30);
			ufoList.get(0).move(level);
		}
		
		if (ufoList.get(0).getX() <= -50) {
			ufo = false;
			ufoList.remove(ufoList.get(0));
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		if (keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_Z]) {
			if (screen == "game") {
				if (validShot == true) {
					shot = true;
					bulletx = shipx + 14;
					bullety = shipy - 23;
				}
				validShot = false;
			}
		}
		if (keys[KeyEvent.VK_ENTER]) {
			if (screen == "next") {
				tracker.restart();
				screen = "game";
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void displayScore(Graphics g) {
		g.setFont(myFont);
		g.drawString(score + "", 50, 75);
	}

	public void move() {
		if (screen == "game") {
			requestFocus();// without this line the program
			// wouldn't be able to "listen" to the key events

			if (keys[KeyEvent.VK_RIGHT]) {
				if (shipx <= 630)
					shipx += 3;
			}

			if (keys[KeyEvent.VK_LEFT]) {
				if (shipx >= 8)
					shipx -= 3;
			}
		}
	}
	
	public void shootBullet(Graphics g) {
		if (shot) {
			g.drawImage(bullet, bulletx, bullety, this);
			bullety -= 7;
		}
		if (bullety <= 85) {
			shot = false;
			validShot = true;
		}
	}

	public void titleScreen(Graphics g) {
		g.drawImage(back, 0, 0, this);
	}
	
	public void drawNext(Graphics g) {
		g.drawImage(nextScreen, 0, 0, this);
		g.setFont(memeFont);
		g.drawString("CLICK ENTER", 200, 200);
	}

	public void drawGame(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(back, 0, 0, this);
		g2.setStroke(new BasicStroke(3));
		g2.setColor(new Color(255, 255, 255));
		g2.drawRect(1, 85, 675, 560);
		g2.setFont(myFont);
		g2.drawString("SCORE", 50, 40);
		g2.drawString("LIVES", 530, 40);
		g2.setFont(titleFont);
		g2.drawString("LEVEL " + level, 265, 60);
		displayShip(g);
		displayAlien(g);
		if (ufo == true) {
			displayUFO(g);
		}
		displayScore(g);
	}

	// all drawing code goes here
	public void paintComponent(Graphics g) {
		if (screen == "game") {
			drawGame(g);
			if (shot == true) {
				shootBullet(g);
				if (ufoList.size() > 0) {
					ufoList.get(0).checkStatus(bulletx, bullety);
					if (ufoList.get(0).checkAlive() == false) {
						System.out.println("UFO DEAD!");
						ufo = false;
						score += 150 * Math.pow(1.2, level - 1);
						bulletx = shipx + 14;
						bullety = shipy - 23;
						shot = false; 
						validShot = true;
						ufoList.remove(ufoList.get(0));
					}
				}
				
				for (int i = 0; i < enemyColumns.size(); i++) {
					if (enemyColumns.get(i).size() > 0) {
						for (int j = 0; j < enemyColumns.get(i).size(); j++) {
							enemyColumns.get(i).get(j).checkStatus(bulletx, bullety);
							if (enemyColumns.get(i).get(j).checkAlive() == false) {
								score += 50 * Math.pow(1.2,  level-1);
								bulletx = shipx + 14;
								bullety = shipy - 23;
								shot = false;
								validShot = true;
								enemyColumns.get(i).remove(enemyColumns.get(i).get(j));
								if (enemyColumns.get(i).size() == 0) {
									enemyColumns.remove(enemyColumns.get(i));
								}
								break;
							}
						}
					}
				}
			}
			if (enemyColumns.size() == 0) {
				tracker.stop();
				initEnemy();
				level+=1;
				screen = "next";
			}
		}
		if (screen == "next") {
			drawNext(g);
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Wow! A UFO!");
		if (ufo == false)
			ufoList.add(new UFO(700, 100));
		ufo = true;
	}
}

class Alien {
	private boolean alive = true;
	private int alieny, alienType, starty;
	private double frame = 0.0, moveInterval, alienx, startx;
	private Rectangle2D hitbox;
	private String img = "src\\GalacticConquerors\\sprites\\enemies\\";
	private Image[] icon;

	public Alien(int x, int y, int level) {
		alienType = ThreadLocalRandom.current().nextInt(1, 3); // increase this to 4 later (when we have 3 sprites)
		// alienType = 1;
		switch (alienType) {
		case 1:
			icon = new Image[7];
			for (int i = 0; i < 7; i++) {
				icon[i] = new ImageIcon(img + "crab" + i + ".png").getImage().getScaledInstance(43, 28,
						Image.SCALE_DEFAULT);
			}
			break;
		case 2:
			icon = new Image[4];
			for (int i = 0; i < 4; i++) {
				icon[i] = new ImageIcon(img + "bigHead" + i + ".png").getImage();
			}
			break;
		// case 3:
		// icon = new Image[]

		}
		moveInterval = 0.5 * Math.pow(1.25, level-1);
		alienx = x;
		alieny = y;
		startx = x;
		starty = y;
		
		hitbox = new Rectangle2D.Double(x, y, 45, 45);
	}

	public void move(ArrayList<ArrayList<Alien>> a, int level) {
		double maxRight = 625 - a.get(a.size()-1).get(0).startx; //Maximum distance that aliens can travel right
		double leftX = a.get(0).get(0).startx; //StartX of left-most alien
		int maxDown = 545 - maxY(a); //Maximum distance that aliens can travel down
		
		if (alienx > startx + maxRight) {
			moveInterval = -0.5 * (Math.pow(1.25, level-1));
			if (alieny + 15 < starty + maxDown) {
				alieny += 15;
			}
		}

		if (alienx < startx - leftX + 5) {
			moveInterval = 0.5 * (Math.pow(1.25, level-1));
			if (alieny + 15 < starty + maxDown) {
				alieny += 15;
			}
		}

		alienx += moveInterval;
		hitboxUpdate();
		if (frame + 0.1 >= icon.length) {
			frame = 0;
		}

		else {
			frame += 0.1;
		}
	}

	public void hitboxUpdate() {
		hitbox = new Rectangle2D.Double(alienx + moveInterval, alieny, 45, 45);
	}

	public void checkStatus(int x, int y) {
		if (hitbox.contains(x, y)) {
			alive = false;
		}
	}
	
	public int maxY(ArrayList<ArrayList<Alien>> a) {
		int highestY = 0;
		for (ArrayList<Alien> list : a) {
			if (list.get(list.size()-1).starty > highestY) {
				highestY = list.get(list.size()-1).starty;
			}
		}
		return highestY;
	}

	public boolean checkAlive() {
		return alive;
	}

	public void shoot() {

	}

	public double getX() {
		return alienx;
	}

	public int getY() {
		return alieny;
	}
	
	public int getHitHeight() {
		return (int) hitbox.getHeight();
	}
	
	public int getHitWidth() {
		return (int) hitbox.getWidth();
	}

	public int getType() {
		return alienType;
	}
	
	public double getFrame() {
		return frame;
	}

	public Image[] getIcon() {
		return icon;
	}

	public void moveX(int interval) {
		alienx += interval;
	}

	public void moveY() {
		alieny += 3;
	}
}

class UFO{
	private boolean alive = true;
	private double ufox, frame;
	private int ufoy;
	private Rectangle2D hitbox;
	private Image[] icon;
	
	public UFO(int x, int y) {
		icon = new Image[4];
		for (int i = 0; i < 4; i++) {
			icon[i] = new ImageIcon("src\\GalacticConquerors\\sprites\\ufo" + i + ".png").getImage();
		}
		ufox = x;
		ufoy = y;
		hitbox = new Rectangle2D.Double(x, y, 100, 30);
	}
	
	public void hitboxUpdate() {
		hitbox = new Rectangle2D.Double(ufox - 0.5, ufoy, 100, 30);
	}
	
	public void checkStatus(int x, int y) {
		if (hitbox.contains(x, y)) {
			alive = false;
		}
	}
	
	public boolean checkAlive() {
		return alive;
	}
		
	public void move(int level) {
		if (frame + 0.025 >= 4) {
			frame = 0;
		}

		else {
			frame += 0.025;
		}
		
		ufox -= 0.5 * Math.pow(1.1, level-1);
		hitboxUpdate();
		if (ufox < -50)
			alive = false;
	}
	
	public double getFrame() {
		return frame;
	}
	
	public Image[] getIcon() {
		return icon;
	}
	
	public double getX() {
		return ufox;
	}
	
	public int getY() {
		return ufoy;
	}
	
	public int getHitHeight() {
		return (int) hitbox.getHeight();
	}
	
	public int getHitWidth() {
		return (int) hitbox.getWidth();
	}
	
}
