package hmPKG;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 * Enemy is an Entity that can also shoot and turn on its own.
 */
public class Enemy extends Entity{
	private int ticksHasSeenPlayer;
	private Line2D tracer;
	private double degree;
	private String movementType;
	private BufferedImage image2;
	private boolean alive;
	/**
	 * Constructs an enemy given an x and y position
	 * @param px the x position
	 * @param py the y position
	 */ 
	public Enemy(int px, int py, String move, int xv, int yv) {
		super(px, py);
		alive = true;
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/columbianSMG.png"));
			bounds = new Rectangle(x, y+image.getHeight()/2, image.getWidth(), image.getHeight()/2);
			image2 = ImageIO.read(getClass().getResourceAsStream("/columbiandead.png"));
		}catch(Exception e){
			
		}
		ticksHasSeenPlayer = 0;
		tracer = null;
		movementType=move;
		velX=xv;
		velXO=xv;
		velY=yv;
		velYO=yv;
	}
	/**
	 * Returns the movement type of the enemy in the form of a String. A - stationary, B - back and forth, C - clockwise, D - counterclockwise.
	 * @Return the movement type
	 */
	public String getMovementType(){
		return movementType;
	}
	/**
	 * Returns true if the enemy is not dead, false otherwise
	 * @return true if the enemy is not dead, false otherwise
	 */
	public boolean isAlive() {
		return alive;
	}
	/**
	 * Manages the Enemy's death (upon the player shooting it).
	 */
	public void die() {
		alive = false;
		velX = 0;
		velXO = 0;
		velY = 0;
		velYO = 0;
		image = image2;
	}
	/**
	 * Returns the angular degree the enemy will be rotated at
	 * @return the angular degree
	 */
	public double getDegree(){
		return degree;
	}
	/**
	 * Sets the enemys angular given the players location at the center of the screen (CX, CY), and is also given whether or not the enemy can "see" the player.
	 * @param canSeePlayer true if the enemy can see the player, false otherwise
	 * @param CX the players x coordinate (the center x position)
	 * @param CY the players y coordinate (the center y position) 
	 */
	public void setDegree(boolean canSeePlayer, double CX, double CY){
		if(canSeePlayer){
			double cxe = getBounds().x+getBounds().getWidth()/2; //cxe stands for center x pos of enemy
			double cye = getBounds().y+getBounds().height/2;
			double dx = CX-cxe, dy = cye-CY;
			if(CY<=cye) {
				degree = Math.toDegrees(Math.atan(dx/dy));
			}else {
				degree = 180+Math.toDegrees(Math.atan(dx/dy));
			}
		}else{
			if(velXO>0){
				degree = 90;
			}
			if(velXO<0){
				degree = 270;
			}
			if(velYO<0){
				degree = 0;
			}
			if(velYO>0){
				degree = 180;
			}
		}
	}
	/**
	 * Returns which direction (N/S/W/E) the enemy is moving in the form of a String.
	 * @return the String representing which direction the enemy is traveling in.
	 */ 
	public String getMovementDirection(){
		String ret = "";
		if(velXO>0){
			ret = "E";
		}
		if(velXO<0){
			ret = "W";
		}
		if(velYO<0){
			ret = "N";
		}
		if(velYO>0){
			ret = "S";
		}
		return ret;
	}
	/**
	 * Overidden update method updates the enemys position based on its current positions and modified velocities.
	 */
	public void update(){
		x+=velX;
		y+=velY;
		bounds.x=x;
		bounds.y=y+image.getHeight()/2;
	}
	/**
	 * Sets the line between the Enemy and Player given a Line2D object.
	 * @param l the Line2D object
	 */ 
	public void setLine(Line2D l) {
		tracer = l;
	}
	/**
	 * Returns the line between the Enemy and Player
	 * @return the line between the Enemy and Player
	 */
	public Line2D getTracer() {
		return tracer;
	}
	/**
	 * Increments the number of consecutive ticks that the enemy has "seen" the player by one.
	 */ 
	public void anotherTick(){
		ticksHasSeenPlayer++;
	}
	/**
	 * Returns the number of consecutive ticks the enemy has seen the player for.
	 * @return the number of consecutive ticks the enemy has seen the player for
	 */
	public int getTicksHasSeenPlayer(){
		return ticksHasSeenPlayer;
	}
	/**
	 * Sets the number of consecutive ticks the enemy has seen the player for back to 0.
	 */
	public void resetTicks(){
		ticksHasSeenPlayer=0;
	}
}
