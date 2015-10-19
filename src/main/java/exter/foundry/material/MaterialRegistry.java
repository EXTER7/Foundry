package exter.foundry.material;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import exter.foundry.api.material.IMaterialRegistry;
import exter.foundry.util.hashstack.HashableItem;

public final class MaterialRegistry implements IMaterialRegistry
{

  private HashMap<HashableItem,String> materials;
  private HashMap<HashableItem,String> types;
  
  private Set<String> material_names;
  private Set<String> type_names;
  
  public static MaterialRegistry instance = new MaterialRegistry();
  
  @SideOnly(Side.CLIENT)
  private Map<String,ItemStack> material_icons;
  @SideOnly(Side.CLIENT)
  private Map<String,ItemStack> type_icons;

  private MaterialRegistry()
  {
    materials = new HashMap<HashableItem,String>();
    types = new HashMap<HashableItem,String>();
    material_names = new HashSet<String>();
    type_names = new HashSet<String>();
  }
  
  public void initIcons()
  {
    material_icons = new HashMap<String,ItemStack>();
    type_icons = new HashMap<String,ItemStack>();
  }

  @Override
  public void registerItem(String oredict_name, String material, String type)
  {
    for(ItemStack item:OreDictionary.getOres(oredict_name))
    {
      registerItem(item, material, type);
    }
  }

  @Override
  public void registerItem(ItemStack item, String material, String type)
  {
    HashableItem hs = new HashableItem(item);
    materials.put(hs,material);
    types.put(hs,type);
    material_names.add(material);
    type_names.add(type);
  }

  @Override
  public String getMaterial(ItemStack item)
  {
    return HashableItem.getFromMap(materials,item);
  }

  @Override
  public String getType(ItemStack item)
  {
    return HashableItem.getFromMap(types,item);
  }

  @Override
  public Set<String> getMaterialNames()
  {
    return Collections.unmodifiableSet(material_names);
  }

  @Override
  public Set<String> getTypeNames()
  {
    return Collections.unmodifiableSet(type_names);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerMaterialIcon(String material, ItemStack icon)
  {
    material_icons.put(material, icon);

  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerTypeIcon(String type, ItemStack icon)
  {
    type_icons.put(type, icon);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack getMaterialIcon(String material)
  {
    return material_icons.get(material);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack getTypeIcon(String type)
  {
    return type_icons.get(type);
  }

}
