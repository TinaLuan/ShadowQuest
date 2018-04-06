/* SWEN20003 Object Oriented Software Development
 * Author: Tian Luan <tluan1>
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Villager. Can talk to and affect the player
 */
public class Villager extends Unit 
{	
	/** The list of dialogues for all villagers */
	String[] dialogues = new String[10];
	
	/** The current dialogue. */
	String currentDialogue = null;
	
	/** The timer of showing dialogue */
	private int show_timer;
	
	/** Dialogue showing max time */
	public static final int SHOW_TIME = 4000;
	
	/**
	 * Instantiates a new villager.
	 *
	 * @param unitData the unit data
	 * @param x the x
	 * @param y the y
	 * @param dialoguePath the dialogue path
	 * @throws SlickException the slick exception
	 */
	public Villager(UnitData unitData, String x, String y, String dialoguePath) 
	throws SlickException
	{		
		super(unitData, x, y);
		try {
			// Read in the dialogues from dialogue2.txt
			BufferedReader in = new BufferedReader(new FileReader(dialoguePath));
			String line;
			int i = 0;
			while ((line = in.readLine())!=null) {
				dialogues[i++] = line;
			}
			in.close();
		}
		catch( IOException e) {
			e.printStackTrace();
		}
		currentDialogue = "";
		show_timer = 0;
	}

	
	/**
	 * Talk to the player (update the corresponding dialogue)
	 *
	 * @param player the player
	 */
	public void talkTo(Player player) 
	{	
		show_timer = 0;
		// Elvira and Garth are villager names
		// Different villagers give out different content (0 to 7) in dialogue list
		if (this.name.equals("Elvira")) {
			if (player.getHp() == player.getMaxHp()) {
				currentDialogue = dialogues[2];
			} else {
				player.setHp(player.getMaxHp());
				currentDialogue = dialogues[3];
			}
		} else if (this.name.equals("Garth")) {
			if (player.inventory[0] == false) {
				currentDialogue = dialogues[4];
			} else if (player.inventory[1] == false) {
				currentDialogue = dialogues[5];
			} else if (player.inventory[2] == false) {
				currentDialogue = dialogues[6];
			} else if (player.inventory[3] == false) {
				currentDialogue = dialogues[7];
			}
		} else {
			if (player.inventory[3] == false) {
				currentDialogue = dialogues[0];
			} else {
				currentDialogue = dialogues[1];
				player.setWin(true);
			}
		}
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		updateTimer(delta);
	}
	
	@Override
	public void render(Graphics g, Camera camera) {
		super.render(g, camera);
		// Show the dialogue
		if (!currentDialogue.equals("")) {
	        int bar_x, bar_y;           // Coordinates to draw rectangles
	        int bar_width, bar_height;  // Size of rectangle to draw
	        
	        bar_width = g.getFont().getWidth(currentDialogue);
	        bar_height = g.getFont().getHeight(currentDialogue);
	        bar_x = (int) (x - bar_width/2);
	        bar_y = (int) (y - height/2 - bar_height/2 - bar_height);
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(VALUE);
	        g.drawString(currentDialogue, bar_x, bar_y);    
		}
	}

	/**
	 * Increase timer of showing dialogue by the passed time in milliseconds
	 *
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void updateTimer(int delta) {
		if (show_timer < SHOW_TIME) {
			show_timer += delta;
		} else {
			show_timer = 0;
			currentDialogue = "";
		}
	}
	
}
