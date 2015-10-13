package exter.foundry.proxy;

import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.BlockMetal;
import exter.foundry.block.BlockMetalSlab;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.integration.ModIntegration;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemIngot;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class ClientFoundryProxy extends CommonFoundryProxy
{
  static private ResourceLocation SUBSTANCES_TEXTURE = new ResourceLocation("foundry:textures/gui/infuser_substances.png");

  private void registerItemModel(Block block,String name)
  {
    registerItemModel(Item.getItemFromBlock(block), name);
  }

  private void registerItemModel(Block block,String name,int meta)
  {
    registerItemModel(Item.getItemFromBlock(block), name, meta);
  }

  private void registerItemModel(Item item,String name)
  {
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    .register(item, 0,
      new ModelResourceLocation("foundry:" + name, "inventory"));
  }

  private void registerItemModel(Item item,String name,int meta)
  {
    name = "foundry:" + name;
    ModelBakery.addVariantName(item, name);
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    .register(item, meta, new ModelResourceLocation(name, "inventory"));
  }

  @Override
  public void preInit()
  {
    MaterialRegistry.instance.InitIcons();
    InfuserRecipeManager.instance.InitTextures();
    ModIntegration.clientPreInit();
  }

  @Override
  public void init()
  {
    //MinecraftForgeClient.registerItemRenderer(FoundryItems.item_container, new RendererItemContainer());
    InfuserRecipeManager.instance.RegisterSubstanceTexture("carbon", SUBSTANCES_TEXTURE, 0, 0);
    int i;
    for(i = 0; i < 15; i++)
    {
      InfuserRecipeManager.instance.RegisterSubstanceTexture("dye." + i, SUBSTANCES_TEXTURE, 8, 0, ItemDye.dyeColors[i]);
    }   

    RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonGun.class, new RenderSkeleton(Minecraft.getMinecraft().getRenderManager()));

    for(BlockFoundryOre.EnumOre ore:BlockFoundryOre.EnumOre.values())
    {
      registerItemModel(FoundryBlocks.block_ore,ore.oredict_name, ore.id);
    }


    for(BlockMetal block:FoundryBlocks.block_metal)
    {
      for(BlockMetal.Variant v:block.getVariants())
      {
        registerItemModel(block,"block" + v.metal, v.id);
      }
    }

    for(BlockMetalSlab block:FoundryBlocks.block_slab)
    {
      for(BlockMetalSlab.Variant v:block.getVariants())
      {
        registerItemModel(block,"slab" + v.metal, block.getBottomVariantMeta(v));
      }
    }

    for(Map.Entry<String, BlockStairs> e:FoundryBlocks.block_metal_stairs.entrySet())
    {
      registerItemModel(e.getValue(),"stairs" + e.getKey());
    }

    for(BlockFoundryMachine.EnumMachine m:BlockFoundryMachine.EnumMachine.values())
    {
      registerItemModel(FoundryBlocks.block_machine,m.model,m.id);
    }

    registerItemModel(FoundryBlocks.block_alloy_furnace,"alloyFurnace");
    registerItemModel(FoundryBlocks.block_refractory_casing,"casing");
    registerItemModel(FoundryBlocks.block_refractory_hopper,"refractoryHopper");

    for(i = 0; i < ItemIngot.OREDICT_NAMES.length; i++)
    {
      registerItemModel(FoundryItems.item_ingot,ItemIngot.OREDICT_NAMES[i], i);
    }

    for(i = 0; i < ItemComponent.REGISTRY_NAMES.length; i++)
    {
      registerItemModel(FoundryItems.item_component,ItemComponent.REGISTRY_NAMES[i], i);
    }

    for(i = 0; i < ItemMold.REGISTRY_NAMES.length; i++)
    {
      registerItemModel(FoundryItems.item_mold,ItemMold.REGISTRY_NAMES[i], i);
    }

    ModIntegration.clientInit();
  }
  

  @Override
  public void postInit()
  {
    for(OreDictMaterial material : OreDictMaterial.MATERIALS)
    {
      List<ItemStack> ores = OreDictionary.getOres(material.default_prefix + material.suffix);
      if(ores.size() > 0)
      {
        MaterialRegistry.instance.registerMaterialIcon(material.suffix, ores.get(0));
      } else
      {
        for(OreDictType type : OreDictType.TYPES)
        {
          ores = OreDictionary.getOres(type.prefix + material.suffix);
          if(ores.size() > 0)
          {
            MaterialRegistry.instance.registerMaterialIcon(material.suffix, ores.get(0));
            break;
          }
        }
      }
    }

    for(OreDictType type : OreDictType.TYPES)
    {
      List<ItemStack> ores = OreDictionary.getOres(type.prefix + type.default_suffix);
      if(ores.size() > 0)
      {
        MaterialRegistry.instance.registerTypeIcon(type.name, ores.get(0));
      } else
      {
        for(OreDictMaterial material : OreDictMaterial.MATERIALS)
        {
          ores = OreDictionary.getOres(type.prefix + material.suffix);
          if(ores.size() > 0)
          {
            MaterialRegistry.instance.registerTypeIcon(type.name, ores.get(0));
            break;
          }
        }
      }
    }
    ModIntegration.clientPostInit();
  }

}
