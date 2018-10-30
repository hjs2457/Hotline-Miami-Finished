package hmPKG;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Entity is an entity with position and velocity data that can be moved. It also has a Rectangle thats used for collision detection.
 * It also has an image that is usually displayed instead of the rectangle 
 */
public class Entity{
	protected int x;
	protected int y;
	protected int velXO;
	protected int velYO;
	protected int velX;
	protected int velY;
	protected BufferedImage image;
	protected Rectangle bounds;
	/**
	 * Instantiates an Entity given a starting x and y position
	 * @param px the x position
	 * @param py the y position
	 */ 
	public Entity(int px, int py) {
		x = px;
		y = py;
	}
	/**
	 * Updates the entity based on the current positions and velocities
	 */
	public void update() {
		x+=velX;
		y+=velY;
		bounds.x=x;
		bounds.y=y;
	}
	/**
	 * Returns the entities image
	 * @return the Image
	 */
	public Image getImage(){
		return image;
	}
	/**
	 * Returns the current bounds as a Rectangle
	 * @return the Rectangle
	 */
	public Rectangle getBounds() {
		return bounds;
	}
	/**
	 * Returns the current x position
	 * @return the x position
	 */
	public int getX(){
		return x;
	}
	/**
	 * Returns the current y position
	 * @return the y position
	 */
	public int getY(){
		return y;
	}
	/**
	 * Returns the modified x velocity
	 * @return the modified x velocity
	 */
	public int getVelX(){
		return velX;
	}
	/**
	 * Returns the modified y velocity
	 * @return the modified  velocity
	 */
	public int getVelY(){
		return velY;
	}
	/**
	 * Returns the original (not modified by the moving of the entire screen) x velocity of the object.
	 * @return the original velocity
	 */
	public int getVelXO() {
		return velXO;
	}
	/**
	 * Returns the original (not modified by the moving of the entire screen) y velocity of the object.
	 * @return the original velocity
	 */
	public int getVelYO() {
		return velYO;
	}
	/**
	 * Sets the x velocity given a velocity value
	 * @param vel the x velocity
	 */
	public void setVelX(int vel){
		velX = vel;
	}
	/**
	 * Sets the y velocity given a velocity value
	 * @param vel the y velocity
	 */
	public void setVelY(int vel){
		velY = vel;
	}
	/**
	 * Sets the original x velocity given a velocity value
	 * @param vel the new original x velocity
	 */
	public void setVelXO(int vel){
		velXO = vel;
	}
	/**
	 * Sets the original y velocity given a velocity value
	 * @param vel the new original y velocity
	 */
	public void setVelYO(int vel){
		velYO = vel;
	}
	/**
	 * Sets the current x position given an x position
	 * @param paramx the x position
	 */
	public void setX(int paramx){
		x = paramx;
	}
	/**
	 * Sets the current y position given a y position
	 * @param paramy the y position
	 */
	public void setY(int paramy){
		y = paramy;
	}
	
}