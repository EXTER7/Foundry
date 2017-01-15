package exter.foundry.entity;

import exter.foundry.entity.ai.EntityAIAttackRangedGun;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.firearm.ItemFirearm;
import exter.foundry.sound.FoundrySounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySkeletonGun extends EntitySkeleton
{

  public EntitySkeletonGun(World world)
  {
    super(world);
    EntityAIAttackRangedGun task = new EntityAIAttackRangedGun(this, 1.0D, 20, 15.0F);
    task.setAttackCooldown(30);
    tasks.addTask(4, task);
  }

  @Override
  public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack item)
  {
    if(slot != EntityEquipmentSlot.MAINHAND || world.isRemote)
    {
      super.setItemStackToSlot(slot, item);
    }
  }
  
  @Override
  public ResourceLocation getLootTable()
  {
    return new ResourceLocation("foundry", "gun_skeleton");
  }
  
  @Override
  public void setCombatTask()
  {
    
  }
  
  @Override
  public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
  {
    float damage = (float)this.world.getDifficulty().getDifficultyId() * 0.1f + 0.7f;
    if(getHeldItem(EnumHand.MAIN_HAND).getItem() == FoundryItems.item_shotgun)
    {
      if(!world.isRemote)
      {
        playSound(FoundrySounds.sound_shotgun_fire, 0.9F, 1F);
      }
      ItemFirearm.shoot(new ItemStack(FoundryItems.item_shell), world, this, target, 6, 0.4f,damage);
    } else
    {
      if(!world.isRemote)
      {
        playSound(FoundrySounds.sound_revolver_fire, 0.9F, 1F);
      }
      ItemFirearm.shoot(new ItemStack(FoundryItems.item_round), world, this, target, 1, 0.015f,damage);
    }
  }
  
  private void setGun()
  {
    if(rand.nextInt(100) < 10)
    {
      super.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, FoundryItems.item_shotgun.empty());
    } else
    {
      super.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, FoundryItems.item_revolver.empty());
    }
  }

  @Override
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
  {
    super.setEquipmentBasedOnDifficulty(difficulty);
    setGun();
  }

 
  @Override
  public boolean canPickUpLoot()
  {
    return false;
  }

  @Override
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
  {
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));

    if (rand.nextFloat() < 0.05F)
    {
      setLeftHanded(true);
    } else
    {
      setLeftHanded(false);
    }
    
    setEquipmentBasedOnDifficulty(difficulty);
    setEnchantmentBasedOnDifficulty(difficulty);

    setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

    return livingdata;
  }
}
