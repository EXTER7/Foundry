package exter.foundry.item.firearm;

import java.util.List;

import exter.foundry.ModFoundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.item.FoundryItems;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.sound.FoundrySounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRevolver extends ItemFirearm
{
  static public final String AMMO_TYPE = "revolver";
  
  
  
  public ItemRevolver()
  {
    setUnlocalizedName("revolver");
    setRegistryName("revolver");
  }

  @Override
  public boolean getShareTag()
  {
    return true;
  }

  public int getPosition(ItemStack stack)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a revolver");
    }

    NBTTagCompound tag = stack.getTagCompound();
    if(tag == null)
    {
      return 0;
    }
    if(!tag.hasKey("position"))
    {
      return 0;
    }
    return tag.getInteger("position");
  }
  
  public void setPosition(ItemStack stack,int position)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a revolver");
    }
    if(position < 0 || position > 7)
    {
      throw new IllegalArgumentException("Slot index not in range: " + position);
    }
    NBTTagCompound tag = stack.getTagCompound();
    if(tag == null)
    {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }
    tag.setInteger("position",position);
  }
  
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int count)
  {
    if(!player.isSneaking())
    {      
      int position = getPosition(stack);
      ItemStack round = getAmmo(stack,position);
      if(roundMatches(round,AMMO_TYPE))
      {
        if(!world.isRemote)
        {
          world.playSound(null, player.posX, player.posY, player.posZ, FoundrySounds.sound_revolver_fire, SoundCategory.PLAYERS, 1, 1);
        }
        shoot(round,world,player,null,1,0.01f,1.0f);
        setAmmo(stack,position,round.getCapability(FoundryAPI.capability_firearmround, null).getCasing().copy());
        if(world.isRemote)
        {
          player.rotationPitch -= 3;
          float pitch = -player.rotationPitch;
          float yaw = -player.rotationYaw;
          float cpitch = -MathHelper.cos(pitch * 0.017453292F);          
          player.motionX -= MathHelper.sin(yaw * 0.017453292F - (float) Math.PI) * cpitch * 0.1;
          player.motionY -= MathHelper.sin(pitch * 0.017453292F) * 0.1;
          player.motionZ -= MathHelper.cos(yaw * 0.017453292F - (float) Math.PI) * cpitch * 0.1;
        }
        stack.damageItem(1, player);
      } else
      {
        if(!world.isRemote)
        {
          world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.3F, 1.5F);
        }        
      }
      setPosition(stack,(position + 1) % 8);
    }    
  }

  
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
  {
    if(player.isSneaking())
    {
      if (!world.isRemote)
      {
        player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_REVOLVER, world, 0, 0, 0);
      }
    } else
    {
       player.setActiveHand(hand);
    }
    return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item,CreativeTabs tabs, NonNullList<ItemStack> list)
  {
    list.add(empty());
    list.add(loaded());
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      NBTTagCompound tag = stack.getTagCompound();
      if(tag == null)
      {
        tag = new NBTTagCompound();
        stack.setTagCompound(tag);
      }
      int position = tag.getInteger("position");
      int i;
      for(i = 0; i < 8; i++)
      {
        int j = (i + position) % 8;
        NBTTagCompound ammo_tag = tag.getCompoundTag("Slot_" + j);
        if(ammo_tag == null || ammo_tag.getBoolean("Empty"))
        {
          list.add(TextFormatting.BLUE + "< Empty >");
        } else
        {
          ItemStack ammo = new ItemStack(ammo_tag);
          list.add(TextFormatting.BLUE + ammo.getDisplayName());
        }
      }
    }
  }

  @Override
  public boolean isFull3D()
  {
    return true;
  }
  
  public void setAmmo(ItemStack stack,int slot,ItemStack ammo)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a revolver");
    }
    if(slot < 0 || slot > 7)
    {
      throw new IllegalArgumentException("Slot index not in range: " + slot);
    }
    NBTTagCompound tag = stack.getTagCompound();
    if(tag == null)
    {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }
    NBTTagCompound ammo_tag = new NBTTagCompound();
    if(ammo == null)
    {
      ammo_tag.setBoolean("Empty", true);
    } else
    {
      ammo_tag.setBoolean("Empty", false);
      ammo.writeToNBT(ammo_tag);
    }
    
    tag.setTag("Slot_" + slot,ammo_tag);
  }
  

  public ItemStack getAmmo(ItemStack stack,int slot)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a revolver");
    }
    if(slot < 0 || slot > 7)
    {
      throw new IllegalArgumentException("Slot index not in range: " + slot);
    }
    NBTTagCompound tag = stack.getTagCompound();
    if(tag == null)
    {
      return null;
    }
    NBTTagCompound ammo_tag = tag.getCompoundTag("Slot_" + slot);
    if(ammo_tag == null || ammo_tag.getBoolean("Empty"))
    {
      return null;
    } else
    {
      return new ItemStack(ammo_tag);
    }
  }

  public ItemStack empty()
  {
    ItemStack stack = new ItemStack(this);
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("position", 0);
    int i;
    for(i = 0; i < 8; i++)
    {
      NBTTagCompound slot = new NBTTagCompound();
      slot.setBoolean("Empty",true);
      nbt.setTag("Slot_" + i,slot);
    }
    stack.setTagCompound(nbt);
    return stack;
  }

  public ItemStack loaded()
  {
    ItemStack stack = new ItemStack(this);
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("position", 0);
    int i;
    ItemStack ammo = new ItemStack(FoundryItems.item_round);
    for(i = 0; i < 8; i++)
    {
      NBTTagCompound slot = new NBTTagCompound();
      slot.setBoolean("Empty",false);
      ammo.writeToNBT(slot);
      nbt.setTag("Slot_" + i,slot);
    }
    stack.setTagCompound(nbt);
    return stack;
  }
}
