/* SWEN20003 Object Oriented Software Development
 * Author: Tian Luan <tluan1>
 */

import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * The ancestor class Unit, representing a character in the world
 */
public class Unit 
{	
	/** The unit's name. */
	protected String name = null;
	/** Position coordinates, in pixels */	
	protected double x, y;
	/** The unit's moving speed. */
	protected double speed;
	
	/** The unit's image and the flipped copy */
	protected Image img = null, imgFlipped = null;
	/** The size of the image */
	protected double width, height;

	/** The unit's properties */
	protected int hp, maxHp, coolDown, cdTimer, damage, maxDamage;
	
	/** Facing direction */
	protected boolean faceLeft = false;
	
	/**  Colors needed. */
	/** label color */
    protected Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold   
    /** value color */
    protected Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White    
    /** name bar background color */
    protected Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp  
    /** name bar color */
    protected Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp   
    
	/** Location of the "units" directory */
	public static final String UNITS_PATH = "/units/"; 

	/**
	 * Instantiates a new unit.
	 *
	 * @param unitData containing the needed property data
	 * @param x the x coordinate, in pixels
	 * @param y the y coordinate, in pixels
	 * @throws SlickException the slick exception
	 */
	public Unit(UnitData unitData, String x, String y)
	throws SlickException
	{
		this.name = unitData.getName();
		this.maxHp = unitData.getMaxHp();
		this.maxDamage = unitData.getMaxDamage();
		this.coolDown = unitData.getCoolDown();
		this.hp = unitData.getMaxHp();
		this.cdTimer = 0;
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
		this.img = new Image(RPG.ASSETS_PATH + UNITS_PATH + unitData.getImgName());
		this.imgFlipped = img.getFlippedCopy(true, false);
		this.width = img.getWidth();
        this.height = img.getHeight();
	}
	

	
	/**
	 * Update the unit's all properties
	 * (including decreasing coolDownTimer by the passed time)
	 *
	 * @param delta the delta
	 */
	public void update(int delta) {
		if (cdTimer > 0)
			cdTimer -= delta;
		else if (cdTimer < 0)
			cdTimer = 0;
	}
	
	/**
	 * Move the unit by given distances.
     * Prevents the unit from moving outside the map space, 
     * Updates the direction the unit is facing.
	 *
	 * @param world the world
	 * @param dx the dx
	 * @param dy the dy
	 */
	public void blockedMove(World world, double dx, double dy) {
		// Update unit facing direction based on X direction
	    if (dx > 0)
	        this.faceLeft = false;
	    else if (dx < 0)
	        this.faceLeft = true;
	
	    // Move the unit by dir_x, dir_y, as a multiple of delta * speed
	    double new_x = this.x + dx;
	    double new_y = this.y + dy;
	
	    // Move in x first
	    double x_sign = Math.signum(dx);
	    if(!world.terrainBlocks(new_x + x_sign * width / 4, this.y + height / 4) 
	            && !world.terrainBlocks(new_x + x_sign * width / 4, this.y - height / 4)) {
	        this.x = new_x;
	    }
	    
	    // Then move in y
	    double y_sign = Math.signum(dy);
	    if(!world.terrainBlocks(this.x + width / 4, new_y + y_sign * height / 4) 
	            && !world.terrainBlocks(this.x - width / 4, new_y + y_sign * height / 4)){
	        this.y = new_y;
	    }
	}
	
    /**
     * Attack action. when cooldownTimer reaches zero.
     *
     * @param target the target
     */
    public void attack(Unit target) {	
		if (cdTimer == 0) {	
			target.setHp(target.getHp() - this.damage);
			if (target instanceof Passive)
				((Passive)target).setBeingAttacked(true);
		}
    }
    
    /**
     * Get ready for next attack
     * Random the damage value and Pull up cooldown timer
     */
    public void readyForNextAttack() {
		// Update Damage and cdTimer for next attack
		this.cdTimer = this.coolDown;
		Random rand = new Random();
		this.damage = rand.nextInt(this.maxDamage+1);
    }
    
    /**
     *  Draw the unit to the screen at the correct place.
     *  when it is in the bounds of the camera
     *
     * @param g The Slick graphics object, used for drawing.
     * @param camera The camera object, used for determining the bound of rendering
     */   
    public void render(Graphics g, Camera camera)
    {
        Image which_img;
    		if (x < camera.getMaxX() && x > camera.getMinX()
    			&& y < camera.getMaxY() && y > camera.getMinY()) {
	        which_img = this.faceLeft ? this.imgFlipped : this.img;
	        which_img.drawCentered((int) x, (int) y);
    		}
    }
    
    /**
     * Draw a bar above unit's head showing the name and health
     *
     * @param g The Slick graphics object, used for drawing.
     */
    public void renderNameBar(Graphics g) {
        int bar_x, bar_y, bar_width, bar_height, hp_bar_width, text_x, text_y;
        if (g.getFont().getWidth(name) + 6 <= 70) 
        		bar_width = 70;
        else 
        		bar_width = g.getFont().getWidth(name) + 6;
        bar_height = g.getFont().getHeight(name);
        bar_x = (int)(x - bar_width/2);
        bar_y = (int)(y - bar_height/2 - height/2);
        hp_bar_width = (int) (bar_width * hp/maxHp);
        text_x = bar_x + (bar_width - g.getFont().getWidth(name)) / 2;
        text_y = bar_y;
	    g.setColor(BAR_BG);
	    g.fillRect(bar_x, bar_y, bar_width, bar_height);
	    g.setColor(BAR);
	    g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
	    g.setColor(VALUE);
		g.drawString(name, text_x, text_y);
    }

	/** Gets the x coordinate
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**Gets the y coordinate.
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**Gets the hp.
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}
	
	/**Sets the hp.
	 * @param hp the new hp
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}	
	
	/**Gets the max hp.
	 * @return the max hp
	 */
	public int getMaxHp() {
		return maxHp;
	}
	
	/**Sets the max hp.
	 * @param maxHp the new max hp
	 */
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}	
	
	/**Gets the cool down.
	 * @return the cool down
	 */
	public int getCoolDown() {
		return coolDown;
	}	
	
	/**Sets the cool down.
	 * @param coolDown the new cool down
	 */
	public void setCoolDown(int coolDown) {
		this.coolDown = coolDown;
	}
	
	/**Gets the cd timer.
	 * @return the cd timer
	 */
	public int getCdTimer() {
		return cdTimer;
	}
	
	/**Sets the cd timer.
	 * @param cdTimer the new cd timer
	 */
	public void setCdTimer(int cdTimer) {
		this.cdTimer = cdTimer;
	}	
	
	/**Gets the max damage.
	 * @return the max damage
	 */
	public int getMaxDamage() {
		return maxDamage;
	}	
	
	/**Sets the max damage.
	 * @param maxDamage the new max damage
	 */
	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}	
}
