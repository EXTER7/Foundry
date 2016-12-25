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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShotgun extends ItemFirearm
{
  static public final String AMMO_TYPE = "shotgun";
  
  

  public ItemShotgun()
  {
    setUnlocalizedName("shotgun");
    setRegistryName("shotgun");
  }

  @Override
  public boolean getShareTag()
  {
    return true;
  }

  
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int count)
  {
    if(!player.isSneaking())
    {
      ItemStack round = null;
      int i;
      int shot = -1;
      for(i = 4; i >= 0; i--)
      {
        round = getAmmo(stack,i);
        if(round != null)
        {
          shot = i;
          break;
        }
      }
      if(roundMatches(round,AMMO_TYPE))
      {
        if(!world.isRemote)
        {
          world.playSound(null, player.posX, player.posY, player.posZ, FoundrySounds.sound_shotgun_fire, SoundCategory.PLAYERS, 1, 1);
        }
        shoot(round,world,player,null,6,0.35f,1.0f);
        float pitch = -player.rotationPitch;
        float yaw = -player.rotationYaw;
        float cpitch = -MathHelper.cos(pitch * 0.017453292F);          
        double look_x = MathHelper.sin(yaw * 0.017453292F - (float) Math.PI) * cpitch;
        double look_y = MathHelper.sin(pitch * 0.017453292F);
        double look_z = MathHelper.cos(yaw * 0.017453292F - (float) Math.PI) * cpitch;

        if(world.isRemote)
        {
          player.rotationPitch -= 3;
          player.motionX -= look_x * 0.1;
          player.motionY -= look_y * 0.1;
          player.motionZ -= look_z * 0.1;
        } else
        {
          EntityItem casing = new EntityItem(
              world,
              player.posX,
              player.posY + player.getEyeHeight() - 0.1,
              player.posZ,
              round.getCapability(FoundryAPI.capability_firearmround, null).getCasing().copy());
          casing.setPickupDelay(10);
          casing.motionX = -look_z * 0.2;
          casing.motionY = look_y * 0.2;
          casing.motionZ = look_x * 0.2;
          world.spawnEntityInWorld(casing);          
        }
        setAmmo(stack,shot,null);
        stack.damageItem(1, player);
      } else
      {
        if(!world.isRemote)
        {
          world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.4F, 1.5F);
        }        
      }
    }    
  }

  
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
  {
    if(player.isSneaking())
    {
      if (!world.isRemote)
      {
        player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_SHOTGUN, world, 0, 0, 0);
      }
    } else
    {
       player.setActiveHand(hand);
       if(!world.isRemote)
       {
         world.playSound(null, player.posX, player.posY, player.posZ, FoundrySounds.sound_shotgun_cock, SoundCategory.PLAYERS, 0.8f, 1);
       }
    }
    return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item,CreativeTabs tabs, @SuppressWarnings("rawtypes") List list)
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
      int i;
      for(i = 0; i < 5; i++)
      {
        NBTTagCompound ammo_tag = tag.getCompoundTag("Slot_" + i);
        if(ammo_tag == null || ammo_tag.getBoolean("Empty"))
        {
          list.add(TextFormatting.BLUE + "< Empty >");
        } else
        {
          ItemStack ammo = ItemStack.loadItemStackFromNBT(ammo_tag);
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
  
  @Override
  public void setAmmo(ItemStack stack,int slot,ItemStack ammo)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a shotgun");
    }
    if(slot < 0 || slot > 4)
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

  @Override
  public ItemStack getAmmo(ItemStack stack,int slot)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a shotgun");
    }
    if(slot < 0 || slot > 4)
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
      return ItemStack.loadItemStackFromNBT(ammo_tag);
    }
  }

  public ItemStack empty()
  {
    ItemStack stack = new ItemStack(this);
    NBTTagCompound nbt = new NBTTagCompound();
    int i;
    for(i = 0; i < 5; i++)
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
    int i;
    ItemStack ammo = new ItemStack(FoundryItems.item_shell);
    for(i = 0; i < 5; i++)
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