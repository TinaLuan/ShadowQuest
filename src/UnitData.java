/* SWEN20003 Object Oriented Software Development
 * Author: Tian Luan <tluan1>
 */

/**
 * Only Storing the properties for units.
 */
public class UnitData {
	
	/** properties */
	private String name = null, imgName = null, type = null, subType = null;
	
	/** properties */
	private int maxHp, coolDown, maxDamage;
	
	/**
	 * Instantiates a new unit data.
	 *
	 * @param data the data
	 */
	public UnitData(String[] data) {
		// 0 to 6 represents the different data content in the list
		this.name = data[0];
		this.imgName = data[1];
		this.type = data[2];
		this.subType = data[3];
		this.maxHp = Integer.parseInt(data[4]);
		this.maxDamage = Integer.parseInt(data[5]);
		this.coolDown = Integer.parseInt(data[6]);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the img name.
	 *
	 * @return the img name
	 */
	public String getImgName() {
		return imgName;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the sub type.
	 *
	 * @return the sub type
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * Gets the max hp.
	 *
	 * @return the max hp
	 */
	public int getMaxHp() {
		return maxHp;
	}

	/**
	 * Gets the cool down.
	 *
	 * @return the cool down
	 */
	public int getCoolDown() {
		return coolDown;
	}

	/**
	 * Gets the max damage.
	 *
	 * @return the max damage
	 */
	public int getMaxDamage() {
		return maxDamage;
	}

}
