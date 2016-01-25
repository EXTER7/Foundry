package exter.foundry.integration;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.FluidLiquidMetal;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "exter.foundry.integration.IModIntegration", modid = "Thaumcraft")
public class ModIntegrationThaumcraft implements IModIntegration
{
  private boolean enable_shards;
  private boolean gear_recipes;
  private boolean plate_recipes;
  
  private FluidLiquidMetal liquid_aer;
  private FluidLiquidMetal liquid_terra;
  private FluidLiquidMetal liquid_aqua;
  private FluidLiquidMetal liquid_ignis;
  private FluidLiquidMetal liquid_ordo;
  private FluidLiquidMetal liquid_perditio;

  private FluidLiquidMetal liquid_potentia;
  private FluidLiquidMetal liquid_victus;
  private FluidLiquidMetal liquid_vacous;

  private FluidLiquidMetal liquid_primal;
  
  private FluidLiquidMetal liquid_thaumium;
  private FluidLiquidMetal liquid_voidmetal;


  @Optional.Method(modid = "Thaumcraft")
  @Override
  public void onPreInit(Configuration config)
  {
    liquid_thaumium = LiquidMetalRegistry.instance.registerLiquidMetal( "Thaumium", 1850, 14);
    liquid_voidmetal = LiquidMetalRegistry.instance.registerLiquidMetal( "Void", 1700, 6);

    FoundryUtils.registerBasicMeltingRecipes("Thaumium", liquid_thaumium);
    FoundryUtils.registerBasicMeltingRecipes("Void", liquid_voidmetal);

    
    enable_shards = config.get(getName() + ".liquidshards", "integration", true).getBoolean(true);
    gear_recipes = config.get("integration", getName() + ".gears", true).getBoolean(true);
    plate_recipes = config.get("integration", getName() + ".plates", true).getBoolean(true);

    if(enable_shards)
    {
      liquid_aer = LiquidMetalRegistry.instance.registerLiquidMetal( "Aer", 1200, 13);
      liquid_terra = LiquidMetalRegistry.instance.registerLiquidMetal( "Terra", 1200, 13);
      liquid_aqua = LiquidMetalRegistry.instance.registerLiquidMetal( "Aqua", 1200, 13);
      liquid_ignis = LiquidMetalRegistry.instance.registerLiquidMetal( "Ignis", 1200, 13);
      liquid_ordo = LiquidMetalRegistry.instance.registerLiquidMetal( "Ordo", 1200, 13);
      liquid_perditio = LiquidMetalRegistry.instance.registerLiquidMetal( "Perditio", 1200, 13);

      liquid_potentia = LiquidMetalRegistry.instance.registerLiquidMetal( "Potentia", 1200, 13);
      liquid_victus = LiquidMetalRegistry.instance.registerLiquidMetal( "Victus", 1200, 13);
      liquid_vacous = LiquidMetalRegistry.instance.registerLiquidMetal( "Vacous", 1200, 13);

      liquid_primal = LiquidMetalRegistry.instance.registerLiquidMetal( "Primal", 1200, 13);
    }
  }

  @Optional.Method(modid = "Thaumcraft")
  @Override
  public void onInit()
  {

  }
  
