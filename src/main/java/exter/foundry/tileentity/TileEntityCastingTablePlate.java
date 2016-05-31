package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;

public class TileEntityCastingTablePlate extends TileEntityCastingTableBase
{
  public TileEntityCastingTablePlate()
  {
    super();    
  }

  @Override
  public int getDefaultCapacity()
  {
    return FoundryAPI.FLUID_AMOUNT_PLATE;
  }
  
  @Override
  public TableType getTableType()
  {
    return ICastingTableRecipe.TableType.PLATE;
  }

}
