package exter.foundry.item.firearm;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.item.FoundryItems;
import exter.foundry.proxy.CommonFoundryProxy;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemShotgun extends ItemFirearm
{
  static public final String AMMO_TYPE = "shotgun";
  
  public IIcon icon;
  
  public ItemShotgun()
  {
    setUnlocalizedName("shotgun");
  }



  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:shotgun");
  }
  

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }
  
  @Override
  public boolean getShareTag()
  {
    return true;
  }

  
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int count)
  {
    if(!player.isSneaking())
    {
      ItemStack ammo_item = null;
      int i;
      int shot = -1;
      for(i = 4; i >= 0; i--)
      {
        ammo_item = GetAmmo(stack,i);
        if(ammo_item != null)
        {
          shot = i;
          break;
        }
      }
      if(ammo_item != null)
      {
        if(!world.isRemote)
        {
          world.playSoundAtEntity(player, "foundry:shotgun_fire", 0.9F, 1F);
        }
        Shoot(ammo_item,world,player,null,6,0.4f);
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
        SetAmmo(stack,shot,null);
        stack.damageItem(1, player);
      } else
      {
        if(!world.isRemote)
        {
          world.playSoundAtEntity(player, "random.click", 0.3F, 1.5F);
        }        
      }
    }    
  }

  
  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
  {
    if(player.isSneaking())
    {
      if (!world.isRemote)
      {
        player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_SHOTGUN, world, 0, 0, 0);
      }
    } else
    {
       player.setItemInUse(stack, getMaxItemUseDuration(stack));
       if(!world.isRemote)
       {
         world.playSoundAtEntity(player, "foundry:shotgun_cock", 0.8F, 1F);
       }
    }
    return stack;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item,CreativeTabs tabs, @SuppressWarnings("rawtypes") List list)
  {
    list.add(Empty());
    list.add(Loaded());
    ItemStack test= Loaded();
    test.setItemDamage(getMaxDamage() - 3);
    list.add(test);
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      int position = stack.getTagCompound().getInteger("position");
      int i;
      for(i = 0; i < 8; i++)
      {
        int j = (i + position) % 8;
        NBTTagCompound tag = stack.getTagCompound().getCompoundTag("Slot_" + j);
        if(tag.getBoolean("Empty"))
        {
          list.add(EnumChatFormatting.BLUE + "< Empty >");
        } else
        {
          ItemStack ammo = ItemStack.loadItemStackFromNBT(tag);
          list.add(EnumChatFormatting.BLUE + ammo.getDisplayName());
        }
      }
    }
  }

  @Override
  public boolean isFull3D() {
    return true;
  }
  
  @Override
  public void SetAmmo(ItemStack stack,int slot,ItemStack ammo)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a shotgun");
    }
    if(slot < 0 || slot > 4)
    {
      throw new IllegalArgumentException("Slot index not in range: " + slot);
    }
    NBTTagCompound tag = new NBTTagCompound();
    if(ammo == null)
    {
      tag.setBoolean("Empty", true);
    } else
    {
      tag.setBoolean("Empty", false);
      ammo.writeToNBT(tag);
    }
    stack.getTagCompound().setTag("Slot_" + slot,tag);
  }

  @Override
  public ItemStack GetAmmo(ItemStack stack,int slot)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a shotgun");
    }
    if(slot < 0 || slot > 4)
    {
      throw new IllegalArgumentException("Slot index not in range: " + slot);
    }
    NBTTagCompound tag = stack.getTagCompound().getCompoundTag("Slot_" + slot);
    if(tag.getBoolean("Empty"))
    {
      return null;
    } else
    {
      return ItemStack.loadItemStackFromNBT(tag);
    }
  }

  public ItemStack Empty()
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

  public ItemStack Loaded()
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