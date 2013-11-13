package exter.foundry.recipes;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Infuser substance stack
 */
public class InfuserSubstance
{
  
  /**
   * Type of substance. e.g: "Carbon".
   */
  public final String type;

  /**
   * Amount of the substance in the stack.
   */
  public int amount;
  
  /**
   * Create a substance stack.
   * @param substance Substance type. e.g: "Carbon".
   * @param amt Amount in the stack.
   */
  public InfuserSubstance(String substance,int amt)
  {
    type = substance;
    amount = amt;
  }

  /**
   * Create a substance stack from another stack.
   * @param is Stack to copy.
   */
  public InfuserSubstance(InfuserSubstance is)
  {    
    type = is.type;
    amount = is.amount;
  }

  /**
   * Write the substance stack to NBT
   * @param tag NBT tag to write.
   */
  public void WriteToNBT(NBTTagCompound tag)
  {
    tag.setString("type", type);
    tag.setInteger("amount", (int)amount);
  }
  
  /**
   * Read a substance stack from NBT data.
   * @param tag NBT tag to read from.
   * @return Substance stack read from the tag.
   */
  public static InfuserSubstance ReadFromNBT(NBTTagCompound tag)
  {
    if(!(tag.hasKey("type") && tag.hasKey("amount")))
    {
      return null;
    }
    String substance = tag.getString("type");
    int amt = tag.getInteger("amount");
    if(substance == null || amt == 0)
    {
      return null;
    }
    return new InfuserSubstance(substance,amt);
  }
  
  /**
   * Check if the substance type is equal to another
   * @param is Stack to compare.
   * @return true if the stack's substance type are equal, false otherwise.
   */
  public boolean IsSubstanceEqual(InfuserSubstance is)
  {
    if(is == null)
    {
      return amount == 0;
    }
    return type.equals(is.type);
  }
  
  /**
   * Check if this stacks contains a substance stack.
   * @param is Stack to compare.
   * @return true this stack contains the substance, false otherwise.
   */
  public boolean Contains(InfuserSubstance is)
  {
    return IsSubstanceEqual(is) && amount >= is.amount;
  }
}
