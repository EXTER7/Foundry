package exter.foundry.api.material;

import java.util.Set;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.item.ItemStack;

public interface IMaterialRegistry
{
  public void registerItem(String oredict_name,String material,String type);

  public void registerItem(ItemStack item,String material,String type);
  
  public String getMaterial(ItemStack item);

  public String getType(ItemStack item);
  
  public Set<String> getTypeNames();

  public Set<String> getMaterialNames();

  @SideOnly(Side.CLIENT)
  public void registerMaterialIcon(String material,ItemStack icon);

  @SideOnly(Side.CLIENT)
  public void registerTypeIcon(String type,ItemStack stack);

  @SideOnly(Side.CLIENT)
  public ItemStack getMaterialIcon(String material);

  @SideOnly(Side.CLIENT)
  public ItemStack getTypeIcon(String type);
}
