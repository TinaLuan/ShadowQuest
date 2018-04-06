/* SWEN20003 Object Oriented Software Development
 * Author: Tian Luan <tluan1>
 */

import java.util.Random;
import org.newdawn.slick.SlickException;

/**
 * Passive monster. Wander in the world and run away when being attacked
 */
public class Passive extends Unit {
	
	/** Passive monster's speed, pixels per millisecond*/
	private final static double SPEED = 0.2;
	
	/** Direction changing time, in milliseconds */
	private static final int DIR_TIME = 3000;
	/** Running away waiting time, in milliseconds */
	private static final int WAIT_TIME = 5000;
	/** timer for Direction changing time */
	private int dirTimer;
	/** timer for Running away waiting time */
	private int waitTimer;
	
	/** status whether it is being attacked. */
	private boolean isBeingAttacked;

	/** The directions (-1, 0, 1) */
	private int dir_x, dir_y;
		
	/**
	 * Instantiates a new passive.
	 *
	 * @param unitData the unit data
	 * @param x the x
	 * @param y the y
	 * @throws SlickException the slick exception
	 */
	public Passive(UnitData unitData, String x, String y) 
	throws SlickException
	{
		super(unitData, x, y);
		speed = SPEED;
		dirTimer = 0;
		waitTimer = 0;
		isBeingAttacked = false;
		dir_x = 0;
		dir_y = 0;
	}
	
	/**
	 * Wander towards a random direction with the same speed
	 *
	 * @param world the world that the passive monster is on
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void wander(World world, int delta) {
		double dx = dir_x * speed * delta;
		double dy = dir_y * speed * delta;
		blockedMove(world, dx, dy);
	}
	
	/**
	 * Run away against the player's moving direction when being attacked
	 *
	 * @param player the player
	 * @param world the world
	 * @param delta the delta
	 */
	public void runAway(Player player, World world, int delta) {
		if (waitTimer < WAIT_TIME) {
			double distX = x - player.getX();
			double distY = y - player.getY();
			double distTotal = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
			double amount = delta * speed;
			double dx = distX / distTotal * amount;
			double dy = distY / distTotal * amount;
			blockedMove(world, dx, dy);
		}
	}
	
	@Override
	public void update(int delta) {
		updateWaitTimer(delta);
		updateDirTimer(delta);
	}
	
	/**
	 * Decrease wait timer by the time passed from last frame
	 *
	 * @param delta the delta
	 */
	public void updateWaitTimer(int delta) {
		if (waitTimer < WAIT_TIME) {
			waitTimer += delta;
		} else {
			waitTimer = 0;
			isBeingAttacked = false;
		}
	}
	
	/**
	 * Decrease change direction timer by the time passed from last frame
	 * Randomly generate x and y directions
	 * @param delta the delta
	 */
	public void updateDirTimer(int delta) {
		if (dirTimer < DIR_TIME) {
			dirTimer += delta;
		} else {
			dirTimer = 0;
			Random rand1 = new Random(), rand2 = new Random();
			dir_x = rand1.nextInt(3) - 1; // -1 or 0 or 1
			dir_y = rand2.nextInt(3) - 1; // -1 or 0 or 1
		}
	}
	
	
	/**Checks if is being attacked.
	 * @return true, if is being attacked
	 */
	public boolean isBeingAttacked() {
		return isBeingAttacked;
	}
	
	/**Sets the being attacked.
	 * @param isBeingAttacked the new being attacked
	 */
	public void setBeingAttacked(boolean isBeingAttacked) {
		this.isBeingAttacked = isBeingAttacked;
		waitTimer = 0;
	}
}
