package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationThaumcraft extends ModIntegration
{
  static public final int ITEM_CAP_IRON = 0;
  static public final int ITEM_CAP_COPPER = 1;
  static public final int ITEM_CAP_GOLD = 2;
  static public final int ITEM_CAP_SILVER = 3;
  static public final int ITEM_CAP_THAUMIUM = 4;
  static public final int ITEM_INGOT_THAUMIUM = 5;
  static public final int ITEM_NUGGET_THAUMIUM = 6;
  
  public ModIntegrationThaumcraft(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    Fluid liquid_thaumium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Thaumium", 1850, 14);
    FoundryUtils.RegisterBasicMeltingRecipes("Thaumium", liquid_thaumium);
  }

  @Override
  public void OnInit()
  {
    if(!Loader.isModLoaded("Thaumcraft"))
    {
      is_loaded = false;
      return;
    }

    items = new ItemStack[7];
    
    items[ITEM_CAP_IRON] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",0));
    items[ITEM_CAP_COPPER] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",3));
    items[ITEM_CAP_GOLD] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",1));
    items[ITEM_CAP_SILVER] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",5));
    items[ITEM_CAP_THAUMIUM] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",6));
    items[ITEM_INGOT_THAUMIUM] = ItemStack.copyItemStack(ItemApi.getItem("itemResource",2));
    items[ITEM_NUGGET_THAUMIUM] = ItemStack.copyItemStack(ItemApi.getItem("itemResource",6));
   
    Fluid liquid_thaumium = LiquidMetalRegistry.instance.GetFluid("Thaumium");
    
    CastingRecipeManager.instance.AddRecipe(
        items[ITEM_INGOT_THAUMIUM],
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_INGOT),
        new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT),
        null);

  }
  
  @Override
  public void OnPostInit()
  {
    AspectList mold_aspects = new AspectList();
    mold_aspects.add(Aspect.ORDER, 5);
    mold_aspects.add(Aspect.EARTH, 4);
    mold_aspects.add(Aspect.WATER, 2);

    ShapelessArcaneRecipe capmold_recipe_iron = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
        "FOUNDRY_capmold",
        new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CAP_TC_CLAY),
        mold_aspects,
        new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD),
        items[ITEM_CAP_IRON]);

    ShapelessArcaneRecipe capmold_recipe_copper = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
        "FOUNDRY_capmold",
        new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CAP_TC_CLAY),
        mold_aspects,
        new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD),
        items[ITEM_CAP_COPPER]);

    ResearchItem mold_research = new ResearchItem(
        "FOUNDRY_capmold", "THAUMATURGY",
        (new AspectList()).add(Aspect.METAL, 1).add(Aspect.CRAFT, 1),
        7, 0,
        1,
        new ResourceLocation("foundry:textures/items/mold_cap_tc.png"));
    mold_research.setPages(
        new ResearchPage("tc.research.capmold.text"),
        new ResearchPage(capmold_recipe_copper),
        new ResearchPage(capmold_recipe_iron));
    mold_research.setParents("CAP_copper","CAP_silver","CAP_thaumium");
    mold_research.registerResearchItem();
    
    ItemStack cap_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CAP_TC);
    
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CAP_TC_CLAY, ItemMold.MOLD_CAP_TC);
    CastingRecipeManager.instance.AddMold( cap_mold);
    
    Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
    Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
    Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
    Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
    Fluid liquid_thaumium = LiquidMetalRegistry.instance.GetFluid("Thaumium");

    MeltingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_IRON],
        new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_COPPER],
        new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_GOLD],
        new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_SILVER],
        new FluidStack(liquid_silver,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_THAUMIUM],
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5));

    CastingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_IRON],
        new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_COPPER],
        new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_GOLD],
        new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_SILVER],
        new FluidStack(liquid_silver,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        items[ITEM_CAP_THAUMIUM],
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
  }

}
