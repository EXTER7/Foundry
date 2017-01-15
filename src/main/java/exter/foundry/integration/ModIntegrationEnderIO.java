package exter.foundry.integration;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModIntegrationEnderIO implements IModIntegration
{
  private Fluid liquid_redstone_alloy;
  private Fluid liquid_energetic_alloy;
  private Fluid liquid_vibrant_alloy;
  private Fluid liquid_dark_steel;
  private Fluid liquid_electrical_steel;
  private Fluid liquid_phased_iron;
  private Fluid liquid_soularium;
  
  @Override
  public void onPreInit(Configuration config)
  {
    liquid_redstone_alloy = LiquidMetalRegistry.instance.registerLiquidMetal( "RedstoneAlloy", 1000, 14);
    liquid_energetic_alloy = LiquidMetalRegistry.instance.registerLiquidMetal( "EnergeticAlloy", 2500, 15);    
    liquid_vibrant_alloy = LiquidMetalRegistry.instance.registerLiquidMetal( "VibrantAlloy", 2500, 15);    
    liquid_dark_steel = LiquidMetalRegistry.instance.registerLiquidMetal( "DarkSteel", 1850, 12);
    liquid_electrical_steel = LiquidMetalRegistry.instance.registerLiquidMetal( "ElectricalSteel", 1850, 15);    
    liquid_phased_iron = LiquidMetalRegistry.instance.registerLiquidMetal( "PulsatingIron", 1850, 15);
    liquid_soularium = LiquidMetalRegistry.instance.registerLiquidMetal( "Soularium", 1350, 12);
    
    FoundryUtils.registerBasicMeltingRecipes( "RedstoneAlloy", liquid_redstone_alloy);    
    FoundryUtils.registerBasicMeltingRecipes( "EnergeticAlloy", liquid_energetic_alloy);    
    FoundryUtils.registerBasicMeltingRecipes( "VibrantAlloy", liquid_vibrant_alloy);    
    FoundryUtils.registerBasicMeltingRecipes( "PhasedGold", liquid_vibrant_alloy);    
    FoundryUtils.registerBasicMeltingRecipes( "DarkSteel", liquid_dark_steel);    
    FoundryUtils.registerBasicMeltingRecipes( "PulsatingIron", liquid_phased_iron);
    FoundryUtils.registerBasicMeltingRecipes( "ElectricalSteel", liquid_electrical_steel);    
    FoundryUtils.registerBasicMeltingRecipes( "Soularium", liquid_soularium);    
  }

  @Override
  public void onInit()
  {
    
  }
  
  private ItemStack getItemStack(String name)
  {
    return getItemStack(name,0);
  }
  private ItemStack getItemStack(String name,int meta)
  {
    Item item = Item.REGISTRY.getObject(new ResourceLocation("EnderIO", name));
    if(item == null)
    {
      return null;
    }
    return new ItemStack(item,1,meta);
  }

  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("enderio"))
    {
      return;
    }
    


    if(FoundryConfig.recipe_equipment)
    {
      ItemStack extra_sticks1 = new ItemStack(Items.STICK, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.STICK, 2);


      ItemStack dark_steel_pickaxe = getItemStack("darkSteel_pickaxe");
      ItemStack dark_steel_axe = getItemStack("darkSteel_axe");
      ItemStack dark_steel_shovel = getItemStack("darkSteel_shovel");
      ItemStack dark_steel_sword = getItemStack("darkSteel_sword");

      ItemStack dark_steel_helmet = getItemStack("darkSteel_helmet");
      ItemStack dark_steel_chestplate = getItemStack("darkSteel_chestplate");
      ItemStack dark_steel_leggings = getItemStack("darkSteel_leggings");
      ItemStack dark_steel_boots = getItemStack("darkSteel_boots");

      
      FoundryMiscUtils.registerCasting(dark_steel_chestplate, liquid_dark_steel, 8, ItemMold.SubItem.CHESTPLATE, null);
      FoundryMiscUtils.registerCasting(dark_steel_helmet, liquid_dark_steel, 5, ItemMold.SubItem.HELMET, null);
      FoundryMiscUtils.registerCasting(dark_steel_leggings, liquid_dark_steel, 7, ItemMold.SubItem.LEGGINGS, null);
      FoundryMiscUtils.registerCasting(dark_steel_boots, liquid_dark_steel, 4, ItemMold.SubItem.BOOTS, null);

      FoundryMiscUtils.registerCasting(dark_steel_pickaxe, liquid_dark_steel, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(dark_steel_axe, liquid_dark_steel, 3, ItemMold.SubItem.AXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(dark_steel_shovel, liquid_dark_steel, 1, ItemMold.SubItem.SHOVEL, extra_sticks2);
      FoundryMiscUtils.registerCasting(dark_steel_sword, liquid_dark_steel, 2, ItemMold.SubItem.SWORD, extra_sticks1);
      
    }
    ItemStack silicon = getItemStack("itemMaterial",0);
    
    Fluid liquid_redstone = FluidRegistry.getFluid("liquidredstone");
    Fluid liquid_enderpearl = FluidRegistry.getFluid("liquidenderpearl");
    Fluid liquid_glowstone = FluidRegistry.getFluid("liquidglowstone");

    if(silicon != null)
    {
      InfuserRecipeManager.instance.addRecipe(
          new FluidStack(liquid_redstone_alloy,108),
          new FluidStack(liquid_redstone,100),
          new ItemStackMatcher(silicon),
          50000);

      InfuserRecipeManager.instance.addRecipe(
          new FluidStack(liquid_electrical_steel,108),
          new FluidStack(FoundryFluids.liquid_steel,108),
          new ItemStackMatcher(silicon),
          30000);
    }

    
    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_energetic_alloy, 54),
        new FluidStack[] {
          new FluidStack(FoundryFluids.liquid_gold, 54),
          new FluidStack(liquid_redstone, 50),
          new FluidStack(liquid_glowstone, 125)
        });

    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_vibrant_alloy, 54),
        new FluidStack[] {
          new FluidStack(liquid_energetic_alloy, 54),
          new FluidStack(liquid_enderpearl, 125)
        });

    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_phased_iron, 54),
        new FluidStack[] {
          new FluidStack(FoundryFluids.liquid_iron, 54),
          new FluidStack(liquid_enderpearl, 125)
        });

    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_dark_steel, 27),
        new FluidStack[] {
          new FluidStack(FoundryFluids.liquid_steel, 27),
          new FluidStack(FluidRegistry.LAVA, 250),
        });  

    InfuserRecipeManager.instance.addRecipe(
        new FluidStack(liquid_soularium,108),
        new FluidStack(FoundryFluids.liquid_gold,108),
        new ItemStackMatcher(new ItemStack(Blocks.SOUL_SAND)),
        50000);
  }

  @Override
  public String getName()
  {
    return "EnderIO";
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
