package exter.foundry.api.material;

import java.util.Set;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.item.ItemStack;

/**
 * Registry for the Material Router.
 */
public interface IMaterialRegistry
{
  /**
   * Registers all items with in the Ore Dictionary.
   * Note: Any item added to the same Ore Dictionary entry after calling this 
   * will not be registered in the Material Registry
   * @param oredict_name Ore Dictionary name to register.
   * @param material Material name to register. Ex: "Iron".
   * @param type Type name to register. Ex: "Ingot".
   */
  public void registerItem(String oredict_name,String material,String type);

  /**
   * Registers an item.
   * @param oredict_name Ore Dictionary name to register.
   * @param material Material name to register. Ex: "Iron".
   * @param type Type name to register. Ex: "Ingot".
   */
  public void registerItem(ItemStack item,String material,String type);
  
  /**
   * Get the material the item is made of. Ex: "Iron"/"Gold".
   */
  public String getMaterial(ItemStack item);

  /**
   * Get what type item is. Ex: "Ingot"/"Dust".
   */
  public String getType(ItemStack item);
  
  /**
   * Get all registered type names.
   */
  public Set<String> getTypeNames();

  /**
   * Get all registered material names.
   */
  public Set<String> getMaterialNames();

  /**
   * Register an icon for a material in the Material Rounter's GUI..
   * @param material The material name to register.
   * @param icon Item stack the icon is taken from.
   */
  @SideOnly(Side.CLIENT)
  public void registerMaterialIcon(String material,ItemStack icon);

  /**
   * Register an icon for a type in the Material Rounter's GUI..
   * @param type The type name to register.
   * @param icon Item stack the icon is taken from.
   */
  @SideOnly(Side.CLIENT)
  public void registerTypeIcon(String type,ItemStack stack);

  /**
   * Get the item stack used for the material icon in the Material Rounter's GUI..
   */
  @SideOnly(Side.CLIENT)
  public ItemStack getMaterialIcon(String material);

  /**
   * Get the item stack used for the type icon in the Material Rounter's GUI..
   */
  @SideOnly(Side.CLIENT)
  public ItemStack getTypeIcon(String type);
}
