/* SWEN20003 Object Oriented Software Development
 * Author: Tian Luan <tluan1>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Item in the world. Can be picked up and affect the player
 */
public class Item 
{	
	/** name of the item */
	String name = null;
	/** Id of the item */
	int Id;
	/** image of the item */
	Image img = null;
	/** The effect number of the item */
	int effectNumber = 0;
	/** The position coordinates, in pixels  */
	double x, y;	
	/** status of having been picked up */
	boolean isPickedUp;
	
	/**
	 * Instantiates a new item.
	 *
	 * @param data the list of property data for item
	 * @param itemImgPath location of the item images directory
	 * @throws SlickException the slick exception
	 */
	public Item(String[] data, String itemImgPath) 
	throws SlickException
	{
		name = data[0];
		img = new Image(itemImgPath + data[1]);
		effectNumber = Integer.parseInt(data[2]);
		x = Double.parseDouble(data[3]);
		y = Double.parseDouble(data[4]);
		isPickedUp = false;
	}	

	/**
	 * Affect the player's property by a certain number
	 *
	 * @param player the player
	 */
	public void affect(Player player) {
		player.inventory[Id] = true;
		if (!isPickedUp) {
			// 0, 1, 2, (3) represent the index of items in the item list
			if (Id == 0) {
				player.setMaxHp(player.getMaxHp() + effectNumber);
				player.setHp(player.getHp() + effectNumber);				
			} else if (Id == 1) {
				player.setMaxDamage(player.getMaxDamage() + effectNumber);
			} else if (Id == 2) {
				player.setCoolDown(player.getCoolDown() + effectNumber);
			}
		}
	}
	
	/**
	 * Draw the items which has not been picked up on the ground
	 * when it is in the range of camera
	 *
	 * @param g Graphics object, used for drawing
	 * @param camera the camera
	 */
	public void render(Graphics g, Camera camera) {
		if (!isPickedUp && x < camera.getMaxX() && x > camera.getMinX()
    			&& y < camera.getMaxY() && y > camera.getMinY()) 
			img.drawCentered((int)x, (int)y);
	}

	/**Gets the id.
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**Sets the id.
	 * @param id the new id
	 */
	public void setId(int id) {
		Id = id;
	}

	/**Gets the img.
	 * @return the img
	 */
	public Image getImg() {
		return img;
	}

	/**Gets the effect number.
	 * @return the effect number
	 */
	public int getEffectNumber() {
		return effectNumber;
	}

	/**Gets the x coordinate
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**Gets the y coordinate
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**Checks if has been picked up.
	 * @return true, if is picked up
	 */
	public boolean isPickedUp() {
		return isPickedUp;
	}
	
	/**Sets the picked up.
	 * @param isPickedUp the new picked up
	 */
	public void setPickedUp(boolean isPickedUp) {
		this.isPickedUp = isPickedUp;
	}
}
