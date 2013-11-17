package exter.foundry.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerInductionCrucibleFurnace;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.gui.GuiAlloyMixer;
import exter.foundry.gui.GuiMetalCaster;
import exter.foundry.gui.GuiInductionCrucibleFurnace;
import exter.foundry.gui.GuiMetalInfuser;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMetalInfuser;

public class CommonFoundryProxy implements IGuiHandler
{
  static public final int GUI_ICF = 0;
  static public final int GUI_CASTER = 1;
  static public final int GUI_ALLOYMIXER = 2;
  static public final int GUI_INFUSER = 3;
  
  public void Init()
  {

  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    switch(ID)
    {
      case GUI_ICF:
        return new ContainerInductionCrucibleFurnace((TileEntityInductionCrucibleFurnace)world.getBlockTileEntity(x, y, z),player.inventory);
      case GUI_CASTER:
        return new ContainerMetalCaster((TileEntityMetalCaster)world.getBlockTileEntity(x, y, z),player.inventory);
      case GUI_ALLOYMIXER:
        return new ContainerAlloyMixer((TileEntityAlloyMixer)world.getBlockTileEntity(x, y, z),player.inventory);
      case GUI_INFUSER:
        return new ContainerMetalInfuser((TileEntityMetalInfuser)world.getBlockTileEntity(x, y, z),player.inventory);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    switch(ID)
    {
      case GUI_ICF:
      {
        TileEntityInductionCrucibleFurnace te = (TileEntityInductionCrucibleFurnace)world.getBlockTileEntity(x, y, z);
        return new GuiInductionCrucibleFurnace(te,player.inventory);
      }
      case GUI_CASTER:
      {
        TileEntityMetalCaster te = (TileEntityMetalCaster)world.getBlockTileEntity(x, y, z);
        return new GuiMetalCaster(te,player.inventory);
      }
      case GUI_ALLOYMIXER:
      {
        TileEntityAlloyMixer te = (TileEntityAlloyMixer)world.getBlockTileEntity(x, y, z);
        return new GuiAlloyMixer(te,player.inventory);
      }
      case GUI_INFUSER:
      {
        TileEntityMetalInfuser te = (TileEntityMetalInfuser)world.getBlockTileEntity(x, y, z);
        return new GuiMetalInfuser(te,player.inventory);
      }
    } 
    return null;
  }
}
