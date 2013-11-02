package exter.foundry.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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


  public static final int AMOUNT_MAX = 1000;

  public ItemFoundryContainer(int id)
  {
    super(id);
    setCreativeTab(CreativeTabs.tabMisc);
    maxStackSize = 1;
    LanguageRegistry.addName(this, "Foundry Container");
    setHasSubtypes(true);
  }
  
  public static FluidStack GetFluidStackFromItemNBT(ItemStack stack)
  {
    if(stack.itemID != FoundryItems.item_container.itemID || stack.stackTagCompound == null)
    {
      return null;
    }
    return FluidStack.loadFluidStackFromNBT(stack.stackTagCompound);
  }

  private static void SetContainerFluidStackNBT(ItemStack is,FluidStack fluid)
  {
    is.stackTagCompound = new NBTTagCompound();
    if(fluid != null)
    {
      fluid.writeToNBT(is.stackTagCompound);
    }
  }

  private static ItemStack StackFromFluid(int id,FluidStack fluid)
  {
    ItemStack stack = new ItemStack(id,1,0);
    if(fluid == null)
    {
      return stack;
    }
    if(fluid.amount > AMOUNT_MAX)
    {
      fluid = new FluidStack(fluid,AMOUNT_MAX);
    }
    SetContainerFluidStackNBT(stack,fluid);
    return stack;
  }

  public static ItemStack StackFromFluid(FluidStack fluid)
  {
    return StackFromFluid(FoundryItems.item_container.itemID,fluid);
  }

  public static int FillContainer(ItemStack stack, FluidStack fluid,boolean do_fill)
  {
    if(stack.itemID != FoundryItems.item_container.itemID || fluid == null)
    {
      return 0;
    }

    FluidStack container_fluid = GetFluidStackFromItemNBT(stack);

    if(!do_fill)
    {
      if(container_fluid == null)
      {
        return Math.min(AMOUNT_MAX, fluid.amount);
      }

      if(!container_fluid.isFluidEqual(fluid))
      {
        return 0;
      }

      return Math.min(AMOUNT_MAX - container_fluid.amount, fluid.amount);
    }

    if(container_fluid == null)
    {
      container_fluid = new FluidStack(fluid, Math.min(AMOUNT_MAX, fluid.amount));

      return container_fluid.amount;
    }

    if(!container_fluid.isFluidEqual(fluid))
    {
      return 0;
    }
    int filled = AMOUNT_MAX - container_fluid.amount;

    if(fluid.amount < filled)
    {
      container_fluid.amount += fluid.amount;
      filled = fluid.amount;
    } else
    {
      container_fluid.amount = AMOUNT_MAX;
    }
    SetContainerFluidStackNBT(stack,container_fluid);
    return filled;
  }

  public static FluidStack DrainContainer(ItemStack stack, int amount, boolean do_drain)
  {
    if(stack.itemID != FoundryItems.item_container.itemID)
    {
      return null;
    }
    FluidStack fluid = GetFluidStackFromItemNBT(stack);

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
      SetContainerFluidStackNBT(stack, fluid);

    }
    return drain_fluid;
  }

  @Override
  public String getUnlocalizedName()
  {
    return "foundryContainer";
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
    list.add(StackFromFluid(id,null));
    Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
    for(Fluid f:fluids.values())
    {
      if(f != null)
      {
        list.add(StackFromFluid(id,new FluidStack(f,AMOUNT_MAX)));
      }
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
    FluidStack fluid = GetFluidStackFromItemNBT(stack);
    if(fluid == null)
    {
      list.add(EnumChatFormatting.BLUE + "Empty");
    } else
    {
      list.add(EnumChatFormatting.BLUE + fluid.getFluid().getLocalizedName());
      list.add(EnumChatFormatting.BLUE + String.valueOf(fluid.amount) + " / 1000 mB");
    }
  }
}
