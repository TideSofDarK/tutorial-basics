package org.tides.tutorial;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private boolean running;
	
	public static int WIDTH = 400;
	public static int HEIGHT = 300;
	public static String NAME = "TUTORIAL 1";
	
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	
	public static Sprite hero;
	private static int x = 0;
	private static int y = 0;
	
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	public void run() {
		long lastTime = System.currentTimeMillis();
		long delta;
		
		init();
		
		while(running) {
			delta = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();	
			render();
			update(delta);
		}
	}
	
	public void init() {
		addKeyListener(new KeyInputHandler());
		hero = getSprite("man.png");
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		
		Graphics g = bs.getDrawGraphics(); //получаем Graphics из созданной нами BufferStrategy
		g.setColor(Color.black); //выбрать цвет
		g.fillRect(0, 0, getWidth(), getHeight()); //заполнить прямоугольник 
		hero.draw(g, x, y);
		g.dispose();
		bs.show(); //показать
	}
	
	public void update(long delta) {
		if (leftPressed == true) {
			x--;
		}
		if (rightPressed == true) {
			x++;
		}
	}

	public Sprite getSprite(String path) {
		BufferedImage sourceImage = null;
		
		try {
			URL url = this.getClass().getClassLoader().getResource(path);
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sprite sprite = new Sprite(Toolkit.getDefaultToolkit().createImage(sourceImage.getSource()));
		
		return sprite;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		JFrame frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		game.start();
	}
	
	private class KeyInputHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
		} 
		
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
		}
	}
}
