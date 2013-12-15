package gregtechmod.api.enums;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Class containing all non-OreDict Items of GregTech.
 */
public enum GT_Items {
	Display_Fluid,
	NC_SensorCard,
	NC_SensorKit,
	Tool_Mortar_Iron,
	Tool_Mortar_Wood,
	Tool_Cheat,
	Tool_Scanner,
	Tool_Crowbar_Iron,
	Tool_Screwdriver_Iron,
	Tool_Screwdriver_TungstenSteel,
	Tool_Screwdriver_Electric,
	Tool_Wrench_Iron,
	Tool_Wrench_Bronze,
	Tool_Wrench_Steel,
	Tool_Wrench_TungstenSteel,
	Tool_Wrench_Electric,
	Tool_Wrench_Advanced,
	Tool_Hammer_Rubber,
	Tool_Hammer_Iron,
	Tool_Hammer_Bronze,
	Tool_Hammer_Steel,
	Tool_Hammer_TungstenSteel,
	Tool_File_Iron,
	Tool_File_Bronze,
	Tool_File_Steel,
	Tool_File_TungstenSteel,
	Tool_Saw_Iron,
	Tool_Saw_Bronze,
	Tool_Saw_Steel,
	Tool_Saw_TungstenSteel,
	Tool_Saw_Electric,
	Tool_Saw_Advanced,
	Tool_Drill_Advanced,
	Tool_SolderingIron_Electric,
	Tool_SolderingMaterial_Tin,
	Tool_SolderingMaterial_Lead,
	Tool_Rockcutter,
	Tool_Teslastaff,
	Tool_DataOrb,
	Tool_Sonictron,
	Tool_Destructopack,
	Tool_Flint_Sword,
	Tool_Flint_Pickaxe,
	Tool_Flint_Shovel,
	Tool_Flint_Axe,
	Tool_Flint_Hoe,
	Tool_Steel_Sword,
	Tool_Steel_Pickaxe,
	Tool_Steel_Shovel,
	Tool_Steel_Axe,
	Tool_Steel_Hoe,
	Tool_TungstenSteel_Sword,
	Tool_TungstenSteel_Pickaxe,
	Tool_TungstenSteel_Shovel,
	Tool_TungstenSteel_Axe,
	Tool_TungstenSteel_Hoe,
	Tool_Jackhammer_Bronze,
	Tool_Jackhammer_Steel,
	Tool_Jackhammer_Diamond,
	Spray_Bug,
	Spray_Ice,
	Spray_Hardener,
	Spray_CFoam,
	Spray_Pepper,
	Spray_Hydration,
	Spray_Color_00,
	Spray_Color_01,
	Spray_Color_02,
	Spray_Color_03,
	Spray_Color_04,
	Spray_Color_05,
	Spray_Color_06,
	Spray_Color_07,
	Spray_Color_08,
	Spray_Color_09,
	Spray_Color_10,
	Spray_Color_11,
	Spray_Color_12,
	Spray_Color_13,
	Spray_Color_14,
	Spray_Color_15,
	Armor_Cheat,
	Armor_Cloaking,
	Armor_Lamp,
	Armor_LithiumPack,
	Armor_LapotronicPack,
	Armor_ForceField,
	Energy_LapotronicOrb,
	Energy_Lithium,
	Energy_Lithium_Empty,
	Reactor_Coolant_He_1,
	Reactor_Coolant_He_3,
	Reactor_Coolant_He_6,
	Reactor_Coolant_NaK_1,
	Reactor_Coolant_NaK_3,
	Reactor_Coolant_NaK_6,
	Reactor_NeutronReflector,
	Component_Turbine_Bronze,
	Component_Turbine_Steel,
	Component_Turbine_Magnalium,
	Component_Turbine_TungstenSteel,
	Component_Turbine_Carbon,
	Component_LavaFilter,
	
	NULL;
	
	private ItemStack mStack;

	public ItemStack set(Item aItem) {
		ItemStack aStack = new ItemStack(aItem, 1, 0);
		mStack = GT_Utility.copyAmount(1, aStack);
		return GT_Utility.copyAmount(1, mStack);
	}
	
	public ItemStack set(ItemStack aStack) {
		mStack = GT_Utility.copyAmount(1, aStack);
		return GT_Utility.copyAmount(1, mStack);
	}
	
	public Item get() {
		if (GT_Utility.isStackInvalid(mStack)) return null;
		return mStack.getItem();
	}
	
	public ItemStack get(long aAmount, Object... aReplacements) {
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmount(aAmount, mStack);
	}
	
	public ItemStack getWildcard(long aAmount, Object... aReplacements) {
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, GregTech_API.ITEM_WILDCARD_DAMAGE, mStack);
	}

	public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, 0, mStack);
	}
	
	public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, mStack.getMaxDamage()-1, mStack);
	}
	
	public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, aMetaValue, mStack);
	}
}