  @Optional.Method(modid = "Thaumcraft")
  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPostInit()
  {
    MaterialRegistry.instance.registerTypeIcon("NativeCluster", FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "clusterIron"));
    MaterialRegistry.instance.registerTypeIcon("Shard", FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardBalanced"));

    MaterialRegistry.instance.registerMaterialIcon("Aer",FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardAir"));
    MaterialRegistry.instance.registerMaterialIcon("Ignis",FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardFire"));
    MaterialRegistry.instance.registerMaterialIcon("Aqua",FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardWater"));
    MaterialRegistry.instance.registerMaterialIcon("Terra",FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardEarth"));
    MaterialRegistry.instance.registerMaterialIcon("Ordo",FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardOrder"));
    MaterialRegistry.instance.registerMaterialIcon("Perditio",FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardEntropy"));

    MaterialRegistry.instance.registerMaterialIcon("Potentia",FoundryItems.component(ItemComponent.COMPONENT_SHARD_ENERGY_TC));
    MaterialRegistry.instance.registerMaterialIcon("Victus",FoundryItems.component(ItemComponent.COMPONENT_SHARD_LIFE_TC));
    MaterialRegistry.instance.registerMaterialIcon("Vacous",FoundryItems.component(ItemComponent.COMPONENT_SHARD_VOID_TC));
    
    MaterialRegistry.instance.registerMaterialIcon("Balanced",FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardBalanced"));
  }

  @Optional.Method(modid = "Thaumcraft")
  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("Thaumcraft"))
    {
      return;
    }
    ItemStack cap_iron = new ItemStack(GameRegistry.findItem("Thaumcraft", "wand_cap"),1,0);
    ItemStack cap_gold = new ItemStack(GameRegistry.findItem("Thaumcraft", "wand_cap"),1,1);
    ItemStack cap_brass = new ItemStack(GameRegistry.findItem("Thaumcraft", "wand_cap"),1,2);
    ItemStack cap_thaumium = new ItemStack(GameRegistry.findItem("Thaumcraft", "wand_cap"),1,3);

    ItemStack thaumium_pickaxe = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_pick"));
    ItemStack thaumium_axe = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_axe"));
    ItemStack thaumium_shovel = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_shovel"));
    ItemStack thaumium_hoe = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_hoe"));
    ItemStack thaumium_sword = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_sword"));

    ItemStack thaumium_helmet = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_helm"));
    ItemStack thaumium_chestplate = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_chest"));
    ItemStack thaumium_leggings = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_legs"));
    ItemStack thaumium_boots = new ItemStack(GameRegistry.findItem("Thaumcraft", "thaumium_boots"));

    ItemStack void_pickaxe = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_pick"));
    ItemStack void_axe = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_axe"));
    ItemStack void_shovel = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_shovel"));
    ItemStack void_hoe = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_hoe"));
    ItemStack void_sword = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_sword"));

    ItemStack void_helmet = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_helm"));
    ItemStack void_chestplate = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_chest"));
    ItemStack void_leggings = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_legs"));
    ItemStack void_boots = new ItemStack(GameRegistry.findItem("Thaumcraft", "void_boots"));

    
    
    
    for(String metal:LiquidMetalRegistry.instance.getFluidNames())
    {
      String oredict_name = "cluster" + metal;
      MeltingRecipeManager.instance.addRecipe(oredict_name,
          new FluidStack(LiquidMetalRegistry.instance.getFluid(metal),FoundryAPI.FLUID_AMOUNT_INGOT * 2));
      MaterialRegistry.instance.registerItem(oredict_name, metal, "NativeCluster");
    }
    
    if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
    {
      ItemStack gear_brass = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "gearBrass");
      ItemStack gear_thaumium = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "gearThaumium");
      ItemStack gear_void = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "gearVoid");

      FoundryMiscUtils.registerCasting(gear_brass, FoundryRecipes.liquid_brass, 4, ItemMold.MOLD_GEAR, null);
      FoundryMiscUtils.registerCasting(gear_thaumium, liquid_thaumium, 4, ItemMold.MOLD_GEAR, null);
      FoundryMiscUtils.registerCasting(gear_void, liquid_voidmetal, 4, ItemMold.MOLD_GEAR, null);
      
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearBrass");
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearThaumium");
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearVoid");
    }
    
    if(plate_recipes)
    {
      ItemStack plate_iron = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "plateIron");
      ItemStack plate_brass = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "plateBrass");
      ItemStack plate_thaumium = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "plateThaumium");
      ItemStack plate_void = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "plateVoid");

