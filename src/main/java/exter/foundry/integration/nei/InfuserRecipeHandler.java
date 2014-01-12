package exter.foundry.integration.nei;

import static codechicken.core.gui.GuiDraw.changeTexture;
import static codechicken.core.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fluids.FluidStack;
import codechicken.core.gui.GuiDraw;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.gui.GuiMetalInfuser;
import exter.foundry.recipes.InfuserSubstance;
import exter.foundry.recipes.SubstanceGuiTexture;
import exter.foundry.recipes.manager.InfuserRecipeManager;

public class InfuserRecipeHandler extends FoundryRecipeHandlerSubstance
{
  
  private static final Rectangle SUBSTANCE_RECT = new Rectangle(71 - 5, 43 - 11, 8, 47);

  public class CachedInfuserRecipe extends CachedFoundryRecipeSubstance
  {
    public List<FluidTank> allTanks = Lists.newLinkedList();
    public FluidTank output;
    public InfuserSubstance substance;

    public CachedInfuserRecipe(IInfuserRecipe recipe)
    {
      substance = new InfuserSubstance(recipe.GetInputSubstanceType(),recipe.GetInputSubstanceAmount());
      allTanks.add(new FluidTank(new FluidStack(recipe.GetInputFluid(),2000), 2000, new Rectangle(80, 32, 16, 47)));
      output = new FluidTank(new FluidStack(recipe.GetOutput(),2000), 2000, new Rectangle(129, 32, 16, 47));
      // maxSize = Math.max(maxSize, recipe.GetOutput().amount);
      allTanks.add(output);
    }

    @Override
    public List<FluidTank> getTanks()
    {
      return allTanks;
    }

    @Override
    public InfuserSubstance GetSubstance()
    {
      return substance;
    }
  }

  @Override
  public String getRecipeName()
  {
    return "Metal Infuser";
  }

  @Override
  public String getGuiTexture()
  {
    return "foundry:textures/gui/infuser.png";
  }

  @Override
  public void drawExtras(int recipe)
  {
    CachedInfuserRecipe foundryRecipe = (CachedInfuserRecipe) arecipes.get(recipe);
    drawTanks(foundryRecipe.getTanks(), 0, TANK_OVERLAY);
    DrawSubstance(foundryRecipe.substance);
  }

  private void DrawSubstance(InfuserSubstance substance)
  {
    if(substance != null && substance.amount > 0)
    {
      SubstanceGuiTexture tex = InfuserRecipeManager.instance.GetSubstanceTexture(substance.type);
      GuiDraw.changeTexture(tex.GetLocation());
      Rectangle rect = GetSubstanceRect();
      GuiDraw.drawTexturedModalRect(rect.x, rect.y, tex.x, tex.y, SubstanceGuiTexture.TEXTURE_WIDTH, 47);
      GuiDraw.changeTexture(getGuiTexture());
    }    
  }
  
  
  public void loadAllRecipes()
  {
    for(IInfuserRecipe recipe : InfuserRecipeManager.instance.GetRecipes())
    {
      arecipes.add(new CachedInfuserRecipe(recipe));
    }
  }

  @Override
  public void loadUsageRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.infuser"))
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
      for(IInfuserRecipe recipe : InfuserRecipeManager.instance.GetRecipes())
      {
        if(recipe.GetInputFluid().isFluidEqual(fluid))
        {
          arecipes.add(new CachedInfuserRecipe(recipe));
        }
      }
    }
    if(outputId.equals("foundry.substance"))
    {
      if(!(results[0] instanceof InfuserSubstance))
      {
        return;
      }
      InfuserSubstance sub = (InfuserSubstance)results[0];
      for(IInfuserRecipe recipe : InfuserRecipeManager.instance.GetRecipes())
      {
        if(recipe.GetInputSubstanceType().equals(sub.type))
        {
          arecipes.add(new CachedInfuserRecipe(recipe));
        }
      }
    }
  }

  @Override
  public void loadCraftingRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.infuser"))
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
      for(IInfuserRecipe recipe : InfuserRecipeManager.instance.GetRecipes())
      {
        if(recipe.GetOutput().isFluidEqual(fluid))
        {
          arecipes.add(new CachedInfuserRecipe(recipe));
        }
      }
    }
  }

  @Override
  public void loadTransferRects()
  {
    transferRects.add(new RecipeTransferRect(new Rectangle(107 - 5, 59 - 11, 22, 15), "foundry.infuser", new Object[0]));
  }

  @Override
  public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis()
  {
    return ImmutableList.<Class<? extends GuiContainer>> of(GuiMetalInfuser.class);
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

  @Override
  protected Rectangle GetSubstanceRect()
  {
    return SUBSTANCE_RECT;
  }
  
}
