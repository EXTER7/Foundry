package exter.foundry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.TextureStitchEvent;
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
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
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
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.worldgen.FoundryWorldGenerator;
import exter.foundry.worldgen.WordGenOre;

@Mod(modid = ModFoundry.MODID, name = ModFoundry.MODNAME, version = "0.1.0", dependencies = "required-after:Forge@[9.10.0.842,)")
@NetworkMod(channels = { ModFoundry.CHANNEL }, clientSideRequired = true, serverSideRequired = true, packetHandler = FoundryPacketHandler.class)
public class ModFoundry
{
  public static final String MODID = "foundry";
  public static final String MODNAME = "Foundry";

  public static final String CHANNEL = "EXTER_Foundry";

  @Instance(MODID)
  public static ModFoundry instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(clientSide = "exter.foundry.proxy.ClientFoundryProxy", serverSide = "exter.foundry.proxy.CommonFoundryProxy")
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

    OreDictionary.registerOre("ingotIron", Item.ingotIron);
    OreDictionary.registerOre("blockIron", Block.blockIron);
    OreDictionary.registerOre("ingotGold", Item.ingotGold);
    OreDictionary.registerOre("blockGold", Block.blockGold);
    OreDictionary.registerOre("nuggetGold", Item.goldNugget);

    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    FoundryItems.RegisterItems(config);
    FoundryBlocks.RegisterBlocks(config);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Iron", 3830);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Gold", 3831);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Copper", 3833);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Tin", 3834);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Bronze", 3835);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Electrum", 3837);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Invar", 3838);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Nickel", 3839);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Zinc", 3840);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Brass", 3841);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Silver", 3842);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Steel", 3845);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "RefinedIron", 3848);
    LiquidMetalRegistry.RegisterLiquidMetal(config, "Lead", 3849);
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
    Fluid liquid_refined_iron = LiquidMetalRegistry.GetMetal("RefinedIron").fluid;

    AlloyRecipe.RegisterRecipe(new FluidStack(liquid_bronze,12), new FluidStack(liquid_copper,9), new FluidStack(liquid_tin,3));
    AlloyRecipe.RegisterRecipe(new FluidStack(liquid_brass,12), new FluidStack(liquid_copper,9), new FluidStack(liquid_zinc,3));
    AlloyRecipe.RegisterRecipe(new FluidStack(liquid_invar,12), new FluidStack(liquid_iron,8), new FluidStack(liquid_nickel,4));
    AlloyRecipe.RegisterRecipe(new FluidStack(liquid_electrum,12), new FluidStack(liquid_gold,6), new FluidStack(liquid_silver,6));

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
    ItemStack extra_sticks1 = new ItemStack(Item.stick,1);
    ItemStack extra_sticks2 = new ItemStack(Item.stick,2);
    
    
    CastingRecipe.RegisterMold(mold_ingot);
    CastingRecipe.RegisterMold(mold_chestplate);
    CastingRecipe.RegisterMold(mold_pickaxe);
    CastingRecipe.RegisterMold(mold_block);
    CastingRecipe.RegisterMold(mold_axe);
    CastingRecipe.RegisterMold(mold_shovel);
    CastingRecipe.RegisterMold(mold_hoe);
    CastingRecipe.RegisterMold(mold_sword);
    CastingRecipe.RegisterMold(mold_leggings);
    CastingRecipe.RegisterMold(mold_helmet);
    CastingRecipe.RegisterMold(mold_boots);
    CastingRecipe.RegisterMold(mold_gear);
    CastingRecipe.RegisterMold(mold_cable_ic2);
    CastingRecipe.RegisterMold(mold_casing_ic2);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.plateIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 8), mold_chestplate, null);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.plateGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 8), mold_chestplate, null);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.pickaxeIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.pickaxeGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.axeIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.axeGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 3), mold_axe, extra_sticks2);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.shovelIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.shovelGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.swordIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.swordGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 2), mold_sword, extra_sticks1);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.hoeIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.hoeGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.legsIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 7), mold_leggings, null);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.legsGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 7), mold_leggings, null);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.helmetIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 5), mold_helmet, null);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.helmetGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 5), mold_helmet, null);

    CastingRecipe.RegisterRecipe(new ItemStack(Item.bootsIron,1,0), new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 4), mold_boots, null);
    CastingRecipe.RegisterRecipe(new ItemStack(Item.bootsGold,1,0), new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 4), mold_boots, null);

    CastingRecipe.RegisterRecipe("gearIron", new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipe.RegisterRecipe("gearGold", new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipe.RegisterRecipe("gearCopper", new FluidStack(liquid_copper,MeltingRecipe.AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipe.RegisterRecipe("gearTin", new FluidStack(liquid_tin,MeltingRecipe.AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipe.RegisterRecipe("gearBronze", new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipe.RegisterRecipe("gearBrass", new FluidStack(liquid_brass,MeltingRecipe.AMOUNT_INGOT * 4), mold_gear, null);
    CastingRecipe.RegisterRecipe("gearInvar", new FluidStack(liquid_invar,MeltingRecipe.AMOUNT_INGOT * 4), mold_gear, null);

    InfuserSubstanceRecipe.RegisterRecipe(new InfuserSubstance("carbon",36), new ItemStack(Item.coal,1,0), 300);
    InfuserSubstanceRecipe.RegisterRecipe(new InfuserSubstance("carbon",6), new ItemStack(Item.coal,1,1), 600);
    InfuserSubstanceRecipe.RegisterRecipe(new InfuserSubstance("carbon",324), new ItemStack(Block.coalBlock,1), 2400);
    InfuserSubstanceRecipe.RegisterRecipe(new InfuserSubstance("carbon",36), "dustCoal", 200);
    InfuserSubstanceRecipe.RegisterRecipe(new InfuserSubstance("carbon",6), "dustCharcoal", 400);

    InfuserSubstanceRecipe.RegisterRecipe(new InfuserSubstance("sand",500), new ItemStack(Block.sand,1), 15);

    InfuserRecipe.RegisterRecipe(new FluidStack(liquid_steel,3), new FluidStack(liquid_iron,3), new InfuserSubstance("carbon",2));
    InfuserRecipe.RegisterRecipe(new FluidStack(liquid_refined_iron,12), new FluidStack(liquid_iron,12), new InfuserSubstance("sand",1));
    
    NetworkRegistry.instance().registerGuiHandler(this, proxy);
  }
  
 
  @EventHandler
  public void load(FMLInitializationEvent event)
  {
    log.setParent(FMLLog.getLogger());

    ModIntegration.RegisterIntegration(new ModIntegrationIC2("ic2"));
    ModIntegration.RegisterIntegration(new ModIntegrationBuildcraft("buildcraft"));
    ModIntegration.RegisterIntegration(new ModIntegrationForestry("forestry"));
    
    GameRegistry.registerTileEntity(TileEntityInductionCrucibleFurnace.class, "Foundry_MeltingFurnace");
    GameRegistry.registerTileEntity(TileEntityMetalCaster.class, "Foundry_MetalCaster");
    GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, "Foundry_AlloyMixer");
    GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, "Foundry_MetalInfuser");

    ItemStack brick_stack = new ItemStack(Item.brick);
    ItemStack iron_stack = new ItemStack(Item.ingotIron);
    ItemStack redstone_stack = new ItemStack(Item.redstone);
    ItemStack cobble_stack = new ItemStack(Block.cobblestone, 1, -1);
    ItemStack furnace_stack = new ItemStack(Block.furnaceIdle);
    ItemStack clayblock_stack = new ItemStack(Block.blockClay, 1, -1);
    ItemStack brickblock_stack = new ItemStack(Block.brick, 1, -1);
    ItemStack crucible_stack = new ItemStack(FoundryBlocks.block_foundry_crucible);
    ItemStack piston_stack = new ItemStack(Block.pistonBase);
    ItemStack goldnugget_stack = new ItemStack(Item.goldNugget);
    ItemStack stick_stack = new ItemStack(Item.stick);
    ItemStack heatingcoil_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_HEATINGCOIL);


    GameRegistry.addRecipe(heatingcoil_stack,
        "IRI",
        "RNR",
        "IRI",
        'I', iron_stack,
        'N', goldnugget_stack,
        'R', redstone_stack);

    GameRegistry.addRecipe(crucible_stack, 
        "IBI",
        "B B",
        "IBI",
        'I', iron_stack,
        'B', brickblock_stack);
    
    GameRegistry.addRecipe(new ItemStack(FoundryBlocks.block_induction_crucible_furnace),
        "IFI",
        "BCB",
        "HRH",
        'F', furnace_stack, 
        'I', iron_stack, 
        'B', brickblock_stack,
        'C', crucible_stack,
        'R', redstone_stack,
        'H', heatingcoil_stack);
    
    GameRegistry.addRecipe(new ItemStack(FoundryBlocks.block_metal_caster),
        "RPR",
        "ICI",
        "III",
        'I', iron_stack, 
        'P', piston_stack,
        'C', crucible_stack,
        'R', redstone_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FoundryBlocks.block_metal_infuser),
        "IGI",
        "BCB",
        "IHI",
        'I', iron_stack, 
        'B', brickblock_stack,
        'C', crucible_stack,
        'G', "gearStone",
        'H', heatingcoil_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FoundryBlocks.block_alloy_mixer),
        "RGR",
        "BCB",
        "IBI",
        'I', iron_stack, 
        'B', brickblock_stack,
        'C', crucible_stack,
        'R', redstone_stack,
        'G', "gearStone"));

    
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Block.planks,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, new ItemStack(Block.stone,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, new ItemStack(Item.brick));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateGold,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, new ItemStack(Item.plateDiamond,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, new ItemStack(Item.legsIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetGold,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, new ItemStack(Item.helmetDiamond,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, new ItemStack(Item.bootsIron,1,-1));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeWood));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeStone));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeIron));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeGold));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, new ItemStack(Item.pickaxeDiamond));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeWood));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeStone));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeIron));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeGold));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, new ItemStack(Item.axeDiamond));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelWood));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelStone));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelIron));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelGold));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, new ItemStack(Item.shovelDiamond));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeWood));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeStone));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeIron));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeGold));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, new ItemStack(Item.hoeDiamond));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordWood));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordStone));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordIron));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordGold));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, new ItemStack(Item.swordDiamond));
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearWood");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearStone");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearIron");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearGold");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearDiamond");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearCopper");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearTin");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearBronze");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearBrass");
    FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_CLAY, "gearInvar");

    
    for(String name:OreDictionary.getOreNames())
    {
      if(name.startsWith("ingot"))
      {
        FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_CLAY, name);
      } else if(name.startsWith("block"))
      {
        FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_CLAY, name);
      }
    }

    FoundryUtils.RegisterOreSmelting(BlockFoundryOre.ORE_COPPER,ItemIngot.INGOT_COPPER);
    FoundryUtils.RegisterOreSmelting(BlockFoundryOre.ORE_TIN,ItemIngot.INGOT_TIN);
    FoundryUtils.RegisterOreSmelting(BlockFoundryOre.ORE_ZINC,ItemIngot.INGOT_ZINC);
    FoundryUtils.RegisterOreSmelting(BlockFoundryOre.ORE_NICKEL,ItemIngot.INGOT_NICKEL);
    FoundryUtils.RegisterOreSmelting(BlockFoundryOre.ORE_SILVER,ItemIngot.INGOT_SILVER);
    
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_BLOCK_CLAY,ItemMold.MOLD_BLOCK);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_CHESTPLATE_CLAY,ItemMold.MOLD_CHESTPLATE);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_INGOT_CLAY,ItemMold.MOLD_INGOT);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_PICKAXE_CLAY,ItemMold.MOLD_PICKAXE);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_AXE_CLAY,ItemMold.MOLD_AXE);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_SHOVEL_CLAY,ItemMold.MOLD_SHOVEL);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_SWORD_CLAY,ItemMold.MOLD_SWORD);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_HOE_CLAY,ItemMold.MOLD_HOE);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_LEGGINGS_CLAY,ItemMold.MOLD_LEGGINGS);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_HELMET_CLAY,ItemMold.MOLD_HELMET);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_BOOTS_CLAY,ItemMold.MOLD_BOOTS);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_GEAR_CLAY,ItemMold.MOLD_GEAR);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_CABLE_IC2_CLAY,ItemMold.MOLD_CABLE_IC2);
    FoundryUtils.RegisterMoldSmelting(ItemMold.MOLD_CASING_IC2_CLAY,ItemMold.MOLD_CASING_IC2);

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
