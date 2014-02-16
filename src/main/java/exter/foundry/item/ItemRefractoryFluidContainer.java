package exter.foundry.item;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabFluids;

public class ItemRefractoryFluidContainer extends Item implements IFluidContainerItem
{

  @SideOnly(Side.CLIENT)
  public IIcon icon_fg;
  @SideOnly(Side.CLIENT)
  public IIcon icon_bg;

  @SideOnly(Side.CLIENT)
  public IIcon icon_def_empty;
  @SideOnly(Side.CLIENT)
  public IIcon icon_def_partial;
  @SideOnly(Side.CLIENT)
  public IIcon icon_def_full;
  
  public final int capacity;
  
  public ItemRefractoryFluidContainer(int container_capacity)
  {
    super();
    capacity = container_capacity;
    setCreativeTab(FoundryTabFluids.tab);
    setMaxStackSize(1);
    setUnlocalizedName("foundryContainer");
    setHasSubtypes(true);

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void PlayerInteract(PlayerInteractEvent event)
  {
    //Prevent Blocks from activating when right clicking with a container in hand.
    ItemStack stack = event.entityPlayer.getHeldItem();
    if(stack != null && stack.getItem() == this && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
    {
      event.setCanceled(true);
    }
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon_fg = register.registerIcon("foundry:container_foreground");
    icon_bg = register.registerIcon("foundry:container_background");
    icon_def_empty = register.registerIcon("foundry:container_def_empty");
    icon_def_partial = register.registerIcon("foundry:container_def_partial");
    icon_def_full = register.registerIcon("foundry:container_def_full");
  }
  
  private void SetFluid(ItemStack is, FluidStack fluid)
  {
    if(fluid != null)
    {
      if(is.stackTagCompound == null)
      {
        is.stackTagCompound = new NBTTagCompound();
      }
      fluid.writeToNBT(is.stackTagCompound);
    } else
    {
      is.stackTagCompound = null;
    }
  }


  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon_def_empty;
  }
  
  private ItemStack FromFluidStack(FluidStack fluid)
  {
    ItemStack stack = new ItemStack(this, 1, 0);
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

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item,CreativeTabs tabs, @SuppressWarnings("rawtypes") List list)
  {
    list.add(FromFluidStack(null));
    Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
    for(Fluid f : fluids.values())
    {
      if(f != null)
      {
        list.add(FromFluidStack(new FluidStack(f, capacity)));
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
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
    if(player.capabilities.isCreativeMode)
    {
      return false;
    }
    
    ItemStack rest_stack = stack.copy();
    rest_stack.stackSize--;
    int slot = player.inventory.getFirstEmptyStack();
    if(slot >= 0)
    {
      player.inventory.setInventorySlotContents(slot, rest_stack);
      player.inventory.markDirty();
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
    if(obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
    {
      int x = obj.blockX;
      int y = obj.blockY;
      int z = obj.blockZ;

      TileEntity entity = world.getTileEntity(x, y, z);
      
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

        Material material = world.getBlock(x, y, z).getMaterial();
        
        if(world.isAirBlock(x, y, z) || !material.isSolid())
        {
          //Place fluid in the world.
          
          FluidStack drained = drain(stack, FluidContainerRegistry.BUCKET_VOLUME, false);
          if(drained != null && drained.getFluid().canBePlacedInWorld() && drained.amount == FluidContainerRegistry.BUCKET_VOLUME)
          {
            drain(stack, FluidContainerRegistry.BUCKET_VOLUME, true);
            if(!world.isRemote && !material.isLiquid())
            {
              world.func_147480_a/*destroyBlock*/(x, y, z, true);
            }
            Block block = drained.getFluid().getBlock();
            if(block == Blocks.water)
            {
              block = Blocks.flowing_water;
            }
            if(block == Blocks.lava)
            {
              block = Blocks.flowing_lava;
            }
            world.setBlock(x, y, z, block, 0, 3);
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
        
        Block block = world.getBlock(x, y, z);
        
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

        if(block.getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0)
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

        if(block.getMaterial() == Material.lava && world.getBlockMetadata(x, y, z) == 0)
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
  
  @Override
  public FluidStack getFluid(ItemStack stack)
  {
    if(stack.stackTagCompound == null)
    {
      return null;
    }
    return FluidStack.loadFluidStackFromNBT(stack.stackTagCompound);
  }

  @Override
  public int getCapacity(ItemStack container)
  {
    return capacity;
  }

  private int fill(ItemStack stack, FluidStack fluid, boolean do_fill,boolean ignore_stacksize)
  {
    if(!ignore_stacksize && stack.stackSize > 1)
    {
      return 0;
    }
    FluidStack container_fluid = getFluid(stack);

    if(!do_fill)
    {
      if(container_fluid == null)
      {
        return Math.min(FluidContainerRegistry.BUCKET_VOLUME, fluid.amount);
      }

      if(!container_fluid.isFluidEqual(fluid))
      {
        return 0;
      }

      return Math.min(FluidContainerRegistry.BUCKET_VOLUME - container_fluid.amount, fluid.amount);
    }

    if(container_fluid == null)
    {
      container_fluid = new FluidStack(fluid, Math.min(FluidContainerRegistry.BUCKET_VOLUME, fluid.amount));

      SetFluid(stack, container_fluid);
      return container_fluid.amount;
    }

    if(!container_fluid.isFluidEqual(fluid))
    {
      return 0;
    }
    int filled = FluidContainerRegistry.BUCKET_VOLUME - container_fluid.amount;

    if(fluid.amount < filled)
    {
      container_fluid.amount += fluid.amount;
      filled = fluid.amount;
    } else
    {
      container_fluid.amount = FluidContainerRegistry.BUCKET_VOLUME;
    }
    SetFluid(stack, container_fluid);
    return filled;
  }

  @Override
  public int fill(ItemStack container, FluidStack resource, boolean doFill)
  {
    return fill(container, resource, doFill, false);
  }
  
  @Override
  public FluidStack drain(ItemStack stack, int amount, boolean do_drain)
  {
    if(stack.stackSize > 1)
    {
      return null;
    }
    FluidStack fluid = getFluid(stack);

    if(fluid == null)
    {
      return null;
    }

    int drained = amount;
    if(fluid.amount < drained)
    {
      drained = fluid.amount;
    }

    FluidStack drain_fluid = new FluidStack(fluid, drained);
    if(do_drain)
    {
      fluid.amount -= drained;
      if(fluid.amount <= 0)
      {
        fluid = null;
      }
      SetFluid(stack, fluid);

    }
    return drain_fluid;
  }
  
  @Override
  public int getItemStackLimit(ItemStack stack)
  {
    FluidStack fluid = getFluid(stack);
    if(fluid == null)
    {
      return 16;
    }
    return 1;
  }
  
  public ItemStack EmptyContainer(int stack_size)
  {
    ItemStack stack = new ItemStack(this,stack_size,0);
    SetFluid(stack, null);
    return stack;
  }
}
