package hmPKG;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 * Player is an entity that the user will control that is capable of shooting. Player does not move about the screen but still needs the rest of entities functions.
 */
public class Player extends Entity{
	private int ammo;
	private BufferedImage image2;
	/**
	 * Constructs a Player given a postion x and position y
	 * @param px the position x
	 * @param py the position y
	 */
	public Player(int px, int py){
		super(px,py);
		ammo = 30;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/thesongun.png"));
			bounds = new Rectangle(x, y+image.getHeight()/2, image.getWidth(), image.getHeight()/2);
			System.out.println ("A");
			image2 = ImageIO.read(getClass().getResourceAsStream("/thesondead.png"));
			System.out.println ("B");
		} catch (Exception e) {}
	}
	/**
	 * Returns the buffered image
	 * @return the buffered image
	 */ 
	public BufferedImage getImage2() {
		return image2;
	}
	/**
	 * Returns true if the player can shoot because it has ammo, false otherwise.
	 * @return true if the player can shoot because it has ammo, false otherwise
	 */
	public boolean canShoot() {
		return ammo>0;
	}
	/**
	 * Returns the players ammo count.
	 * @return the players ammo count
	 */
	public int getAmmo() {
		return ammo;
	}
	/**
	 * Reduces the players ammo count because the player shot.
	 */
	public void shoot() {
		ammo--;
	}
	/**
	 * Resets the players ammo back to 30 rounds. Needed for when the player dies and restarts.
	 */
	public void reset(){
		ammo = 30;
	}
}
