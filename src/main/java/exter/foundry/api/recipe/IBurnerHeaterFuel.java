package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;

public interface IBurnerHeaterFuel
{
  public int getBurnTime();

  public int getHeat();
  
  public IItemMatcher getFuel();
}
