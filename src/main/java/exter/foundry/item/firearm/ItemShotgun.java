package exter.foundry.item.firearm;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.item.FoundryItems;
import exter.foundry.proxy.CommonFoundryProxy;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
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
      ItemStack round = null;
      int i;
      int shot = -1;
      for(i = 4; i >= 0; i--)
      {
        round = GetAmmo(stack,i);
        if(round != null)
        {
          shot = i;
          break;
        }
      }
      if(RoundMatches(round,AMMO_TYPE))
      {
        if(!world.isRemote)
        {
          world.playSoundAtEntity(player, "foundry:shotgun_fire", 1F, 1F);
        }
        Shoot(round,world,player,null,6,0.35f,1.0f);
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
              ((IFirearmRound)(round.getItem())).GetCasing(round).copy());
          casing.delayBeforeCanPickup = 10;
          casing.motionX = -look_z * 0.2;
          casing.motionY = look_y * 0.2;
          casing.motionZ = look_x * 0.2;
          world.spawnEntityInWorld(casing);          
        }
        SetAmmo(stack,shot,null);
        stack.damageItem(1, player);
      } else
      {
        if(!world.isRemote)
        {
          world.playSoundAtEntity(player, "random.click", 0.4F, 1.5F);
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
      for(i = 0; i < 5; i++)
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
  public boolean isFull3D()
  {
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