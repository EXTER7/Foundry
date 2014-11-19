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
    MaterialRegistry.instance.RegisterTypeIcon("NativeCluster", ItemStack.copyItemStack(ItemApi.getItem("itemNugget",16)));
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Thaumcraft"))
    {
      is_loaded = false;
      return;
    }
    ItemStack cap_iron = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",0));
    ItemStack cap_copper = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",3));
    ItemStack cap_gold = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",1));
    ItemStack cap_silver = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",5));
    ItemStack cap_thaumium = ItemStack.copyItemStack(ItemApi.getItem("itemWandCap",6));
    ItemStack ingot_thaumium = ItemStack.copyItemStack(ItemApi.getItem("itemResource",2));
    //ItemStack NUGGET_THAUMIUM = ItemStack.copyItemStack(ItemApi.getItem("itemResource",6));

    ItemStack cluster_iron = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",16));
    ItemStack cluster_copper = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",17));
    ItemStack cluster_tin = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",18));
    ItemStack cluster_gold = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",31));
    ItemStack cluster_silver = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",19));
    ItemStack cluster_lead = ItemStack.copyItemStack(ItemApi.getItem("itemNugget",20));
    
    ItemStack thaumium_pickaxe = ItemStack.copyItemStack(ItemApi.getItem("itemPickThaumium",0));
    ItemStack thaumium_axe = ItemStack.copyItemStack(ItemApi.getItem("itemAxeThaumium",0));
    ItemStack thaumium_shovel = ItemStack.copyItemStack(ItemApi.getItem("itemShovelThaumium",0));
    ItemStack thaumium_hoe = ItemStack.copyItemStack(ItemApi.getItem("itemHoeThaumium",0));
    ItemStack thaumium_sword = ItemStack.copyItemStack(ItemApi.getItem("itemSwordThaumium",0));

    ItemStack thaumium_helmet = ItemStack.copyItemStack(ItemApi.getItem("itemHelmetThaumium",0));
    ItemStack thaumium_chestplate = ItemStack.copyItemStack(ItemApi.getItem("itemChestThaumium",0));
    ItemStack thaumium_leggings = ItemStack.copyItemStack(ItemApi.getItem("itemLegsThaumium",0));
    ItemStack thaumium_boots = ItemStack.copyItemStack(ItemApi.getItem("itemBootsThaumium",0));

    MaterialRegistry.instance.RegisterItem(cluster_iron, "Iron", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(cluster_copper, "Copper", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(cluster_tin, "Tin", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(cluster_gold, "Gold", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(cluster_silver, "Silver", "NativeCluster");
    MaterialRegistry.instance.RegisterItem(cluster_lead, "Lead", "NativeCluster");
    
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

      if(thaumium_chestplate != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_chestplate, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, thaumium_chestplate);
      }
      if(thaumium_pickaxe != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_pickaxe, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, thaumium_pickaxe);
      }
      if(thaumium_axe != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_axe, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, thaumium_axe);
      }

      if(thaumium_shovel != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_shovel, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, thaumium_shovel);
      }
      if(thaumium_sword != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_sword, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, thaumium_sword);
      }

      if(thaumium_hoe != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_hoe, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, thaumium_hoe);
      }

      if(thaumium_leggings != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_leggings, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, thaumium_leggings);
      }

      if(thaumium_helmet != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_helmet, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, thaumium_helmet);
      }

      if(thaumium_boots != null)
      {
        CastingRecipeManager.instance.AddRecipe(thaumium_boots, new FluidStack(liquid_thaumium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, thaumium_boots);
      }
    }

    
    MeltingRecipeManager.instance.AddRecipe(cluster_iron, new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(cluster_copper, new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(cluster_tin, new FluidStack(liquid_tin,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(cluster_gold, new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(cluster_silver, new FluidStack(liquid_silver,FoundryAPI.FLUID_AMOUNT_INGOT*2));
    MeltingRecipeManager.instance.AddRecipe(cluster_lead, new FluidStack(liquid_lead,FoundryAPI.FLUID_AMOUNT_INGOT*2));

    
    CastingRecipeManager.instance.AddRecipe(
        ingot_thaumium,
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
        cap_iron);

    ShapelessArcaneRecipe capmold_recipe_copper = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
        "FOUNDRY_capmold",
        capmold_soft,
        mold_aspects,
        new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD),
        cap_copper);

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
        cap_iron,
        new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_copper,
        new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_gold,
        new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_silver,
        new FluidStack(liquid_silver,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_thaumium,
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5));

    CastingRecipeManager.instance.AddRecipe(
        cap_iron,
        new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_copper,
        new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_gold,
        new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_silver,
        new FluidStack(liquid_silver,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_thaumium,
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);    
  }
}
