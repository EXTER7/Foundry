package exter.foundry.entity;

import java.util.Calendar;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemFirearm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntitySkeletonGun extends EntitySkeleton
{

  private ItemStack ammo;
  

  static private ItemStack[] LOOT_COMMON =
  {
    FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET),
    FoundryItems.Component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW),
    FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING),
    FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING_SHELL),
    FoundryItems.Component(ItemComponent.COMPONENT_AMMO_PELLET),
  };

  static private ItemStack[] LOOT_RARE =
  {
    FoundryItems.Component(ItemComponent.COMPONENT_GUN_BARREL),
    FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_DRUM),
    FoundryItems.Component(ItemComponent.COMPONENT_REVOLVER_FRAME),
    FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_PUMP),
    FoundryItems.Component(ItemComponent.COMPONENT_SHOTGUN_FRAME)
  };

  public EntitySkeletonGun(World p_i1741_1_)
  {
    super(p_i1741_1_);
    tasks.addTask(4, new EntityAIArrowAttack(this, 1.0D, 20, 210, 15.0F));
    setCurrentItemOrArmor(0,FoundryItems.item_revolver.Empty());
    ammo = new ItemStack(FoundryItems.item_round);
  }

  @Override
  public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
  {
    if(!worldObj.isRemote)
    {
      worldObj.playSoundAtEntity(this, "foundry:revolver_fire", 0.9F, 1F);
    }                 
    ItemFirearm.Shoot(ammo, worldObj, this, target, 1, 0.025f);
  }

  @Override
  protected void addRandomArmor()
  {
    super.addRandomArmor();
    this.setCurrentItemOrArmor(0, FoundryItems.item_revolver.Empty());
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
      dropItem(Items.bone, 1);
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
  protected void dropRareDrop(int p_70600_1_)
  {

  }

  @Override
  public void setSkeletonType(int type)
  {
    super.setSkeletonType(0);
  }
  

  @Override
  public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
  {
    getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));

    addRandomArmor();
    enchantEquipment();

    setCanPickUpLoot(this.rand.nextFloat() < 0.55F * worldObj.func_147462_b(posX, posY, posZ));

    if(getEquipmentInSlot(4) == null)
    {
      Calendar calendar = worldObj.getCurrentDate();

      if(calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F)
      {
        setCurrentItemOrArmor(4, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
        equipmentDropChances[4] = 0.0F;
      }
    }
    return data;
  }
}
