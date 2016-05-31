package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;

public class TileEntityCastingTableRod extends TileEntityCastingTableBase
{
  public TileEntityCastingTableRod()
  {
    super();    
  }
  
  @Override
  public int getDefaultCapacity()
  {
    return FoundryAPI.FLUID_AMOUNT_ROD;
  }

  @Override
  public TableType getTableType()
  {
    return ICastingTableRecipe.TableType.ROD;
  }
}
