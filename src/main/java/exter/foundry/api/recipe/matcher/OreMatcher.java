package exter.foundry.api.recipe.matcher;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.FoundryUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreMatcher implements IItemMatcher
{
  private String match;
  private int amount;
  
  public OreMatcher(String match)
  {
    this(match,1);
  }
  
  public OreMatcher(String match,int amount)
  {
    if(amount < 1)
    {
      throw new IllegalArgumentException("Amount must be > 1");
    }
    this.match = match;
    this.amount = amount;
  }

  @Override
  public boolean apply(ItemStack input)
  {
    return FoundryUtils.isItemInOreDictionary(match,input) && input.getCount() >= amount;
  }

  @Override
  public int getAmount()
  {
    return amount;
  }
  
  public String getOreName()
  {
    return match;
  }

  @Override
  public List<ItemStack> getItems()
  {
    List<ItemStack> list = new ArrayList<ItemStack>();
    for(ItemStack ore:OreDictionary.getOres(match))
    {
      ore = ore.copy();
      ore.setCount(amount);
      list.add(ore);
    }
    return list;
  }

  @Override
  public ItemStack getItem()
  {
    List<ItemStack> list = OreDictionary.getOres(match);
    if(list.isEmpty())
    {
      return ItemStack.EMPTY;
    }
    ItemStack res = list.get(0).copy();
    res.setCount(amount);
    return res;
  }
}
