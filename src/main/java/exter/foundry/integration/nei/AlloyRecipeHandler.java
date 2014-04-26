package exter.foundry.integration.nei;

import static codechicken.core.gui.GuiDraw.changeTexture;
import static codechicken.core.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.gui.GuiAlloyMixer;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;

public class AlloyRecipeHandler extends FoundryRecipeHandler
{

  public class CachedAlloyRecipe extends CachedFoundryRecipe
  {
    public List<FluidTank> allTanks = Lists.newLinkedList();
    public FluidTank output;

    public CachedAlloyRecipe(IAlloyMixerRecipe recipe)
    {
      int maxSize = 0;
      int i;
      for(i = 0; i < recipe.GetInputCount(); i++)
      {
        if(recipe.GetInput(i) != null)
        {
          allTanks.add(new FluidTank(recipe.GetInput(i), 2000, new Rectangle(21 + i * 21, 34, 16, 35)));
          maxSize = Math.max(maxSize, recipe.GetInput(i).amount);
        }
      }
      output = new FluidTank(recipe.GetOutput(), 2000, new Rectangle(128, 34, 16, 35));
      maxSize = Math.max(maxSize, recipe.GetOutput().amount);
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
    for(IAlloyMixerRecipe recipe : AlloyMixerRecipeManager.instance.GetRecipes())
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
      for(IAlloyMixerRecipe recipe : AlloyMixerRecipeManager.instance.GetRecipes())
      {
        for(int idx = 0; idx < recipe.GetInputCount(); idx++)
        {
          if(recipe.GetInput(idx).isFluidEqual(fluid))
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
      for(IAlloyMixerRecipe recipe : AlloyMixerRecipeManager.instance.GetRecipes())
      {
        if(recipe.GetOutput().isFluidEqual(fluid))
        {
          arecipes.add(new CachedAlloyRecipe(recipe));
        }
      }
    }
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
    changeTexture(getGuiTexture());
    drawTexturedModalRect(0, 0, 5, 11, 166, 108);
  }
}
