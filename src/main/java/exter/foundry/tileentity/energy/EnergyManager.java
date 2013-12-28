package exter.foundry.tileentity.energy;

import net.minecraft.nbt.NBTTagCompound;

public class EnergyManager
{
  static public int RATIO_MJ = 100;
  static public int RATIO_RF = 10;
  static public int RATIO_EU = 40;
  
  private int stored;
  private int capacity;
  
  public EnergyManager(int max_capacity)
  {
    capacity = max_capacity;
    stored = 0;
  }
  
  private int ReceiveEnergy(int en,boolean do_receive, boolean allow_overflow)
  {
    if(!allow_overflow)
    {
      int needed = capacity - stored;
      if(en > needed)
      {
        en = needed;
      }
    }
    if(do_receive)
    {
      stored += en;
    }
    return en;
  }

  public float ReceiveMJ(float mj,boolean do_receive)
  {
    return (float)ReceiveEnergy((int)(mj * RATIO_MJ),do_receive,false) / RATIO_MJ;
  }
  
  public int ReceiveRF(int rf,boolean do_receive)
  {
    return ReceiveEnergy(rf * RATIO_RF,do_receive,false) / RATIO_RF;
  }
  
  public double ReceiveEU(double eu,boolean do_receive)
  {
    return (double)ReceiveEnergy((int)(eu * RATIO_EU),do_receive,true) / RATIO_EU;
  }
  
  public int UseEnergy(int amount,boolean do_use)
  {
    if(amount > stored)
    {
      amount = stored;
    }
    if(do_use)
    {
      stored -= amount;
    }
    return amount;
  }
  
  public int GetStoredEnergy()
  {
    if(stored > capacity)
    {
      return capacity;
    } else
    {
      return stored;
    }
  }
  
  public int GetCapacity()
  {
    if(stored > capacity)
    {
      return capacity;
    } else
    {
      return stored;
    }
  }

  public void WriteToNBT(NBTTagCompound tag)
  {
    tag.setInteger("energy", stored);
  }

  public void ReadFromNBT(NBTTagCompound tag)
  {
    if(tag.hasKey("energy"))
    {
      stored = tag.getInteger("energy");
    }
  }
}
