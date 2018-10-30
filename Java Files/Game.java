package hmPKG;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
/**
 * Game manages the the majority of the games functionality as well as the map building.
 */
public class Game implements ActionListener, MouseListener, KeyListener, MouseMotionListener{
	public static Game game;
	public static final int SCREENSPEED = 8;
	public static final int DIFFICULTY = 50; //change this for testing/grading purposes, it represents the time it takes for enemy to shoot you, so a lower number is harder.
	private int screenVelX = 0;
	private int screenVelY = 0;
	private JFrame frame;	
	private Display disp;
	private Set<Rectangle> map;
	private ArrayList<Rectangle> walls;
	private LinkedList<Bullet> bullets;
	private LinkedList<Enemy> enemies;
	private BufferedImage car;
	private Player player;
	private double mouseAngle = 0;
	@SuppressWarnings("unused")
	private String data;
	//center x and center y coords
	private int CX;
	private int CY;
	// key pressed fields
	private boolean w;
	private boolean a;
	private boolean s;
	private boolean d;
	//bg color changing fields
	private boolean colorGoingUP = true;
	private Color bg;
	private int ticks;
	//other fields
	private boolean alive = true;
	private boolean end = false;
	private int kills = 0;
	private MusicThread mt;
	/**
	 * Constructs a Game.
	 */
	public Game() {
		mt = new MusicThread("FUTURECLUB.WAV");
		mt.start();
		ticks = 0;
		bg = Color.magenta.darker().darker().darker();
		data = "";
		CX = 1920/2;
		CY = 1080/2;
		try {
			car = ImageIO.read(getClass().getResourceAsStream("/car.png"));
			BufferedImage playerRef = ImageIO.read(getClass().getResourceAsStream("/thesongun.png"));
			player = new Player(CX-playerRef.getWidth()/2-1, CY-playerRef.getHeight()/2-35);
		} catch (IOException e) {}
		d = false;
		a = false;
		s = false;
		w = false;
		bullets = new LinkedList<Bullet>();
		walls = new ArrayList<Rectangle>();
		map = new HashSet<Rectangle>();
		enemies = new LinkedList<Enemy>();
		enemies.add(new Enemy(500,-600,"D",-2,0));
		enemies.add(new Enemy(100,-100,"D",2,0));
		enemies.add(new Enemy(1400,-600,"C",2,0));
		enemies.add(new Enemy(1800,-100,"C",-2,0));
		enemies.add(new Enemy(100,-800,"C",0,-2));
		enemies.add(new Enemy(1800,-800,"C",-2,0));
		enemies.add(new Enemy(100,-1700,"C",2,0));
		enemies.add(new Enemy(1800,-1700,"C",0,2));
		enemies.add(new Enemy(700,-1400,"B",0,0));
		enemies.add(new Enemy(1200,-1400,"B",0,0));
		enemies.add(new Enemy(-400,-1000,"B",0,-2));
		enemies.add(new Enemy(-100,-1700,"B",0,2));
		enemies.add(new Enemy(2000,-1700,"B",0,2));
		enemies.add(new Enemy(2300,-1000,"B",0,-2));
		for(Enemy en : enemies) {
			en.setLine(new Line2D.Double(CX,CY,en.getBounds().x+en.getBounds().getWidth()/2, en.getBounds().y+en.getBounds().getHeight()/2));
		}
		//horizontals
		walls.add(new Rectangle(0,100,875,40));
		walls.add(new Rectangle(1025,100,875,40));
		walls.add(new Rectangle(600,-200,200,40));
		walls.add(new Rectangle(1100,-200,200,40));
		walls.add(new Rectangle(0,-600,600,40));
		walls.add(new Rectangle(1300,-600,600,40));
		walls.add(new Rectangle(-500,-800,500,40));
		walls.add(new Rectangle(1900,-800,500,40));
		walls.add(new Rectangle(640,-1100,200,40));
		walls.add(new Rectangle(1100,-1100,200,40));
		walls.add(new Rectangle(640,-1500,200,40));
		walls.add(new Rectangle(1100,-1500,200,40));
		walls.add(new Rectangle(-500,-1700,2900,40));
		//verticals
		walls.add(new Rectangle(600,-200,40,100));
		walls.add(new Rectangle(600,40,40,100));
		walls.add(new Rectangle(1300,-200,40,100));
		walls.add(new Rectangle(1300,40,40,100));
		walls.add(new Rectangle(600,-600,40,100));
		walls.add(new Rectangle(600,-260,40,100));
		walls.add(new Rectangle(1300,-600,40,100));
		walls.add(new Rectangle(1300,-260,40,100));
		walls.add(new Rectangle(0,-600,40,700));
		walls.add(new Rectangle(1900,-600,40,740));
		walls.add(new Rectangle(0,-800,40,200));
		walls.add(new Rectangle(1900,-800,40,200));
		walls.add(new Rectangle(640,-1500,40,100));
		walls.add(new Rectangle(1260,-1500,40,100));
		walls.add(new Rectangle(640,-1160,40,100));
		walls.add(new Rectangle(1260,-1160,40,100));
		walls.add(new Rectangle(-500,-1700,40,900));
		walls.add(new Rectangle(2360,-1700,40,900));
		walls.add(new Rectangle(0,-1700,40,100));
		walls.add(new Rectangle(0,-1400,40,300));
		walls.add(new Rectangle(0,-860,40,100));
		walls.add(new Rectangle(1900,-1700,40,100));
		walls.add(new Rectangle(1900,-1400,40,300));
		walls.add(new Rectangle(1900,-860,40,100));
		//floors and end zone
		map.add(new Rectangle(600,600,200,200));
		map.add(new Rectangle(0,-800,1900,940));
		map.add(new Rectangle(-500,-1700,2900,900));
		//frame
		Timer t = new Timer(10, this);
		frame = new JFrame("HOTLINE MIAMI");
		disp = new Display();
		frame.add(disp);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);		
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image cursorIMG;
		try {
			cursorIMG = ImageIO.read(getClass().getResourceAsStream("/cursor.png"));
			Cursor cursor = toolkit.createCustomCursor(cursorIMG, new Point(16,16), "");
			frame.getContentPane().setCursor(cursor);
		} catch (Exception e) {System.out.println("cursor not loaded");}
		frame.setVisible(true);
		t.start();
	}
	/**
     * ActionPerformed is triggered repeatedly by the timer that creates action events.
     * @param e the action event
     */
	public void actionPerformed(ActionEvent e) {
		if(mt!=null && kills==enemies.size() && !end) {
			mt.stop();
			mt = null;
		}
		//see if player has won
		@SuppressWarnings("rawtypes")
		Iterator it = map.iterator();
		Rectangle g = (Rectangle)it.next();
		if(player.getBounds().intersects(g) && kills==enemies.size() && alive){
			mt = new MusicThread("DUST.WAV");
			mt.start();
			a = false;
			d = false;
			s = false;
			w = false;
			end = true;
			alive = false;
			player.setVelY(SCREENSPEED);
		}
		//manages the background color changing every 5 ticks
		if(ticks%5==0) {
			try{
				if(colorGoingUP){
					if(bg.getBlue() == 100){
						colorGoingUP = !colorGoingUP;
						bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue()-1);
					}else{
						bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue()+1);
					}     
		       }else{
		    	   if(bg.getBlue() == 10){
		    		   colorGoingUP=!colorGoingUP;
		    		   bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue()+1);
		    	   }else{
		    		   bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue()-1);
		    	   }
		       }  
			}catch(Exception ex){
				System.out.println (ex.getMessage());
				colorGoingUP=!colorGoingUP;
		    }
		}
		//movement - initial variables
		boolean moveX = false, moveY = false;
		if(a&&d) moveX = false;
		if(s&&w) moveY = false;
		if(s && !w) moveY = true;
		if(w && !s) moveY = true;
		if(a && !d) moveX = true;
		if(d && !a) moveX = true;
		//determine if the player can move after taking keys pressed into consideration, and seeing if the player would hit a wall if they were to move
		//if the player cant move in the first place then all other moving entities are reset to their original velocities (vxo/vyo)
		if(moveY){
			if(s) {
				for(Rectangle r : walls) {
					if(new Rectangle(r.x,r.y-SCREENSPEED,r.width,r.height).intersects(player.getBounds())){
						moveY = false;
					}
				}
			}else if(w) {
				for(Rectangle r : walls) {
					if(new Rectangle(r.x,r.y+SCREENSPEED,r.width,r.height).intersects(player.getBounds())){
						moveY = false;
					}
				}
			}
		}
		if(!moveY){
			screenVelY = 0;
			for(Bullet b : bullets) {
				b.setVelY(b.getVelYO());
			}
			for(Enemy en : enemies) {
				en.setVelY(en.getVelYO());
			}
		}
		if(moveX){
			if(a){
				for(Rectangle r : walls) {
					if(new Rectangle(r.x+SCREENSPEED,r.y,r.width,r.height).intersects(player.getBounds())) {
						moveX = false;
					}
				}
			}else if(d) {
				for(Rectangle r : walls) {
					if(new Rectangle(r.x-SCREENSPEED,r.y,r.width,r.height).intersects(player.getBounds())) {
						moveX = false;
					}
				}
			}
		}
		if(!moveX){
			screenVelX = 0;
			for(Bullet b : bullets) {
				b.setVelX(b.getVelXO());
			}
			for(Enemy en : enemies) {
				en.setVelX(en.getVelXO());
			}
		}
		//after seeing if the player can actually move, sets the player/screen velocity accordingly
		if(moveX) {
			if(a) {
				screenVelX = SCREENSPEED;
				for(Enemy en : enemies) {
					en.setVelX(en.getVelXO()+SCREENSPEED);
				}
				for(Bullet b : bullets) {
					b.setVelX(b.getVelXO()+SCREENSPEED);
				}
			}else if(d) {
				screenVelX = SCREENSPEED*-1;
				for(Enemy en : enemies) {
					en.setVelX(en.getVelXO()-SCREENSPEED);
				}
				for(Bullet b : bullets) {
					b.setVelX(b.getVelXO()-SCREENSPEED);
				}
			}
		}else {
			screenVelX = 0;
		}
		if(moveY) {
			if(w) {
				screenVelY = SCREENSPEED;
				for(Enemy en : enemies) {
					en.setVelY(en.getVelYO()+SCREENSPEED);
				}
				for(Bullet b : bullets) {
					b.setVelY(b.getVelYO()+SCREENSPEED);
				}
			}else if(s) {
				screenVelY = SCREENSPEED *-1;
				for(Enemy en : enemies) {
					en.setVelY(en.getVelYO()-SCREENSPEED);
				}
				for(Bullet b : bullets) {
					b.setVelY(b.getVelYO()-SCREENSPEED);
				}
			}
		}else {
			screenVelY = 0;
		}
		//update everything
		for(Rectangle w : walls) {
			w.x+=screenVelX;
			w.y+=screenVelY;
		}
		for(Rectangle m : map) {
			m.x+=screenVelX;
			m.y+=screenVelY;
		}
		for(Bullet b : bullets) {
			b.update();
		}
		//change directions of enemies if they gonna hit a wall
		for(Enemy en : enemies) {
			if(en.isAlive()) {
			Rectangle b = en.getBounds();
			b.x+=en.getVelX();
			b.y+=en.getVelY();
			for(Rectangle w : walls){
				if(b.intersects(w)){
					if(en.getMovementType().equals("B")){ //back n forth 180 degree turns
						if(en.getMovementDirection().equals("N")){
							if(screenVelY<=0){
								en.setVelYO(-1*en.getVelYO());
								en.setVelY(-1*en.getVelY());
							}else{
								en.setVelYO(-1*en.getVelYO());
								en.setVelY(screenVelY+en.getVelYO());
							}
						}else if(en.getMovementDirection().equals("S")){
							if(screenVelY>=0){
								en.setVelYO(-1*en.getVelYO());
								en.setVelY(-1*en.getVelY());
							}else{
								en.setVelYO(-1*en.getVelYO());
								en.setVelY(screenVelY+en.getVelYO());
							}
						}else if(en.getMovementDirection().equals("W")){
							if(screenVelX<=0){
								en.setVelXO(-1*en.getVelXO());
								en.setVelX(-1*en.getVelX());
							}else{
								en.setVelXO(-1*en.getVelXO());
								en.setVelX(screenVelX+en.getVelXO());
							}
						}else if(en.getMovementDirection().equals("E")){
							if(screenVelX>=0){
								en.setVelXO(-1*en.getVelXO());
								en.setVelX(-1*en.getVelX());
							}else{
								en.setVelXO(-1*en.getVelXO());
								en.setVelX(screenVelX+en.getVelXO());
							}
						}
					}
					if(en.getMovementType().equals("C")){ // clockwise 90 degree turns
						if(en.getMovementDirection().equals("N") || en.getMovementDirection().equals("S")){
							en.setVelXO(-1*en.getVelYO());
							en.setVelX(en.getVelXO()+screenVelX);
							en.setVelYO(0);
							en.setVelY(en.getVelYO()+screenVelY);
						}else if(en.getMovementDirection().equals("W") || en.getMovementDirection().equals("E")){
							en.setVelYO(en.getVelXO());
							en.setVelY(en.getVelYO()+screenVelY);
							en.setVelXO(0);
							en.setVelX(en.getVelXO()+screenVelX);
						}
					}
					if(en.getMovementType().equals("D")){ // counter clockwise 90 degree turns
						if(en.getMovementDirection().equals("N") || en.getMovementDirection().equals("S")){
							en.setVelXO(en.getVelYO());
							en.setVelX(en.getVelXO()+screenVelX);
							en.setVelYO(0);
							en.setVelY(en.getVelYO()+screenVelY);
						}else if(en.getMovementDirection().equals("W") || en.getMovementDirection().equals("E")){
							en.setVelYO(-1*en.getVelXO());
							en.setVelY(en.getVelYO()+screenVelY);
							en.setVelXO(0);
							en.setVelX(en.getVelXO()+screenVelX);
						}
					}
				}					
			}
			}
			en.update();
		}
		//remove bullet if hits wall
		for(Rectangle wall : walls) {
			for(int i = 0; i<bullets.size(); i++) {
				if(bullets.get(i).getBounds().intersects(wall)) {
					bullets.remove(i); 
					i--;
				}
			} 
		}
		//remove bullet and enemy on collision
		int eni = -1;
		int ii = -1;
		for(int en = 0; en<enemies.size(); en++){
			for(int i = 0; i<bullets.size(); i++) {
				if(bullets.get(i).getBounds().intersects(enemies.get(en).getBounds()) && bullets.get(i).isPlayerOwned()) {	
					eni=en;
					ii=i;
				}
			}
		}
		if(eni!=-1 && ii!=-1 && enemies.get(eni).isAlive()){
			kills++;
			bullets.remove(ii);
			enemies.get(eni).die();
		}
		//player bullet collisions
		for(int i = 0; i<bullets.size(); i++) {
			if(bullets.get(i).getBounds().intersects(player.getBounds()) && !bullets.get(i).isPlayerOwned()){
				a = false;
				d = false;
				s = false;
				w = false;
				alive = false;
			}	
		}
		//update tracers and update the angle the enemy is facing
		for(Enemy en : enemies) {
			en.setLine(new Line2D.Double(CX,CY,en.getBounds().x+en.getBounds().getWidth()/2, en.getBounds().y+en.getBounds().getHeight()/2));
			Line2D l = en.getTracer();
			boolean canSeePlayer = true;
			for(Rectangle w : walls) {
				if(l.intersects(w)) {
					canSeePlayer = false;
				}
			}
			if(!alive) canSeePlayer = false;
			if(!en.isAlive()) canSeePlayer = false;
			en.setDegree(canSeePlayer,(double)CX,(double)CY);
			if(canSeePlayer) {
				//SHOOT THE PLAYER AND ROTATE ENEMY
				if(en.getTicksHasSeenPlayer()==DIFFICULTY){
					Bullet b = new Bullet(en.getBounds().x+en.getBounds().width/2, en.getBounds().y+en.getBounds().height/2, CX, CY, false);
					bullets.add(b);
					en.resetTicks();
				}else{
					en.anotherTick();
				}
			}else{
				en.resetTicks();
			}
		}
		ticks++;
		disp.repaint(); 
	}
	/**
	 * Resets the game when the player dies.
	 */
	public void resetGame(){
		kills = 0;
		alive = true;
		player.reset();
		enemies = new LinkedList<Enemy>();
		bullets = new LinkedList<Bullet>();
		walls = new ArrayList<Rectangle>();
		map = new HashSet<Rectangle>();
		enemies.add(new Enemy(500,-600,"D",-2,0));
		enemies.add(new Enemy(100,-100,"D",2,0));
		enemies.add(new Enemy(1400,-600,"C",2,0));
		enemies.add(new Enemy(1800,-100,"C",-2,0));
		enemies.add(new Enemy(100,-800,"C",0,-2));
		enemies.add(new Enemy(1800,-800,"C",-2,0));
		enemies.add(new Enemy(100,-1700,"C",2,0));
		enemies.add(new Enemy(1800,-1700,"C",0,2));
		enemies.add(new Enemy(700,-1400,"B",0,0));
		enemies.add(new Enemy(1200,-1400,"B",0,0));
		enemies.add(new Enemy(-400,-1000,"B",0,-2));
		enemies.add(new Enemy(-100,-1700,"B",0,2));
		enemies.add(new Enemy(2000,-1700,"B",0,2));
		enemies.add(new Enemy(2300,-1000,"B",0,-2));
		walls.add(new Rectangle(0,100,875,40));
		walls.add(new Rectangle(1025,100,875,40));
		walls.add(new Rectangle(600,-200,200,40));
		walls.add(new Rectangle(1100,-200,200,40));
		walls.add(new Rectangle(0,-600,600,40));
		walls.add(new Rectangle(1300,-600,600,40));
		walls.add(new Rectangle(-500,-800,500,40));
		walls.add(new Rectangle(1900,-800,500,40));
		walls.add(new Rectangle(640,-1100,200,40));
		walls.add(new Rectangle(1100,-1100,200,40));
		walls.add(new Rectangle(640,-1500,200,40));
		walls.add(new Rectangle(1100,-1500,200,40));
		walls.add(new Rectangle(-500,-1700,2900,40));
		walls.add(new Rectangle(600,-200,40,100));
		walls.add(new Rectangle(600,40,40,100));
		walls.add(new Rectangle(1300,-200,40,100));
		walls.add(new Rectangle(1300,40,40,100));
		walls.add(new Rectangle(600,-600,40,100));
		walls.add(new Rectangle(600,-260,40,100));
		walls.add(new Rectangle(1300,-600,40,100));
		walls.add(new Rectangle(1300,-260,40,100));
		walls.add(new Rectangle(0,-600,40,700));
		walls.add(new Rectangle(1900,-600,40,740));
		walls.add(new Rectangle(0,-800,40,200));
		walls.add(new Rectangle(1900,-800,40,200));
		walls.add(new Rectangle(640,-1500,40,100));
		walls.add(new Rectangle(1260,-1500,40,100));
		walls.add(new Rectangle(640,-1160,40,100));
		walls.add(new Rectangle(1260,-1160,40,100));
		walls.add(new Rectangle(-500,-1700,40,900));
		walls.add(new Rectangle(2360,-1700,40,900));
		walls.add(new Rectangle(0,-1700,40,100));
		walls.add(new Rectangle(0,-1400,40,300));
		walls.add(new Rectangle(0,-860,40,100));
		walls.add(new Rectangle(1900,-1700,40,100));
		walls.add(new Rectangle(1900,-1400,40,300));
		walls.add(new Rectangle(1900,-860,40,100));
		map.add(new Rectangle(600,600,200,200));
		map.add(new Rectangle(0,-800,1900,940));
		map.add(new Rectangle(-500,-1700,2900,900));
	}
	/**
     * Paints onto the JFrame given a graphics object
     * @param g the graphics object
     */
	public void repaint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//fills the ever changing bg
		g.setColor(bg);
		g.fillRect(0, 0, 1920,1080);		
		//draw map and entities
		g.setColor(Color.white);
		@SuppressWarnings("rawtypes")
		Iterator it = map.iterator();
		Rectangle r = (Rectangle)it.next();
		g.drawImage(car, r.x,r.y,null);
		while(it.hasNext()) {
			r = (Rectangle)it.next();
			g.fillRect(r.x,r.y,r.width,r.height);
		}
		for(Bullet b : bullets) {
			g.setColor(Color.yellow);
			g.drawRect(b.getBounds().x,b.getBounds().y, b.getBounds().width+1, b.getBounds().height+1);
			g.setColor(Color.black);
			g.fillRect(b.getBounds().x,b.getBounds().y, b.getBounds().width+1, b.getBounds().height+1);
		}
		g.setColor(Color.black);
		for(Rectangle w : walls) {
			g.fillRect(w.x, w.y, w.width, w.height);
		}
