package exter.foundry.recipes;

import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.api.recipe.matcher.IItemMatcher;

public class BurnerHeaterFuel implements IBurnerHeaterFuel
{
  public final int burn_time;
  public final int heat;

  public final IItemMatcher fuel;


  @Override
  public IItemMatcher getFuel()
  {
    return fuel;
  }
  
  public BurnerHeaterFuel(IItemMatcher fuel,int burn_time,int heat)
  {
    if(fuel == null)
    {
      throw new IllegalArgumentException("Fuel output cannot be null");
    }
    if(burn_time < 1)
    {
      throw new IllegalArgumentException("Fuel burn time must be > 0.");
    }
    if(heat < 1)
    {
      throw new IllegalArgumentException("Fuel heat must be > 0.");
    }
    this.fuel = fuel;
    this.burn_time = burn_time;
    this.heat = heat;
  }

  @Override
  public int getBurnTime()
  {
    return burn_time;
  }

  @Override
  public int getHeat()
  {
    return heat;
  }
}
