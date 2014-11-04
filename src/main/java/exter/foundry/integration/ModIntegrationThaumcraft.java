package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
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
import net.minecraft.init.Items;
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

  static public final int ITEM_CLUSTER_IRON = 7;
  static public final int ITEM_CLUSTER_COPPER = 8;
  static public final int ITEM_CLUSTER_TIN = 9;
  static public final int ITEM_CLUSTER_GOLD = 10;
  static public final int ITEM_CLUSTER_SILVER = 11;
  static public final int ITEM_CLUSTER_LEAD = 12;

  static public final int ITEM_THAUMIUM_PICKAXE = 13;
  static public final int ITEM_THAUMIUM_AXE = 14;
  static public final int ITEM_THAUMIUM_SHOVEL = 15;
  static public final int ITEM_THAUMIUM_HOE = 16;
  static public final int ITEM_THAUMIUM_SWORD = 17;

  static public final int ITEM_THAUMIUM_HELMET = 18;
  static public final int ITEM_THAUMIUM_CHESTPLATE = 19;
  static public final int ITEM_THAUMIUM_LEGGINGS = 20;
  static public final int ITEM_THAUMIUM_BOOTS = 21;

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

  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void OnClientPostInit()
  {
    MaterialRegistry.instance.RegisterTypeIcon("NativeCluster", items[ITEM_CLUSTER_IRON]);
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Thaumcraft"))
    {
      is_loaded = false;
      return;
    }

    items = new ItemStack[22];
    
    items[ITEM_CAP_IRON] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",0));
    items[ITEM_CAP_COPPER] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",3));
    items[ITEM_CAP_GOLD] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",1));
    items[ITEM_CAP_SILVER] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",5));
    items[ITEM_CAP_THAUMIUM] = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",6));
    items[ITEM_INGOT_THAUMIUM] = ItemStack.copyItemStack(ItemApi.getItem("itemResource",2));
    items[ITEM_NUGGET_THAUMIUM] = ItemStack.copyItemStack(ItemApi.getItem("itemResource",6));

    items[ITEM_CLUSTER_IRON] = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",16));
    items[ITEM_CLUSTER_COPPER] = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",17));
    items[ITEM_CLUSTER_TIN] = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",18));
    items[ITEM_CLUSTER_GOLD] = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",31));
    items[ITEM_CLUSTER_SILVER] = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",19));
    items[ITEM_CLUSTER_LEAD] = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",20));
    
    items[ITEM_THAUMIUM_PICKAXE] = ItemStack.copyItemStack(ItemApi.getItem("itemPickThaumium",0));
    items[ITEM_THAUMIUM_AXE] = ItemStack.copyItemStack(ItemApi.getItem("itemAxeThaumium",0));
    items[ITEM_THAUMIUM_SHOVEL] = ItemStack.copyItemStack(ItemApi.getItem("itemShovelThaumium",0));
    items[ITEM_THAUMIUM_HOE] = ItemStack.copyItemStack(ItemApi.getItem("itemHoeThaumium",0));
    items[ITEM_THAUMIUM_SWORD] = ItemStack.copyItemStack(ItemApi.getItem("itemSwordThaumium",0));

    items[ITEM_THAUMIUM_HELMET] = ItemStack.copyItemStack(ItemApi.getItem("itemHelmetThaumium",0));
    items[ITEM_THAUMIUM_CHESTPLATE] = ItemStack.copyItemStack(ItemApi.getItem("itemChestThaumium",0));
    items[ITEM_THAUMIUM_LEGGINGS] = ItemStack.copyItemStack(ItemApi.getItem("itemLegsThaumium",0));
    items[ITEM_THAUMIUM_BOOTS] = ItemStack.copyItemStack(ItemApi.getItem("itemBootsThaumium",0));

    MaterialRegistry.instance.RegisterItem(items[ITEM_CLUSTER_IRON], "Iron", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(items[ITEM_CLUSTER_COPPER], "Copper", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(items[ITEM_CLUSTER_TIN], "Tin", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(items[ITEM_CLUSTER_GOLD], "Gold", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(items[ITEM_CLUSTER_SILVER], "Silver", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(items[ITEM_CLUSTER_LEAD], "Lead", "NativeCluster");
    
    Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
    Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
    Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
    Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
    Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
    Fluid liquid_lead = LiquidMetalRegistry.instance.GetFluid("Lead");
    Fluid liquid_thaumium = LiquidMetalRegistry.instance.GetFluid("Thaumium");

    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);
      ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CHESTPLATE);
      ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PICKAXE);
      ItemStack mold_axe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_AXE);
      ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SHOVEL);
      ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HOE);
      ItemStack mold_sword = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SWORD);
      ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_LEGGINGS);
      ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HELMET);
      ItemStack mold_boots = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BOOTS);

      if(items[ITEM_THAUMIUM_CHESTPLATE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_CHESTPLATE], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, items[ITEM_THAUMIUM_CHESTPLATE]);
      }
      if(items[ITEM_THAUMIUM_PICKAXE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_PICKAXE], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, items[ITEM_THAUMIUM_PICKAXE]);
      }
      if(items[ITEM_THAUMIUM_AXE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_AXE], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, items[ITEM_THAUMIUM_AXE]);
      }

      if(items[ITEM_THAUMIUM_SHOVEL] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_SHOVEL], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, items[ITEM_THAUMIUM_SHOVEL]);
      }
      if(items[ITEM_THAUMIUM_SWORD] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_SWORD], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, items[ITEM_THAUMIUM_SWORD]);
      }

      if(items[ITEM_THAUMIUM_HOE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_HOE], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, items[ITEM_THAUMIUM_HOE]);
      }

      if(items[ITEM_THAUMIUM_LEGGINGS] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_LEGGINGS], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, items[ITEM_THAUMIUM_LEGGINGS]);
      }

      if(items[ITEM_THAUMIUM_HELMET] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_HELMET], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, items[ITEM_THAUMIUM_HELMET]);
      }

      if(items[ITEM_THAUMIUM_BOOTS] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_THAUMIUM_BOOTS], new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, items[ITEM_THAUMIUM_BOOTS]);
      }
    }

    
    MeltingRecipeManager.instance.AddRecipe(items[ITEM_CLUSTER_IRON], new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(items[ITEM_CLUSTER_COPPER], new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(items[ITEM_CLUSTER_TIN], new FluidStack(liquid_tin,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(items[ITEM_CLUSTER_GOLD], new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(items[ITEM_CLUSTER_SILVER], new FluidStack(liquid_silver,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(items[ITEM_CLUSTER_LEAD], new FluidStack(liquid_lead,FoundryAPI.FLUID_AMOUNT_INGOT*2));

    
    CastingRecipeManager.instance.AddRecipe(
        items[ITEM_INGOT_THAUMIUM],
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_INGOT),
        new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT),
        null);

    AspectList mold_aspects = new AspectList();
    mold_aspects.add(Aspect.ORDER, 5);
    mold_aspects.add(Aspect.EARTH, 4);
    mold_aspects.add(Aspect.WATER, 2);
    
    ItemStack capmold_soft = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CAP_TC_SOFT);

    ShapelessArcaneRecipe capmold_recipe_iron = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
        "FOUNDRY_capmold",
        capmold_soft,
        mold_aspects,
        new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD),
        items[ITEM_CAP_IRON]);

    ShapelessArcaneRecipe capmold_recipe_copper = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
        "FOUNDRY_capmold",
        capmold_soft,
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
        new ResearchPage(capmold_recipe_iron),
        new ResearchPage(capmold_soft));
    mold_research.setParents("CAP_copper","CAP_silver","CAP_thaumium");
    mold_research.registerResearchItem();
    
    ItemStack cap_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CAP_TC);
    
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CAP_TC_SOFT, ItemMold.MOLD_CAP_TC);
    CastingRecipeManager.instance.AddMold( cap_mold);
    
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
