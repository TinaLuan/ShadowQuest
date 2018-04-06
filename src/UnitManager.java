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
 * Manage all units including the player and other character
 * Handles the creations, actions, updates and renderings
 */
public class UnitManager 
{
	/** The list of units, except for the player */
	private ArrayList<Unit> NPCs = new ArrayList<Unit>(150);
	
	/** The list of data used for all units */
	private ArrayList<UnitData> dataList = new ArrayList<UnitData>(10);
	
	/** The player. */
	private static Player player = null;
	
	/**
	 * Instantiates a new unit manager.
	 * Read needed data files and initialise all units
	 *
	 * @param unitPosPath the path of the .dat file containing all positions
	 * @param unitDataPath the path of the .txt file containing all other data
	 * @param unitDialoguePath the path of the .txt file containing all dialogues
	 * @throws SlickException the slick exception
	 */
	public UnitManager(String unitPosPath, String unitDataPath, String unitDialoguePath)
	throws SlickException
	{
		try {
			String line, splittedLine[];
			
			// Read the data file units_data.txt
			BufferedReader in1 = new BufferedReader(new FileReader(unitDataPath));
			while ((line = in1.readLine()) != null) {
				dataList.add(new UnitData(line.split(","))); // delimiter
			}
			in1.close();
			
			// Read the  position file units.dat
			// Create all unit objects according to their types
			BufferedReader in2 = new BufferedReader(new FileReader(unitPosPath));
			while ((line = in2.readLine()) != null) {
				splittedLine = line.split(",");
				for (UnitData oneData : dataList) { // delimiter
		    			if (splittedLine[0].equals(oneData.getName())) {
		    				if (oneData.getType().equals("Player")) {
		    					player = new Player(oneData, splittedLine[1], splittedLine[2]);
		    				} else if (oneData.getType().equals("Villager")) {
		    					NPCs.add(new Villager(oneData, splittedLine[1], splittedLine[2], unitDialoguePath));
		    				} else if (oneData.getType().equals("Monster")) {
		    					if (oneData.getSubType().equals("Aggressive")) 
		    						NPCs.add(new Aggressive(oneData, splittedLine[1], splittedLine[2]));
		    					else
		    						NPCs.add(new Passive(oneData, splittedLine[1], splittedLine[2]));
		    				}
					}
				}
    			}
			in2.close();
		}
		catch( IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** Gets the player.
	 * @return the player
	 */
	public static Player getPlayer() 
	{
		return player;
	}
	
	/**
	 *  Update all units and perform the corresponding actions .
	 *
	 * @param dir_x The player's movement in the x axis (-1, 0 or 1).
	 * @param dir_y The player's movement in the y axis (-1, 0 or 1).
	 * @param isTDown The player's talk action
	 * @param isADown The player's attack action
	 * @param world The world all units are on, to check terrain blocking
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(int dir_x, int dir_y, boolean isTDown, boolean isADown, World world, int delta) 
	{
		// Update and move the player by the given directions
		player.update(delta);
		player.moveByControl(dir_x, dir_y, world, delta);
		
		Boolean playerAttacked = false;
		// Update other characters and Perform the actions
        for (Unit oneNPC : NPCs) {
        		oneNPC.update(delta);
        		
        		// Near aggressive monsters chase the player
	    		if (isNearPlayer(oneNPC, 150)) {
	    			if (oneNPC instanceof Aggressive) {
    					((Aggressive) oneNPC).chase(player, world, delta);
    				}
	    		}
	    		if (isNearPlayer(oneNPC, 50)) {
	    			// Near villagers talk to the player
	    			if (isTDown && oneNPC instanceof Villager) {
	    				((Villager)oneNPC).talkTo(player);
	    			}
	    			// Player attack all near monsters
	    			if (isADown && oneNPC instanceof Aggressive) {
	    				player.attack((Aggressive)oneNPC);
	    				if (player.getCdTimer() == 0)
	    					playerAttacked = true;
	    			}
	    			if (isADown && oneNPC instanceof Passive) {
	    				player.attack((Passive)oneNPC);
	    				if (player.getCdTimer() == 0)
	    					playerAttacked = true;
	    			}
	    			// Near aggressive monsters attack the player
	    			if (oneNPC instanceof Aggressive) {
    					((Aggressive)oneNPC).attack(player);
    				}
	    		}
	    		// Passive monsters run away or wander in the world
	    		if (oneNPC instanceof Passive) {
	    			Passive pssv = (Passive) oneNPC;
	    			if (pssv.isBeingAttacked())
	    				pssv.runAway(player, world, delta);
	    			else
	    				pssv.wander(world, delta);
	    		}
	    		// Remove the dead character
	    		if (oneNPC.getHp() <= 0) {
	    			NPCs.remove(oneNPC);
	    			break;
	    		}		
        }
        // Get the player ready after a round of combat
        if (playerAttacked == true)
        		player.readyForNextAttack();
	}
	
	/**
	 *  Check whether the current character is nearby the player within the given distance.
	 *
	 * @param NPC The current character
	 * @param distance In pixels, used for judgment
	 * @return true If the character is nearby
	 */
	private boolean isNearPlayer(Unit NPC, int distance) {
		if ( Math.sqrt(Math.pow(player.getX()-NPC.getX(), 2) + 
				Math.pow(player.getY()-NPC.getY(), 2) ) < distance)
			return true;
		else
			return false;
	}
	
	/**
	 *  Render the player and other characters 
	 *  when they are in the range of camera
	 *
	 * @param g The Slick graphics object, used for drawing.
	 * @param camera The camera object, used for determining the bound of rendering
	 */
	public void render(Graphics g, Camera camera) {
        player.render(g, camera);
		for (Unit oneNPC : NPCs) {
			oneNPC.render(g, camera);
			oneNPC.renderNameBar(g);
        }
	}
}
