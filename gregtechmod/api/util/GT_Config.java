package gregtechmod.api.util;

import gregtechmod.api.enums.GT_ConfigCategories;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class GT_Config {
	public static volatile int VERSION = 405;
	
	public static boolean system = false;
	
	public static Configuration sConfigFileStandard, sConfigFileIDs, sConfigFileAdvRecipes;
	
	public GT_Config(Configuration aConfigFileStandard, Configuration aConfigFileIDs, Configuration aConfigFileAdvRecipes) {
		sConfigFileAdvRecipes = aConfigFileAdvRecipes;
		sConfigFileStandard = aConfigFileStandard;
		sConfigFileIDs = aConfigFileIDs;
	}
	
	public String getStackConfigName(ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) return null;
		String rName;
		if (GT_Utility.isStringValid(rName = GT_OreDictUnificator.getAssociation(aStack))) return rName;
		try {if (GT_Utility.isStringValid(rName = aStack.getUnlocalizedName())) return rName;} catch (Throwable e) {}
		return aStack.getItem() + "." + aStack.getItemDamage();
	}
	
	public int addIDConfig(Object aCategory, String aName, int aDefault) {
		if (GT_Utility.isStringInvalid(aName)) return aDefault;
		Property tProperty = sConfigFileIDs.get(aCategory.toString().replaceAll("\\|", "."), aName.replaceAll("\\|", "."), aDefault);
		int rResult = tProperty.getInt(aDefault);
		if (!tProperty.wasRead()) sConfigFileIDs.save();
		return rResult;
	}
	
	public boolean addAdvConfig(Object aCategory, ItemStack aStack, boolean aDefault) {
		return addAdvConfig(aCategory, getStackConfigName(aStack), aDefault);
	}
	
	public boolean addAdvConfig(Object aCategory, String aName, boolean aDefault) {
		if (GT_Utility.isStringInvalid(aName)) return aDefault;
		Property tProperty = sConfigFileAdvRecipes.get(aCategory.toString().replaceAll("\\|", "."), (aName+"_"+aDefault).replaceAll("\\|", "."), aDefault);
		boolean rResult = tProperty.getBoolean(aDefault);
		if (!tProperty.wasRead()) sConfigFileAdvRecipes.save();
		return rResult;
	}
	
	public int addAdvConfig(Object aCategory, ItemStack aStack, int aDefault) {
		return addAdvConfig(aCategory, getStackConfigName(aStack), aDefault);
	}
	
	public int addAdvConfig(Object aCategory, String aName, int aDefault) {
		if (GT_Utility.isStringInvalid(aName)) return aDefault;
		Property tProperty = sConfigFileAdvRecipes.get(aCategory.toString().replaceAll("\\|", "."), (aName+"_"+aDefault).replaceAll("\\|", "."), aDefault);
		int rResult = tProperty.getInt(aDefault);
		if (!tProperty.wasRead()) sConfigFileAdvRecipes.save();
		return rResult;
	}
}
