package exter.foundry.integration;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemIngot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;


public class ModIntegrationTE4 extends ModIntegration
{

  public ModIntegrationTE4(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {

  }

  @Override
  public void OnInit()
  {
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("ThermalExpansion") || !Loader.isModLoaded("ThermalFoundation"))
    {
      is_loaded = false;
      return;
    }
    ItemStack copper_dust = GameRegistry.findItemStack("ThermalFoundation", "dustCopper", 1);
    ItemStack copper_dust3 = copper_dust.copy();
    copper_dust3.stackSize = 3;
    ItemStack nickel_dust = GameRegistry.findItemStack("ThermalFoundation", "dustNickel", 1);
    ItemStack zinc_dust = FoundryItems.Component(ItemComponent.COMPONENT_DUST_ZINC);
    
    ThermalExpansionHelper.addSmelterRecipe(1600, copper_dust3, zinc_dust, FoundryItems.Ingot(ItemIngot.INGOT_BRASS,4));
    ThermalExpansionHelper.addSmelterRecipe(1600, copper_dust, nickel_dust, FoundryItems.Ingot(ItemIngot.INGOT_CUPRONICKEL,2));

    ThermalExpansionHelper.addSmelterRecipe(2400, FoundryItems.Ingot(ItemIngot.INGOT_COPPER,3), FoundryItems.Ingot(ItemIngot.INGOT_ZINC,1), FoundryItems.Ingot(ItemIngot.INGOT_BRASS,4));
    ThermalExpansionHelper.addSmelterRecipe(2400, FoundryItems.Ingot(ItemIngot.INGOT_COPPER,1), FoundryItems.Ingot(ItemIngot.INGOT_NICKEL,1), FoundryItems.Ingot(ItemIngot.INGOT_CUPRONICKEL,2));
  }
}
