package exter.foundry.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.util.FoundryContainer;

public class ItemFoundryContainer extends Item
{

  @SideOnly(Side.CLIENT)
  public Icon icon_fg;
  @SideOnly(Side.CLIENT)
  public Icon icon_bg;

  @SideOnly(Side.CLIENT)
  public Icon icon_def_empty;
  @SideOnly(Side.CLIENT)
  public Icon icon_def_partial;
  @SideOnly(Side.CLIENT)
  public Icon icon_def_full;

  public ItemFoundryContainer(int id)
  {
    super(id);
    setCreativeTab(CreativeTabs.tabMisc);
    setMaxStackSize(1);
    setUnlocalizedName("foundryContainer");
    setHasSubtypes(true);

    

  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister register)
  {
    icon_fg = register.registerIcon("foundry:container_foreground");
    icon_bg = register.registerIcon("foundry:container_background");
    icon_def_empty = register.registerIcon("foundry:container_def_empty");
    icon_def_partial = register.registerIcon("foundry:container_def_partial");
    icon_def_full = register.registerIcon("foundry:container_def_full");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public Icon getIconFromDamage(int dmg)
  {
    return icon_def_empty;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(int id, CreativeTabs tabs, List list)
  {
    int i;
    list.add(FoundryContainer.FromFluidStack( null));
    Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
    for(Fluid f : fluids.values())
    {
      if(f != null)
      {
        list.add(FoundryContainer.FromFluidStack(new FluidStack(f, FluidContainerRegistry.BUCKET_VOLUME)));
      }
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
    FluidStack fluid = FoundryContainer.GetFluidStack(stack);
    if(fluid == null)
    {
      list.add(EnumChatFormatting.BLUE + "Empty");
    } else
    {
      list.add(EnumChatFormatting.BLUE + fluid.getFluid().getLocalizedName());
      list.add(EnumChatFormatting.BLUE + String.valueOf(fluid.amount) + " / 1000 mB");
    }
  }

  /**
   * Called whenever this item is equipped and the right mouse button is
   * pressed. Args: itemStack, world, entityPlayer
   */
  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
  {
    FluidStack fluid = FoundryContainer.GetFluidStack(stack);
    MovingObjectPosition obj = getMovingObjectPositionFromPlayer(world, player, fluid == null || fluid.amount == 0);

    
    if(obj == null)
    {
      return stack;
    }
    if(obj.typeOfHit == EnumMovingObjectType.TILE)
    {
      int x = obj.blockX;
      int y = obj.blockY;
      int z = obj.blockZ;

      if(!world.canMineBlock(player, x, y, z))
      {
        return stack;
      }

      if(player.isSneaking())
      {
        switch(obj.sideHit)
        {
          case 0:
            --y;
            break;
          case 1:
            ++y;
            break;
          case 2:
            --z;
            break;
          case 3:
            ++z;
            break;
          case 4:
            --x;
            break;
          case 5:
            ++x;
            break;
        }

        if(!player.canPlayerEdit(x, y, z, obj.sideHit, stack))
        {
          return stack;
        }

        Material material = world.getBlockMaterial(x, y, z);
        
        if(world.isAirBlock(x, y, z) || !material.isSolid())
        {
          FluidStack drained = FoundryContainer.Drain(stack, FluidContainerRegistry.BUCKET_VOLUME, false);
          if(drained != null && drained.getFluid().canBePlacedInWorld() && drained.amount == FluidContainerRegistry.BUCKET_VOLUME)
          {
            FoundryContainer.Drain(stack, FluidContainerRegistry.BUCKET_VOLUME, true);
            if(!world.isRemote && !material.isLiquid())
            {
              world.destroyBlock(x, y, z, true);
            }
            int id = drained.getFluid().getBlockID();
            if(id == Block.waterStill.blockID)
            {
              id = Block.waterMoving.blockID;
            }
            if(id == Block.lavaStill.blockID)
            {
              id = Block.lavaMoving.blockID;
            }
            world.setBlock(x, y, z, id, 0, 3);
          }
          return stack;
        }

      } else
      {
        if(!player.canPlayerEdit(x, y, z, obj.sideHit, stack))
        {
          return stack;
        }
        
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        
        if(block instanceof IFluidBlock)
        {
          IFluidBlock fluid_block = (IFluidBlock)block;
          if(!fluid_block.canDrain(world, x, y, z))
          {
            return stack;
          }
          FluidStack drained = fluid_block.drain(world, x, y, z, false);
          if(drained == null)
          {
            return stack;
          }
          int filled = FoundryContainer.Fill(stack, drained, false);
          if(filled != drained.amount)
          {
            return stack;
          }
          fluid_block.drain(world, x, y, z, true);
          FoundryContainer.Fill(stack, drained, true);
          
          return stack;
        }

        if(world.getBlockMaterial(x, y, z) == Material.water && world.getBlockMetadata(x, y, z) == 0)
        {
          if(player.capabilities.isCreativeMode)
          {
            world.setBlockToAir(x, y, z);
            return stack;
          }

          FluidStack fill = new FluidStack(FluidRegistry.WATER,FluidContainerRegistry.BUCKET_VOLUME);
          if(FoundryContainer.Fill(stack, fill, false) == FluidContainerRegistry.BUCKET_VOLUME)
          {
            FoundryContainer.Fill(stack, fill, true);
            world.setBlockToAir(x, y, z);
          }

          return stack;
        }

        if(world.getBlockMaterial(x, y, z) == Material.lava && world.getBlockMetadata(x, y, z) == 0)
        {
          if(player.capabilities.isCreativeMode)
          {
            world.setBlockToAir(x, y, z);
            return stack;
          }

          FluidStack fill = new FluidStack(FluidRegistry.LAVA,FluidContainerRegistry.BUCKET_VOLUME);
          if(FoundryContainer.Fill(stack, fill, false) == FluidContainerRegistry.BUCKET_VOLUME)
          {
            FoundryContainer.Fill(stack, fill, true);
            world.setBlockToAir(x, y, z);
          }

          return stack;
        }
      }
    }
    return stack;
  }
}
