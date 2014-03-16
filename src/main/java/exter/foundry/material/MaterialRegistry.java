package exter.foundry.material;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import exter.foundry.ModFoundry;
import exter.foundry.api.material.IMaterialRegistry;
import exter.foundry.util.hashstack.HashableItemStack;

public final class MaterialRegistry implements IMaterialRegistry
{

  private HashMap<HashableItemStack,String> materials;
  private HashMap<HashableItemStack,String> types;
  
  private Set<String> material_names;
  private Set<String> type_names;
  
  public static MaterialRegistry instance = new MaterialRegistry();
  
  @SideOnly(Side.CLIENT)
  private Map<String,ItemStack> material_icons;
  @SideOnly(Side.CLIENT)
  private Map<String,ItemStack> type_icons;

  private MaterialRegistry()
  {
    materials = new HashMap<HashableItemStack,String>();
    types = new HashMap<HashableItemStack,String>();
    material_names = new HashSet<String>();
    type_names = new HashSet<String>();
  }
  
  public void InitIcons()
  {
    material_icons = new HashMap<String,ItemStack>();
    type_icons = new HashMap<String,ItemStack>();
  }

  @Override
  public void RegisterItem(ItemStack item, String material, String type)
  {
    HashableItemStack hs = new HashableItemStack(item,1);
    materials.put(hs,material);
    types.put(hs,type);
    material_names.add(material);
    type_names.add(type);
  }

  @Override
  public String GetMaterial(ItemStack item)
  {
    return materials.get(HashableItemStack.Cache(item));
  }

  @Override
  public String GetType(ItemStack item)
  {
    return types.get(HashableItemStack.Cache(item));
  }

  @Override
  public Set<String> GetMaterialNames()
  {
    return Collections.unmodifiableSet(material_names);
  }

  @Override
  public Set<String> GetTypeNames()
  {
    return Collections.unmodifiableSet(type_names);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void RegisterMaterialIcon(String material, ItemStack icon)
  {
    material_icons.put(material, icon);

  }

  @Override
  @SideOnly(Side.CLIENT)
  public void RegisterTypeIcon(String type, ItemStack icon)
  {
    type_icons.put(type, icon);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack GetMaterialIcon(String material)
  {
    return material_icons.get(material);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack GetTypeIcon(String type)
  {
    return type_icons.get(type);
  }

}
