package exter.foundry.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerMetalSmelter;
import exter.foundry.gui.GuiAlloyMixer;
import exter.foundry.gui.GuiMetalCaster;
import exter.foundry.gui.GuiMetalSmelter;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalSmelter;

public class CommonFoundryProxy implements IGuiHandler
{
  public void Init()
  {

  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    switch(ID)
    {
      case 0:
        return new ContainerMetalSmelter((TileEntityMetalSmelter)world.getBlockTileEntity(x, y, z),player.inventory);
      case 1:
        return new ContainerMetalCaster((TileEntityMetalCaster)world.getBlockTileEntity(x, y, z),player.inventory);
      case 2:
        return new ContainerAlloyMixer((TileEntityAlloyMixer)world.getBlockTileEntity(x, y, z),player.inventory);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    switch(ID)
    {
      case 0:
      {
        TileEntityMetalSmelter te = (TileEntityMetalSmelter)world.getBlockTileEntity(x, y, z);
        return new GuiMetalSmelter(te,player.inventory);
      }
      case 1:
      {
        TileEntityMetalCaster te = (TileEntityMetalCaster)world.getBlockTileEntity(x, y, z);
        return new GuiMetalCaster(te,player.inventory);
      }
      case 2:
      {
        TileEntityAlloyMixer te = (TileEntityAlloyMixer)world.getBlockTileEntity(x, y, z);
        return new GuiAlloyMixer(te,player.inventory);
      }
    } 
    return null;
  }

}
