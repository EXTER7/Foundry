package exter.foundry.integration.minetweaker.orestack;

import exter.foundry.api.orestack.OreStack;
import minetweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("mods.foundry.IOreStack")
public interface IOreStack extends IIngredient
{
  public OreStack getOreStack();
  
  @ZenOperator(OperatorType.MUL)
  @ZenMethod
  public IOreStack amount(int amount);
}
