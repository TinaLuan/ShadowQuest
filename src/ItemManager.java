
/* SWEN20003 Object Oriented Software Development
 * Author: Tian Luan <tluan1>
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Manage all items
 * Handles the creations, effects, updates and renderings
 */
public class ItemManager 
{	
	/** The list of all items. */
	private ArrayList<Item> items = new ArrayList<Item>(4);

	/**
	 * Instantiates a new item manager.
	 *
	 * @param itemDataPath location of the data for items directory
	 * @param itemImgPath location of the item images directory
	 * @throws SlickException the slick exception
	 */
	public ItemManager(String itemDataPath, String itemImgPath) 
	throws SlickException 
	{
		try {
			// Read the data file for items
			// Create all the items
			String line, splittedLine[];
			BufferedReader in = new BufferedReader(new FileReader(itemDataPath));
			while ((line = in.readLine()) != null) {
				splittedLine = line.split(","); // delimiter
				items.add(new Item(splittedLine, itemImgPath));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		giveItemsId();
	}

	/**
	 * Give all items id according to its index in the list
	 */
	private void giveItemsId() {
		for (Item oneItem : items) {
			oneItem.setId(items.indexOf(oneItem));
		}
	}

	/**
	 * Find the item which is nearby player
	 *
	 * @param player the player
	 * @param distance distance between the player and current item
	 * @return the item
	 */
	public Item detectNearItem(Player player, int distance) {
		Item theItem = null;
		for (Item oneItem : items) {
			if (Math.sqrt(Math.pow(player.getX() - oneItem.getX(), 2)
					+ Math.pow(player.getY() - oneItem.getY(), 2)) < distance) {
				theItem = oneItem;
			}
		}
		return theItem;
	}

	/**
	 * Draw all the unpicked-up items on the ground
	 *
	 * @param g the Slick graphics object, used for drawing.
	 * @param camera The camera object, used for determining the bound of rendering
	 */
	public void render(Graphics g, Camera camera) {
		for (Item oneItem : items) {
			if (!oneItem.isPickedUp())
				oneItem.render(g, camera);
		}
	}

	/**
	 * Update the item "ispickedup"status
	 *
	 * @param nearItem the near item that will be picked up
	 */
	public void update(Item nearItem) {
		nearItem.setPickedUp(true);
	}

	/** Draw all the picked-up items on the panel
	 *  Modified from the given code
	 *  
	 * @param g the g
	 * @param win the win
	 */
	public void renderOnPanel(Graphics g, boolean win) {
		// Coordinates to draw inventory item
		int inv_x, inv_y;
        inv_x = 490;
        inv_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT
            + ((RPG.PANEL_HEIGHT - 72) / 2);
        // draw all the picked up items
        for (Item oneItem : items) {	
			if (oneItem.isPickedUp()) { 
				if (oneItem.getId() != 3) {
				oneItem.getImg().draw(inv_x, inv_y);
				inv_x += 72;
				}
				// disappear from the panel, if being handed to the prince
				if (!win && oneItem.getId() == 3){
					oneItem.getImg().draw(inv_x, inv_y);
					inv_x += 72;
				}
			}	
        }
	}
	
}
