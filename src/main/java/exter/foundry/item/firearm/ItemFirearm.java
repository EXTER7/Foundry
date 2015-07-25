package exter.foundry.item.firearm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.MutablePair;

import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import exter.foundry.item.FoundryItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class ItemFirearm extends ItemTool
{
  static private Random random = new Random();
  
  @SuppressWarnings("rawtypes")
  public ItemFirearm()
  {
    super(2,ToolMaterial.IRON,new HashSet());
    setMaxDamage(800);
    setCreativeTab(FoundryTabFirearms.tab);
    setMaxStackSize(1);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public final boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
  {
    ItemStack mat = FoundryItems.ingot_stacks.get("Steel");
    if(mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, p_82789_2_, false))
    {
      return true;
    }
    return false;
  }

  static private MovingObjectPosition Trace(World world, EntityPlayer player,float spread)
  {
    float pitch = -player.rotationPitch;
    float yaw = -player.rotationYaw;
    Vec3 start = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight() - 0.1, player.posZ);
    float cyaw = MathHelper.cos(yaw * 0.017453292F - (float) Math.PI);
    float syaw = MathHelper.sin(yaw * 0.017453292F - (float) Math.PI);
    float cpitch = -MathHelper.cos(pitch * 0.017453292F);
    
    double distance = 150.0D;
    
    Vec3 vspread = Vec3.createVectorHelper(
        (random.nextFloat() * 2 - 1),
        (random.nextFloat() * 2 - 1),
        (random.nextFloat() * 2 - 1)).normalize();
    
    spread = random.nextFloat() * spread;
    
    Vec3 dir = Vec3.createVectorHelper(
        syaw * cpitch + vspread.xCoord * spread,
        MathHelper.sin(pitch * 0.017453292F) + vspread.yCoord * spread,
        cyaw * cpitch + vspread.zCoord * spread).normalize();
    Vec3 end = start.addVector(dir.xCoord * distance,dir.yCoord * distance,dir.zCoord * distance);

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
        MovingObjectPosition ent_obj = ent.boundingBox.expand(0.1, 0.1, 0.1).calculateIntercept(start, end);
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
  
  static protected final void Shoot(ItemStack ammo_item, World world, EntityPlayer player, int times, float spread)
  {
    Map<EntityLiving,MutablePair<Float,Integer>> entities_hit = new HashMap<EntityLiving,MutablePair<Float,Integer>>();
    IFirearmRound ammo = (IFirearmRound) ammo_item.getItem();
    int i;
    for(i = 0; i < times; i++)
    {
      MovingObjectPosition obj = Trace(world, player,spread);
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
              if(!world.isRemote)
              {
                world.setBlockToAir(obj.blockX, obj.blockY, obj.blockZ);
              }
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
                MutablePair<Float,Integer> accum = entities_hit.get(obj.entityHit);
                if(accum == null)
                {
                  accum = new MutablePair<Float,Integer>(0.0f,0);
                  entities_hit.put((EntityLiving)obj.entityHit, accum);
                }
                accum.left += (float)damage;
                accum.right++;
              }
            }
            break;
          default:
            break;
        } 
      }
    }
    for(Map.Entry<EntityLiving, MutablePair<Float,Integer>> hit : entities_hit.entrySet())
    {
      EntityLiving en = hit.getKey();
      if(en.attackEntityFrom((new EntityDamageSourceIndirect("bullet", en, player)).setProjectile(), hit.getValue().left))
      {
        ammo.OnBulletDamagedLivingEntity(ammo_item, en,hit.getValue().right);
      }
    }
  }

  
  @Override
  public final int getMaxItemUseDuration(ItemStack p_77626_1_)
  {
    return 72000;
  }


  @Override
  public final int getItemStackLimit(ItemStack stack)
  {
    return 1;
  }

  @Override
  public final EnumAction getItemUseAction(ItemStack p_77661_1_)
  {
      return EnumAction.bow;
  }
  
  public abstract void SetAmmo(ItemStack stack,int slot,ItemStack ammo);
  
  public abstract ItemStack GetAmmo(ItemStack stack,int slot);

}
