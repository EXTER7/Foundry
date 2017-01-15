package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModIntegrationBotania implements IModIntegration
{
  private FluidLiquidMetal liquid_manasteel;
  private FluidLiquidMetal liquid_terrasteel;
  private FluidLiquidMetal liquid_elementium;

  private ItemStack getItemStack(String name)
  {
    return getItemStack(name,0);
  }
  private ItemStack getItemStack(String name,int meta)
  {
    Item item = Item.REGISTRY.getObject(new ResourceLocation("Botania", name));
    if(item == null)
    {
      return null;
    }
    return new ItemStack(item,1,meta);
  }

  @Override
  public void onPreInit(Configuration config)
  {
    liquid_manasteel = LiquidMetalRegistry.instance.registerLiquidMetal("Manasteel", 1950, 15);
    liquid_terrasteel = LiquidMetalRegistry.instance.registerLiquidMetal("Terrasteel", 2100, 15);
    liquid_elementium = LiquidMetalRegistry.instance.registerLiquidMetal("ElvenElementium", 2400, 15);

    FoundryUtils.registerBasicMeltingRecipes("Manasteel", liquid_manasteel);
    FoundryUtils.registerBasicMeltingRecipes("Terrasteel", liquid_terrasteel);
    FoundryUtils.registerBasicMeltingRecipes("ElvenElementium", liquid_elementium);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPostInit()
  {

  }

  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("botania"))
    {
      return;
    }

    ItemStack manasteel_block = getItemStack("storage", 0);
    ItemStack terrasteel_block = getItemStack("storage", 1);
    ItemStack elementium_block = getItemStack("storage",  2);
    ItemStack mold_block = FoundryItems.mold(ItemMold.SubItem.BLOCK);
    
    MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(manasteel_block), new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_BLOCK));
    MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(terrasteel_block), new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_BLOCK));
    MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(elementium_block), new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_BLOCK));

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(manasteel_block), new FluidStack(liquid_manasteel, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(terrasteel_block), new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(elementium_block), new FluidStack(liquid_elementium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

    if(FoundryConfig.recipe_equipment)
    {
      ItemStack manasteel_pickaxe = getItemStack("manasteelPick");
      ItemStack manasteel_axe = getItemStack("manasteelAxe");
      ItemStack manasteel_shovel = getItemStack("manasteelShovel");
      ItemStack manasteel_sword = getItemStack("manasteelSword");

      ItemStack manasteel_helmet = getItemStack("manasteelHelm");
      ItemStack manasteel_chestplate = getItemStack("manasteelChest");
      ItemStack manasteel_leggings = getItemStack("manasteelLegs");
      ItemStack manasteel_boots = getItemStack("manasteelBoots");

      ItemStack terrasteel_sword = getItemStack("terraSword");

      ItemStack elementium_pickaxe = getItemStack("elementiumPick");
      ItemStack elementium_axe = getItemStack("elementiumAxe");
      ItemStack elementium_shovel = getItemStack("elementiumShovel");
      ItemStack elementium_sword = getItemStack("elementiumSword");

      ItemStack elementium_helmet = getItemStack("elementiumHelm");
      ItemStack elementium_chestplate = getItemStack("elementiumChest");
      ItemStack elementium_leggings = getItemStack("elementiumLegs");
      ItemStack elementium_boots = getItemStack("elementiumBoots");

      ItemStack livingwood_twig = getItemStack("manaResource", 3);
      ItemStack dreamwood_twig = getItemStack("manaResource", 13);
      
      ItemStack extra_sticks1 = livingwood_twig.copy();
      ItemStack extra_sticks2 = livingwood_twig.copy();
      extra_sticks2.setCount(2);

      ItemStack extra_dreamsticks1 = dreamwood_twig.copy();
      ItemStack extra_dreamsticks2 = dreamwood_twig.copy();
      extra_dreamsticks2.setCount(2);


      FoundryMiscUtils.registerCasting(manasteel_pickaxe, liquid_manasteel, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(manasteel_axe, liquid_manasteel, 3, ItemMold.SubItem.AXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(manasteel_shovel, liquid_manasteel, 1, ItemMold.SubItem.SHOVEL, extra_sticks2);
      FoundryMiscUtils.registerCasting(manasteel_sword, liquid_manasteel, 2, ItemMold.SubItem.SWORD, extra_sticks1);
      FoundryMiscUtils.registerCasting(manasteel_chestplate, liquid_manasteel, 8, ItemMold.SubItem.CHESTPLATE, null);
      FoundryMiscUtils.registerCasting(manasteel_leggings, liquid_manasteel, 7, ItemMold.SubItem.LEGGINGS, null);
      FoundryMiscUtils.registerCasting(manasteel_helmet, liquid_manasteel, 5, ItemMold.SubItem.HELMET, null);
      FoundryMiscUtils.registerCasting(manasteel_boots, liquid_manasteel, 4, ItemMold.SubItem.BOOTS, null);

      FoundryMiscUtils.registerCasting(terrasteel_sword, new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), ItemMold.SubItem.SWORD, extra_sticks1);

      FoundryMiscUtils.registerCasting(elementium_pickaxe, liquid_elementium, 3, ItemMold.SubItem.PICKAXE, extra_dreamsticks2);
      FoundryMiscUtils.registerCasting(elementium_axe, liquid_elementium, 3, ItemMold.SubItem.AXE, extra_dreamsticks2);
      FoundryMiscUtils.registerCasting(elementium_shovel, liquid_elementium, 1, ItemMold.SubItem.SHOVEL, extra_dreamsticks2);
      FoundryMiscUtils.registerCasting(elementium_sword, liquid_elementium, 2, ItemMold.SubItem.SWORD, extra_dreamsticks1);
      FoundryMiscUtils.registerCasting(elementium_chestplate, liquid_elementium, 8, ItemMold.SubItem.CHESTPLATE, null);
      FoundryMiscUtils.registerCasting(elementium_leggings, liquid_elementium, 7, ItemMold.SubItem.LEGGINGS, null);
      FoundryMiscUtils.registerCasting(elementium_helmet, liquid_elementium, 5, ItemMold.SubItem.HELMET, null);
      FoundryMiscUtils.registerCasting(elementium_boots, liquid_elementium, 4, ItemMold.SubItem.BOOTS, null);
    }
  }

  @Override
  public String getName()
  {
    return "Botania";
  }

  @Override
  public void onInit()
  {
    
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
}