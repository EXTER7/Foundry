package exter.foundry.integration;

import exter.foundry.FoundryUtils;
import exter.foundry.ModFoundry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModIntegrationBuildcraft extends ModIntegration
{
  static public final int ITEM_WOOD_GEAR = 0;
  static public final int ITEM_STONE_GEAR = 1;
  static public final int ITEM_IRON_GEAR = 2;
  static public final int ITEM_GOLD_GEAR = 3;
  static public final int ITEM_DIAMOND_GEAR = 4;
  
  public ModIntegrationBuildcraft(String mod_name)
  {
    super(mod_name);
    try
    {
      items = new ItemStack[5];
      Class BuildCraftCore = Class.forName("buildcraft.BuildCraftCore");
      items[ITEM_WOOD_GEAR] = GetItemFromField(BuildCraftCore,"woodenGearItem");
      items[ITEM_STONE_GEAR] = GetItemFromField(BuildCraftCore,"stoneGearItem");
      items[ITEM_IRON_GEAR] = GetItemFromField(BuildCraftCore,"ironGearItem");
      items[ITEM_GOLD_GEAR] = GetItemFromField(BuildCraftCore,"goldGearItem");
      items[ITEM_DIAMOND_GEAR] = GetItemFromField(BuildCraftCore,"diamondGearItem");
      VerifyItems();
    } catch(ClassNotFoundException e)
    {
      ModFoundry.log.info("[ModIntegration ("+ mod_name +")] Cannot find buildcraft.BuildCraftCore class");
      is_loaded = false;
      return;
    }

    if(is_loaded)
    {
      FoundryUtils.RegisterInOreDictionary("gearWood",items[ITEM_WOOD_GEAR]);
      FoundryUtils.RegisterInOreDictionary("gearStone",items[ITEM_STONE_GEAR]);
      FoundryUtils.RegisterInOreDictionary("gearIron",items[ITEM_IRON_GEAR]);
      FoundryUtils.RegisterInOreDictionary("gearGold",items[ITEM_GOLD_GEAR]);
      FoundryUtils.RegisterInOreDictionary("gearDiamond",items[ITEM_DIAMOND_GEAR]);
    }
  }
}
