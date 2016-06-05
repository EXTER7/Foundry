package exter.foundry.capability;

import exter.foundry.api.heatable.IHeatProvider;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHeatProvider
{
  static private class DummyHeatProvider implements IHeatProvider
  {

    @Override
    public int provideHeat(int max_heat)
    {
      return 0;
    }
  }
  
  static private class Storage implements IStorage<IHeatProvider>
  {
    @Override
    public NBTBase writeNBT(Capability<IHeatProvider> capability, IHeatProvider instance, EnumFacing side)
    {
      return null;
    }

    @Override
    public void readNBT(Capability<IHeatProvider> capability, IHeatProvider instance, EnumFacing side, NBTBase nbt)
    {
      
    }
  }
  
  static public void init()
  {
    CapabilityManager.INSTANCE.register(IHeatProvider.class,new Storage(),DummyHeatProvider.class);
  }
}
