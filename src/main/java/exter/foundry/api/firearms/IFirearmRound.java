package exter.foundry.api.firearms;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * API for a firearm round. This interface is implemented in the item's class.
 */
public interface IFirearmRound
{
  /**
   * Determine which gun this round fits.
   * Possible values are "revolver" for the Revolver, or "shotgun" for the shotgun.
   * @return The type of gun this ammo is used for.
   */
  public String getRoundType();
  
  /**
   * Called when a bullet hits a block.
   * @param round Round that was fired.
   * @param shooter Player or mob, that made the shot.
   * @param from Location the shot originated.
   * @param world World the shot hit.
   * @param pos coordinates of the block hit.
   * @param side Side of the block hit.
   */
  public void onBulletHitBlock(EntityLivingBase shooter, Vec3d from, World world, BlockPos pos, EnumFacing side);

  /**
   * Called after a shot hit and damaged an entity
   * This method can be used to apply potion effects in special rounds.
   * @param round Round that was fired.
   * @param entity Entity that the shot hit.
   * @param count How many bullets/pellets the shot hit the entity.
   */
  public void onBulletDamagedLivingEntity(EntityLivingBase entity, int count);

  /**
   * Should the round break glass.
   * @param round The Round item.
   * @return Should the round break glass
   */
  public boolean breaksGlass();
  
  /**
   * Get the base range of the round.
   * Bullets that hit with a distance below the base range do their base damage.
   * @param round The Round item.
   * @return The base range of the round.
   */
  public double getBaseRange();
  
  /**
   * Get the damage fall-off range of the round.
   * Bullets that hit with a distance above the base range have their damage reduced the further they hit.
   * Bullets that hit further than their base+falloff range do no damage.
   * @param round The Round item.
   * @return The base range of the round.
   */
  public double getFalloffRange();
  
  /**
   * Get the base damage of the round.
   * Note: The base damage is applied per bullet/pellet (revolvers shoot 1 bullet, shotguns shoot 6 pellets).
   * @param round The Round item.
   * @param entity_hit The entity the hit by the bullet/pellet.
   * @return The base damage of the round.
   */
  public double getBaseDamage(EntityLivingBase entity_hit);

  /**
   * Get the casing that the round uses.
   * @param round The Round item.
   * @return ItemStack of the casing.
   */
  public ItemStack getCasing();
  
  /**
   * Check if the round ignores armor.
   * @param round The Round item.
   * @return true if the round ignores armor.
   */
  public boolean ignoresArmor();
}