//		g.setColor(Color.white);
//		for(Enemy e: enemies) {
//			Line2D l = e.getTracer();
//			g.drawLine((int)l.getX1(), (int)l.getY1(), (int)l.getX2(), (int)l.getY2());
//		}
		//player rotation and drawing
		AffineTransform at = g2d.getTransform(); //original transform
		AffineTransform at2 = AffineTransform.getRotateInstance(Math.toRadians(mouseAngle), CX, CY); //player transform
		g2d.setTransform(at2);
		if(alive){
			g2d.drawImage(player.getImage(), player.getX(), player.getY(), null);
		}else if(end == false){
			g2d.drawImage(player.getImage2(), player.getX(), player.getY(), null);
		}else if(end == true) {
			
		}
		
		//g2d.drawRect(player.getBounds().x,player.getBounds().y,player.getBounds().width,player.getBounds().height);
		for(Enemy e : enemies) {
			AffineTransform at3 = AffineTransform.getRotateInstance(Math.toRadians(e.getDegree()), e.getBounds().x+e.getBounds().width/2, e.getBounds().y+e.getBounds().height/2);
			g2d.setTransform(at3);
			g2d.drawImage(e.getImage(),e.getX(),e.getY(),null);
			//g2d.drawRect(e.getBounds().x, e.getBounds().y,e.getBounds().width, e.getBounds().height);			
		}
		g2d.setTransform(at);
		//drawing text
		g.setColor(Color.black);
		g.setFont(new Font("Impact",Font.BOLD,60));
		g.drawString(""+player.getAmmo(), 100, 1080-100);
		//g.setFont(new Font("Dialog",Font.PLAIN, 20));
		//g.drawString(data, 60, 60);
		if(!alive){
			g.setColor(Color.red);
			g.setFont(new Font("Impact",Font.BOLD, 64));
			if(!end) g.drawString("Press R to RESTART",700,400);
			else {
				g.drawString("The deed is done...", 700,400);
				g.drawString("Press ESC to exit.", 800,500);
			}
		}
	}
	/**
     * When a key is pressed, this will decide how to handle it based on which key was pressed
     * @param e the key event
     */
	public void keyPressed(KeyEvent e) {
		if(alive){
			if(e.getKeyCode()==KeyEvent.VK_W) {
				w = true;
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				s = true;
			}
			if(e.getKeyCode()==KeyEvent.VK_A) { 
				a = true;			
			}		
			if(e.getKeyCode()==KeyEvent.VK_D) {
				d = true;
			}	
		}else{
			if(e.getKeyCode()==KeyEvent.VK_R){
				if(end == false)
					resetGame();
			}
		}
		if(e.getKeyChar()==KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}
	/**
     * When a key is released, this will decide how to handle it based on which key was pressed
     * @param e the key event
     */
	public void keyReleased(KeyEvent e) {
		if(alive){
			if(e.getKeyCode()==KeyEvent.VK_W) {
				w = false;
			}
			if(e.getKeyCode()==KeyEvent.VK_A) {
				a = false;
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				s = false;
			}
			if(e.getKeyCode()==KeyEvent.VK_D) {
				d = false;
			}
		}
	}
	/**
     * Required by interface but is not needed
     * @param e the mouse event
     */
	public void keyTyped(KeyEvent e) {}
	/**
     * When the mouse is pressed, this will decide how to handle it based on the MouseEvent coordinates
     * @param e the mouse event
     */
	public void mousePressed(MouseEvent e) {
		if(player.canShoot() && alive) {
			player.shoot();
			Bullet b = new Bullet(CX,CY, e.getX(), e.getY(),true);
			bullets.add(b);
		}		
	}
	/**
     * Required by interface but is not needed
     * @param e the mouse event
     */
	public void mouseReleased(MouseEvent e) {}
	/**
     * Required by interface but is not needed
     * @param e the mouse event
     */
	public void mouseEntered(MouseEvent e) {}
	/**
     * Required by interface but is not needed
     * @param e the mouse event
     */
	public void mouseExited(MouseEvent e) {}
	/**
     * Required by interface but is not needed
     * @param e the mouse event
     */
	public void mouseClicked(MouseEvent e) {}
	/**
     * When the mouse is moved, this will decide how to handle it based on the MouseEvent coordinates
     * @param e the mouse event
     */
	public void mouseMoved(MouseEvent e) {
		if(alive){
			double x = e.getX(), y = e.getY();
			double dx = x-CX, dy = CY-y;
			if(y<=CY) {
				mouseAngle = Math.toDegrees(Math.atan(dx/dy));
			}else {
				mouseAngle = 180+Math.toDegrees(Math.atan(dx/dy));
			}
			data = dx + " " + dy + " " + Math.toDegrees(Math.atan(dx/dy));
		}
		
	}
	/**
     * Required by interface but is not needed
     * @param e the mouse event
     */
	public void mouseDragged(MouseEvent e) {}
}