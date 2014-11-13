package exter.foundry.integration.nei;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.gui.GuiInductionCrucibleFurnace;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.tileentity.TileEntityFoundryPowered;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;


public class InductionCrucibleFurnaceRecipeHandler extends FoundryRecipeHandler
{

  public static int GUI_SMELT_TIME = 40;

  public static final float FIXED_POWER = 16.66f;
  public static final float TEMP_POWER = 0.002f;

  public static ProgressBar PROGRESS = new ProgressBar(74, 12, 176, 78, 27, 15, 0, GUI_SMELT_TIME);
  public static ProgressBar HEAT = new ProgressBar(36, 46, 176, 53, 54, 12, TileEntityInductionCrucibleFurnace.HEAT_MIN, TileEntityInductionCrucibleFurnace.HEAT_MAX);
  public static Point TANK_OVERLAY = new Point(176, 0);

  public class CachedMeltingRecipe extends CachedFoundryRecipe
  {

    PositionedStack input;
    FluidTank tank;
    int meltingPoint;

    public CachedMeltingRecipe(IMeltingRecipe recipe)
    {
      input = new PositionedStack(asItemStackOrList(recipe.GetInput()), 50, 12, true);
      tank = new FluidTank(recipe.GetOutput(), 6000, new Rectangle(102, 11, 16, 47));
      meltingPoint = recipe.GetMeltingPoint();
    }

    @Override
    public PositionedStack getIngredient()
    {
      randomRenderPermutation(input, cycleticks / 20);
      return input;
    }

    @Override
    public FluidTank getTank()
    {
      return tank;
    }
  }

  @Override
  public String getRecipeName()
  {
    return "Ind. Crucible Furnace";
  }

  @Override
  public String getGuiTexture()
  {
    return "foundry:textures/gui/metalsmelter.png";
  }

  @Override
  public void drawExtras(int recipe)
  {
    CachedMeltingRecipe meltingRecipe = (CachedMeltingRecipe) arecipes.get(recipe);
    int currentProgress = meltingRecipe.getAgeTicks() % GUI_SMELT_TIME;
    if(currentProgress > 0)
    {
      drawProgressBar(PROGRESS, currentProgress);
    }
    drawProgressBar(HEAT, meltingRecipe.meltingPoint * 100);
    drawTanks(meltingRecipe.getTanks(), meltingRecipe.getAgeTicks() / GUI_SMELT_TIME, TANK_OVERLAY);
  }

  @Override
  public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
  {
    CachedMeltingRecipe meltingRecipe = (CachedMeltingRecipe) arecipes.get(recipe);
    if(isMouseOver(HEAT.asRectangle(), gui, recipe))
    {
      currenttip.add(EnumChatFormatting.GRAY + "Melting point: " + EnumChatFormatting.YELLOW + meltingRecipe.meltingPoint + " K");
      float internalPower = TileEntityInductionCrucibleFurnace.GetEnergyPerTickNeeded(meltingRecipe.meltingPoint * 100);
      currenttip.add(EnumChatFormatting.GRAY + "Mininum power: " + EnumChatFormatting.AQUA + String.format("%.2f RF/t", internalPower / TileEntityFoundryPowered.RATIO_RF));
      currenttip.add(EnumChatFormatting.GRAY + "Mininum power: " + EnumChatFormatting.AQUA + String.format("%.2f EU/t", internalPower / TileEntityFoundryPowered.RATIO_EU));
    }
    return super.handleTooltip(gui, currenttip, recipe);
  }

  public void loadAllRecipes()
  {
    for(IMeltingRecipe recipe : MeltingRecipeManager.instance.GetRecipes())
    {
      addRecipe(recipe);
    }
  }

  @Override
  public void loadUsageRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.melting"))
    {
      loadAllRecipes();
    }
    if(outputId.equals("item") && results[0] instanceof ItemStack)
    {
      for(IMeltingRecipe recipe : MeltingRecipeManager.instance.GetRecipes())
      {
        if(recipe.MatchesRecipe((ItemStack) results[0]))
        {
          addRecipe(recipe);
        }
      }
    }
  }

  @Override
  public void loadCraftingRecipes(String outputId, Object... results)
  {
    if(outputId.equals("foundry.melting"))
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
      for(IMeltingRecipe recipe : MeltingRecipeManager.instance.GetRecipes())
      {
        if(recipe.GetOutput().isFluidEqual(fluid))
        {
          addRecipe(recipe);
        }
      }
    }
  }

  public void addRecipe(IMeltingRecipe recipe)
  {
    if(!isEmptyOre(recipe.GetInput()))
    {
      arecipes.add(new CachedMeltingRecipe(recipe));
    }
  }

  
  @Override
  public ItemStack getMachineItem()
  {
    return new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ICF);
  }
  
  @Override
  public Rectangle getRecipeRect()
  {
    return new Rectangle(74, 12, 22, 15);
  }
  
  @Override
  public void loadTransferRects()
  {
    transferRects.add(new RecipeTransferRect(new Rectangle(74, 12, 22, 15), "foundry.melting", new Object[0]));
  }

  @Override
  public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis()
  {
    return ImmutableList.<Class<? extends GuiContainer>> of(GuiInductionCrucibleFurnace.class);
  }

}
