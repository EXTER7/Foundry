package exter.foundry.proxy;

import exter.foundry.item.FoundryItems;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.renderer.RendererItemContainer;
import net.minecraft.item.ItemDye;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientFoundryProxy extends CommonFoundryProxy
{
  static private ResourceLocation SUBSTANCES_TEXTURE = new ResourceLocation("foundry:textures/gui/infuser_substances.png");
  
  @Override
  public void Init()
  {
    MinecraftForgeClient.registerItemRenderer(FoundryItems.item_container, new RendererItemContainer());
    InfuserRecipeManager.instance.InitTextures();
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
}
