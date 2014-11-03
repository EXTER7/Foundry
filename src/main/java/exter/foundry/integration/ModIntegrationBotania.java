package exter.foundry.integration;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationBotania extends ModIntegration
{

  
  static public final int ITEM_MANASTEEL_PICKAXE = 0;
  static public final int ITEM_MANASTEEL_AXE = 1;
  static public final int ITEM_MANASTEEL_SHOVEL = 2;
  static public final int ITEM_MANASTEEL_SWORD = 3;

  static public final int ITEM_MANASTEEL_HELMET = 4;
  static public final int ITEM_MANASTEEL_CHESTPLATE = 5;
  static public final int ITEM_MANASTEEL_LEGGINGS = 6;
  static public final int ITEM_MANASTEEL_BOOTS = 7;

  static public final int ITEM_TERRASTEEL_SWORD = 8;

  static public final int ITEM_TERRASTEEL_HELMET = 9;
  static public final int ITEM_TERRASTEEL_CHESTPLATE = 10;
  static public final int ITEM_TERRASTEEL_LEGGINGS = 11;
  static public final int ITEM_TERRASTEEL_BOOTS = 12;

  static public final int ITEM_ELEMENTIUM_PICKAXE = 13;
  static public final int ITEM_ELEMENTIUM_AXE = 14;
  static public final int ITEM_ELEMENTIUM_SHOVEL = 15;
  static public final int ITEM_ELEMENTIUM_SWORD = 16;

  static public final int ITEM_ELEMENTIUM_HELMET = 17;
  static public final int ITEM_ELEMENTIUM_CHESTPLATE = 18;
  static public final int ITEM_ELEMENTIUM_LEGGINGS = 19;
  static public final int ITEM_ELEMENTIUM_BOOTS = 20;

  static public final int ITEM_MANASTEEL_INGOT = 21;
  static public final int ITEM_MANASTEEL_BLOCK = 22;

  static public final int ITEM_TERRASTEEL_INGOT = 23;
  static public final int ITEM_TERRASTEEL_BLOCK = 24;

  static public final int ITEM_ELEMENTIUM_INGOT = 25;
  static public final int ITEM_ELEMENTIUM_BLOCK = 26;

  static public final int ITEM_LIVINGWOOD_TWIG = 27;

  public ModIntegrationBotania(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    Map<String,Fluid> fluids = new HashMap<String,Fluid>();
    fluids.put("Manasteel",LiquidMetalRegistry.instance.RegisterLiquidMetal( "Manasteel", 1950, 15));
    fluids.put("Terrasteel",LiquidMetalRegistry.instance.RegisterLiquidMetal( "Terrasteel", 2100, 15));
    fluids.put("Elementium",LiquidMetalRegistry.instance.RegisterLiquidMetal( "Elementium", 2400, 15));
    for(Map.Entry<String, Fluid> entry:fluids.entrySet())
    {
      FoundryUtils.RegisterBasicMeltingRecipes(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void OnInit()
  {
    if(!Loader.isModLoaded("Botania"))
    {
      is_loaded = false;
      return;
    }

    items = new ItemStack[28];

    items[ITEM_MANASTEEL_INGOT] = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,0);
    items[ITEM_MANASTEEL_BLOCK] = new ItemStack(GameRegistry.findItem("Botania", "storage"),1,0);

    items[ITEM_TERRASTEEL_INGOT] = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,4);
    items[ITEM_TERRASTEEL_BLOCK] = new ItemStack(GameRegistry.findItem("Botania", "storage"),1,1);

    items[ITEM_ELEMENTIUM_INGOT] = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,7);
    items[ITEM_ELEMENTIUM_BLOCK] = new ItemStack(GameRegistry.findItem("Botania", "storage"),1,2);

    items[ITEM_MANASTEEL_PICKAXE] = new ItemStack(GameRegistry.findItem("Botania", "manasteelPick"), 1);
    items[ITEM_MANASTEEL_AXE] = new ItemStack(GameRegistry.findItem("Botania", "manasteelAxe"), 1);
    items[ITEM_MANASTEEL_SHOVEL] = new ItemStack(GameRegistry.findItem("Botania", "manasteelShovel"), 1);
    items[ITEM_MANASTEEL_SWORD] = new ItemStack(GameRegistry.findItem("Botania", "manasteelSword"), 1);

    items[ITEM_MANASTEEL_HELMET] = new ItemStack(GameRegistry.findItem("Botania", "manasteelHelm"), 1);
    items[ITEM_MANASTEEL_CHESTPLATE] = new ItemStack(GameRegistry.findItem("Botania", "manasteelChest"), 1);
    items[ITEM_MANASTEEL_LEGGINGS] = new ItemStack(GameRegistry.findItem("Botania", "manasteelLegs"), 1);
    items[ITEM_MANASTEEL_BOOTS] = new ItemStack(GameRegistry.findItem("Botania", "manasteelBoots"), 1);


    items[ITEM_TERRASTEEL_SWORD] = new ItemStack(GameRegistry.findItem("Botania", "terraSword"), 1);

    items[ITEM_TERRASTEEL_HELMET] = new ItemStack(GameRegistry.findItem("Botania", "terrasteelHelm"), 1);
    items[ITEM_TERRASTEEL_CHESTPLATE] = new ItemStack(GameRegistry.findItem("Botania", "terrasteelChest"), 1);
    items[ITEM_TERRASTEEL_LEGGINGS] = new ItemStack(GameRegistry.findItem("Botania", "terrasteelLegs"), 1);
    items[ITEM_TERRASTEEL_BOOTS] = new ItemStack(GameRegistry.findItem("Botania", "terrasteelBoots"), 1);

    
    items[ITEM_ELEMENTIUM_PICKAXE] = new ItemStack(GameRegistry.findItem("Botania", "elementiumPick"), 1);
    items[ITEM_ELEMENTIUM_AXE] = new ItemStack(GameRegistry.findItem("Botania", "elementiumAxe"), 1);
    items[ITEM_ELEMENTIUM_SHOVEL] = new ItemStack(GameRegistry.findItem("Botania", "elementiumShovel"), 1);
    items[ITEM_ELEMENTIUM_SWORD] = new ItemStack(GameRegistry.findItem("Botania", "elementiumSword"), 1);

    items[ITEM_ELEMENTIUM_HELMET] = new ItemStack(GameRegistry.findItem("Botania", "elementiumHelm"), 1);
    items[ITEM_ELEMENTIUM_CHESTPLATE] = new ItemStack(GameRegistry.findItem("Botania", "elementiumChest"), 1);
    items[ITEM_ELEMENTIUM_LEGGINGS] = new ItemStack(GameRegistry.findItem("Botania", "elementiumLegs"), 1);
    items[ITEM_ELEMENTIUM_BOOTS] = new ItemStack(GameRegistry.findItem("Botania", "elementiumBoots"), 1);

    
    items[ITEM_LIVINGWOOD_TWIG] = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,3);
    
    VerifyItems();
    
    if(is_loaded)
    {
      Fluid liquid_manasteel = LiquidMetalRegistry.instance.GetFluid("Manasteel");
      Fluid liquid_terrasteel = LiquidMetalRegistry.instance.GetFluid("Terrasteel");
      Fluid liquid_elementium = LiquidMetalRegistry.instance.GetFluid("Elementium");

      if(FoundryConfig.recipe_tools_armor)
      {
        //TODO
        ItemStack extra_sticks1 = items[ITEM_LIVINGWOOD_TWIG].copy();
        ItemStack extra_sticks2 = items[ITEM_LIVINGWOOD_TWIG].copy();
        extra_sticks2.stackSize = 2;

        ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CHESTPLATE);
        ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PICKAXE);
        ItemStack mold_axe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_AXE);
        ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SHOVEL);
        ItemStack mold_sword = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SWORD);
        ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_LEGGINGS);
        ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HELMET);
        ItemStack mold_boots = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BOOTS);

        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_CHESTPLATE], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_PICKAXE], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_AXE], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_SHOVEL], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_SWORD], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_LEGGINGS], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_HELMET], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_BOOTS], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);


        CastingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_CHESTPLATE], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_SWORD], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_LEGGINGS], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_HELMET], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_BOOTS], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);


        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_CHESTPLATE], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_PICKAXE], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_AXE], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_SHOVEL], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_SWORD], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_LEGGINGS], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_HELMET], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_BOOTS], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

        
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, items[ITEM_MANASTEEL_CHESTPLATE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, items[ITEM_MANASTEEL_LEGGINGS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, items[ITEM_MANASTEEL_HELMET]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, items[ITEM_MANASTEEL_BOOTS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, items[ITEM_MANASTEEL_PICKAXE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, items[ITEM_MANASTEEL_AXE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, items[ITEM_MANASTEEL_SHOVEL]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, items[ITEM_MANASTEEL_SWORD]);

        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, items[ITEM_TERRASTEEL_CHESTPLATE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, items[ITEM_TERRASTEEL_LEGGINGS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, items[ITEM_TERRASTEEL_HELMET]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, items[ITEM_TERRASTEEL_BOOTS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, items[ITEM_TERRASTEEL_SWORD]);
        
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, items[ITEM_ELEMENTIUM_CHESTPLATE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, items[ITEM_ELEMENTIUM_LEGGINGS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, items[ITEM_ELEMENTIUM_HELMET]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, items[ITEM_ELEMENTIUM_BOOTS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, items[ITEM_ELEMENTIUM_PICKAXE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, items[ITEM_ELEMENTIUM_AXE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, items[ITEM_ELEMENTIUM_SHOVEL]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, items[ITEM_ELEMENTIUM_SWORD]);
      }
      ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_INGOT);
      ItemStack mold_block = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BLOCK);

      MeltingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_INGOT], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_BLOCK], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_BLOCK));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_INGOT], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_BLOCK], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_BLOCK));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_INGOT], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_BLOCK], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_BLOCK));

      CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_INGOT], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_MANASTEEL_BLOCK], new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_INGOT], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_TERRASTEEL_BLOCK], new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_INGOT], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_ELEMENTIUM_BLOCK], new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
      
      MaterialRegistry.instance.RegisterItem(items[ITEM_MANASTEEL_INGOT], "Manasteel", "Ingot");
      MaterialRegistry.instance.RegisterItem(items[ITEM_MANASTEEL_BLOCK], "Manasteel", "Block");
      MaterialRegistry.instance.RegisterItem(items[ITEM_TERRASTEEL_INGOT], "Terrasteel", "Ingot");
      MaterialRegistry.instance.RegisterItem(items[ITEM_TERRASTEEL_BLOCK], "Terrasteel", "Block");
      MaterialRegistry.instance.RegisterItem(items[ITEM_ELEMENTIUM_INGOT], "Elementium", "Ingot");
      MaterialRegistry.instance.RegisterItem(items[ITEM_ELEMENTIUM_BLOCK], "Elementium", "Block");
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void OnClientInit()
  {
    MaterialRegistry.instance.RegisterMaterialIcon("Manasteel", items[ITEM_MANASTEEL_INGOT]);
    MaterialRegistry.instance.RegisterMaterialIcon("Terrasteel", items[ITEM_TERRASTEEL_INGOT]);
    MaterialRegistry.instance.RegisterMaterialIcon("Elementium", items[ITEM_ELEMENTIUM_INGOT]);
  }
  
  @Override
  public void OnPostInit()
  {

  }

}
