package exter.foundry.item;

import java.util.HashSet;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import exter.foundry.proxy.CommonFoundryProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemRevolver extends ItemTool
{
  public IIcon icon;
  
  @SuppressWarnings("rawtypes")
  public ItemRevolver()
  {
    super(2,ToolMaterial.IRON,new HashSet());
    setMaxDamage(800);
    setCreativeTab(FoundryTabFirearms.tab);
    setMaxStackSize(1);
    setUnlocalizedName("revolver");
    setHasSubtypes(true);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
  {
    ItemStack mat = FoundryItems.ingot_stacks.get("Steel");
    if(mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, p_82789_2_, false))
    {
      return true;
    }
    return false;
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:revolver");
  }
  

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }
  


  private MovingObjectPosition Trace(World world, EntityPlayer player)
  {
    float pitch = -player.rotationPitch;
    float yaw = -player.rotationYaw;
    Vec3 start = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight() - 0.1, player.posZ);
    float cyaw = MathHelper.cos(yaw * 0.017453292F - (float) Math.PI);
    float syaw = MathHelper.sin(yaw * 0.017453292F - (float) Math.PI);
    float cpitch = -MathHelper.cos(pitch * 0.017453292F);
    
    double distance = 150.0D;
    
    double dx = syaw * cpitch * distance;
    double dy = MathHelper.sin(pitch * 0.017453292F) * distance;
    double dz = cyaw * cpitch * distance;
    Vec3 end = start.addVector(dx,dy,dz);

    Vec3 tstart = Vec3.createVectorHelper(start.xCoord, start.yCoord, start.zCoord);
    Vec3 tend = Vec3.createVectorHelper(end.xCoord, end.yCoord, end.zCoord);
    MovingObjectPosition obj = world.func_147447_a(tstart, tend, true, false, false);
    
    
    @SuppressWarnings("unchecked")
    List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(150, 150, 100));
    double min_dist = obj != null?obj.hitVec.distanceTo(start):150;
    for(Entity ent:entities)
    {
      if(ent.canBeCollidedWith() && ent.boundingBox != null)
      {
        MovingObjectPosition ent_obj = ent.boundingBox.expand(0.3, 0.3, 0.3).calculateIntercept(start, end);
        if(ent_obj != null)
        {
          if(ent_obj.typeOfHit == MovingObjectType.BLOCK)
          {
            ent_obj.typeOfHit = MovingObjectType.ENTITY;
            ent_obj.entityHit = ent;
          }
          double d = ent_obj.hitVec.distanceTo(start);
          if(obj == null || d < min_dist)
          {
            min_dist = d;
            
            obj = ent_obj;
          }
        }
      }
    }
    if(obj != null)
    {
      obj.hitInfo = start;
    }
    return obj;
  }

  @Override
  public boolean getShareTag()
  {
    return true;
  }

  
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int p_77615_4_)
  {
    if(!player.isSneaking())
    {

      int position = stack.getTagCompound().getInteger("position");
      NBTTagCompound tag = stack.getTagCompound().getCompoundTag("Slot_" + position);
      if(!tag.getBoolean("Empty"))
      {
        ItemStack ammo_item = ItemStack.loadItemStackFromNBT(tag);
        if(!world.isRemote)
        {
          MovingObjectPosition obj = Trace(world, player);
          IFirearmRound ammo = (IFirearmRound) ammo_item.getItem();
          world.playSoundAtEntity(player, "foundry:revolver_fire", 0.9F, 1F);
          if(obj != null)
          {
            switch(obj.typeOfHit)
            {
              case BLOCK:
                Block b = world.getBlock(obj.blockX, obj.blockY, obj.blockZ);
                int m = world.getBlockMetadata(obj.blockX, obj.blockY, obj.blockZ);
                if(ammo.BreakGlass() && b.getMaterial() == Material.glass && b.getBlockHardness(world, obj.blockX, obj.blockY, obj.blockZ) < 0.4)
                {
                  world.playAuxSFXAtEntity(null, 2001, obj.blockX, obj.blockY, obj.blockZ, Block.getIdFromBlock(b)+(m<<12));
                  world.setBlockToAir(obj.blockX, obj.blockY, obj.blockZ);
                } else
                {
                  ammo.OnBulletHitBlock(ammo_item, player, (Vec3)obj.hitInfo, world, obj.blockX, obj.blockY, obj.blockZ, ForgeDirection.getOrientation(obj.sideHit));
                }
                break;
              case ENTITY:
                if(obj.entityHit instanceof EntityLiving)
                {
                  Vec3 end = Vec3.createVectorHelper(obj.entityHit.posX, obj.entityHit.posY, obj.entityHit.posZ);
                  double distance = end.distanceTo((Vec3)obj.hitInfo);
                  double base_range = ammo.GetBaseRange();
                  double falloff_range = ammo.GetFalloffRange();
                  double base_damage = ammo.GetBaseDamage();
                  double damage;
                  if(distance < base_range)
                  {
                    damage = base_damage;
                  } else if(distance > base_range + falloff_range)
                  {
                    damage = 0;
                  } else
                  {
                    damage = base_damage * (1.0f - (distance - base_range) / falloff_range);
                  }    
                  if(damage >= 1)
                  {
                    if(obj.entityHit.attackEntityFrom((new EntityDamageSourceIndirect("bullet", obj.entityHit, player)).setProjectile(), (float)damage))
                    {
                      ammo.OnBulletDamagedLivingEntity(ammo_item,(EntityLiving)obj.entityHit);
                    }
                  }
                }
                break;
              default:
                break;
            } 
          }
        } else
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
        tag.setBoolean("Empty", true);
      } else
      {
        if(!world.isRemote)
        {
          world.playSoundAtEntity(player, "random.click", 0.3F, 1.5F);
        }        
      }
      stack.getTagCompound().setInteger("position", (position + 1) % 8);
    }    
  }

  
  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
  {
    if(player.isSneaking())
    {
      if (!world.isRemote)
      {
        player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_REVOLVER, world, 0, 0, 0);
      }
    } else
    {
       player.setItemInUse(stack, getMaxItemUseDuration(stack));
    }
    return stack;
  }

  @Override
  public int getMaxItemUseDuration(ItemStack p_77626_1_)
  {
    return 72000;
  }


  @Override
  public int getItemStackLimit(ItemStack stack)
  {
    return 1;
  }

  @Override
  public EnumAction getItemUseAction(ItemStack p_77661_1_)
  {
      return EnumAction.bow;
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
  
  public void SetAmmo(ItemStack stack,int slot,ItemStack ammo)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a revolver");
    }
    if(slot < 0 || slot > 7)
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
  

  public ItemStack GetAmmo(ItemStack stack,int slot)
  {
    if(stack.getItem() != this)
    {
      throw new IllegalArgumentException("Stack is not a revolver");
    }
    if(slot < 0 || slot > 7)
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

  public ItemStack Loaded()
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
