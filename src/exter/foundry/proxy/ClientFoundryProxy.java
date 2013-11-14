package exter.foundry.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import exter.foundry.util.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.SubstanceGuiTexture;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.renderer.RendererItemContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientFoundryProxy extends CommonFoundryProxy
{
  static private ResourceLocation SUBSTANCES_TEXTURE = new ResourceLocation("foundry:textures/gui/infuser_substances.png");
  
  @Override
  public void Init()
  {
    MinecraftForgeClient.registerItemRenderer(FoundryItems.item_container.itemID, new RendererItemContainer());
    InfuserRecipeManager.instance.InitTextures();
    InfuserRecipeManager.instance.RegisterSubstanceTexture("carbon", SUBSTANCES_TEXTURE,0,0);
  }
}
