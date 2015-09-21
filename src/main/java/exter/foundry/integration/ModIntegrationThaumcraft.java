package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModIntegrationThaumcraft extends ModIntegration
{
  private boolean enable_shards;
  
  private Fluid liquid_aer;
  private Fluid liquid_terra;
  private Fluid liquid_aqua;
  private Fluid liquid_ignis;
  private Fluid liquid_ordo;
  private Fluid liquid_perditio;

  private Fluid liquid_potentia;
  private Fluid liquid_victus;
  private Fluid liquid_vacous;

  private Fluid liquid_primal;
  
  private Fluid liquid_thaumium;
  private Fluid liquid_voidmetal;

  public ModIntegrationThaumcraft(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    liquid_thaumium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Thaumium", 1850, 14);
    liquid_voidmetal = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Void", 1700, 6);

    FoundryUtils.RegisterBasicMeltingRecipes("Thaumium", liquid_thaumium);
    FoundryUtils.RegisterBasicMeltingRecipes("Void", liquid_voidmetal);

    
    enable_shards = config.get("thaumcraft.liquidshards", "integration", true).getBoolean(true);
    
    if(enable_shards)
    {
      liquid_aer = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Aer", 1200, 13);
      liquid_terra = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Terra", 1200, 13);
      liquid_aqua = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Aqua", 1200, 13);
      liquid_ignis = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Ignis", 1200, 13);
      liquid_ordo = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Ordo", 1200, 13);
      liquid_perditio = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Perditio", 1200, 13);

      liquid_potentia = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Potentia", 1200, 13);
      liquid_victus = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Victus", 1200, 13);
      liquid_vacous = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Vacous", 1200, 13);

      liquid_primal = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Primal", 1200, 13);
    }
  }

  @Override
  public void OnInit()
  {

  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void OnClientPostInit()
  {
    Item shard = ItemApi.getItem("itemShard",6).getItem();

    MaterialRegistry.instance.RegisterTypeIcon("NativeCluster", ItemStack.copyItemStack(ItemApi.getItem("itemNugget",16)));
    MaterialRegistry.instance.RegisterTypeIcon("Shard", ItemStack.copyItemStack(ItemApi.getItem("itemShard",7)));

    MaterialRegistry.instance.RegisterMaterialIcon("Aer",new ItemStack(shard,1,0));
    MaterialRegistry.instance.RegisterMaterialIcon("Ignis",new ItemStack(shard,1,1));
    MaterialRegistry.instance.RegisterMaterialIcon("Aqua",new ItemStack(shard,1,2));
    MaterialRegistry.instance.RegisterMaterialIcon("Terra",new ItemStack(shard,1,3));
    MaterialRegistry.instance.RegisterMaterialIcon("Ordo",new ItemStack(shard,1,4));
    MaterialRegistry.instance.RegisterMaterialIcon("Perditio",new ItemStack(shard,1,5));

    MaterialRegistry.instance.RegisterMaterialIcon("Energy",FoundryItems.Component(ItemComponent.COMPONENT_SHARD_ENERGY_TC));
    MaterialRegistry.instance.RegisterMaterialIcon("Life",FoundryItems.Component(ItemComponent.COMPONENT_SHARD_LIFE_TC));
    MaterialRegistry.instance.RegisterMaterialIcon("Void",FoundryItems.Component(ItemComponent.COMPONENT_SHARD_VOID_TC));
    
    MaterialRegistry.instance.RegisterMaterialIcon("Balanced",new ItemStack(shard,1,6));
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

    ItemStack ingot_voidmetal = ItemStack.copyItemStack(ItemApi.getItem("itemResource",16));

    ItemStack thaumium_pickaxe = ItemStack.copyItemStack(ItemApi.getItem("itemPickThaumium",0));
    ItemStack thaumium_axe = ItemStack.copyItemStack(ItemApi.getItem("itemAxeThaumium",0));
    ItemStack thaumium_shovel = ItemStack.copyItemStack(ItemApi.getItem("itemShovelThaumium",0));
    ItemStack thaumium_hoe = ItemStack.copyItemStack(ItemApi.getItem("itemHoeThaumium",0));
    ItemStack thaumium_sword = ItemStack.copyItemStack(ItemApi.getItem("itemSwordThaumium",0));

    ItemStack thaumium_helmet = ItemStack.copyItemStack(ItemApi.getItem("itemHelmetThaumium",0));
    ItemStack thaumium_chestplate = ItemStack.copyItemStack(ItemApi.getItem("itemChestThaumium",0));
    ItemStack thaumium_leggings = ItemStack.copyItemStack(ItemApi.getItem("itemLegsThaumium",0));
    ItemStack thaumium_boots = ItemStack.copyItemStack(ItemApi.getItem("itemBootsThaumium",0));

    ItemStack void_pickaxe = ItemStack.copyItemStack(ItemApi.getItem("itemPickVoid",0));
    ItemStack void_axe = ItemStack.copyItemStack(ItemApi.getItem("itemAxeVoid",0));
    ItemStack void_shovel = ItemStack.copyItemStack(ItemApi.getItem("itemShovelVoid",0));
    ItemStack void_hoe = ItemStack.copyItemStack(ItemApi.getItem("itemHoeVoid",0));
    ItemStack void_sword = ItemStack.copyItemStack(ItemApi.getItem("itemSwordVoid",0));

    ItemStack void_helmet = ItemStack.copyItemStack(ItemApi.getItem("itemHelmetVoid",0));
    ItemStack void_chestplate = ItemStack.copyItemStack(ItemApi.getItem("itemChestVoid",0));
    ItemStack void_leggings = ItemStack.copyItemStack(ItemApi.getItem("itemLegsVoid",0));
    ItemStack void_boots = ItemStack.copyItemStack(ItemApi.getItem("itemBootsVoid",0));

    for(String metal:LiquidMetalRegistry.instance.GetFluidNames())
    {
      String oredict_name = "cluster" + metal;
      MeltingRecipeManager.instance.AddRecipe(oredict_name,
          new FluidStack(LiquidMetalRegistry.instance.GetFluid(metal),FoundryAPI.FLUID_AMOUNT_INGOT * 2));
      MaterialRegistry.instance.RegisterItem(oredict_name, metal, "NativeCluster");
    }
    

    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);

      RegisterCasting(thaumium_chestplate, liquid_thaumium, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(thaumium_helmet, liquid_thaumium, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(thaumium_leggings, liquid_thaumium, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(thaumium_boots, liquid_thaumium, 4, ItemMold.MOLD_BOOTS, null);

      RegisterCasting(thaumium_pickaxe, liquid_thaumium, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(thaumium_axe, liquid_thaumium, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(thaumium_hoe, liquid_thaumium, 2, ItemMold.MOLD_HOE, extra_sticks2);
      RegisterCasting(thaumium_shovel, liquid_thaumium, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(thaumium_sword, liquid_thaumium, 2, ItemMold.MOLD_SWORD, extra_sticks1);

      RegisterCasting(void_chestplate, liquid_voidmetal, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(void_helmet, liquid_voidmetal, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(void_leggings, liquid_voidmetal, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(void_boots, liquid_voidmetal, 4, ItemMold.MOLD_BOOTS, null);

      RegisterCasting(void_pickaxe, liquid_voidmetal, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(void_axe, liquid_voidmetal, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(void_hoe, liquid_voidmetal, 2, ItemMold.MOLD_HOE, extra_sticks2);
      RegisterCasting(void_shovel, liquid_voidmetal, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(void_sword, liquid_voidmetal, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, thaumium_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, thaumium_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, thaumium_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, thaumium_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, thaumium_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, thaumium_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, thaumium_hoe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, thaumium_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, thaumium_sword);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, void_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, void_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, void_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, void_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, void_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, void_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, void_hoe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, void_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, void_sword);
    }

    
    CastingRecipeManager.instance.AddRecipe(
        ingot_thaumium,
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_INGOT),
        FoundryItems.Mold(ItemMold.MOLD_INGOT),
        null);
    CastingRecipeManager.instance.AddRecipe(
        ingot_voidmetal,
        new FluidStack(liquid_voidmetal,FoundryAPI.FLUID_AMOUNT_INGOT),
        FoundryItems.Mold(ItemMold.MOLD_INGOT),
        null);

    AspectList mold_aspects = new AspectList();
    mold_aspects.add(Aspect.ORDER, 5);
    mold_aspects.add(Aspect.EARTH, 4);
    mold_aspects.add(Aspect.WATER, 2);
    
    ItemStack capmold_soft = FoundryItems.Mold(ItemMold.MOLD_CAP_TC_SOFT);

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
        cap_copper);

    ResearchCategories.registerCategory(
        "FOUNDRY",
        new ResourceLocation("foundry:textures/misc/tc_research_shard.png"), 
        new ResourceLocation("thaumcraft:textures/gui/gui_researchback.png"));
    
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
    mold_research.setParents("CAP_copper","CAP_silver","CAP_thaumium");
    mold_research.registerResearchItem();
    
    ItemStack cap_mold = FoundryItems.Mold(ItemMold.MOLD_CAP_TC);
    
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CAP_TC_SOFT, ItemMold.MOLD_CAP_TC);
    CastingRecipeManager.instance.AddMold( cap_mold);
    
    MeltingRecipeManager.instance.AddRecipe(
        cap_iron,
        new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_copper,
        new FluidStack(FoundryRecipes.liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_gold,
        new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_silver,
        new FluidStack(FoundryRecipes.liquid_silver,FoundryAPI.FLUID_AMOUNT_NUGGET*5));
    MeltingRecipeManager.instance.AddRecipe(
        cap_thaumium,
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5));

    CastingRecipeManager.instance.AddRecipe(
        cap_iron,
        new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_copper,
        new FluidStack(FoundryRecipes.liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_gold,
        new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_silver,
        new FluidStack(FoundryRecipes.liquid_silver,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);
    CastingRecipeManager.instance.AddRecipe(
        cap_thaumium,
        new FluidStack(liquid_thaumium,FoundryAPI.FLUID_AMOUNT_NUGGET*5),
        cap_mold, null);    

    if(enable_shards)
    {
      ThaumcraftApi.registerObjectTag(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_ENERGY_TC),
          (new AspectList()).add(Aspect.ENERGY, 2).add(Aspect.CRYSTAL, 1).add(Aspect.MAGIC, 1));
      ThaumcraftApi.registerObjectTag(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_LIFE_TC),
          (new AspectList()).add(Aspect.LIFE, 2).add(Aspect.CRYSTAL, 1).add(Aspect.MAGIC, 1));
      ThaumcraftApi.registerObjectTag(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_VOID_TC),
          (new AspectList()).add(Aspect.VOID, 2).add(Aspect.CRYSTAL, 1).add(Aspect.MAGIC, 1));


      ItemStack shardmold_soft = FoundryItems.Mold(ItemMold.MOLD_SHARD_TC_SOFT);
      Item shard = ItemApi.getItem("itemShard",6).getItem();
      
      ShapelessArcaneRecipe shardmold_recipe = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
          "FOUNDRY_shardmold",
          shardmold_soft,
          mold_aspects,
          new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD),
          new ItemStack(shard,1,OreDictionary.WILDCARD_VALUE));

      ResearchItem shardmold_research = new ResearchItem(
          "FOUNDRY_shardmold", "FOUNDRY",
          (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.CRAFT, 1).add(Aspect.MAGIC, 1),
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
      
      ItemStack shard_mold = FoundryItems.Mold(ItemMold.MOLD_SHARD_TC);
      
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SHARD_TC_SOFT, ItemMold.MOLD_SHARD_TC);
      CastingRecipeManager.instance.AddMold(shard_mold);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,0), new FluidStack(liquid_aer,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,1), new FluidStack(liquid_ignis,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,2), new FluidStack(liquid_aqua,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,3), new FluidStack(liquid_terra,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,4), new FluidStack(liquid_ordo,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,5), new FluidStack(liquid_perditio,FoundryAPI.FLUID_AMOUNT_INGOT));

      MeltingRecipeManager.instance.AddRecipe(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_ENERGY_TC), new FluidStack(liquid_potentia,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_LIFE_TC), new FluidStack(liquid_victus,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_VOID_TC), new FluidStack(liquid_vacous,FoundryAPI.FLUID_AMOUNT_INGOT));
      
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,6), new FluidStack(liquid_primal,FoundryAPI.FLUID_AMOUNT_INGOT));

      CastingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,0), new FluidStack(liquid_aer,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,1), new FluidStack(liquid_ignis,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,2), new FluidStack(liquid_aqua,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,3), new FluidStack(liquid_terra,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,4), new FluidStack(liquid_ordo,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,5), new FluidStack(liquid_perditio,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);

      CastingRecipeManager.instance.AddRecipe(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_ENERGY_TC), new FluidStack(liquid_potentia,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.AddRecipe(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_LIFE_TC), new FluidStack(liquid_victus,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      CastingRecipeManager.instance.AddRecipe(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_VOID_TC), new FluidStack(liquid_vacous,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);
      
      CastingRecipeManager.instance.AddRecipe(new ItemStack(shard,1,6), new FluidStack(liquid_primal,FoundryAPI.FLUID_AMOUNT_INGOT),shard_mold,null);

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_potentia,2),
          new FluidStack[] {
              new FluidStack(liquid_ignis,1),
              new FluidStack(liquid_ordo,1)
          });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_victus,2),
          new FluidStack[] {
              new FluidStack(liquid_aqua,1),
              new FluidStack(liquid_terra,1)
          });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_vacous,2),
          new FluidStack[] {
              new FluidStack(liquid_aer,1),
              new FluidStack(liquid_perditio,1)
          });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_primal,3),
          new FluidStack[] {
              new FluidStack(liquid_potentia,1),
              new FluidStack(liquid_victus,1),
              new FluidStack(liquid_vacous,1)
          });
      
      
      MaterialRegistry.instance.RegisterItem(new ItemStack(shard,1,0),"Aer","Shard");
      MaterialRegistry.instance.RegisterItem(new ItemStack(shard,1,1),"Ignis","Shard");
      MaterialRegistry.instance.RegisterItem(new ItemStack(shard,1,2),"Aqua","Shard");
      MaterialRegistry.instance.RegisterItem(new ItemStack(shard,1,3),"Terra","Shard");
      MaterialRegistry.instance.RegisterItem(new ItemStack(shard,1,4),"Ordo","Shard");
      MaterialRegistry.instance.RegisterItem(new ItemStack(shard,1,5),"Perditio","Shard");

      MaterialRegistry.instance.RegisterItem(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_ENERGY_TC),"Energy","Shard");
      MaterialRegistry.instance.RegisterItem(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_LIFE_TC),"Life","Shard");
      MaterialRegistry.instance.RegisterItem(FoundryItems.Component(ItemComponent.COMPONENT_SHARD_VOID_TC),"Void","Shard");
      
      MaterialRegistry.instance.RegisterItem(new ItemStack(shard,1,6),"Balanced","Shard");
    }
  }
}
