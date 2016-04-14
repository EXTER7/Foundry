package exter.foundry.entity;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemFirearm;
import exter.foundry.sound.FoundrySounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySkeletonGun extends EntitySkeleton
{

  static private final ItemStack[] LOOT_COMMON =
  {
    FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET),
    FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_HOLLOW),
    FoundryItems.component(ItemComponent.SubItem.AMMO_CASING),
    FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL),
    FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET)
  };

  static private final ItemStack[] LOOT_RARE =
  {
    FoundryItems.component(ItemComponent.SubItem.GUN_BARREL),
    FoundryItems.component(ItemComponent.SubItem.REVOLVER_DRUM),
    FoundryItems.component(ItemComponent.SubItem.REVOLVER_FRAME),
    FoundryItems.component(ItemComponent.SubItem.SHOTGUN_PUMP),
    FoundryItems.component(ItemComponent.SubItem.SHOTGUN_FRAME)
  };

  public EntitySkeletonGun(World p_i1741_1_)
  {
    super(p_i1741_1_);
    tasks.addTask(4, new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F));
  }

  @Override
  public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack item)
  {
    if(slot != EntityEquipmentSlot.MAINHAND || worldObj.isRemote)
    {
      super.setItemStackToSlot(slot, item);
    }
  }
  
  @Override
  public void setCombatTask()
  {
    
  }
  
  @Override
  public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
  {
    float damage = (float)this.worldObj.getDifficulty().getDifficultyId() * 0.1f + 0.7f;
    if(getHeldItem(EnumHand.MAIN_HAND).getItem() == FoundryItems.item_shotgun)
    {
      if(!worldObj.isRemote)
      {
        playSound(FoundrySounds.sound_shotgun_fire, 0.9F, 1F);
      }
      ItemFirearm.shoot(new ItemStack(FoundryItems.item_shell), worldObj, this, target, 6, 0.4f,damage);
    } else
    {
      if(!worldObj.isRemote)
      {
        playSound(FoundrySounds.sound_shotgun_fire, 0.9F, 1F);
      }
      ItemFirearm.shoot(new ItemStack(FoundryItems.item_round), worldObj, this, target, 1, 0.015f,damage);
    }
  }
  
  private void SetGun()
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
    SetGun();
  }

  @Override
  protected Item getDropItem()
  {
    return FoundryItems.item_round;
  }

  @Override
  protected void dropFewItems(boolean player, int looting)
  {
    int r;
    int i;

    r = rand.nextInt(1 + looting);

    for(i = 0; i < r; ++i)
    {
      this.dropItem(FoundryItems.item_round, 1);
    }

    r = rand.nextInt(3 + looting);

    for(i = 0; i < r; ++i)
    {
      dropItem(Items.BONE, 1);
    }

    r = rand.nextInt(100);
    if(r < 50 + looting * 5)
    {
      entityDropItem(LOOT_COMMON[rand.nextInt(LOOT_COMMON.length)].copy(), 0);
    }

    r = rand.nextInt(100);
    if(r < 20 + looting * 5)
    {
      entityDropItem(LOOT_RARE[rand.nextInt(LOOT_RARE.length)].copy(), 0);
    }
  }

  @Override
  public void setSkeletonType(int type)
  {
    super.setSkeletonType(0);
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

    setEquipmentBasedOnDifficulty(difficulty);
    setEnchantmentBasedOnDifficulty(difficulty);

    setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

    return livingdata;
  }
}
