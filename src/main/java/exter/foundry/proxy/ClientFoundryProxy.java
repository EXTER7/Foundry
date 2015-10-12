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
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class ClientFoundryProxy extends CommonFoundryProxy
{
  static private ResourceLocation SUBSTANCES_TEXTURE = new ResourceLocation("foundry:textures/gui/infuser_substances.png");

  
  
  @Override
  public void PreInit()
  {
    MaterialRegistry.instance.InitIcons();
    InfuserRecipeManager.instance.InitTextures();
    ModIntegration.ClientPreInit();
  }

  @Override
  public void Init()
  {
    //MinecraftForgeClient.registerItemRenderer(FoundryItems.item_container, new RendererItemContainer());
    InfuserRecipeManager.instance.RegisterSubstanceTexture("carbon", SUBSTANCES_TEXTURE, 0, 0);
    int i;
    for(i = 0; i < 15; i++)
    {
      InfuserRecipeManager.instance.RegisterSubstanceTexture("dye." + i, SUBSTANCES_TEXTURE, 8, 0, ItemDye.dyeColors[i]);
    }   

    /*
    hopper_renderer_id = RenderingRegistry.getNextAvailableRenderId();
    RenderingRegistry.registerBlockHandler(hopper_renderer_id,new RendererRefractoryHopper());
    */
    RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonGun.class, new RenderSkeleton(Minecraft.getMinecraft().getRenderManager()));

    for(BlockFoundryOre.EnumOre ore:BlockFoundryOre.EnumOre.values())
    {
      Item ore_item = Item.getItemFromBlock(FoundryBlocks.block_ore);
      String name = "foundry:" + ore.oredict_name;
      ModelBakery.addVariantName(ore_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(ore_item, ore.id, new ModelResourceLocation(name, "inventory"));
    }


    for(BlockMetal.Variant v:FoundryBlocks.block_metal1.getVariants())
    {
      Item slab_item = Item.getItemFromBlock(FoundryBlocks.block_metal1);
      String name = "foundry:block" + v.metal;
      ModelBakery.addVariantName(slab_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(slab_item, v.id, new ModelResourceLocation(name, "inventory"));
    }

    for(BlockMetal.Variant v:FoundryBlocks.block_metal2.getVariants())
    {
      Item slab_item = Item.getItemFromBlock(FoundryBlocks.block_metal2);
      String name = "foundry:block" + v.metal;
      ModelBakery.addVariantName(slab_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(slab_item, v.id, new ModelResourceLocation(name, "inventory"));
    }

    for(BlockMetalSlab.Variant v:FoundryBlocks.block_slab1.getVariants())
    {
      Item slab_item = Item.getItemFromBlock(FoundryBlocks.block_slab1);
      String name = "foundry:slab" + v.metal;
      ModelBakery.addVariantName(slab_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(slab_item, FoundryBlocks.block_slab1.getBottomVariantMeta(v), new ModelResourceLocation(name, "inventory"));
    }

    for(BlockMetalSlab.Variant v:FoundryBlocks.block_slab2.getVariants())
    {
      Item slab_item = Item.getItemFromBlock(FoundryBlocks.block_slab2);
      String name = "foundry:slab" + v.metal;
      ModelBakery.addVariantName(slab_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(slab_item, FoundryBlocks.block_slab2.getBottomVariantMeta(v), new ModelResourceLocation(name, "inventory"));
    }

    for(BlockMetalSlab.Variant v:FoundryBlocks.block_slab3.getVariants())
    {
      Item slab_item = Item.getItemFromBlock(FoundryBlocks.block_slab3);
      String name = "foundry:slab" + v.metal;
      ModelBakery.addVariantName(slab_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(slab_item, FoundryBlocks.block_slab3.getBottomVariantMeta(v), new ModelResourceLocation(name, "inventory"));
    }

    for(Map.Entry<String, BlockStairs> e:FoundryBlocks.block_metal_stairs.entrySet())
    {
      BlockStairs block = e.getValue();
      Item stairs_item = Item.getItemFromBlock(block);
      String name = "foundry:stairs" + e.getKey();
      ModelBakery.addVariantName(stairs_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(stairs_item, 0, new ModelResourceLocation(name, "inventory"));
    }

    for(BlockFoundryMachine.EnumMachine m:BlockFoundryMachine.EnumMachine.values())
    {
      Item machine_item = Item.getItemFromBlock(FoundryBlocks.block_machine);
      String name = "foundry:" + m.model;
      ModelBakery.addVariantName(machine_item, name);
      Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(machine_item, m.id, new ModelResourceLocation(name, "inventory"));
    }

//    ModelBakery.addVariantName(Item.getItemFromBlock(FoundryBlocks.block_alloy_furnace), "alloyFurnace");
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    .register(Item.getItemFromBlock(FoundryBlocks.block_alloy_furnace), 0, new ModelResourceLocation("foundry:alloyFurnace", "inventory"));

    ModIntegration.ClientInit();
  }

  @Override
  public void PostInit()
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
    ModIntegration.ClientPostInit();
  }

}
