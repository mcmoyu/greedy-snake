package com.greedy.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MPanel extends JPanel implements KeyListener, ActionListener{
	
	ImageIcon title = new ImageIcon("title.jpg");
	ImageIcon body = new ImageIcon("body.png");
	ImageIcon up = new ImageIcon("up.png");
	ImageIcon down = new ImageIcon("down.png");
	ImageIcon left = new ImageIcon("left.png");
	ImageIcon right = new ImageIcon("right.png");
	ImageIcon food = new ImageIcon("food.png");
	
	// 蛇的数据结构
	int len = 3;
	int score = 0;
	int[] snakeX = new int[750];
	int[] snakeY = new int[750];
	String direction = "R"; // 方向：U、D、L、R(默认)
	boolean isStarted = false; // 是否启动状态
	boolean isFailed = false; // 是否失败
	Timer timer = new Timer(300, this);
	int foodX;
	int foodY;
	Random r = new Random();
	
	public MPanel() {
		initSnake(); // 初始化蛇
		this.setFocusable(true); // 可以获取焦点，可以获取键盘监听事件
		this.addKeyListener(this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		title.paintIcon(this, g, 25, 11);
		g.fillRect(25, 75, 850, 600);
		g.setColor(Color.WHITE);
		g.drawString("Len " + (len - 1), 750, 35);
		g.drawString("Score " + score, 750, 50);
		
		// 头
		switch(direction) {
		case "U":
			up.paintIcon(this, g, snakeX[0], snakeY[0]);
			break;			
		case "D":
			down.paintIcon(this, g, snakeX[0], snakeY[0]);
			break;
		case "L":
			left.paintIcon(this, g, snakeX[0], snakeY[0]);
			break;
		case "R":
			right.paintIcon(this, g, snakeX[0], snakeY[0]);
			break;
			default:
		}

		// 身体
		for(int i = 1; i < len-1; i++) {
			body.paintIcon(this, g, snakeX[i], snakeY[i]);
		}
		
		// 食物
		food.paintIcon(this, g, foodX, foodY);
		
		if(!isStarted) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString("Press Space To Start", 190, 300);
		}
		if(isFailed) {
			g.setColor(Color.RED);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString("Failed: Press Space To Restart", 80, 300);
		}
		
	}
	
	public void initSnake() {
		len = 4;
		snakeX[0] = 100;
		snakeY[0] = 100;
		snakeX[1] = 75;
		snakeY[1] = 100;
		snakeX[2] = 50;
		snakeY[2] = 100;
		setFoodLocation(getFoodX(), getFoodY());
		direction = "R";
		score = 0;
	}
	
	private int getFoodX() {
		return 25 + 25 * r.nextInt(34);
	}
	
	private int getFoodY() {
		return 75 + 25 * r.nextInt(24);
	}

	private void setFoodLocation(int x, int y) {
		for(int i = 0; i < len-1; i++) {
			if(x == snakeX[i] && y == snakeY[i]) {
				setFoodLocation(getFoodX(), getFoodY());
			} else {
				foodX = x;
				foodY = y;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_SPACE:
			if(isFailed) {
				isFailed = false;
				initSnake();
			} else {
				isStarted = !isStarted;
			}
			repaint();
			break;
		case KeyEvent.VK_UP:
			if(direction != "D")
			direction = "U";
			break;
		case KeyEvent.VK_DOWN:
			if(direction != "U")
			direction = "D";
			break;
		case KeyEvent.VK_LEFT:
			if(direction != "R")
			direction = "L";
			break;
		case KeyEvent.VK_RIGHT:
			if(direction != "L")
			direction = "R";
			break;
			default:
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(isStarted && !isFailed) {
			for(int i = len - 1; i > 0; i--) {
				snakeX[i] = snakeX[i-1];
				snakeY[i] = snakeY[i-1];
			}
			
			switch(direction) {
			case "U":
				snakeY[0] -= 25;
				if(snakeY[0] < 75) snakeY[0] = 650;
				break;
			case "D":
				snakeY[0] += 25;
				if(snakeY[0] > 650) snakeY[0] = 75;
				break;
			case "L":
				snakeX[0] -= 25;
				if(snakeX[0] < 25) snakeX[0] = 850;
				break;
			case "R":
				snakeX[0] += 25;
				if(snakeX[0] > 850) snakeX[0] = 25;
				break;
				default:
			}
			
			// 如果头与身体重合 - 游戏结束
			for(int i = 1; i < len; i++) {
				if(snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
					isFailed = true;
				}
			}
			
			// 如果头与食物重合
			if(snakeX[0] == foodX && snakeY[0] == foodY) {
				snakeX[len] = foodX;
				snakeY[len] = foodY;
				len+=1;
				score += len;
				timer.setDelay(320 - len*5);
				
				setFoodLocation(getFoodX(), getFoodY());
			}
			
			repaint();
		}
		timer.start();
	}
}
