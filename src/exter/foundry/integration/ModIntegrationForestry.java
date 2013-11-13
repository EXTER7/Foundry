package exter.foundry.integration;

import net.minecraft.item.ItemStack;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.ModFoundry;
import forestry.api.core.ItemInterface;

public class ModIntegrationForestry extends ModIntegration
{
  static public final int ITEM_COPPER_GEAR = 0;
  static public final int ITEM_TIN_GEAR = 1;
  static public final int ITEM_BRONZE_GEAR = 2;
  
  public ModIntegrationForestry(String mod_name)
  {
    super(mod_name);
    items = new ItemStack[3];
    items[ITEM_COPPER_GEAR] = ItemStack.copyItemStack(ItemInterface.getItem("gearCopper"));
    items[ITEM_TIN_GEAR] = ItemStack.copyItemStack(ItemInterface.getItem("gearTin"));
    items[ITEM_BRONZE_GEAR] = ItemStack.copyItemStack(ItemInterface.getItem("gearBronze"));
    VerifyItems();

    if(is_loaded)
    {
      FoundryMiscUtils.RegisterInOreDictionary("gearCopper",items[ITEM_COPPER_GEAR]);
      FoundryMiscUtils.RegisterInOreDictionary("gearTin",items[ITEM_TIN_GEAR]);
      FoundryMiscUtils.RegisterInOreDictionary("gearBronze",items[ITEM_BRONZE_GEAR]);
    }
  }
}
