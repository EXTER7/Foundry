package exter.foundry.integration.minetweaker.substance;

import exter.foundry.api.substance.InfuserSubstance;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;

@ZenClass("mods.foundry.IInfuserSubstance")
public interface IInfuserSubstance
{
  public InfuserSubstance getSubstance();
  
  @ZenOperator(OperatorType.MUL)
  @ZenMethod
  public IInfuserSubstance amount(int amount);
}
