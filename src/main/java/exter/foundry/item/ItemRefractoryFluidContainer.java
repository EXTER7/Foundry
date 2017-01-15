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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabFluids;

public class ItemRefractoryFluidContainer extends Item
{
  private class FluidHandler implements IFluidHandlerItem,IFluidTankProperties,ICapabilityProvider
  {
    private IFluidTankProperties[] props;
    private ItemStack stack;
    
    public FluidHandler(ItemStack stack)
    {
      this.stack = stack;
      props = new IFluidTankProperties[] { this };
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
      return props;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
      return ItemRefractoryFluidContainer.this.fill(stack, resource, doFill, false);
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
      FluidStack fluid = getFluid(stack);
      if(resource == null || (fluid != null && !fluid.isFluidEqual(resource)))
      {
        return null;
      }
      return drain(resource.amount,doDrain);
    }

    @Override
    public FluidStack drain(int amount, boolean doDrain)
    {
      return ItemRefractoryFluidContainer.this.drain(stack, amount, doDrain);
    }

    @Override
    public FluidStack getContents()
    {
      if(stack.getTagCompound() == null)
      {
        return null;
      }
      return FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
    }

    @Override
    public int getCapacity()
    {
      return Fluid.BUCKET_VOLUME;
    }

    @Override
    public boolean canFill()
    {
      return true;
    }

    @Override
    public boolean canDrain()
    {
      return true;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack)
    {
      return true;
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluidStack)
    {
      return true;
    }    
    @Override
    public boolean hasCapability(Capability<?> cap,EnumFacing facing)
    {
      return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }
    
    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing facing)
    {
      if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
      {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
      } else
      {
        return null;
      }
    }

    @Override
    public ItemStack getContainer()
    {
      return stack;
    }
  }
  
  public ItemRefractoryFluidContainer()
  {
    super();
    setCreativeTab(FoundryTabFluids.tab);
    setMaxStackSize(1);
    setUnlocalizedName("foundry.fluid_container");
    setRegistryName("fluid_container");
    setHasSubtypes(true);
    

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void playerInteract(PlayerInteractEvent.RightClickBlock event)
  {
    //Prevent Blocks from activating when right clicking with a container in hand.
    ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
    if(stack != null && stack.getItem() == this)
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
    if(fluid.amount > Fluid.BUCKET_VOLUME)
    {
      fluid = new FluidStack(fluid, Fluid.BUCKET_VOLUME);
    }
    setFluid(stack, fluid);
    return stack;
  }
  
  public FluidStack getFluid(ItemStack stack)
  {
    if(stack.getTagCompound() == null)
    {
      return null;
    }
    return FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item,CreativeTabs tabs, NonNullList<ItemStack> list)
  {
    list.add(fromFluidStack(null));
    Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
    for(Fluid f : fluids.values())
    {
      if(f != null)
      {
        list.add(fromFluidStack(new FluidStack(f, Fluid.BUCKET_VOLUME)));
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
      list.add(TextFormatting.BLUE + String.valueOf(fluid.amount) + " / " + String.valueOf(Fluid.BUCKET_VOLUME) + " mB");
    }
  }

  private boolean splitStack(ItemStack stack,EntityPlayer player)
  {
    if(stack.getCount() == 1)
    {
      return true;
    }
    if(player.capabilities.isCreativeMode)
    {
      return false;
    }
    
    ItemStack rest_stack = stack.copy();
    rest_stack.shrink(1);
    int slot = player.inventory.getFirstEmptyStack();
    if(slot >= 0)
    {
      player.inventory.setInventorySlotContents(slot, rest_stack);
      player.inventory.markDirty();
      stack.setCount(1);
      return true;
    }
    return false;
  }
  
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
  {
    ItemStack stack = player.getHeldItem(hand);
    FluidStack fluid = getFluid(stack);
    RayTraceResult obj = rayTrace(world, player, fluid == null || fluid.amount == 0);

    
    if(obj == null)
    {
      return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
    if(obj.typeOfHit == RayTraceResult.Type.BLOCK)
    {
      TileEntity tile = world.getTileEntity(obj.getBlockPos());
      
      if(tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,obj.sideHit))
      {
        IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, obj.sideHit);
        if(player.isSneaking())
        {
          //Drain from container to the Tile Entity.
          
          FluidStack drained = drain(stack, 50, false);
          if(drained == null || drained.amount == 0)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          int filled = handler.fill(drained, false);
          if(filled == 0)
          {
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
          }
          drained.amount = filled;
          
          drain(stack, filled, true);
          handler.fill(drained, true);
        } else
        {
          //Fill container from the Tile Entity.

          FluidStack drained = handler.drain(50, false);
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
          handler.drain(filled, true);
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
          
          FluidStack drained = drain(stack, Fluid.BUCKET_VOLUME, false);
          if(drained != null && drained.getFluid().canBePlacedInWorld() && drained.amount == Fluid.BUCKET_VOLUME)
          {
            drain(stack, Fluid.BUCKET_VOLUME, true);
            if(!world.isRemote && !material.isLiquid())
            {
              world.destroyBlock(pos, true);
            }
            Block block = drained.getFluid().getBlock();
            if(block == Blocks.WATER)
            {
              block = Blocks.FLOWING_WATER;
            }
            if(block == Blocks.LAVA)
            {
              block = Blocks.FLOWING_LAVA;
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

        if(state.getMaterial() == Material.WATER && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
        {
          FluidStack fill = new FluidStack(FluidRegistry.WATER,Fluid.BUCKET_VOLUME);
          if(fill(stack, fill, false, true) == Fluid.BUCKET_VOLUME)
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

        if(state.getMaterial() == Material.LAVA && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
        {
          FluidStack fill = new FluidStack(FluidRegistry.LAVA,Fluid.BUCKET_VOLUME);
          if(fill(stack, fill, false, true) == Fluid.BUCKET_VOLUME)
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
  

  public FluidStack drain(ItemStack stack,int amount, boolean doDrain)
  {
    if(stack.getCount() > 1)
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
    if(doDrain)
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

  private int fill(ItemStack stack, FluidStack fluid, boolean do_fill,boolean ignore_stacksize)
  {
    if(!ignore_stacksize && stack.getCount() > 1)
    {
      return 0;
    }
    FluidStack container_fluid = getFluid(stack);

    if(!do_fill)
    {
      if(container_fluid == null)
      {
        return Math.min(Fluid.BUCKET_VOLUME, fluid.amount);
      }

      if(!container_fluid.isFluidEqual(fluid))
      {
        return 0;
      }

      return Math.min(Fluid.BUCKET_VOLUME - container_fluid.amount, fluid.amount);
    }

    if(container_fluid == null)
    {
      container_fluid = new FluidStack(fluid, Math.min(Fluid.BUCKET_VOLUME, fluid.amount));

      setFluid(stack, container_fluid);
      return container_fluid.amount;
    }

    if(!container_fluid.isFluidEqual(fluid))
    {
      return 0;
    }
    int filled = Fluid.BUCKET_VOLUME - container_fluid.amount;

    if(fluid.amount < filled)
    {
      container_fluid.amount += fluid.amount;
      filled = fluid.amount;
    } else
    {
      container_fluid.amount = Fluid.BUCKET_VOLUME;
    }
    setFluid(stack, container_fluid);
    return filled;
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
  
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FluidHandler(stack);
  }
}
