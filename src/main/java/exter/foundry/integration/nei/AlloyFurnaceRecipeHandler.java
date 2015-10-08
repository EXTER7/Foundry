package exter.foundry.integration.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.gui.GuiAlloyFurnace;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;

public class AlloyFurnaceRecipeHandler extends FoundryRecipeHandler
{
  private class CachedAlloyFurnaceRecipe extends CachedFoundryRecipe
  {
    public CachedAlloyFurnaceRecipe(IAlloyFurnaceRecipe recipe)
    {
      this.in_a = new PositionedStack(getItems(recipe.getInputA()), 38 - 5, 17 - 11);
      this.in_b = new PositionedStack(getItems(recipe.getInputB()), 56 - 5, 17 - 11);
      this.result = new PositionedStack(recipe.getOutput(), 116 - 5, 35 - 11);
    }

    @SuppressWarnings("unchecked")
    private List<ItemStack> getItems(Object item)
    {
      if(item == null)
      {
        return Lists.newArrayList();
      } else if(item instanceof OreStack)
      {
        OreStack stack = (OreStack) item;
        List<ItemStack> list = (List<ItemStack>) asItemStackOrList(stack.name);
        if(list != null && !list.isEmpty())
        {
          for(ItemStack s : list)
          {
            s.stackSize = stack.amount;
          }
        }
        return list;
      } else if(item instanceof ItemStack)
      {
        return Lists.newArrayList((ItemStack) item);
      }
      return Lists.newArrayList();
    }

    
    public List<PositionedStack> getIngredients()
    {
      return getCycledIngredients(cycleticks / 48, Arrays.asList(in_a, in_b));
    }

    public PositionedStack getResult()
    {
      return result;
    }

    public PositionedStack getOtherStack()
    {
      return afuels.get((cycleticks / 48) % afuels.size()).item;
    }

    PositionedStack in_a;
    PositionedStack in_b;
    PositionedStack result;
  }

  public static class FurnaceFuel
  {
    public FurnaceFuel(ItemStack stack, int burn)
    {
      item = new PositionedStack(stack, 51 - 8, 42, false);
      burn_time = burn;
    }

    public PositionedStack item;
    public int burn_time;
  }

  public static ArrayList<FurnaceFuel> afuels;
  public static Set<Block> efuels;


  @Override
  public ItemStack getMachineItem()
  {
    return new ItemStack(FoundryBlocks.block_alloy_furnace);
  }
  
  @Override
  public Rectangle getRecipeRect()
  {
    return new Rectangle(80 - 5, 35 - 5, 24, 16);
  }
  
  @Override
  public void loadTransferRects()
  {
    transferRects.add(new RecipeTransferRect(new Rectangle(80 - 5, 35 - 5, 24, 16), "foundry.alloyfurnace"));
    transferRects.add(new RecipeTransferRect(new Rectangle(48 - 5, 36 - 5, 14, 14), "fuel"));
  }

  @Override
  public Class<? extends GuiContainer> getGuiClass()
  {
    return GuiAlloyFurnace.class;
  }

  @Override
  public TemplateRecipeHandler newInstance()
  {
    if(afuels == null)
    {
      findFuels();
    }
    return super.newInstance();
  }

  public void addRecipe(IAlloyFurnaceRecipe recipe)
  {
    Object in_a = recipe.getInputA();
    Object in_b = recipe.getInputB();
    if(recipe.getOutput() != null && in_a != null && in_b != null 
        && (!(in_a instanceof OreStack) || OreDictionary.getOres(((OreStack)in_a).name).size() > 0)
        && (!(in_b instanceof OreStack) || OreDictionary.getOres(((OreStack)in_b).name).size() > 0))
    {
      arecipes.add(new CachedAlloyFurnaceRecipe(recipe));
    }
  }

  public void loadAllRecipes()
  {
    for(IAlloyFurnaceRecipe recipe : AlloyFurnaceRecipeManager.instance.getRecipes())
    {
      addRecipe(recipe);
    }
  }

  @Override
  public void loadCraftingRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.alloyfurnace"))
    {
      loadAllRecipes();
    }
    if(outputId.equals("item"))
    {
      for(IAlloyFurnaceRecipe recipe : AlloyFurnaceRecipeManager.instance.getRecipes())
      {
        Object output = recipe.getOutput();
        if(output != null && FoundryUtils.isItemMatch((ItemStack) results[0], output))
        {
          arecipes.add(new CachedAlloyFurnaceRecipe(recipe));
        }
      }
    }
  }

  @Override
  public void loadUsageRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.alloyfurnace"))
    {
      loadAllRecipes();
    }
    if(outputId.equals("item") && results[0] instanceof ItemStack)
    {
      for(IAlloyFurnaceRecipe recipe : AlloyFurnaceRecipeManager.instance.getRecipes())
      {
        if(FoundryUtils.isItemMatch((ItemStack) results[0], recipe.getInputA()) || FoundryUtils.isItemMatch((ItemStack) results[0], recipe.getInputB()))
        {
          addRecipe(recipe);
        }
      }
    }
  }

  @Override
  public String getGuiTexture()
  {
    return "foundry:textures/gui/alloyfurnace.png";
  }

  @Override
  public void drawExtras(int recipe)
  {
    drawProgressBar(80 - 5, 35 - 11, 176, 14, 24, 16, 48, 0);
    drawProgressBar(48 - 5, 36 - 11, 176, 0, 14, 14, 48, 7);
  }

  private static Set<Item> excludedFuels()
  {
    Set<Item> efuels = new HashSet<Item>();
    efuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
    efuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
    efuels.add(Item.getItemFromBlock(Blocks.standing_sign));
    efuels.add(Item.getItemFromBlock(Blocks.wall_sign));
    efuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
    return efuels;
  }

  private static void findFuels()
  {
    afuels = new ArrayList<FurnaceFuel>();
    Set<Item> efuels = excludedFuels();
    for(ItemStack item : ItemList.items)
      if(!efuels.contains(item.getItem()))
      {
        int burnTime = TileEntityFurnace.getItemBurnTime(item);
        if(burnTime > 0)
          afuels.add(new FurnaceFuel(item.copy(), burnTime));
      }
  }

  @Override
  public String getOverlayIdentifier()
  {
    return "smelting";
  }

  @Override
  public String getRecipeName()
  {
    return "Alloy Furnace";
  }
}
