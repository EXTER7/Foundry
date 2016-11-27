package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.recipes.CastingTableRecipe;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.CastingTable")
public class MTCastingTableHandler
{
  public static class CastingTableAction extends AddRemoveAction
  {
    
    ICastingTableRecipe recipe;
    
    public CastingTableAction(ICastingTableRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    protected void add()
    {
      CastingTableRecipeManager.instance.recipes.get(recipe.getTableType()).put(recipe.getInput().getFluid().getName(), recipe);

    }

    @Override
    protected void remove()
    {
      CastingTableRecipeManager.instance.recipes.get(recipe.getTableType()).remove(recipe.getInput().getFluid().getName());
    }

    @Override
    public String getRecipeType()
    {
      return "casting table";
    }

    @Override
    public String getDescription()
    {
      return String.format("( %s, %s ) -> %s",
          MTHelper.getFluidDescription(recipe.getInput()),
          recipe.getTableType().toString(),
          MTHelper.getItemDescription(recipe.getOutput()));
    }
  }

  static private void addRecipe(IItemStack output,ILiquidStack input,ICastingTableRecipe.TableType table)
  {
    ItemStackMatcher out = new ItemStackMatcher(MineTweakerMC.getItemStack(output));
    FluidStack in = MineTweakerMC.getLiquidStack(input);
    CastingTableRecipe recipe;
    try
    {
      recipe = new CastingTableRecipe( out, in, table);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid casting recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new CastingTableAction(recipe).action_add));
  }

  @ZenMethod
  static public void addIngotRecipe(IItemStack output,ILiquidStack input)
  {
    addRecipe(output,input,ICastingTableRecipe.TableType.INGOT);
  }

  @ZenMethod
  static public void addPlateRecipe(IItemStack output,ILiquidStack input)
  {
    addRecipe(output,input,ICastingTableRecipe.TableType.PLATE);
  }

  @ZenMethod
  static public void addRodRecipe(IItemStack output,ILiquidStack input)
  {
    addRecipe(output,input,ICastingTableRecipe.TableType.ROD);
  }
  
  @ZenMethod
  static public void addBlockRecipe(IItemStack output,ILiquidStack input)
  {
    addRecipe(output,input,ICastingTableRecipe.TableType.BLOCK);
  }

  static public void removeRecipe(ILiquidStack input, ICastingTableRecipe.TableType table)
  {
    ICastingTableRecipe recipe = CastingTableRecipeManager.instance.findRecipe(
        MineTweakerMC.getLiquidStack(input),
        table);
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Casting table recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new CastingTableAction(recipe)).action_remove);
  }

  @ZenMethod
  static public void removeIngotRecipe(ILiquidStack input)
  {
    removeRecipe(input,ICastingTableRecipe.TableType.INGOT);
  }
  
  @ZenMethod
  static public void removePlateRecipe(ILiquidStack input)
  {
    removeRecipe(input,ICastingTableRecipe.TableType.PLATE);
  }
  
  @ZenMethod
  static public void removeRodRecipe(ILiquidStack input)
  {
    removeRecipe(input,ICastingTableRecipe.TableType.ROD);
  }

  @ZenMethod
  static public void removeBlockRecipe(ILiquidStack input)
  {
    removeRecipe(input,ICastingTableRecipe.TableType.BLOCK);
  }
}
