package exter.foundry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.container.FoundryContainerManager;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.block.BlockAlloyMixer;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.BlockMetalCaster;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.BlockInductionCrucibleFurnace;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.integration.ModIntegration;
import exter.foundry.integration.ModIntegrationBuildcraft;
import exter.foundry.integration.ModIntegrationForestry;
import exter.foundry.integration.ModIntegrationIC2;
import exter.foundry.integration.ModIntegrationRailcraft;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemFoundryContainer;
import exter.foundry.item.ItemIngot;
import exter.foundry.item.ItemMold;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.recipes.AlloyRecipe;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.InfuserSubstance;
import exter.foundry.recipes.InfuserSubstanceRecipe;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.AlloyRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.util.FoundryContainerHandler;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.util.LiquidMetalRegistry;
import exter.foundry.worldgen.FoundryWorldGenerator;
import exter.foundry.worldgen.WordGenOre;

@Mod(
  modid = ModFoundry.MODID,
  name = ModFoundry.MODNAME,
  version = "0.1.0",
  dependencies = "required-after:Forge@[9.11.1.951,)"
)
@NetworkMod(
  channels = { ModFoundry.CHANNEL },
  clientSideRequired = true,
  serverSideRequired = true,
  packetHandler = FoundryPacketHandler.class
)
public class ModFoundry
{
  public static final String MODID = "foundry";
  public static final String MODNAME = "Foundry";

  public static final String CHANNEL = "EXTER_Foundry";

  @Instance(MODID)
  public static ModFoundry instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(
    clientSide = "exter.foundry.proxy.ClientFoundryProxy",
    serverSide = "exter.foundry.proxy.CommonFoundryProxy"
  )
  public static CommonFoundryProxy proxy;


  private static boolean wordgen_copper;
  private static boolean wordgen_tin;
  private static boolean wordgen_zinc;
  private static boolean wordgen_nickel;
  private static boolean wordgen_silver;
  private static boolean wordgen_lead;


  public static Logger log = Logger.getLogger(MODNAME);

  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    int i;
    FoundryRecipes.melting = MeltingRecipeManager.instance;
    FoundryRecipes.casting = CastingRecipeManager.instance;
    FoundryRecipes.alloy = AlloyRecipeManager.instance;
    FoundryRecipes.infuser = InfuserRecipeManager.instance;
    FoundryContainerManager.handler = FoundryContainerHandler.instance;


