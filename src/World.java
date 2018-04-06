/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Modified from Sample Solution
 * Author: Tian Luan <tluan1>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
    private UnitManager unitManager = null;
    private ItemManager itemManager = null;
    private Player player = null;
    private TiledMap map = null;
    private Camera camera = null;

    /** Map width, in pixels. */
    private int getMapWidth()
    {
        return map.getWidth() * getTileWidth();
    }

    /** Map height, in pixels. */
    private int getMapHeight()
    {
        return map.getHeight() * getTileHeight();
    }

    /** Tile width, in pixels. */
    private int getTileWidth()
    {
        return map.getTileWidth();
    }

    /** Tile height, in pixels. */
    private int getTileHeight()
    {
        return map.getTileHeight();
    }

    /** Create a new World object. 
     * @throws SlickException the slick exception
     * */
    public World()
    throws SlickException
    {
        map = new TiledMap(RPG.ASSETS_PATH + "/map.tmx", RPG.ASSETS_PATH);
        unitManager = new UnitManager(RPG.DATA_PATH + "/units.dat", 
        		RPG.DATA_PATH + "/units_data.txt", RPG.DATA_PATH + "/dialogue2.txt");
        player = UnitManager.getPlayer();
        itemManager = new ItemManager(RPG.DATA_PATH + "/items_data.txt", RPG.ASSETS_PATH + "/items/");
        camera = new Camera(player, RPG.SCREEN_WIDTH, RPG.SCREEN_HEIGHT, RPG.PANEL_HEIGHT);
   
    }

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
	 * @param isTDown The player's talk action
	 * @param isADown The player's attack action 
     * @param delta Time passed since last frame (milliseconds).
     * @throws SlickException the slick exception
     */
    public void update(int dir_x, int dir_y, int delta, boolean isTDown, boolean isADown)
    throws SlickException
    {
    		// Perform the actions and Update for the player and all other characters
        unitManager.update(dir_x, dir_y, isTDown, isADown, this, delta);
        
        // Follow the player and Update camera
        camera.followUnit(player);
        camera.update();
        
        // Pick up items and Update items
        Item nearItem;     
        if ((nearItem = itemManager.detectNearItem(player, 50)) != null ) {
	        nearItem.affect(player);
	        	itemManager.update(nearItem);
	    	}
        
    }
    
    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     * @throws SlickException the slick exception
     */
    public void render(Graphics g)
    throws SlickException
    {
        // Render the relevant section of the map
        int x = -(camera.getMinX() % getTileWidth());
        int y = -(camera.getMinY() % getTileHeight());
        int sx = camera.getMinX() / getTileWidth();
        int sy = camera.getMinY() / getTileHeight();
        int w = (camera.getMaxX() / getTileWidth()) - (camera.getMinX() / getTileWidth()) + 1;
        int h = (camera.getMaxY() / getTileHeight()) - (camera.getMinY() / getTileHeight()) + 1;
        map.render(x, y, sx, sy, w, h);  
        
        // Render the player, other characters and the items
        g.translate(-camera.getMinX(), -camera.getMinY());
        unitManager.render(g, camera);   
        itemManager.render(g, camera);
        
        // Render the panel
        g.translate(camera.getMinX(), camera.getMinY());
		player.renderPanel(g);
		itemManager.renderOnPanel(g,player.getWin());
    }

    /** Determines whether a particular map coordinate blocks movement.
     * @param x Map x coordinate (in pixels).
     * @param y Map y coordinate (in pixels).
     * @return true if the coordinate blocks movement.
     */
    public boolean terrainBlocks(double x, double y)
    {
        // Check we are within the bounds of the map
        if (x < 0 || y < 0 || x > getMapWidth() || y > getMapHeight()) {
            return true;
        }
        
        // Check the tile properties
        int tile_x = (int) x / getTileWidth();
        int tile_y = (int) y / getTileHeight();
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String block = map.getTileProperty(tileid, "block", "0");
        return !block.equals("0");
    }

}