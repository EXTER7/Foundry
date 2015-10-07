package exter.foundry.api.orestack;

/**
 * An item stack based on the Ore Dictionary
 */
public class OreStack
{
  public String name;
  
  public int amount;

  public OreStack(String ore_name)
  {
    this(ore_name,1);
  }

  public OreStack(OreStack stack)
  {
    this(stack.name,stack.amount);
  }

  public OreStack(String ore_name,int ore_amount)
  {
    name = ore_name;
    amount = ore_amount;
  }
  
  public boolean isStackEqual(OreStack stack)
  {
    return name.equals(stack.name);
  }
}
