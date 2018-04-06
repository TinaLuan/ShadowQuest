/* SWEN20003 Object Oriented Software Development
 * Author: Tian Luan <tluan1>
 */

import org.newdawn.slick.SlickException;

/**
 * Aggressive monster. Can chase and attack the player
 */
public class Aggressive extends Unit {
	
	/** Player's speed, pixels per millisecond*/
	private final static double SPEED = 0.25;
	
	/**
	 * Instantiates a new aggressive.
	 *
	 * @param unitData the unit data
	 * @param x the x
	 * @param y the y
	 * @throws SlickException the slick exception
	 */
	public Aggressive(UnitData unitData, String x, String y) 
	throws SlickException
	{
		super(unitData, x, y);
		speed = SPEED;
	}	
	
	/**
	 * Chase the player. (follow the direction of the player)
	 *
	 * @param player the player
	 * @param world the world for checking terrain blocking
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void chase(Player player, World world, int delta) {
		double distX = player.getX() - x;
		double distY = player.getY() - y;
		double distTotal = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
		double amount = delta * speed;
		double dx = distX / distTotal * amount;
		double dy = distY / distTotal * amount;
		if (distTotal > 50)
			blockedMove(world, dx, dy);
	}
	
	@Override
	public void attack(Unit target) {
		super.attack(target);
		if (cdTimer == 0) {	
			readyForNextAttack();
		}
	}
}

	