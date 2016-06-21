package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.api.recipe.manager.IBurnerHeaterFuelManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.BurnerHeaterFuel;
import exter.foundry.tileentity.TileEntityFoundryHeatable;

public class BurnerHeaterFuelManager implements IBurnerHeaterFuelManager
{
  public List<IBurnerHeaterFuel> fuels;

  public static final BurnerHeaterFuelManager instance = new BurnerHeaterFuelManager();
  
  private BurnerHeaterFuelManager()
  {
    fuels = new ArrayList<IBurnerHeaterFuel>();
  }
  
  @Override
  public void addFuel(IItemMatcher fuel,int burn_time,int heat)
  {
    fuels.add(new BurnerHeaterFuel(fuel,burn_time,heat));
  }

  @Override
  public IBurnerHeaterFuel getFuel(ItemStack item)
  {
    for(IBurnerHeaterFuel f:fuels)
    {
      if(f.getFuel().apply(item))
      {
        return f;
      }
    }
    return null;
  }

  @Override
  public List<IBurnerHeaterFuel> getFuels()
  {
    return Collections.unmodifiableList(fuels);
  }

  @Override
  public void removeFuel(IBurnerHeaterFuel fuel)
  {
    fuels.remove(fuel);    
  }

  @Override
  public int getHeatNeeded(int temperature,int temp_loss_rate)
  {
    return TileEntityFoundryHeatable.getMaxHeatRecieve(temperature, temp_loss_rate);
  }
}
