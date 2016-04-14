package exter.foundry.integration.minetweaker.orestack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exter.foundry.api.recipe.matcher.OreMatcher;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemCondition;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IItemTransformer;
import minetweaker.api.item.IngredientOr;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import net.minecraft.item.ItemStack;

public class MTOreStack implements IIngredient
{
  private final OreMatcher stack;

  public MTOreStack(OreMatcher stack)
  {
    this.stack = stack; 
  }

  @Override
  public IIngredient amount(int amount)
  {
    return new MTOreStack(new OreMatcher(stack.getOreName(),stack.getAmount() * amount));
  }

  @Override
  public IItemStack applyTransform(IItemStack arg0, IPlayer arg1)
  {
    return null;
  }


  @Override
  public boolean contains(IIngredient ingredient) {
    List<IItemStack> items = ingredient.getItems();
    for (IItemStack item : items) {
      if (!matches(item))
        return false;
    }

    return true;
  }
  @Override
  public int getAmount()
  {
    return stack.getAmount();
  }

  @Override
  public Object getInternal()
  {
    return stack;
  }

  @Override
  public List<IItemStack> getItems()
  {
    List<IItemStack> result = new ArrayList<IItemStack>();
    for (ItemStack item : stack.getItems())
    {
      result.add(MineTweakerMC.getIItemStack(item));
    }
    return result;
  }

  @Override
  public List<ILiquidStack> getLiquids()
  {
    return Collections.emptyList();
  }

  @Override
  public String getMark()
  {
    return null;
  }

  @Override
  public boolean hasTransformers()
  {
    return false;
  }

  @Override
  public IIngredient marked(String arg0)
  {
    return this;
  }

  @Override
  public boolean matches(IItemStack iitem)
  {
    ItemStack item = MineTweakerMC.getItemStack(iitem);
    return stack.apply(item);
  }

  @Override
  public boolean matches(ILiquidStack arg0)
  {
    return false;
  }

  @Override
  public IIngredient only(IItemCondition arg0)
  {
    return this;
  }

  @Override
  public IIngredient or(IIngredient ingredient)
  {
    return new IngredientOr(this, ingredient);
  }

  @Override
  public IIngredient transform(IItemTransformer arg0)
  {
    return this;
  }
}
