package exter.foundry.api.material;

import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

public interface IMaterialRegistry
{
  public void RegisterItem(ItemStack item,String material,String type);
  
  public String GetMaterial(ItemStack item);

  public String GetType(ItemStack item);
  
  public Set<String> GetTypeNames();

  public Set<String> GetMaterialNames();

  @SideOnly(Side.CLIENT)
  public void RegisterMaterialIcon(String material,ItemStack icon);

  @SideOnly(Side.CLIENT)
  public void RegisterTypeIcon(String type,ItemStack stack);

  @SideOnly(Side.CLIENT)
  public ItemStack GetMaterialIcon(String material);

  @SideOnly(Side.CLIENT)
  public ItemStack GetTypeIcon(String type);
}
