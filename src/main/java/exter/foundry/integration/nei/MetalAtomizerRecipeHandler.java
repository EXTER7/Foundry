package exter.foundry.integration.nei;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.gui.GuiMetalAtomizer;
import exter.foundry.recipes.manager.AtomizerRecipeManager;

public class MetalAtomizerRecipeHandler extends FoundryRecipeHandler
{

  public static int GUI_SMELT_TIME = 40;

  public static ProgressBar PROGRESS = new ProgressBar(55, 26, 176, 53, 27, 15, 0, GUI_SMELT_TIME);

  public class CachedAtomizerRecipe extends CachedFoundryRecipe
  {

    FluidTank tank;
    FluidTank tank_water;
    PositionedStack output;

    public CachedAtomizerRecipe(IAtomizerRecipe recipe)
    {
      tank = new FluidTank(recipe.GetInputFluid(), 6000, new Rectangle(34, 10, 16, 47));
      tank_water = new FluidTank(new FluidStack(FluidRegistry.WATER,50), 6000, new Rectangle(118, 10, 16, 47));
      output = new PositionedStack(asItemStackOrList(recipe.GetOutput()), 81, 26, true);
      output.setPermutationToRender(0);
    }

    @Override
    public FluidTank getTank()
    {
      return tank;
    }

    @Override
    public List<FluidTank> getTanks()
    {
      List<FluidTank> result = Lists.newArrayList();
      result.add(tank);
      result.add(tank_water);
      return result;
    }

    @Override
    public PositionedStack getResult()
    {
      return output;
    }

    @Override
    public List<PositionedStack> getIngredients()
    {
      return ImmutableList.<PositionedStack>of();
    }
  }

  @Override
  public String getRecipeName()
  {
    return "Metal Atomizer";
  }

  @Override
  public String getGuiTexture()
  {
    return "foundry:textures/gui/atomizer.png";
  }

  @Override
  public void drawExtras(int recipe)
  {
    CachedAtomizerRecipe atomizer_recipe = (CachedAtomizerRecipe) arecipes.get(recipe);
    int currentProgress = atomizer_recipe.getAgeTicks() % GUI_SMELT_TIME;
    if(currentProgress > 0)
    {
      drawProgressBar(new ProgressBar(55, 26, 176, 53, 27, 15, 0, GUI_SMELT_TIME), currentProgress);
    }
    drawTanks(atomizer_recipe.getTanks(), GUI_SMELT_TIME - atomizer_recipe.getAgeTicks() / GUI_SMELT_TIME, TANK_OVERLAY);
  }

  @Override
  public boolean mouseClicked(GuiRecipe gui, int button, int recipe)
  {
    CachedAtomizerRecipe castingRecipe = (CachedAtomizerRecipe) arecipes.get(recipe);
    if(isMouseOver(castingRecipe.getTank().position, gui, recipe))
    {
      if(button == 0)
      {
        return GuiCraftingRecipe.openRecipeGui("liquid", castingRecipe.getTank().fluid);
      }
      if(button == 1)
      {
        return GuiUsageRecipe.openRecipeGui("liquid", castingRecipe.getTank().fluid);
      }
    }
    return super.mouseClicked(gui, button, recipe);
  }

  public void loadAllRecipes()
  {
    for(IAtomizerRecipe recipe : AtomizerRecipeManager.instance.GetRecipes())
    {
      addRecipe(recipe);
    }
  }

  @Override
  public void loadUsageRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.atomizer"))
    {
      loadAllRecipes();
    }
    if(outputId.equals("item"))
    {
      for(IAtomizerRecipe recipe : AtomizerRecipeManager.instance.GetRecipes())
      {
        FluidStack fluid = getFluidStackFor((ItemStack) results[0]);
        if(fluid != null && fluid.isFluidEqual(recipe.GetInputFluid()))
        {
          addRecipe(recipe);
        }
      }
    }
    if(outputId.equals("liquid"))
    {
      for(IAtomizerRecipe recipe : AtomizerRecipeManager.instance.GetRecipes())
      {
        if(recipe.GetInputFluid().isFluidEqual((FluidStack) results[0]))
        {
          addRecipe(recipe);
        }
      }
    }
  }

  @Override
  public void loadCraftingRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.atomizer"))
    {
      loadAllRecipes();
    }
    if(outputId.equals("item"))
    {
      for(IAtomizerRecipe recipe : AtomizerRecipeManager.instance.GetRecipes())
      {
        Object output = recipe.GetOutput();
        if(output != null && FoundryUtils.IsItemMatch((ItemStack) results[0], output))
        {
          arecipes.add(new CachedAtomizerRecipe(recipe));
        }
      }
    }
  }

  public void addRecipe(IAtomizerRecipe recipe)
  {
    if(recipe.GetOutput() != null && recipe.GetInputFluid() != null)
    {
      if(recipe.GetOutput() instanceof String)
      {
        if(OreDictionary.getOres((String)recipe.GetOutput()).size() == 0)
        {
          return;
        }
      }
      arecipes.add(new CachedAtomizerRecipe(recipe));
    }
  }


  @Override
  public ItemStack getMachineItem()
  {
    return new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ATOMIZER);
  }
  
  @Override
  public Rectangle getRecipeRect()
  {
    return new Rectangle(50, 27, 25, 15);
  }
  
  @Override
  public void loadTransferRects()
  {
    transferRects.add(new RecipeTransferRect(new Rectangle(50, 27, 25, 15), "foundry.atomizer", new Object[0]));
  }

  @Override
  public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis()
  {
    return ImmutableList.<Class<? extends GuiContainer>> of(GuiMetalAtomizer.class);
  }
}
