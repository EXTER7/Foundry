package exter.foundry.integration.minetweaker.substance;

import exter.foundry.api.substance.InfuserSubstance;

public class MTInfuserSubstance implements IInfuserSubstance
{
  private final InfuserSubstance sub;

  public MTInfuserSubstance(InfuserSubstance sub)
  {
    this.sub = sub; 
  }
  
  @Override
  public InfuserSubstance getSubstance()
  {
    return sub;
  }

  @Override
  public IInfuserSubstance amount(int amount)
  {
    return new MTInfuserSubstance(new InfuserSubstance(sub.type,sub.amount * amount));
  }
}
