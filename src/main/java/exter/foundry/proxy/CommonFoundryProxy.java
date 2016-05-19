package exter.foundry.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import exter.foundry.container.ContainerAlloyFurnace;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.container.ContainerBurnerHeater;
import exter.foundry.container.ContainerMaterialRouter;
import exter.foundry.container.ContainerMetalAtomizer;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.container.ContainerMoldStation;
import exter.foundry.container.ContainerRefractoryHopper;
import exter.foundry.container.ContainerRefractoryTank;
import exter.foundry.container.ContainerRevolver;
import exter.foundry.container.ContainerShotgun;
import exter.foundry.gui.GuiBurnerHeater;
import exter.foundry.gui.GuiAlloyFurnace;
import exter.foundry.gui.GuiAlloyMixer;
import exter.foundry.gui.GuiMaterialRouter;
import exter.foundry.gui.GuiMetalAtomizer;
import exter.foundry.gui.GuiMetalCaster;
import exter.foundry.gui.GuiMeltingCrucible;
import exter.foundry.gui.GuiMetalInfuser;
import exter.foundry.gui.GuiMoldStation;
import exter.foundry.gui.GuiRefractoryHopper;
import exter.foundry.gui.GuiRefractoryTank;
import exter.foundry.gui.GuiRevolver;
import exter.foundry.gui.GuiShotgun;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMeltingCrucible;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityMoldStation;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractoryTank;

public class CommonFoundryProxy implements IGuiHandler
{
  static public final int GUI_CRUCIBLE = 0;
  static public final int GUI_CASTER = 1;
  static public final int GUI_ALLOYMIXER = 2;
  static public final int GUI_INFUSER = 3;
  static public final int GUI_ALLOYFURNACE = 4;
  static public final int GUI_MATERIALROUTER = 5;
  static public final int GUI_REFRACTORYHOPPER = 6;
  static public final int GUI_REVOLVER = 7;
  static public final int GUI_SHOTGUN = 8;
  static public final int GUI_ATOMIZER = 9;
  static public final int GUI_MOLDSTATION = 10;
  static public final int GUI_BURNERHEATER = 11;
  static public final int GUI_REFRACTORYTANK = 12;
  
  public void preInit()
  {
    
  }

  public void init()
  {

  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    BlockPos pos = new BlockPos(x,y,z);
    switch(ID)
    {
      case GUI_CRUCIBLE:
        return new ContainerMeltingCrucible((TileEntityMeltingCrucible)world.getTileEntity(pos),player);
      case GUI_CASTER:
        return new ContainerMetalCaster((TileEntityMetalCaster)world.getTileEntity(pos),player);
      case GUI_ALLOYMIXER:
        return new ContainerAlloyMixer((TileEntityAlloyMixer)world.getTileEntity(pos),player);
      case GUI_INFUSER:
        return new ContainerMetalInfuser((TileEntityMetalInfuser)world.getTileEntity(pos),player);
      case GUI_ALLOYFURNACE:
        return new ContainerAlloyFurnace((TileEntityAlloyFurnace)world.getTileEntity(pos),player);
      case GUI_MATERIALROUTER:
        return new ContainerMaterialRouter((TileEntityMaterialRouter)world.getTileEntity(pos),player);
      case GUI_REFRACTORYHOPPER:
        return new ContainerRefractoryHopper((TileEntityRefractoryHopper)world.getTileEntity(pos),player);
      case GUI_REVOLVER:
        return new ContainerRevolver(player.getHeldItem(EnumHand.MAIN_HAND),player.inventory);
      case GUI_SHOTGUN:
        return new ContainerShotgun(player.getHeldItem(EnumHand.MAIN_HAND),player.inventory);
      case GUI_ATOMIZER:
        return new ContainerMetalAtomizer((TileEntityMetalAtomizer)world.getTileEntity(pos),player);
      case GUI_MOLDSTATION:
        return new ContainerMoldStation((TileEntityMoldStation)world.getTileEntity(pos),player);
      case GUI_BURNERHEATER:
        return new ContainerBurnerHeater((TileEntityBurnerHeater)world.getTileEntity(pos),player);
      case GUI_REFRACTORYTANK:
        return new ContainerRefractoryTank((TileEntityRefractoryTank)world.getTileEntity(pos),player);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    BlockPos pos = new BlockPos(x,y,z);
    switch(ID)
    {
      case GUI_CRUCIBLE:
      {
        TileEntityMeltingCrucible te = (TileEntityMeltingCrucible)world.getTileEntity(pos);
        return new GuiMeltingCrucible(te,player);
      }
      case GUI_CASTER:
      {
        TileEntityMetalCaster te = (TileEntityMetalCaster)world.getTileEntity(pos);
        return new GuiMetalCaster(te,player);
      }
      case GUI_ALLOYMIXER:
      {
        TileEntityAlloyMixer te = (TileEntityAlloyMixer)world.getTileEntity(pos);
        return new GuiAlloyMixer(te,player);
      }
      case GUI_INFUSER:
      {
        TileEntityMetalInfuser te = (TileEntityMetalInfuser)world.getTileEntity(pos);
        return new GuiMetalInfuser(te,player);
      }
      case GUI_ALLOYFURNACE:
      {
        TileEntityAlloyFurnace te = (TileEntityAlloyFurnace)world.getTileEntity(pos);
        return new GuiAlloyFurnace(te,player);
      }
      case GUI_MATERIALROUTER:
      {
        TileEntityMaterialRouter te = (TileEntityMaterialRouter)world.getTileEntity(pos);
        return new GuiMaterialRouter(te,player);
      }
      case GUI_REFRACTORYHOPPER:
      {
        TileEntityRefractoryHopper te = (TileEntityRefractoryHopper)world.getTileEntity(pos);
        return new GuiRefractoryHopper(te,player);
      }
      case GUI_REVOLVER:
      {
        return new GuiRevolver(player.getHeldItem(EnumHand.MAIN_HAND),player.inventory);
      }
      case GUI_SHOTGUN:
      {
        return new GuiShotgun(player.getHeldItem(EnumHand.MAIN_HAND),player.inventory);
      }
      case GUI_ATOMIZER:
      {
        TileEntityMetalAtomizer te = (TileEntityMetalAtomizer)world.getTileEntity(pos);
        return new GuiMetalAtomizer(te,player);
      }
      case GUI_MOLDSTATION:
      {
        TileEntityMoldStation te = (TileEntityMoldStation)world.getTileEntity(pos);
        return new GuiMoldStation(te,player);
      }
      case GUI_BURNERHEATER:
      {
        TileEntityBurnerHeater te = (TileEntityBurnerHeater)world.getTileEntity(pos);
        return new GuiBurnerHeater(te,player);
      }
      case GUI_REFRACTORYTANK:
      {
        TileEntityRefractoryTank te = (TileEntityRefractoryTank)world.getTileEntity(pos);
        return new GuiRefractoryTank(te,player);
      }
    } 
    return null;
  }
  
  public void postInit()
  {
    
  }
}
