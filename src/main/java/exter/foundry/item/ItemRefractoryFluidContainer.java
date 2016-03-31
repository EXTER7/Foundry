package exter.foundry.item;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabFluids;

public class ItemRefractoryFluidContainer extends Item implements IFluidContainerItem
{

 
  public final int capacity;
  
  public ItemRefractoryFluidContainer(int container_capacity)
  {
    super();
    capacity = container_capacity;
    setCreativeTab(FoundryTabFluids.tab);
    setMaxStackSize(1);
    setUnlocalizedName("foundry.fluidContainer");
    setHasSubtypes(true);

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void playerInteract(PlayerInteractEvent event)
  {
    //Prevent Blocks from activating when right clicking with a container in hand.
    ItemStack stack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
    if(stack != null && stack.getItem() == this && event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
    {
      event.setCanceled(true);
    }
  }

  private void setFluid(ItemStack is, FluidStack fluid)
  {
    if(fluid != null)
    {
      NBTTagCompound tag = is.getTagCompound();
      if(tag == null)
      {
        tag = new NBTTagCompound();
        is.setTagCompound(tag);
      }
      fluid.writeToNBT(tag);
    } else
    {
      is.setTagCompound(null);
    }
  }

  private ItemStack fromFluidStack(FluidStack fluid)
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
    list.add(fromFluidStack(null));
    Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
    for(Fluid f : fluids.values())
    {
      if(f != null)
      {
        list.add(fromFluidStack(new FluidStack(f, capacity)));
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    FluidStack fluid = getFluid(stack);
    if(fluid == null)
    {
      list.add(TextFormatting.BLUE + "Empty");
    } else
    {
      list.add(TextFormatting.BLUE + fluid.getLocalizedName());
      list.add(TextFormatting.BLUE + String.valueOf(fluid.amount) + " / " + String.valueOf(capacity) + " mB");
    }
  }

  private boolean splitStack(ItemStack stack,EntityPlayer player)
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
  
  @SuppressWarnings("deprecation")
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
  {
    FluidStack fluid = getFluid(stack);
    RayTraceResult obj = getMovingObjectPositionFromPlayer(world, player, fluid == null || fluid.amount == 0);

    
    if(obj == null)
    {
      return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
    if(obj.typeOfHit == RayTraceResult.Type.BLOCK)
    {
      TileEntity entity = world.getTileEntity(obj.getBlockPos());
      
      if(entity instanceof IFluidHandler)
      {
        IFluidHandler handler = (IFluidHandler)entity;
        if(player.isSneaking())
        {
          //Drain from container to the Tile Entity.
          
          FluidStack drained = drain(stack, 50, false);
          if(drained == null || drained.amount == 0)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          int filled = handler.fill(obj.sideHit, drained, false);
          if(filled == 0)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          drained.amount = filled;
          
          drain(stack, filled, true);
          handler.fill(obj.sideHit, drained, true);
        } else
        {
          //Fill container from the Tile Entity.

          FluidStack drained = handler.drain(obj.sideHit, 50, false);
          if(drained == null || drained.amount == 0)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          int filled = fill(stack, drained, false, true);
          if(filled == 0)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          if(!splitStack(stack,player))
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          drained.amount = filled;
          handler.drain(obj.sideHit, filled, true);
          fill(stack, drained, true, false);
        }
        
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
      }
      
      if(!world.canMineBlockBody(player, obj.getBlockPos()))
      {
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
      }

      if(player.isSneaking())
      {
        BlockPos pos = obj.getBlockPos().add(obj.sideHit.getDirectionVec());

        if(!player.canPlayerEdit(pos, obj.sideHit, stack))
        {
          return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

        Material material = world.getBlockState(pos).getMaterial();
        
        if(world.isAirBlock(pos) || !material.isSolid())
        {
          //Place fluid in the world.
          
          FluidStack drained = drain(stack, FluidContainerRegistry.BUCKET_VOLUME, false);
          if(drained != null && drained.getFluid().canBePlacedInWorld() && drained.amount == FluidContainerRegistry.BUCKET_VOLUME)
          {
            drain(stack, FluidContainerRegistry.BUCKET_VOLUME, true);
            if(!world.isRemote && !material.isLiquid())
            {
              world.destroyBlock(pos, true);
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
            world.setBlockState(pos, block.getDefaultState());
          }
          return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

      } else
      {
        BlockPos pos = obj.getBlockPos();
        //Drain fluid from the world.

        if(!player.canPlayerEdit(pos, obj.sideHit, stack))
        {
          return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }
        
        IBlockState state = world.getBlockState(pos);
        
        if(state.getBlock() instanceof IFluidBlock)
        {
          IFluidBlock fluid_block = (IFluidBlock)state.getBlock();
          if(!fluid_block.canDrain(world, pos))
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          FluidStack drained = fluid_block.drain(world, pos, false);
          if(drained == null)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          int filled = fill(stack, drained, false, true);
          if(filled != drained.amount)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          if(!splitStack(stack,player))
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          fluid_block.drain(world, pos, true);
          fill(stack, drained, true, false);
          
          return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

        if(state.getMaterial() == Material.water && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
        {
          FluidStack fill = new FluidStack(FluidRegistry.WATER,FluidContainerRegistry.BUCKET_VOLUME);
          if(fill(stack, fill, false, true) == FluidContainerRegistry.BUCKET_VOLUME)
          {
            if(!splitStack(stack,player))
            {
              return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
            fill(stack, fill, true, false);
            world.setBlockToAir(pos);
          }

          return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

        if(state.getMaterial() == Material.lava && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
        {
          FluidStack fill = new FluidStack(FluidRegistry.LAVA,FluidContainerRegistry.BUCKET_VOLUME);
          if(fill(stack, fill, false, true) == FluidContainerRegistry.BUCKET_VOLUME)
          {
            if(!splitStack(stack,player))
            {
              return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
            fill(stack, fill, true, false);
            world.setBlockToAir(pos);
          }

          return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }
      }
    }
    return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
  }
  
  @Override
  public FluidStack getFluid(ItemStack stack)
  {
    if(stack.getTagCompound() == null)
    {
      return null;
    }
    return FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
  }

  @Override
  public int getCapacity(ItemStack container)
  {
    return capacity;
  }

  @SuppressWarnings("deprecation")
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

      setFluid(stack, container_fluid);
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
    setFluid(stack, container_fluid);
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
      setFluid(stack, fluid);

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
  
  public ItemStack empty(int stack_size)
  {
    ItemStack stack = new ItemStack(this,stack_size,0);
    setFluid(stack, null);
    return stack;
  }
}
