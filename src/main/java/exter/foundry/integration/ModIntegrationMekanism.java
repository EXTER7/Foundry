package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationMekanism extends ModIntegration
{
  private Fluid liquid_osmium;
  private Fluid liquid_refined_glowstone;
  
  public ModIntegrationMekanism(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    liquid_osmium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Osmium", 3200, 15);
    liquid_refined_glowstone = LiquidMetalRegistry.instance.RegisterLiquidMetal( "RefinedGlowstone", 3200, 15);
    FoundryUtils.RegisterBasicMeltingRecipes("Osmium", liquid_osmium);
    FoundryUtils.RegisterBasicMeltingRecipes("RefinedGlowstone", liquid_refined_glowstone);
  }

  @Override
  public void OnInit()
  {

  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Mekanism"))
    {
      is_loaded = false;
      return;
    }
    
    if(FoundryConfig.recipe_tools_armor && Loader.isModLoaded("MekanismTools"))
    {
      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);


      ItemStack dark_steel_pickaxe = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstonePickaxe"));
      ItemStack dark_steel_axe = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneAxe"));
      ItemStack dark_steel_shovel = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneShovel"));
      ItemStack dark_steel_sword = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneSword"));
      ItemStack dark_steel_hoe = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneHoe"));

      ItemStack dark_steel_helmet = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneHelmet"));
      ItemStack dark_steel_chestplate = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneChestplate"));
      ItemStack dark_steel_leggings = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneLeggings"));
      ItemStack dark_steel_boots = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneBoots"));

      RegisterCasting(dark_steel_chestplate, liquid_refined_glowstone, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(dark_steel_helmet, liquid_refined_glowstone, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(dark_steel_leggings, liquid_refined_glowstone, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(dark_steel_boots, liquid_refined_glowstone, 4, ItemMold.MOLD_BOOTS, null);

      RegisterCasting(dark_steel_pickaxe, liquid_refined_glowstone, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(dark_steel_axe, liquid_refined_glowstone, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(dark_steel_shovel, liquid_refined_glowstone, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(dark_steel_sword, liquid_refined_glowstone, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      RegisterCasting(dark_steel_hoe, liquid_refined_glowstone, 2, ItemMold.MOLD_HOE, extra_sticks2);
      
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, dark_steel_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, dark_steel_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, dark_steel_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, dark_steel_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, dark_steel_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, dark_steel_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, dark_steel_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, dark_steel_sword);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, dark_steel_hoe);
    }

    
    Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
    if(liquid_glowstone != null)
    {
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_refined_glowstone, 54),
          new FluidStack[] {
            new FluidStack(liquid_osmium, 54),
            new FluidStack(liquid_glowstone, 125)
          });
    }
  }

}
