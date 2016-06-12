package exter.foundry.api.heatable;

/**
 * Interface for a Melting Crucible heater.
 * Do not implement this in the block's class or TileEntity.
 * Use {@link exter.foundry.api.FoundryAPI.capability_heatprovider FoundryAPI.capability_heatprovider} by
 * overriding the TileEntity's hasCapability and getCapability methods.
 * <br/>
 * See <a href="https://gist.github.com/williewillus/c8dc2a1e7963b57ef436c699f25a710d#if-the-item--entity--tileentity-is-from-your-mod"> this page</a>
 * for more detailed info on how implement capabilities.
 */
public interface IHeatProvider
{
  /**
   * Called by Melting Crucibles to request heat from the heater block.
   * @param max_heat Heat requested in 1/100 of degK.
   * @return Heat provided in 1/100 of degK.
   */
  public int provideHeat(int max_heat);
}
