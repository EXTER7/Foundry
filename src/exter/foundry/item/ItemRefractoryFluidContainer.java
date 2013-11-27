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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.ItemFluidContainer;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRefractoryFluidContainer extends ItemFluidContainer
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

  public ItemRefractoryFluidContainer(int id,int container_capacity)
  {
    super(id,container_capacity);
    setCreativeTab(CreativeTabs.tabMisc);
    setMaxStackSize(1);
    setUnlocalizedName("foundryContainer");
    setHasSubtypes(true);

    MinecraftForge.EVENT_BUS.register(this);
  }

  @ForgeSubscribe
  public void PlayerInteract(PlayerInteractEvent event)
  {
    //Prevent Blocks from activating when right clicking with a container in hand.
    ItemStack stack = event.entityPlayer.getHeldItem();
    if(stack != null && stack.itemID == itemID && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
    {
      event.setCanceled(true);
    }
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
  
  private ItemStack FromFluidStack(int id,FluidStack fluid)
  {
    ItemStack stack = new ItemStack(id, 1, 0);
    if(fluid == null)
    {
      return stack;
    }
    if(fluid.amount > capacity)
    {
      fluid = new FluidStack(fluid, capacity);
    }
    fill(stack, fluid, true);
    return stack;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(int id, CreativeTabs tabs, List list)
  {
    int i;
    list.add(FromFluidStack(id, null));
    Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
    for(Fluid f : fluids.values())
    {
      if(f != null)
      {
        list.add(FromFluidStack(id, new FluidStack(f, capacity)));
      }
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
    FluidStack fluid = getFluid(stack);
    if(fluid == null)
    {
      list.add(EnumChatFormatting.BLUE + "Empty");
    } else
    {
      list.add(EnumChatFormatting.BLUE + fluid.getFluid().getLocalizedName());
      list.add(EnumChatFormatting.BLUE + String.valueOf(fluid.amount) + " / " + String.valueOf(capacity) + " mB");
    }
  }

  private boolean SplitStack(ItemStack stack,EntityPlayer player)
  {
    if(stack.stackSize == 1)
    {
      return true;
    }
    ItemStack rest_stack = stack.copy();
    rest_stack.stackSize--;
    if(player.inventory.addItemStackToInventory(rest_stack))
    {
      stack.stackSize = 1;
      return true;
    }
    return false;
  }
  
  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
  {
    FluidStack fluid = getFluid(stack);
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

      TileEntity entity = world.getBlockTileEntity(x, y, z);
      
      if(entity instanceof IFluidHandler)
      {
        IFluidHandler handler = (IFluidHandler)entity;
        ForgeDirection side = ForgeDirection.getOrientation(obj.sideHit);
        if(player.isSneaking())
        {
          //Drain from container to the Tile Entity.
          
          FluidStack drained = drain(stack, 50, false);
          if(drained == null || drained.amount == 0)
          {
            return stack;
          }
          int filled = handler.fill(side, drained, false);
          if(filled == 0)
          {
            return stack;
          }
          drained.amount = filled;
          
          drain(stack, filled, true);
          handler.fill(side, drained, true);
        } else
        {
          //Fill container from the Tile Entity.

          FluidStack drained = handler.drain(side, 50, false);
          if(drained == null || drained.amount == 0)
          {
            return stack;
          }
          int filled = fill(stack, drained, false, true);
          if(filled == 0)
          {
            return stack;
          }
          if(!SplitStack(stack,player))
          {
            return stack;
          }
          drained.amount = filled;
          handler.drain(side, filled, true);
          fill(stack, drained, true, false);
        }
        
        return stack;
      }
      
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
          //Place fluid in the world.
          
          FluidStack drained = drain(stack, FluidContainerRegistry.BUCKET_VOLUME, false);
          if(drained != null && drained.getFluid().canBePlacedInWorld() && drained.amount == FluidContainerRegistry.BUCKET_VOLUME)
          {
            drain(stack, FluidContainerRegistry.BUCKET_VOLUME, true);
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
        //Drain fluid from the world.

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
          int filled = fill(stack, drained, false, true);
          if(filled != drained.amount)
          {
            return stack;
          }
          if(!SplitStack(stack,player))
          {
            return stack;
          }
          fluid_block.drain(world, x, y, z, true);
          fill(stack, drained, true, false);
          
          return stack;
        }

        if(world.getBlockMaterial(x, y, z) == Material.water && world.getBlockMetadata(x, y, z) == 0)
        {
          FluidStack fill = new FluidStack(FluidRegistry.WATER,FluidContainerRegistry.BUCKET_VOLUME);
          if(fill(stack, fill, false, true) == FluidContainerRegistry.BUCKET_VOLUME)
          {
            if(!SplitStack(stack,player))
            {
              return stack;
            }
            fill(stack, fill, true, false);
            world.setBlockToAir(x, y, z);
          }

          return stack;
        }

        if(world.getBlockMaterial(x, y, z) == Material.lava && world.getBlockMetadata(x, y, z) == 0)
        {
          FluidStack fill = new FluidStack(FluidRegistry.LAVA,FluidContainerRegistry.BUCKET_VOLUME);
          if(fill(stack, fill, false, true) == FluidContainerRegistry.BUCKET_VOLUME)
          {
            if(!SplitStack(stack,player))
            {
              return stack;
            }
            fill(stack, fill, true, false);
            world.setBlockToAir(x, y, z);
          }

          return stack;
        }
      }
    }
    return stack;
  }
  
  private int fill(ItemStack container, FluidStack resource, boolean doFill,boolean ignore_stacksize)
  {
    if(!ignore_stacksize && container.stackSize > 1)
    {
      return 0;
    }
    return super.fill(container, resource, doFill);
  }

  @Override
  public int fill(ItemStack container, FluidStack resource, boolean doFill)
  {
    return fill(container, resource, doFill, false);
  }

  @Override
  public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
  {
    if(container.stackSize > 1)
    {
      return null;
    }
    FluidStack result = super.drain(container, maxDrain, doDrain);
    if(doDrain)
    {
      FluidStack stack = getFluid(container);
      if(stack == null || stack.amount == 0)
      {
        container.stackTagCompound = null;
      }
    }
    return result;
  }
  
  @Override
  public int getItemStackLimit(ItemStack stack)
  {
    FluidStack fluid = getFluid(stack);
    if(fluid == null || fluid.amount == 0)
    {
      return 16;
    }
    return 1;
  }
}