      FoundryMiscUtils.registerCasting(plate_iron, FoundryRecipes.liquid_iron, 1, ItemMold.MOLD_PLATE, null);
      FoundryMiscUtils.registerCasting(plate_brass, FoundryRecipes.liquid_brass, 1, ItemMold.MOLD_PLATE, null);
      FoundryMiscUtils.registerCasting(plate_thaumium, liquid_thaumium, 1, ItemMold.MOLD_PLATE, null);
      FoundryMiscUtils.registerCasting(plate_void, liquid_voidmetal, 1, ItemMold.MOLD_PLATE, null);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PLATE_SOFT, "plateIron");
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PLATE_SOFT, "plateBrass");
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PLATE_SOFT, "plateThaumium");
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PLATE_SOFT, "plateVoid");
    }
    
    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);

      FoundryMiscUtils.registerCasting(thaumium_chestplate, liquid_thaumium, 8, ItemMold.MOLD_CHESTPLATE, null);
      FoundryMiscUtils.registerCasting(thaumium_helmet, liquid_thaumium, 5, ItemMold.MOLD_HELMET, null);
      FoundryMiscUtils.registerCasting(thaumium_leggings, liquid_thaumium, 7, ItemMold.MOLD_LEGGINGS, null);
      FoundryMiscUtils.registerCasting(thaumium_boots, liquid_thaumium, 4, ItemMold.MOLD_BOOTS, null);

      FoundryMiscUtils.registerCasting(thaumium_pickaxe, liquid_thaumium, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(thaumium_axe, liquid_thaumium, 3, ItemMold.MOLD_AXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(thaumium_hoe, liquid_thaumium, 2, ItemMold.MOLD_HOE, extra_sticks2);
      FoundryMiscUtils.registerCasting(thaumium_shovel, liquid_thaumium, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      FoundryMiscUtils.registerCasting(thaumium_sword, liquid_thaumium, 2, ItemMold.MOLD_SWORD, extra_sticks1);

      FoundryMiscUtils.registerCasting(void_chestplate, liquid_voidmetal, 8, ItemMold.MOLD_CHESTPLATE, null);
      FoundryMiscUtils.registerCasting(void_helmet, liquid_voidmetal, 5, ItemMold.MOLD_HELMET, null);
      FoundryMiscUtils.registerCasting(void_leggings, liquid_voidmetal, 7, ItemMold.MOLD_LEGGINGS, null);
      FoundryMiscUtils.registerCasting(void_boots, liquid_voidmetal, 4, ItemMold.MOLD_BOOTS, null);

      FoundryMiscUtils.registerCasting(void_pickaxe, liquid_voidmetal, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(void_axe, liquid_voidmetal, 3, ItemMold.MOLD_AXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(void_hoe, liquid_voidmetal, 2, ItemMold.MOLD_HOE, extra_sticks2);
      FoundryMiscUtils.registerCasting(void_shovel, liquid_voidmetal, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      FoundryMiscUtils.registerCasting(void_sword, liquid_voidmetal, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, thaumium_leggings);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, thaumium_helmet);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, thaumium_leggings);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, thaumium_boots);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, thaumium_pickaxe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, thaumium_axe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, thaumium_hoe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, thaumium_shovel);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, thaumium_sword);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, void_leggings);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, void_helmet);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, void_leggings);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, void_boots);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, void_pickaxe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, void_axe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, void_hoe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, void_shovel);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, void_sword);
    }

 
    AspectList mold_aspects = new AspectList();
    mold_aspects.add(Aspect.ORDER, 5);
    mold_aspects.add(Aspect.EARTH, 4);
    mold_aspects.add(Aspect.WATER, 2);
    
    ItemStack capmold_soft = FoundryItems.mold(ItemMold.MOLD_CAP_TC_SOFT);

    ShapelessArcaneRecipe capmold_recipe_iron = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
        "FOUNDRY_capmold",
        capmold_soft,
        mold_aspects,
        new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD),
        cap_iron);

    ShapelessArcaneRecipe capmold_recipe_copper = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
        "FOUNDRY_capmold",
        capmold_soft,
        mold_aspects,
        new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD),
        cap_brass);

    ResearchCategories.registerCategory(
        "FOUNDRY",
        null,
        new ResourceLocation("foundry:textures/misc/tc_research_shard.png"), 
        new ResourceLocation("thaumcraft:textures/gui/gui_research_back_1.jpg"));
    
    ResearchItem mold_research = new ResearchItem(
        "FOUNDRY_capmold", "FOUNDRY",
        (new AspectList()).add(Aspect.METAL, 1).add(Aspect.CRAFT, 1),
        3, 0,
        1,
        new ResourceLocation("foundry:textures/misc/tc_research_cap.png"));
    mold_research.setPages(
        new ResearchPage("tc.research.capmold.text"),
        new ResearchPage(capmold_recipe_copper),
        new ResearchPage(capmold_recipe_iron),
        new ResearchPage(capmold_soft));
    mold_research.setParents("CAP_thaumium");
    mold_research.registerResearchItem();
    
    ItemStack cap_mold = FoundryItems.mold(ItemMold.MOLD_CAP_TC);
    
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_CAP_TC_SOFT, ItemMold.MOLD_CAP_TC);
    CastingRecipeManager.instance.addMold( cap_mold);
    
    MeltingRecipeManager.instance.addRecipe(
        cap_iron,
        new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.addRecipe(
        cap_gold,
        new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.addRecipe(
        cap_brass,
        new FluidStack(FoundryRecipes.liquid_brass,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.addRecipe(
        cap_thaumium,
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5));

    CastingRecipeManager.instance.addRecipe(
        cap_iron,
        new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.addRecipe(
        cap_gold,
        new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.addRecipe(
        cap_brass,
        new FluidStack(FoundryRecipes.liquid_brass,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.addRecipe(
        cap_thaumium,
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);    

    if(enable_shards)
    {
      ThaumcraftApi.registerObjectTag(FoundryItems.component(ItemComponent.COMPONENT_SHARD_ENERGY_TC),
          (new AspectList()).add(Aspect.ENERGY, 2).add(Aspect.CRYSTAL, 1));
      ThaumcraftApi.registerObjectTag(FoundryItems.component(ItemComponent.COMPONENT_SHARD_LIFE_TC),
          (new AspectList()).add(Aspect.LIFE, 2).add(Aspect.CRYSTAL, 1));
      ThaumcraftApi.registerObjectTag(FoundryItems.component(ItemComponent.COMPONENT_SHARD_VOID_TC),
          (new AspectList()).add(Aspect.VOID, 2).add(Aspect.CRYSTAL, 1));

      
      ItemStack shard_air = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardAir");
      ItemStack shard_fire = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardFire");
      ItemStack shard_water = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardWater");
      ItemStack shard_earth = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardEarth");
      ItemStack shard_order = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardOrder");
      ItemStack shard_entropy = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardEntropy");
      ItemStack shard_balanced = FoundryMiscUtils.getModItemFromOreDictionary("Thaumcraft", "shardBalanced");

      ItemStack shardmold_soft = FoundryItems.mold(ItemMold.MOLD_SHARD_TC_SOFT);
      
      ShapelessArcaneRecipe shardmold_recipe = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
          "FOUNDRY_shardmold",
          shardmold_soft,
          mold_aspects,
          new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD),
          new ItemStack(shard_balanced.getItem(),1,OreDictionary.WILDCARD_VALUE));

      ResearchItem shardmold_research = new ResearchItem(
          "FOUNDRY_shardmold", "FOUNDRY",
          (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.CRAFT, 1).add(Aspect.FIRE, 1),
          -3, 0,
          1,
          new ResourceLocation("foundry:textures/misc/tc_research_shard.png"));
      shardmold_research.setPages(
          new ResearchPage("tc.research.shardmold.text1"),
          new ResearchPage("tc.research.shardmold.text2"),
          new ResearchPage(shardmold_recipe),
          new ResearchPage(shardmold_soft));
      shardmold_research.setParents("BASICARTIFACE");
      shardmold_research.registerResearchItem();
      
      ItemStack shard_mold = FoundryItems.mold(ItemMold.MOLD_SHARD_TC);
      
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SHARD_TC_SOFT, ItemMold.MOLD_SHARD_TC);
      CastingRecipeManager.instance.addMold(shard_mold);

      MeltingRecipeManager.instance.addRecipe(shard_air, new FluidStack(liquid_aer,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.addRecipe(shard_fire, new FluidStack(liquid_ignis,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.addRecipe(shard_water, new FluidStack(liquid_aqua,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.addRecipe(shard_earth, new FluidStack(liquid_terra,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.addRecipe(shard_order, new FluidStack(liquid_ordo,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.addRecipe(shard_entropy, new FluidStack(liquid_perditio,FoundryAPI.FLUID_AMOUNT_INGOT));

      MeltingRecipeManager.instance.addRecipe(FoundryItems.component(ItemComponent.COMPONENT_SHARD_ENERGY_TC), new FluidStack(liquid_potentia,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.addRecipe(FoundryItems.component(ItemComponent.COMPONENT_SHARD_LIFE_TC), new FluidStack(liquid_victus,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.addRecipe(FoundryItems.component(ItemComponent.COMPONENT_SHARD_VOID_TC), new FluidStack(liquid_vacous,FoundryAPI.FLUID_AMOUNT_INGOT));
      
      MeltingRecipeManager.instance.addRecipe(shard_balanced, new FluidStack(liquid_primal,FoundryAPI.FLUID_AMOUNT_INGOT));

      CastingRecipeManager.instance.addRecipe(shard_air, new FluidStack(liquid_aer,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.addRecipe(shard_fire, new FluidStack(liquid_ignis,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.addRecipe(shard_water, new FluidStack(liquid_aqua,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.addRecipe(shard_earth, new FluidStack(liquid_terra,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.addRecipe(shard_order, new FluidStack(liquid_ordo,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.addRecipe(shard_entropy, new FluidStack(liquid_perditio,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);

      CastingRecipeManager.instance.addRecipe(FoundryItems.component(ItemComponent.COMPONENT_SHARD_ENERGY_TC), new FluidStack(liquid_potentia,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.addRecipe(FoundryItems.component(ItemComponent.COMPONENT_SHARD_LIFE_TC), new FluidStack(liquid_victus,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.addRecipe(FoundryItems.component(ItemComponent.COMPONENT_SHARD_VOID_TC), new FluidStack(liquid_vacous,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      
      CastingRecipeManager.instance.addRecipe(shard_balanced, new FluidStack(liquid_primal,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);

      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_potentia,2),
          new FluidStack[] {
              new FluidStack(liquid_ignis,1),
              new FluidStack(liquid_ordo,1)
          });

      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_victus,2),
          new FluidStack[] {
              new FluidStack(liquid_aqua,1),
              new FluidStack(liquid_terra,1)
          });

      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_vacous,2),
          new FluidStack[] {
              new FluidStack(liquid_aer,1),
              new FluidStack(liquid_perditio,1)
          });

      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_primal,3),
          new FluidStack[] {
              new FluidStack(liquid_potentia,1),
              new FluidStack(liquid_victus,1),
              new FluidStack(liquid_vacous,1)
          });
      
      
      MaterialRegistry.instance.registerItem(shard_air,"Aer","Shard");
      MaterialRegistry.instance.registerItem(shard_fire,"Ignis","Shard");
      MaterialRegistry.instance.registerItem(shard_water,"Aqua","Shard");
      MaterialRegistry.instance.registerItem(shard_earth,"Terra","Shard");
      MaterialRegistry.instance.registerItem(shard_order,"Ordo","Shard");
      MaterialRegistry.instance.registerItem(shard_entropy,"Perditio","Shard");

      MaterialRegistry.instance.registerItem(FoundryItems.component(ItemComponent.COMPONENT_SHARD_ENERGY_TC),"Potentia","Shard");
      MaterialRegistry.instance.registerItem(FoundryItems.component(ItemComponent.COMPONENT_SHARD_LIFE_TC),"Victus","Shard");
      MaterialRegistry.instance.registerItem(FoundryItems.component(ItemComponent.COMPONENT_SHARD_VOID_TC),"Vacous","Shard");
      
      MaterialRegistry.instance.registerItem(shard_balanced,"Balanced","Shard");
      
      AtomizerRecipeManager.instance.addRecipe(new ItemStack(GameRegistry.findItem("Thaumcraft", "salis_mundus")), new FluidStack(liquid_primal,FoundryAPI.FLUID_AMOUNT_INGOT));
    }
  }

  @Optional.Method(modid = "Thaumcraft")
  @Override
  public String getName()
  {
    return "thaumcraft";
  }

  @Optional.Method(modid = "Thaumcraft")
  @Override
  public void onAfterPostInit()
  {

  }

  @Optional.Method(modid = "Thaumcraft")
  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInit()
  {

  }

  @Optional.Method(modid = "Thaumcraft")
  @SideOnly(Side.CLIENT)
  @Override
  public void onClientInit()
  {

  }
}
