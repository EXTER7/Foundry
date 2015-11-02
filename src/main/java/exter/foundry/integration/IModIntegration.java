package exter.foundry.integration;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModIntegration
{
  public String getName();
  
  public void onPreInit(Configuration config);
  public void onInit();
  public void onPostInit();

  public void onAfterPostInit();

  @SideOnly(Side.CLIENT)
  public void onClientPreInit();
  @SideOnly(Side.CLIENT)
  public void onClientInit();
  @SideOnly(Side.CLIENT)
  public void onClientPostInit();
}
