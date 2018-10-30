package hmPKG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 * Menu has a JFrame which represents the main menu screen of the game which will ultimately start up the game.
 */
public class Menu implements ActionListener, MouseListener{
		public static Menu m;
		private MenuDisplay md;
        private JFrame f;
        private Rectangle play;
        private Rectangle help;
        private Rectangle exit;
        private Rectangle back;
        private Color c;
        private boolean goingUpB = true;
        private boolean goingUpR = true;
        private boolean goingUpG = true;
        private int tick;
        private boolean drawHelp;
        private BufferedImage bg;
        private MusicThread mt;
	//main method
    public static void main(String[] args) {
        m = new Menu();
    }
    /**
     * Constructs the Menu.
     */
    public Menu() {
    	mt = new MusicThread("MISSMINNIE.WAV");
    	mt.start();
    	try{
    		bg = ImageIO.read(getClass().getResourceAsStream("/songgg.png"));
    	}catch(Exception e){}
    	drawHelp = false;
    	c = Color.green.brighter();
    	md = new MenuDisplay();
    	Timer t = new Timer(20,this);
    	f = new JFrame();
    	f.add(md);
    	f.setSize(1000,1000);
    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	f.setResizable(false);
    	f.setUndecorated(true);
    	f.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	f.addMouseListener(this);
    	f.setVisible(true);
    	play = new Rectangle(f.getWidth()/5-50,850,f.getWidth()/5,150);
    	help = new Rectangle(f.getWidth()/5*2,850,f.getWidth()/5,150);
    	exit = new Rectangle(f.getWidth()/5*2+f.getWidth()/5+50,850,f.getWidth()/5,150);
    	back = new Rectangle(f.getWidth()/5-50,850,f.getWidth()/5,150);
    	t.start();
    }
    /**
     * ActionPerformed is triggered repeatedly by the timer that creates action events.
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e){
    	md.repaint();
    }
    /**
     * Draws everything given a graphics object and keeps track of the color changing background.
     * @param g the graphics object
     */
    public void repaint(Graphics g){
    	g.setColor(c);
    	g.fillRect(0,0,1920,1080);
    	if(tick%2==0){
    		if(goingUpR){
    			try{
    				c = new Color(c.getRed()+1,c.getGreen(),c.getBlue());
    			}catch(Exception e){
    				goingUpR = false;
    			}
    			
    		}else{
    			try{
    				c = new Color(c.getRed()-1,c.getGreen(),c.getBlue());
    			}catch(Exception e){
    				goingUpR = true;                                                                                                                                                                                                                      
    			}
    			
    		}if(goingUpB){
    			try{
    				c = new Color(c.getRed(),c.getGreen(),c.getBlue()+1);
    			}catch(Exception e){
    				goingUpB = false;
    			}
    			
    		}else{
    			try{
    				c = new Color(c.getRed(),c.getGreen(),c.getBlue()-1);
    			}catch(Exception e){
    				goingUpB = true;
    			}
    			
    		}if(goingUpG){
    			try{
    				c = new Color(c.getRed(),c.getGreen()+1,c.getBlue());
    			}catch(Exception e){
    				goingUpG = false;
    			}
    		}else{
    			try{
    				c = new Color(c.getRed(),c.getGreen()-1,c.getBlue());
    			}catch(Exception e){
    				goingUpG = true;
    			}	
    		}
    	}
    	g.setColor(Color.black);
    	if(!drawHelp){
    		g.setFont(new Font("Impact",Font.BOLD, 64));
    		//g.drawRect(play.x,play.y,play.width,play.height);
    		//g.drawRect(help.x,help.y,help.width,help.height);
    		//g.drawRect(exit.x,exit.y,exit.width,exit.height);
    		g.drawString("PLAY",play.x+100,play.y+100);
    		g.drawString("HELP",help.x+100,help.y+100);
    		g.drawString("EXIT",exit.x+100,exit.y+100);
    		g.drawImage(bg,1920/2-bg.getWidth()/2,100,null);
    	}else{
    		g.setFont(new Font("Impact",Font.PLAIN, 64));
    		g.drawString("Controls:",200,100);
    		g.drawString("W - MOVE UP",200,200);
    		g.drawString("A - MOVE LEFT",200,300);
    		g.drawString("S - MOVE DOWN",200,400);
    		g.drawString("D - MOVE RIGHT",200,500);
    		g.drawString("MOUSE - AIM",200,600);
    		g.drawString("LCLICK - SHOOT",200,700);
    		g.drawString("ESC - EXITS",200,800);
    		g.drawString("  Instructions",1000,100);
    		g.drawString(" Decimate the Colombians that are",800,200);
    		g.drawString("stealing your business.",800,300);
    		g.drawString(" Use the controls to clear the",800,400);
    		g.drawString("building and then get to the car.",800,500);
    		g.drawString("",900,400);
    		//g.drawRect(back.x,back.y,back.width,back.height);
    		g.drawString("GO BACK",back.x+50,back.y+100);
    	}
    	tick++;
    }
    /**
     * When the mouse is pressed, this will decide how to handle it based on the MouseEvent coordinates
     * @param e the mouse event
     */
    public void mousePressed(MouseEvent e) {
		Rectangle r = new Rectangle(e.getX(),e.getY(),1,1);
		if(!drawHelp){
			if(r.intersects(play)){
				mt.stop();
				Game.game = new Game();
			}else if(r.intersects(help)){
				drawHelp = true;
			}else if(r.intersects(exit)){
				System.exit(0);
			}
		}else{
			if(r.intersects(back)){
				drawHelp=false;
			}
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
}