    OreDictionary.registerOre("ingotIron", Item.ingotIron);
    OreDictionary.registerOre("blockIron", Block.blockIron);
    OreDictionary.registerOre("ingotGold", Item.ingotGold);
    OreDictionary.registerOre("blockGold", Block.blockGold);
    OreDictionary.registerOre("nuggetGold", Item.goldNugget);

    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    FoundryItems.RegisterItems(config);
    FoundryBlocks.RegisterBlocks(config);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Iron", 1850, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Gold", 1350, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Copper", 1400, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Tin", 550, 7);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Bronze", 1400, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Electrum", 1350, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Invar", 1850, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Nickel", 1750, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Zinc", 700, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Brass", 1400, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Silver", 1250, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Steel", 1850, 15);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Lead", 650, 1);
    wordgen_copper = config.get("worldgen", "copper", true).getBoolean(true);
    wordgen_tin = config.get("worldgen", "tin", true).getBoolean(true);
    wordgen_zinc = config.get("worldgen", "zinc", true).getBoolean(true);
    wordgen_nickel = config.get("worldgen", "nickel", true).getBoolean(true);
    wordgen_silver = config.get("worldgen", "silver", true).getBoolean(true);
    wordgen_lead = config.get("worldgen", "lead", true).getBoolean(true);

    config.save();



    Fluid liquid_copper = LiquidMetalRegistry.GetMetal("Copper").fluid;
    Fluid liquid_tin = LiquidMetalRegistry.GetMetal("Tin").fluid;
    Fluid liquid_zinc = LiquidMetalRegistry.GetMetal("Zinc").fluid;
    Fluid liquid_silver = LiquidMetalRegistry.GetMetal("Silver").fluid;
    Fluid liquid_gold = LiquidMetalRegistry.GetMetal("Gold").fluid;
    Fluid liquid_nickel = LiquidMetalRegistry.GetMetal("Nickel").fluid;
    Fluid liquid_iron = LiquidMetalRegistry.GetMetal("Iron").fluid;
    Fluid liquid_electrum = LiquidMetalRegistry.GetMetal("Electrum").fluid;
    Fluid liquid_invar = LiquidMetalRegistry.GetMetal("Invar").fluid;
    Fluid liquid_bronze = LiquidMetalRegistry.GetMetal("Bronze").fluid;
    Fluid liquid_brass = LiquidMetalRegistry.GetMetal("Brass").fluid;
    Fluid liquid_steel = LiquidMetalRegistry.GetMetal("Steel").fluid;

    
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_bronze,12), new FluidStack(liquid_copper,9), new FluidStack(liquid_tin,3));
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_brass,12), new FluidStack(liquid_copper,9), new FluidStack(liquid_zinc,3));
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_invar,12), new FluidStack(liquid_iron,8), new FluidStack(liquid_nickel,4));
    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_electrum,12), new FluidStack(liquid_gold,6), new FluidStack(liquid_silver,6));

    ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
    ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CHESTPLATE);
    ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PICKAXE);
    ItemStack mold_block = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BLOCK);
    ItemStack mold_axe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_AXE);
    ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SHOVEL);
    ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_HOE);
    ItemStack mold_sword = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SWORD);
    ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_LEGGINGS);
    ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_HELMET);
    ItemStack mold_boots = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BOOTS);
    ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
    ItemStack mold_cable_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CABLE_IC2);
    ItemStack mold_casing_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CASING_IC2);
    ItemStack mold_slab = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SLAB);
    ItemStack mold_stairs = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_STAIRS);
    ItemStack extra_sticks1 = new ItemStack(Item.stick,1);
    ItemStack extra_sticks2 = new ItemStack(Item.stick,2);
    
    
    CastingRecipeManager.instance.AddMold(mold_ingot);
    CastingRecipeManager.instance.AddMold(mold_chestplate);
    CastingRecipeManager.instance.AddMold(mold_pickaxe);
    CastingRecipeManager.instance.AddMold(mold_block);
    CastingRecipeManager.instance.AddMold(mold_axe);
    CastingRecipeManager.instance.AddMold(mold_shovel);
    CastingRecipeManager.instance.AddMold(mold_hoe);
    CastingRecipeManager.instance.AddMold(mold_sword);
    CastingRecipeManager.instance.AddMold(mold_leggings);
    CastingRecipeManager.instance.AddMold(mold_helmet);
    CastingRecipeManager.instance.AddMold(mold_boots);
    CastingRecipeManager.instance.AddMold(mold_gear);
    CastingRecipeManager.instance.AddMold(mold_cable_ic2);
    CastingRecipeManager.instance.AddMold(mold_casing_ic2);
    CastingRecipeManager.instance.AddMold(mold_slab);
    CastingRecipeManager.instance.AddMold(mold_stairs);

    //Ingot casting recipes.
    for(Entry<String,ItemStack> entry:FoundryItems.ingot_stacks.entrySet())
    {
      CastingRecipeManager.instance.AddRecipe(
          entry.getValue(),
          new FluidStack(
              LiquidMetalRegistry.GetMetal(entry.getKey()).fluid,
              FoundryRecipes.FLUID_AMOUNT_INGOT),
          mold_ingot, null);
    }
    
    //Metal block casting recipes.
    for(Entry<String,ItemStack> entry:FoundryBlocks.block_stacks.entrySet())
    {
      CastingRecipeManager.instance.AddRecipe(
          entry.getValue(),
          new FluidStack(
              LiquidMetalRegistry.GetMetal(entry.getKey()).fluid,
              FoundryRecipes.FLUID_AMOUNT_BLOCK),
          mold_ingot, null);
    }
    
    //Metal slab casting recipes
    for(Entry<String,ItemStack> entry:FoundryBlocks.slab_stacks.entrySet())
    {
      ItemStack stack = entry.getValue();
      FluidStack fluid = new FluidStack(
          LiquidMetalRegistry.GetMetal(entry.getKey()).fluid,
          FoundryRecipes.FLUID_AMOUNT_BLOCK / 2);

      CastingRecipeManager.instance.AddRecipe(stack, fluid, mold_slab, null);
      MeltingRecipeManager.instance.AddRecipe(stack, fluid);
    }

    //Metal stairs casting recipes
    for(i = 0; i < FoundryBlocks.block_metal_stairs.length; i++)
    {
      FoundryBlocks.MetalStair mr = FoundryBlocks.STAIRS_BLOCKS[i];
      ItemStack stack = new ItemStack(FoundryBlocks.block_metal_stairs[i]);
      FluidStack fluid = new FluidStack(
          LiquidMetalRegistry.GetMetal(mr.metal).fluid,
          FoundryRecipes.FLUID_AMOUNT_BLOCK * 3 / 4);
      
      CastingRecipeManager.instance.AddRecipe(stack, fluid, mold_stairs, null);
      MeltingRecipeManager.instance.AddRecipe(stack, fluid);
    }

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.plateIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.plateGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.pickaxeIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.pickaxeGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.axeIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.axeGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.shovelIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.shovelGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.swordIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.swordGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.hoeIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.hoeGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.legsIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.legsGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.helmetIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.helmetGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);

    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.bootsIron,1,0), new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
    CastingRecipeManager.instance.AddRecipe(new ItemStack(Item.bootsGold,1,0), new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

    CastingRecipeManager.instance.AddRecipe("gearIron", new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearGold", new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearCopper", new FluidStack(liquid_copper,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearTin", new FluidStack(liquid_tin,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearBronze", new FluidStack(liquid_bronze,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearBrass", new FluidStack(liquid_brass,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearInvar", new FluidStack(liquid_invar,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipeManager.instance.AddRecipe("gearSteel", new FluidStack(liquid_steel,FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_gear, null);

    
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",36, new ItemStack(Item.coal,1,0), 300);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",6, new ItemStack(Item.coal,1,1), 600);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",324, new ItemStack(Block.coalBlock,1), 2400);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",36, "dustCoal", 200);
    InfuserRecipeManager.instance.AddSubstanceRecipe("carbon",6, "dustCharcoal", 400);

    InfuserRecipeManager.instance.AddRecipe(new FluidStack(liquid_steel,3), new FluidStack(liquid_iron,3), "carbon", 2);
    
    NetworkRegistry.instance().registerGuiHandler(this, proxy);
  }
  
 
  @EventHandler
  public void load(FMLInitializationEvent event)
  {
    int i;
    log.setParent(FMLLog.getLogger());

    ModIntegration.RegisterIntegration(new ModIntegrationIC2("ic2"));
    ModIntegration.RegisterIntegration(new ModIntegrationBuildcraft("buildcraft"));
    ModIntegration.RegisterIntegration(new ModIntegrationForestry("forestry"));
    ModIntegration.RegisterIntegration(new ModIntegrationRailcraft("railcraft"));
    
    GameRegistry.registerTileEntity(TileEntityInductionCrucibleFurnace.class, "Foundry_MeltingFurnace");
    GameRegistry.registerTileEntity(TileEntityMetalCaster.class, "Foundry_MetalCaster");
    GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, "Foundry_AlloyMixer");
    GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, "Foundry_MetalInfuser");

    ItemStack brick_stack = new ItemStack(Item.brick);
    ItemStack iron_stack = new ItemStack(Item.ingotIron);
    ItemStack redstone_stack = new ItemStack(Item.redstone);
    ItemStack cobble_stack = new ItemStack(Block.cobblestone, 1, -1);
    ItemStack furnace_stack = new ItemStack(Block.furnaceIdle);
    ItemStack clay_stack = new ItemStack(Item.clay);
    ItemStack sand_stack = new ItemStack(Block.sand,1,-1);
    ItemStack clayblock_stack = new ItemStack(Block.blockClay, 1, -1);
    ItemStack brickblock_stack = new ItemStack(Block.brick, 1, -1);
    ItemStack crucible_stack = new ItemStack(FoundryBlocks.block_foundry_crucible);
    ItemStack piston_stack = new ItemStack(Block.pistonBase);
    ItemStack goldnugget_stack = new ItemStack(Item.goldNugget);
    ItemStack stick_stack = new ItemStack(Item.stick);
    ItemStack foundryclay_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundryclay8_stack = new ItemStack(FoundryItems.item_component,8,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundrybrick_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYBRICK);
    ItemStack blankmold_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD);
    ItemStack heatingcoil_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_HEATINGCOIL);
    ItemStack glasspane_stack = new ItemStack(Block.thinGlass);
    ItemStack emptycontainer_stack = FoundryContainerHandler.instance.FromFluidStack(null);
    
    GameRegistry.addRecipe(foundryclay8_stack,
        "CCC",
        "CSC",
        "CCC",
        'C', clay_stack,
        'S', sand_stack);

    GameRegistry.addShapelessRecipe(foundryclay8_stack,clayblock_stack, clayblock_stack, sand_stack);

    FurnaceRecipes.smelting().addSmelting(FoundryItems.item_component.itemID, ItemFoundryComponent.COMPONENT_FOUNDRYCLAY,
        foundrybrick_stack, 0.0f);

    GameRegistry.addRecipe(blankmold_stack,
        "CC",
        'C', foundryclay_stack);


    GameRegistry.addRecipe(new ShapedOreRecipe(heatingcoil_stack,
        "RCR",
        "NGN",
        "RCR",
        'C', "ingotCopper",
        'N', "ingotNickel",
        'G', goldnugget_stack,
        'R', redstone_stack));

    GameRegistry.addRecipe(crucible_stack, 
        "IBI",
        "B B",
        "IBI",
        'I', iron_stack,
        'B', foundrybrick_stack);
    
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FoundryBlocks.block_induction_crucible_furnace),
        "IFI",
        "HCH",
        "HRH",
        'F', furnace_stack, 
        'I', "ingotCopper", 
        'C', crucible_stack,
        'R', redstone_stack,
        'H', heatingcoil_stack));
    
    GameRegistry.addRecipe(new ItemStack(FoundryBlocks.block_metal_caster),
        " R ",
        "ICI",
        "IPI",
        'I', iron_stack, 
        'P', piston_stack,
        'C', crucible_stack,
        'R', redstone_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FoundryBlocks.block_metal_infuser),
        "IRI",
        "GCG",
        "HRH",
        'I', iron_stack, 
        'R', redstone_stack, 
        'B', foundrybrick_stack,
        'C', crucible_stack,
        'G', "gearStone",
        'H', heatingcoil_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FoundryBlocks.block_alloy_mixer),
        "GIG",
        "GCG",
        "IRI",
        'I', iron_stack, 
        'C', crucible_stack,
        'R', redstone_stack,
        'G', "gearStone"));

    GameRegistry.addRecipe(new ShapedOreRecipe(emptycontainer_stack,
        " T ",
        "BGB",
        " T ",
        'T', "ingotTin", 
        'B', foundrybrick_stack,
        'G', glasspane_stack));

    //Mold crafting with vanilla items
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Block.planks,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Block.stone,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, new ItemStack(Item.brick));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateGold,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateDiamond,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetGold,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetDiamond,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeDiamond));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordWood));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordStone));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordIron));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordGold));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordDiamond));
    for(i = 0; i < Block.blocksList.length; i++)
    {
      Block block = Block.blocksList[i];
      if(block instanceof BlockStairs)
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_STAIRS_CLAY, new ItemStack(block,1,-1));
      } else if(block instanceof BlockHalfSlab && !block.isOpaqueCube())
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SLAB_CLAY, new ItemStack(block,1,-1));
      }
    }
    
    //Gear Mold crafting recipes.
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearWood");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearStone");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearIron");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearGold");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearDiamond");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearCopper");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearTin");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearBronze");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearBrass");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearInvar");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearSteel");

    //Ingot and block mold crafting recipes
    for(String name:OreDictionary.getOreNames())
    {
      if(name.startsWith("ingot"))
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, name);
      } else if(name.startsWith("block"))
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, name);
      }
    }

    //Ore -> ingot furnace recipes
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_COPPER,ItemIngot.INGOT_COPPER);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_TIN,ItemIngot.INGOT_TIN);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_ZINC,ItemIngot.INGOT_ZINC);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_NICKEL,ItemIngot.INGOT_NICKEL);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_SILVER,ItemIngot.INGOT_SILVER);
    
    //Clay mold furnace recipes
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_BLOCK_CLAY,ItemMold.MOLD_BLOCK);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CHESTPLATE_CLAY,ItemMold.MOLD_CHESTPLATE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_INGOT_CLAY,ItemMold.MOLD_INGOT);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_PICKAXE_CLAY,ItemMold.MOLD_PICKAXE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_AXE_CLAY,ItemMold.MOLD_AXE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SHOVEL_CLAY,ItemMold.MOLD_SHOVEL);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SWORD_CLAY,ItemMold.MOLD_SWORD);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HOE_CLAY,ItemMold.MOLD_HOE);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_LEGGINGS_CLAY,ItemMold.MOLD_LEGGINGS);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HELMET_CLAY,ItemMold.MOLD_HELMET);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_BOOTS_CLAY,ItemMold.MOLD_BOOTS);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_GEAR_CLAY,ItemMold.MOLD_GEAR);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CABLE_IC2_CLAY,ItemMold.MOLD_CABLE_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CASING_IC2_CLAY,ItemMold.MOLD_CASING_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SLAB_CLAY,ItemMold.MOLD_SLAB);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_STAIRS_CLAY,ItemMold.MOLD_STAIRS);

    GameRegistry.registerCraftingHandler(new MoldCraftingHandler());

    Fluid liquid_copper = LiquidMetalRegistry.GetMetal("Copper").fluid;
    Fluid liquid_tin = LiquidMetalRegistry.GetMetal("Tin").fluid;
    Fluid liquid_zinc = LiquidMetalRegistry.GetMetal("Zinc").fluid;
    Fluid liquid_silver = LiquidMetalRegistry.GetMetal("Silver").fluid;
    Fluid liquid_gold = LiquidMetalRegistry.GetMetal("Gold").fluid;
    Fluid liquid_nickel = LiquidMetalRegistry.GetMetal("Nickel").fluid;
    Fluid liquid_iron = LiquidMetalRegistry.GetMetal("Iron").fluid;
    Fluid liquid_electrum = LiquidMetalRegistry.GetMetal("Electrum").fluid;
    Fluid liquid_invar = LiquidMetalRegistry.GetMetal("Invar").fluid;
    Fluid liquid_bronze = LiquidMetalRegistry.GetMetal("Bronze").fluid;
    Fluid liquid_brass = LiquidMetalRegistry.GetMetal("Brass").fluid;



    int ore_id = FoundryBlocks.block_ore.blockID;
    if(wordgen_copper)
    {
      WordGenOre.RegisterOre(16, 80, 12, ore_id, BlockFoundryOre.ORE_COPPER);
    }
    if(wordgen_tin)
    {
      WordGenOre.RegisterOre(16, 52, 8, ore_id, BlockFoundryOre.ORE_TIN);
    }
    if(wordgen_zinc)
    {
      WordGenOre.RegisterOre(8, 48, 6, ore_id, BlockFoundryOre.ORE_ZINC);
    }
    if(wordgen_nickel)
    {
      WordGenOre.RegisterOre(8, 36, 5, ore_id, BlockFoundryOre.ORE_NICKEL);
    }
    if(wordgen_silver)
    {
      WordGenOre.RegisterOre(2, 30, 3, ore_id, BlockFoundryOre.ORE_SILVER);
    }
    if(wordgen_lead)
    {
      WordGenOre.RegisterOre(8, 48, 5, ore_id, BlockFoundryOre.ORE_LEAD);
    }
    GameRegistry.registerWorldGenerator(new FoundryWorldGenerator());

    proxy.Init();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    //Check for the existence of a stone gear, add it's own if it doesn't.
    if(OreDictionary.getOres("gearStone").size() == 0)
    {
      ItemStack cobble_stack = new ItemStack(Block.cobblestone, 1, -1);
      ItemStack stick_stack = new ItemStack(Item.stick);

      ItemStack gear_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_GEAR);
      OreDictionary.registerOre("gearStone", gear_stack);
      GameRegistry.addRecipe((ItemStack)gear_stack,
          " C ",
          "CSC",
          " C ",
          'C', cobble_stack,
          'S', stick_stack);
    }
  }
}
