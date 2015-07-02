package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryAPI;
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

  
  public ModIntegrationBotania(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
  }

  @Override
  public void OnInit()
  {
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void OnClientPostInit()
  {
    MaterialRegistry.instance.RegisterMaterialIcon("Manasteel", new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,0));
    MaterialRegistry.instance.RegisterMaterialIcon("Terrasteel", new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,4));
    MaterialRegistry.instance.RegisterMaterialIcon("Elementium", new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,7));
  }
  
  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Botania"))
    {
      is_loaded = false;
      return;
    }

    ItemStack manasteel_ingot = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,0);
    ItemStack manasteel_block = new ItemStack(GameRegistry.findItem("Botania", "storage"),1,0);

    ItemStack terrasteel_ingot = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,4);
    ItemStack terrasteel_block = new ItemStack(GameRegistry.findItem("Botania", "storage"),1,1);

    ItemStack elementium_ingot = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,7);
    ItemStack elementium_block = new ItemStack(GameRegistry.findItem("Botania", "storage"),1,2);

    ItemStack manasteel_pickaxe = new ItemStack(GameRegistry.findItem("Botania", "manasteelPick"), 1);
    ItemStack manasteel_axe = new ItemStack(GameRegistry.findItem("Botania", "manasteelAxe"), 1);
    ItemStack manasteel_shovel = new ItemStack(GameRegistry.findItem("Botania", "manasteelShovel"), 1);
    ItemStack manasteel_sword = new ItemStack(GameRegistry.findItem("Botania", "manasteelSword"), 1);

    ItemStack manasteel_helmet = new ItemStack(GameRegistry.findItem("Botania", "manasteelHelm"), 1);
    ItemStack manasteel_chestplate = new ItemStack(GameRegistry.findItem("Botania", "manasteelChest"), 1);
    ItemStack manasteel_leggings = new ItemStack(GameRegistry.findItem("Botania", "manasteelLegs"), 1);
    ItemStack manasteel_boots = new ItemStack(GameRegistry.findItem("Botania", "manasteelBoots"), 1);


    ItemStack terrasteel_sword = new ItemStack(GameRegistry.findItem("Botania", "terraSword"), 1);

    ItemStack terrasteel_helmet = new ItemStack(GameRegistry.findItem("Botania", "terrasteelHelm"), 1);
    ItemStack terrasteel_chestplate = new ItemStack(GameRegistry.findItem("Botania", "terrasteelChest"), 1);
    ItemStack terrasteel_leggings = new ItemStack(GameRegistry.findItem("Botania", "terrasteelLegs"), 1);
    ItemStack terrasteel_boots = new ItemStack(GameRegistry.findItem("Botania", "terrasteelBoots"), 1);

    
    ItemStack elementium_pickaxe = new ItemStack(GameRegistry.findItem("Botania", "elementiumPick"), 1);
    ItemStack elementium_axe = new ItemStack(GameRegistry.findItem("Botania", "elementiumAxe"), 1);
    ItemStack elementium_shovel = new ItemStack(GameRegistry.findItem("Botania", "elementiumShovel"), 1);
    ItemStack elementium_sword = new ItemStack(GameRegistry.findItem("Botania", "elementiumSword"), 1);

    ItemStack elementium_helmet = new ItemStack(GameRegistry.findItem("Botania", "elementiumHelm"), 1);
    ItemStack elementium_chestplate = new ItemStack(GameRegistry.findItem("Botania", "elementiumChest"), 1);
    ItemStack elementium_leggings = new ItemStack(GameRegistry.findItem("Botania", "elementiumLegs"), 1);
    ItemStack elementium_boots = new ItemStack(GameRegistry.findItem("Botania", "elementiumBoots"), 1);

    
    ItemStack livingwood_twig = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,3);
    ItemStack dreamwood_twig = new ItemStack(GameRegistry.findItem("Botania", "manaResource"),1,13);
    
    
    if(is_loaded)
    {
      Fluid liquid_manasteel = LiquidMetalRegistry.instance.GetFluid("Manasteel");
      Fluid liquid_terrasteel = LiquidMetalRegistry.instance.GetFluid("Terrasteel");
      Fluid liquid_elementium = LiquidMetalRegistry.instance.GetFluid("Elementium");

      if(FoundryConfig.recipe_tools_armor)
      {
        ItemStack extra_sticks1 = livingwood_twig.copy();
        ItemStack extra_sticks2 = livingwood_twig.copy();
        extra_sticks2.stackSize = 2;

        ItemStack extra_dreamsticks1 = dreamwood_twig.copy();
        ItemStack extra_dreamsticks2 = dreamwood_twig.copy();
        extra_dreamsticks2.stackSize = 2;

        ItemStack mold_chestplate = FoundryItems.Mold(ItemMold.MOLD_CHESTPLATE);
        ItemStack mold_pickaxe = FoundryItems.Mold(ItemMold.MOLD_PICKAXE);
        ItemStack mold_axe = FoundryItems.Mold(ItemMold.MOLD_AXE);
        ItemStack mold_shovel = FoundryItems.Mold(ItemMold.MOLD_SHOVEL);
        ItemStack mold_sword = FoundryItems.Mold(ItemMold.MOLD_SWORD);
        ItemStack mold_leggings = FoundryItems.Mold(ItemMold.MOLD_LEGGINGS);
        ItemStack mold_helmet = FoundryItems.Mold(ItemMold.MOLD_HELMET);
        ItemStack mold_boots = FoundryItems.Mold(ItemMold.MOLD_BOOTS);

        CastingRecipeManager.instance.AddRecipe(manasteel_chestplate, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(manasteel_pickaxe, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(manasteel_axe, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(manasteel_shovel, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(manasteel_sword, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        CastingRecipeManager.instance.AddRecipe(manasteel_leggings, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(manasteel_helmet, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(manasteel_boots, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);


        CastingRecipeManager.instance.AddRecipe(terrasteel_chestplate, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(terrasteel_sword, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        CastingRecipeManager.instance.AddRecipe(terrasteel_leggings, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(terrasteel_helmet, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(terrasteel_boots, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);


        CastingRecipeManager.instance.AddRecipe(elementium_chestplate, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(elementium_pickaxe, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_dreamsticks2);
        CastingRecipeManager.instance.AddRecipe(elementium_axe, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_dreamsticks2);
        CastingRecipeManager.instance.AddRecipe(elementium_shovel, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_dreamsticks2);
        CastingRecipeManager.instance.AddRecipe(elementium_sword, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_dreamsticks1);
        CastingRecipeManager.instance.AddRecipe(elementium_leggings, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(elementium_helmet, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(elementium_boots, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

        
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, manasteel_chestplate);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, manasteel_leggings);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, manasteel_helmet);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, manasteel_boots);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, manasteel_pickaxe);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, manasteel_axe);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, manasteel_shovel);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, manasteel_sword);

        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, terrasteel_chestplate);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, terrasteel_leggings);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, terrasteel_helmet);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, terrasteel_boots);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, terrasteel_sword);
        
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, elementium_chestplate);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT,   elementium_leggings);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT,     elementium_helmet);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT,      elementium_boots);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT,    elementium_pickaxe);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT,        elementium_axe);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT,     elementium_shovel);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT,      elementium_sword);
      }
      ItemStack mold_ingot = FoundryItems.Mold(ItemMold.MOLD_INGOT);
      ItemStack mold_block = FoundryItems.Mold(ItemMold.MOLD_BLOCK);

      MeltingRecipeManager.instance.AddRecipe(manasteel_ingot, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(manasteel_block, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_BLOCK));
      MeltingRecipeManager.instance.AddRecipe(terrasteel_ingot, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(terrasteel_block, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_BLOCK));
      MeltingRecipeManager.instance.AddRecipe(elementium_ingot, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(elementium_block, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_BLOCK));

      CastingRecipeManager.instance.AddRecipe(manasteel_ingot, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(manasteel_block, new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
      CastingRecipeManager.instance.AddRecipe(terrasteel_ingot, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(terrasteel_block, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
      CastingRecipeManager.instance.AddRecipe(elementium_ingot, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(elementium_block, new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
      
      MaterialRegistry.instance.RegisterItem(manasteel_ingot, "Manasteel", "Ingot");
      MaterialRegistry.instance.RegisterItem(manasteel_block, "Manasteel", "Block");
      MaterialRegistry.instance.RegisterItem(terrasteel_ingot, "Terrasteel", "Ingot");
      MaterialRegistry.instance.RegisterItem(terrasteel_block, "Terrasteel", "Block");
      MaterialRegistry.instance.RegisterItem(elementium_ingot, "Elementium", "Ingot");
      MaterialRegistry.instance.RegisterItem(elementium_block, "Elementium", "Block");
    }
  }
}
