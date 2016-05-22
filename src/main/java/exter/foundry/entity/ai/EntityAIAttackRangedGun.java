package exter.foundry.entity.ai;

import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.item.FoundryItems;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityAIAttackRangedGun extends EntityAIAttackRangedBow
{
  private final EntitySkeletonGun entity;
  public EntityAIAttackRangedGun(EntitySkeletonGun skeleton, double speedAmplifier, int delay, float maxDistance)
  {
    super(skeleton, speedAmplifier, delay, maxDistance);
    entity = skeleton;
  }

  @Override
  protected boolean isBowInMainhand()
  {
    ItemStack mainhand = entity.getHeldItemMainhand();
    if(mainhand == null)
    {
      return false;
    }
    Item item = mainhand.getItem();
    return item == FoundryItems.item_revolver || item == FoundryItems.item_shotgun;
  }

}
