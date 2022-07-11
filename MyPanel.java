package pongGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.*;

public class MyPanel extends JPanel implements KeyListener{
	public static final int WIDTH=800;
	public static final int HEIGHT=600;
	public static final int playersSpeed=7;
	public static final int TOLERANCE=playersSpeed;
	
	public int xBall;
	public int yBall;
	public int xSpeedBall=2;
	public int ySpeedBall=1;
	public static final int ballDimension=20;
	
	public int yPlayer1=HEIGHT/2;
	public int yPlayer2=HEIGHT/2;
	public static final int xPlayer1=10;
	public static final int xPlayer2=WIDTH-20;
	public int playersHeight=100; //lunghezza delle barrette
	public int playersWidth=10; //larghezza delle barrette
	public int player1Points=0;
	public int player2Points=0;
	public boolean[] player1Moving= {false,false};  //[0] verso alto, [1]verso basso
	
	
	Timer movingTimer;
	public Random random =new Random();
	
	
	
	
	public MyPanel() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(this);
		generateBall();
		movingTimer=new Timer(10,e->{
			move();
			repaint();
		});
		movingTimer.start();
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D =(Graphics2D) g;
		
		//___________________________PAINT SEPARATION______________________________________
		int rectHeight=10;
		int rectWidth=5;
		int rectSpacing=10;
		int numberOfRects=HEIGHT/(rectHeight+rectSpacing);
		for(int i=0;i<HEIGHT;i+=rectHeight+rectSpacing) {
			g2D.setPaint(Color.white);
			g2D.fillRect(WIDTH/2, i, rectWidth, rectHeight);	
		}
		//_____________________________PAINT POINTS__________________________________________
		g2D.setPaint(Color.white);
		g2D.setFont(new Font("Courier",Font.BOLD,70));
		g2D.drawString(player1Points+"", WIDTH/2 - 100, 100);
		g2D.drawString(player2Points+"",WIDTH/2+60 ,100);
		
		
		//____________________________PAINT PLAYERS__________________________________________
		g2D.fillRect(xPlayer1, yPlayer1, playersWidth, playersHeight);
		g2D.fillRect(xPlayer2, yPlayer2, playersWidth, playersHeight);
		
		//____________________________PAINT BALL_____________________________________________
		g2D.fillOval(xBall, yBall, ballDimension,ballDimension);
	}
	
	public void move() {
		
		//____________________PLAYER 1 MOVE___________________________
		if(player1Moving[0] && yPlayer1>0) {
			yPlayer1-=playersSpeed;
		}else if(player1Moving[1] && yPlayer1<(HEIGHT-playersHeight)) {
			yPlayer1+=playersSpeed;
		}
		//____________________PC MOVE__________________________
		
		pcMove();
		
		//____________________BALL MOVE________________________________
		xBall+=xSpeedBall;
		yBall+=ySpeedBall;
		checkBallCollision();
		checkScored();
	}
	
	private void pcMove() {
		if(xSpeedBall>0) {//ball moving towards Player2(pc)
			int xFutureBall=xBall;
			int yFutureBall=yBall;
			int xFutureSpeedBall=xSpeedBall;
			int yFutureSpeedBall=ySpeedBall;
			//CALCOLO DOVE FINIRA' LA PALLA
			while(xFutureBall<xPlayer2) {
				xFutureBall+=xFutureSpeedBall;
				yFutureBall+=yFutureSpeedBall;
				if((yFutureBall<=0 && xFutureBall>xPlayer1 && xBall< xPlayer2) ||
						(yFutureBall>=HEIGHT-ballDimension/*delay per sembrare ok*/ &&  xBall>xPlayer1 && xBall< xPlayer2)) {
					yFutureSpeedBall=yFutureSpeedBall*(-1);
				}
			}
			//yFutureBall-playersHeight/2 è l'altezza da raggiungere!
			if(yPlayer2<yFutureBall-playersHeight/2 - TOLERANCE && yPlayer2<(HEIGHT-playersHeight)) {
				yPlayer2+=playersSpeed;
			}else if(yPlayer2>yFutureBall-playersHeight/2 +TOLERANCE&& yPlayer2>0) {
				yPlayer2-=playersSpeed;
			}
		
		}else {//ball moving towards player1, pc go place middle
			if(yPlayer2<HEIGHT/2-playersHeight - TOLERANCE) {
				yPlayer2+=playersSpeed;
			}else if(yPlayer2>HEIGHT/2-playersHeight + TOLERANCE) {
				yPlayer2-=playersSpeed;
			}
		}
	}

	private void checkScored() {
		if(xBall<0) {
			player2Points++;
			generateBall();
		}else if(xBall>WIDTH-ballDimension) {
			player1Points++;
			generateBall();
		}
		
	}

	private void checkBallCollision() {              
		if( (xBall<=xPlayer1 && xBall>=0 && yBall>=yPlayer1-ballDimension/2 && yBall<=yPlayer1+playersHeight ) || 
				xBall>=xPlayer2-ballDimension /*delay per sembrare ok*/ &&xBall<= WIDTH && yBall>=yPlayer2 && yBall<=yPlayer2+playersHeight) {
			xSpeedBall=xSpeedBall*(-1);
			
			if(xSpeedBall<8) {
				int speedPlus=random.nextInt(3);
				if(xSpeedBall>0) {
					xSpeedBall+=speedPlus;
				}else {
				xSpeedBall-=speedPlus;
				}
			}
		}
		
		if((yBall<=0 && xBall>xPlayer1 && xBall< xPlayer2) ||
				(yBall>=HEIGHT-ballDimension/*delay per sembrare ok*/ &&  xBall>xPlayer1 && xBall< xPlayer2)) {
			ySpeedBall=ySpeedBall*(-1);
			int speedPlus=random.nextInt(1);
			if(ySpeedBall>0) {
				ySpeedBall+=speedPlus;
			}else {
				ySpeedBall-=speedPlus;
			}
		}
	}

	public void generateBall() {
		xBall=WIDTH/2;
		yBall=random.nextInt(HEIGHT);
		xSpeedBall=random.nextInt(3);
		ySpeedBall=random.nextInt(3);
		xSpeedBall+=3;
		ySpeedBall+=3;
		int dir=random.nextInt(100); //50% di possibilita di andare a sx o dx
		if(dir>50) {
			xSpeedBall= xSpeedBall*(-1);
		}
		
		 dir=random.nextInt(100); //50% di possibilita di andare su o giu
		if(dir>50) {
			ySpeedBall= ySpeedBall*(-1);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
	
		switch(e.getKeyChar()) {
			case 'w':
				player1Moving[0]=true;
				break;
			case 's':
				player1Moving[1]=true;
				break;
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case 87: //w
				player1Moving[0]=false;
				break;
			case 83: //s
				player1Moving[1]=false;
				break;
				
		}
		
	}
}











