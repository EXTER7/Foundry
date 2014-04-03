package forestry.api.storage;

import net.minecraft.item.Item;

public interface IBackpackInterface {

	/**
	 * Adds a backpack with the given definition and type, returning the item.
	 * 
	 * @param definition
	 *            Definition of backpack behaviour.
	 * @param type
	 *            Type of backpack. (T1 or T2 (= Woven)
	 * @return Created backpack item.
	 */
	Item addBackpack(IBackpackDefinition definition, EnumBackpackType type);
}
