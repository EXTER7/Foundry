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

      ItemStack osmium_pickaxe = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumPickaxe"));
      ItemStack osmium_axe = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumAxe"));
      ItemStack osmium_shovel = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumShovel"));
      ItemStack osmium_sword = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumSword"));
      ItemStack osmium_hoe = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumHoe"));

      ItemStack osmium_helmet = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumHelmet"));
      ItemStack osmium_chestplate = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumChestplate"));
      ItemStack osmium_leggings = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumLeggings"));
      ItemStack osmium_boots = newItemStack(GameRegistry.findItem("MekanismTools", "OsmiumBoots"));

      ItemStack glowstone_pickaxe = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstonePickaxe"));
      ItemStack glowstone_axe = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneAxe"));
      ItemStack glowstone_shovel = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneShovel"));
      ItemStack glowstone_sword = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneSword"));
      ItemStack glowstone_hoe = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneHoe"));

      ItemStack glowstone_helmet = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneHelmet"));
      ItemStack glowstone_chestplate = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneChestplate"));
      ItemStack glowstone_leggings = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneLeggings"));
      ItemStack glowstone_boots = newItemStack(GameRegistry.findItem("MekanismTools", "GlowstoneBoots"));

      RegisterCasting(osmium_chestplate, liquid_osmium, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(osmium_helmet, liquid_osmium, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(osmium_leggings, liquid_osmium, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(osmium_boots, liquid_osmium, 4, ItemMold.MOLD_BOOTS, null);

      RegisterCasting(osmium_pickaxe, liquid_osmium, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(osmium_axe, liquid_osmium, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(osmium_shovel, liquid_osmium, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(osmium_sword, liquid_osmium, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      RegisterCasting(osmium_hoe, liquid_osmium, 2, ItemMold.MOLD_HOE, extra_sticks2);
      
      RegisterCasting(glowstone_chestplate, liquid_refined_glowstone, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(glowstone_helmet, liquid_refined_glowstone, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(glowstone_leggings, liquid_refined_glowstone, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(glowstone_boots, liquid_refined_glowstone, 4, ItemMold.MOLD_BOOTS, null);

      RegisterCasting(glowstone_pickaxe, liquid_refined_glowstone, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(glowstone_axe, liquid_refined_glowstone, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(glowstone_shovel, liquid_refined_glowstone, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(glowstone_sword, liquid_refined_glowstone, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      RegisterCasting(glowstone_hoe, liquid_refined_glowstone, 2, ItemMold.MOLD_HOE, extra_sticks2);

      
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, osmium_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, osmium_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, osmium_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, osmium_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, osmium_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, osmium_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, osmium_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, osmium_sword);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, osmium_hoe);
      
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, glowstone_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, glowstone_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, glowstone_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, glowstone_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, glowstone_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, glowstone_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, glowstone_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, glowstone_sword);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, glowstone_hoe);
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
