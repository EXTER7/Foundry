package exter.foundry.api.firearms;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Interface for a firearm round capability.
 * Do not implement this in the item's class.
 * Use {@link exter.foundry.api.FoundryAPI.capability_firearmround FoundryAPI.capability_firearmround} with an
 * {@link net.minecraftforge.common.capability.ICapabilityProvider ICapabilityProvider} implementation to implement this in an item.
 * <br/>
 * See <a href="https://gist.github.com/williewillus/c8dc2a1e7963b57ef436c699f25a710d#if-the-item--entity--tileentity-is-from-your-mod"> this page</a>
 * for more detailed info on how implement capabilities.
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
   * @param entity Entity that the shot hit.
   * @param count How many bullets/pellets the shot hit the entity.
   */
  public void onBulletDamagedLivingEntity(EntityLivingBase entity, int count);

  /**
   * Should the round break glass.
   * @return Should the round break glass
   */
  public boolean breaksGlass();
  
  /**
   * Get the base range of the round.
   * Bullets that hit with a distance below the base range do their base damage.
   * @return The base range of the round.
   */
  public double getBaseRange();
  
  /**
   * Get the damage fall-off range of the round.
   * Bullets that hit with a distance above the base range have their damage reduced the further they hit.
   * Bullets that hit further than their base+falloff range do no damage.
   * @return The base range of the round.
   */
  public double getFalloffRange();
  
  /**
   * Get the base damage of the round.
   * Note: The base damage is applied per bullet/pellet (revolvers shoot 1 bullet, shotguns shoot 6 pellets).
   * @param entity_hit The entity the hit by the bullet/pellet.
   * @return The base damage of the round.
   */
  public double getBaseDamage(EntityLivingBase entity_hit);

  /**
   * Get the casing that the round uses.
   * @return ItemStack of the casing.
   */
  public ItemStack getCasing();
  
  /**
   * Check if the round ignores armor.
   * @return true if the round ignores armor.
   */
  public boolean ignoresArmor();
}
