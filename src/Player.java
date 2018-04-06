/* SWEN20003 Object Oriented Software Development
 * Modified from sample solution
 * Author: Tian Luan <tluan1>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** The character which the user plays as.
 */
public class Player extends Unit
{   
    /** Player's speed, pixels per millisecond*/
    private static final double SPEED = 0.25;
    
    /** The inventory, representing items the player have */
    Boolean[] inventory = new Boolean[4];
    
    /** the image of the panel */
    private Image panel = null;
    
    /** coordinates of reborn position, in pixels*/
    private static final double REBORN_X = 738;
    private static final double REBORN_Y = 549;
    
    /** Status whether the player wins the game */
    private Boolean win;
    
    /**
     *  Instantiates a new Player.
     *
     * @param unitData containing the needed property data.
     * @param x The Player's x location in pixels.
     * @param y The Player's y location in pixels.
     * @throws SlickException the slick exception
     */
    public Player(UnitData unitData, String x, String y) 
	throws SlickException
	{
		super(unitData, x, y);
		panel = new Image(RPG.ASSETS_PATH + "/panel.png");
		for (int i = 0; i < 4; i++) {
			inventory[i] = false;
		}
		win = false;
	}
    
    /**
     *  Move the player in a given direction (terrain blocking is on).
     *
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param world The world the player is on (to check blocking).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void moveByControl(int dir_x, int dir_y, World world, int delta)
    {
        // Move the player by dir_x, dir_y, as a multiple of delta * speed
        double dx = dir_x * delta * SPEED;
        double dy = dir_y * delta * SPEED;
        blockedMove(world, dx, dy);
    }

    /* (non-Javadoc)
     * @see Unit#update(int)
     */
    @Override
    public void update(int delta) {
    		super.update(delta);
    		// if player gets killed, reborn him
    		if (hp <= 0) {
    			hp = maxHp;
        		x = REBORN_X;
        		y = REBORN_Y;
    		}
    } 
    
    /** Renders the player's status panel.
     *  Modified from the given code
     * @param g The current Slick graphics context.
     */
    public void renderPanel(Graphics g)
    {
        // Variables for layout
        String text;                // Text to display
        int text_x, text_y;         // Coordinates to draw text
        int bar_x, bar_y;           // Coordinates to draw rectangles
        int bar_width, bar_height;  // Size of rectangle to draw
        int hp_bar_width;           // Size of red (HP) rectangle
        float health_percent;       // Player's health, as a percentage

        // Panel background image
        panel.draw(0, RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT);

        // Display the player's health
        text_x = 15;
        text_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT + 25;
        g.setColor(LABEL);
        g.drawString("Health:", text_x, text_y);
        text = Integer.toString(hp) + "/" + Integer.toString(maxHp);
        bar_x = 90;
        bar_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT + 20;
        bar_width = 90;
        bar_height = 30;
        health_percent = (float)hp/maxHp;
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's damage and cooldown
        text_x = 200;
        g.setColor(LABEL);
        g.drawString("Damage:", text_x, text_y);
        text_x += 80;
        text = Integer.toString(maxDamage);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
        text_x += 40;
        g.setColor(LABEL);
        g.drawString("Rate:", text_x, text_y);
        text_x += 55;
        text = Integer.toString(coolDown);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's inventory
        g.setColor(LABEL);
        g.drawString("Items:", 420, text_y);
        bar_x = 490;
        bar_y = RPG.SCREEN_HEIGHT - RPG.PANEL_HEIGHT + 10;
        bar_width = 288;
        bar_height = bar_height + 20;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
    }

	/**Gets the win.
	 * @return the win
	 */
	public Boolean getWin() {
		return win;
	}

	/**Sets the win.
	 * @param win the new win
	 */
	public void setWin(Boolean win) {
		this.win = win;
	}
}
