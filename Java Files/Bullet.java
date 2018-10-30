package hmPKG;

import java.awt.Rectangle;
import java.awt.Rectangle;

/**
 * A bullet is a projectile shot by a player or an enemy. It extends the Entity class too.
 */ 
public class Bullet extends Entity{
	private boolean playerOwned;
	public static final int NETSPEED = 5*Game.SCREENSPEED;
	/**
	 * Constructs a bullet given a start x, start y, destination x, destination y, and whether or not it is played owned.
	 * @param px the start x;
	 * @param py the start y;
	 * @param destX the destination x
	 * @param destY the destination y
	 * @param pO whether the new bullet is player owned or not.
	 */
	public Bullet(int px, int py, int destX, int destY, boolean pO){
			super(px, py);
			playerOwned = pO;
			bounds = new Rectangle(x, y, 3, 3);
			int dX = destX-px;
			int dY = destY-py;
			double distance = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
			double time = distance / NETSPEED;
			velXO = (int)(dX/time);
			velYO = (int)(dY/time);
			if(dX<0) velX*=-1;
			if(dY<0) velY*=-1;
			velX = velXO;
			velY = velYO;
	}
	/**
	 * Returns true if the bullet is player owned, false otherwise.
	 * @return true if the bullet is player owned, false otherwise
	 */
	public boolean isPlayerOwned(){
		return playerOwned;
	}
}
