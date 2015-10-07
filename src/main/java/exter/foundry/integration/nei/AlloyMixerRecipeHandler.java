package exter.foundry.integration.nei;


import java.awt.Rectangle;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import codechicken.lib.gui.GuiDraw;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.gui.GuiAlloyMixer;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;

public class AlloyMixerRecipeHandler extends FoundryRecipeHandler
{

  public class CachedAlloyRecipe extends CachedFoundryRecipe
  {
    public List<FluidTank> allTanks = Lists.newLinkedList();
    public FluidTank output;

    public CachedAlloyRecipe(IAlloyMixerRecipe recipe)
    {
      int maxSize = 0;
      int i;
      for(i = 0; i < recipe.getInputCount(); i++)
      {
        if(recipe.getInput(i) != null)
        {
          allTanks.add(new FluidTank(recipe.getInput(i), 2000, new Rectangle(21 + i * 21, 34, 16, 35)));
          maxSize = Math.max(maxSize, recipe.getInput(i).amount);
        }
      }
      output = new FluidTank(recipe.getOutput(), 2000, new Rectangle(128, 34, 16, 35));
      maxSize = Math.max(maxSize, recipe.getOutput().amount);
      allTanks.add(output);
      for(FluidTank tank : allTanks)
      {
        tank.capacity = maxSize;
      }
    }

    @Override
    public List<FluidTank> getTanks()
    {
      return allTanks;
    }
  }

  @Override
  public String getRecipeName()
  {
    return "Alloy Mixer";
  }

  @Override
  public String getGuiTexture()
  {
    return "foundry:textures/gui/alloymixer.png";
  }

  @Override
  public void drawExtras(int recipe)
  {
    CachedAlloyRecipe foundryRecipe = (CachedAlloyRecipe) arecipes.get(recipe);
    drawTanks(foundryRecipe.getTanks(), 0, TANK_OVERLAY);
  }

  public void loadAllRecipes()
  {
    for(IAlloyMixerRecipe recipe : AlloyMixerRecipeManager.instance.getRecipes())
    {
      arecipes.add(new CachedAlloyRecipe(recipe));
    }
  }

  @Override
  public void loadUsageRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.alloy"))
    {
      loadAllRecipes();
    }
    if(outputId.equals("liquid") || outputId.equals("item"))
    {
      FluidStack fluid = getFluidStackFor(results[0]);
      if(fluid == null)
      {
        return;
      }
      for(IAlloyMixerRecipe recipe : AlloyMixerRecipeManager.instance.getRecipes())
      {
        for(int idx = 0; idx < recipe.getInputCount(); idx++)
        {
          if(recipe.getInput(idx).isFluidEqual(fluid))
          {
            arecipes.add(new CachedAlloyRecipe(recipe));
          }
        }
      }
    }
  }

  @Override
  public void loadCraftingRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.alloy"))
    {
      loadAllRecipes();
    }
    if(outputId.equals("liquid") || outputId.equals("item"))
    {
      FluidStack fluid = getFluidStackFor(results[0]);
      if(fluid == null)
      {
        return;
      }
      for(IAlloyMixerRecipe recipe : AlloyMixerRecipeManager.instance.getRecipes())
      {
        if(recipe.getOutput().isFluidEqual(fluid))
        {
          arecipes.add(new CachedAlloyRecipe(recipe));
        }
      }
    }
  }

  @Override
  public ItemStack getMachineItem()
  {
    return new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER);
  }
  
  @Override
  public Rectangle getRecipeRect()
  {
    return new Rectangle(105, 42, 22, 15);
  }

  @Override
  public void loadTransferRects()
  {
    transferRects.add(new RecipeTransferRect(new Rectangle(105, 42, 22, 15), "foundry.alloy", new Object[0]));
  }

  @Override
  public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis()
  {
    return ImmutableList.<Class<? extends GuiContainer>> of(GuiAlloyMixer.class);
  }

  @Override
  public int recipiesPerPage()
  {
    return 1;
  }

  @Override
  public void drawBackground(int recipe)
  {
    GL11.glColor4f(1, 1, 1, 1);
    GuiDraw.changeTexture(getGuiTexture());
    GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 108);
  }
}
