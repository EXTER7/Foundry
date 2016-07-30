package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;

public class TileEntityCastingTableBlock extends TileEntityCastingTableBase
{
  public TileEntityCastingTableBlock()
  {
    super();    
  }
 

  @Override
  public int getDefaultCapacity()
  {
    return FoundryAPI.FLUID_AMOUNT_BLOCK;
  }

  @Override
  public TableType getTableType()
  {
    return ICastingTableRecipe.TableType.BLOCK;
  }
}
