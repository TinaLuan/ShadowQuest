/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: <Tian Luan> <tluan1>
 */

import org.newdawn.slick.SlickException;

/** Represents the camera that controls our viewpoint.
 */
public class Camera
{
    
    /**  The unit this camera is following. */
    private Player unitFollow;
    
    /**  The width and height of the screen. */
    /** Screen width, in pixels. */
    private int screenwidth;
    /** Screen height, in pixels. */
    private int screenheight;
    
    /**  Panel height, in pixels. */
    private int panelheight;
    
    /** The camera's position in the world, in x and y coordinates. */
    private int xPos;
    
    /** The y pos. */
    private int yPos;

    /**
     *  Create a new Camera object.
     *
     * @param player The player to follow
     * @param screenwidth The width of the viewport in pixels
     * @param screenheight The height of the viewport in pixels
     * @param panelheight The height of the panel
     */
    public Camera(Player player, int screenwidth, int screenheight, int panelheight)
    {   
        this.unitFollow = player;
        this.screenwidth = screenwidth;
        this.screenheight = screenheight;
        this.panelheight = panelheight;
    }

    /**
     *  Update the game camera to re-centre its viewpoint around the player .
     *
     * @throws SlickException the slick exception
     */
    public void update()
    throws SlickException
    {
        // Update the camera based on the player's position
        xPos = (int) unitFollow.getX() - (screenwidth/2);
        yPos = (int) unitFollow.getY() - ((screenheight - panelheight)/2); 
    }
        
    /**
     *  Returns the minimum x value on screen.
     *
     * @return the min X
     */
    public int getMinX(){
        return xPos;
    }
    
    /**
     *  Returns the maximum x value on screen.
     *
     * @return the max X
     */
    public int getMaxX(){
        return xPos + screenwidth;
    }
    
    /**
     *  Returns the minimum y value on screen.
     *
     * @return the min Y
     */
    public int getMinY(){
        return yPos;
    }
    
    /**
     *  Returns the maximum y value on screen.
     *
     * @return the max Y
     */
    public int getMaxY(){
        return yPos+screenheight;
    }

    /** Tells the camera to follow a given unit.
     * 
     * @param unit The new unit to follow
     */
    public void followUnit(Object unit)
    {
        if(unit instanceof Player){
            unitFollow = (Player) unit;
        }
    }
    
}