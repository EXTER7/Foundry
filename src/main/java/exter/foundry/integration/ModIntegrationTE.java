package exter.foundry.integration;

//import cofh.api.modhelpers.ThermalExpansionHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.config.Configuration;


public class ModIntegrationTE implements IModIntegration
{

  @Override
  public void onPreInit(Configuration config)
  {

  }

  @Override
  public void onInit()
  {
  }

  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("ThermalExpansion") || !Loader.isModLoaded("ThermalFoundation"))
    {
      return;
    }
//    ItemStack copper_dust = GameRegistry.findItemStack("ThermalFoundation", "dustCopper", 1);
//    ItemStack copper_dust3 = copper_dust.copy();
//    copper_dust3.stackSize = 3;
//    ItemStack nickel_dust = GameRegistry.findItemStack("ThermalFoundation", "dustNickel", 1);
//    ItemStack zinc_dust = FoundryItems.Component(ItemComponent.COMPONENT_DUST_ZINC);
//    
//    ThermalExpansionHelper.addSmelterRecipe(1600, copper_dust3, zinc_dust, FoundryItems.Ingot(ItemIngot.INGOT_BRASS,4));
//    ThermalExpansionHelper.addSmelterRecipe(1600, copper_dust, nickel_dust, FoundryItems.Ingot(ItemIngot.INGOT_CUPRONICKEL,2));
//
//    ThermalExpansionHelper.addSmelterRecipe(2400, FoundryItems.Ingot(ItemIngot.INGOT_COPPER,3), FoundryItems.Ingot(ItemIngot.INGOT_ZINC,1), FoundryItems.Ingot(ItemIngot.INGOT_BRASS,4));
//    ThermalExpansionHelper.addSmelterRecipe(2400, FoundryItems.Ingot(ItemIngot.INGOT_COPPER,1), FoundryItems.Ingot(ItemIngot.INGOT_NICKEL,1), FoundryItems.Ingot(ItemIngot.INGOT_CUPRONICKEL,2));
  }

  @Override
  public String getName()
  {
    return "thermal_expansion";
  }

  @Override
  public void onAfterPostInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPostInit()
  {
    
  }
}
