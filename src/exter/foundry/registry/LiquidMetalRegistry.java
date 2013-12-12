package exter.foundry.registry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.api.registry.IFluidRegistry;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.BlockMetal;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemRefractoryFluidContainer;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.AlloyRecipe;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.renderer.RendererItemContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Utility class for registering a metal's corresponding block, items, fluid, and recipes.
 */
public class LiquidMetalRegistry implements IFluidRegistry
{
  private Map<String,Fluid> registry;
  
  static public LiquidMetalRegistry instance = new LiquidMetalRegistry();
  
  private LiquidMetalRegistry()
  {
    registry = new HashMap<String,Fluid>();
    MinecraftForge.EVENT_BUS.register(this);
  }

  /**
   * Helper method to register a metal's fluid, block, melting, and casting.
   * @param config Forge Configuration file.
   * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
   * @param default_container_id Default item id of the fluid container.
   */
  public void RegisterLiquidMetal(Configuration config,String metal_name,int temperature,int luminosity)
  {
    int i;
    
    int block_id = config.getBlock("liquid" + metal_name, FoundryBlocks.GetNextID()).getInt();

    Fluid fluid = new Fluid("liquid" + metal_name).setTemperature(temperature).setLuminosity(luminosity).setDensity(2000);
    FluidRegistry.registerFluid(fluid);

    List<ItemStack> ore_list = OreDictionary.getOres("block" + metal_name);
    ItemStack solid = FoundryBlocks.block_stacks.get(metal_name);
    Block liquid_block = new BlockLiquidMetal(block_id, fluid, Material.lava,"liquid" + metal_name,solid!=null?solid.itemID:-1,solid!=null?solid.getItemDamage():-1);
    liquid_block.setUnlocalizedName("liquid" + metal_name);
    LanguageRegistry.addName(liquid_block, "Liquid " + metal_name);
    GameRegistry.registerBlock(liquid_block, "liquid" + metal_name);

    fluid.setBlockID(liquid_block);

    FoundryUtils.RegisterBasicMeltingRecipes(metal_name,fluid);
    
    registry.put(metal_name, fluid);
  }
  
  @ForgeSubscribe
  @SideOnly(Side.CLIENT)
  public void textureHook(TextureStitchEvent.Post event)
  {
    if(event.map.textureType == 0)
    {
      for(Fluid fluid:registry.values())
      {
        fluid.setIcons(Block.blocksList[fluid.getBlockID()].getBlockTextureFromSide(1));
      }
    }
  }
 
  @Override
  public Fluid GetFluid(String name)
  {
    return registry.get(name);
  }
  
  public Set<String> GetFluidNames()
  {
    return Collections.unmodifiableSet(registry.keySet());
  }
}
