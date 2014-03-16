package exter.foundry.proxy;

import java.util.List;

import exter.foundry.item.FoundryItems;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.renderer.RendererItemContainer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;

public class ClientFoundryProxy extends CommonFoundryProxy
{
  static private ResourceLocation SUBSTANCES_TEXTURE = new ResourceLocation("foundry:textures/gui/infuser_substances.png");
  
  @Override
  public void PreInit()
  {
    MaterialRegistry.instance.InitIcons();
    InfuserRecipeManager.instance.InitTextures();
  }

  @Override
  public void Init()
  {
    MinecraftForgeClient.registerItemRenderer(FoundryItems.item_container, new RendererItemContainer());
    InfuserRecipeManager.instance.RegisterSubstanceTexture("carbon", SUBSTANCES_TEXTURE,0,0);
    int i;
    for(i = 0; i < ItemDye.field_150921_b/*icon_names*/.length; i++)
    {
      InfuserRecipeManager.instance.RegisterSubstanceTexture(
          "dye." + ItemDye.field_150921_b/*icon_names*/[i],
          SUBSTANCES_TEXTURE,
          8,0,
          ItemDye.field_150922_c/*colors*/[i]);
    }
  }
  
  @Override
  public void PostInit()
  {
    for(OreDictMaterial material:OreDictMaterial.MATERIALS)
    {
      List<ItemStack> ores = OreDictionary.getOres(material.default_prefix + material.suffix);
      if(ores.size() > 0)
      {
        MaterialRegistry.instance.RegisterMaterialIcon(material.suffix, ores.get(0));
      }
    }

    for(OreDictType type:OreDictType.TYPES)
    {
      List<ItemStack> ores = OreDictionary.getOres(type.prefix + type.default_suffix);
      if(ores.size() > 0)
      {
        MaterialRegistry.instance.RegisterTypeIcon(type.name,ores.get(0));
      }
    }
}

}
