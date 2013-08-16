package exter.foundry.integration;

import exter.foundry.ModFoundry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModIntegrationBuildcraft extends ModIntegration
{
  public ModIntegrationBuildcraft(String mod_name)
  {
    super(mod_name);
    try
    {
      Class BuildCraftCore = Class.forName("buildcraft.BuildCraftCore");
      RegisterItem(BuildCraftCore,"woodenGearItem");
      RegisterItem(BuildCraftCore,"stoneGearItem");
      RegisterItem(BuildCraftCore,"ironGearItem");
      RegisterItem(BuildCraftCore,"goldGearItem");
      RegisterItem(BuildCraftCore,"diamondGearItem");
    } catch(ClassNotFoundException e)
    {
      ModFoundry.log.info("[ModIntegration ("+ mod_name +")] Cannot find buildcraft.BuildCraftCore class");
      is_loaded = false;
      
      return;
    }
  }
}
