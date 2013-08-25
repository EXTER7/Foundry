package exter.foundry.recipes;

import net.minecraft.nbt.NBTTagCompound;

public class InfuserSubstance
{
  static public final int MAX_AMOUNT = 1000;
  
  public final String type;
  public int amount;
  
  public InfuserSubstance(String substance,int amt)
  {
    type = substance;
    amount = amt;
  }

  public InfuserSubstance(InfuserSubstance is)
  {    
    type = is.type;
    amount = is.amount;
  }

  public void WriteToNBT(NBTTagCompound tag)
  {
    tag.setString("type", type);
    tag.setInteger("amount", (int)amount);
  }
  
  public static InfuserSubstance ReadFromNBT(NBTTagCompound tag)
  {
    String substance = tag.getString("type");
    int amt = tag.getInteger("amount");
    if(substance == null || amt == 0)
    {
      return null;
    }
    return new InfuserSubstance(substance,amt);
  }
  
  public boolean IsSubstanceEqual(InfuserSubstance is)
  {
    if(is == null)
    {
      return amount == 0;
    }
    return type.equals(is.type);
  }
  
  public boolean Contains(InfuserSubstance is)
  {
    return IsSubstanceEqual(is) && amount >= is.amount;
  }
}
