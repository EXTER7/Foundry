package exter.foundry.init;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class InitFirearmRecipes
{
  static public void init()
  {
    ItemStack mold_bullet = FoundryItems.mold(ItemMold.SubItem.BULLET);
    ItemStack mold_bullet_hollow = FoundryItems.mold(ItemMold.SubItem.BULLET_HOLLOW);
    ItemStack mold_round_casing = FoundryItems.mold(ItemMold.SubItem.ROUND_CASING);
    ItemStack mold_pellet = FoundryItems.mold(ItemMold.SubItem.PELLET);
    ItemStack mold_shell_casing = FoundryItems.mold(ItemMold.SubItem.SHELL_CASING);
    ItemStack mold_gun_barrel = FoundryItems.mold(ItemMold.SubItem.GUN_BARREL);
    ItemStack mold_revolver_drum = FoundryItems.mold(ItemMold.SubItem.REVOLVER_DRUM);
    ItemStack mold_revolver_frame = FoundryItems.mold(ItemMold.SubItem.REVOLVER_FRAME);
    ItemStack mold_shotgun_pump = FoundryItems.mold(ItemMold.SubItem.SHOTGUN_PUMP);
    ItemStack mold_shotgun_frame = FoundryItems.mold(ItemMold.SubItem.SHOTGUN_FRAME);

    MoldRecipeManager.instance.addRecipe(mold_bullet, 3, 3, new int[]
        {
            0, 1, 0,
            1, 2, 1,
            0, 1, 0,
        });

    MoldRecipeManager.instance.addRecipe(mold_bullet_hollow, 3, 3, new int[]
        {
            0, 2, 0,
            2, 1, 2,
            0, 2, 0,
        });

    MoldRecipeManager.instance.addRecipe(mold_round_casing, 4, 4, new int[]
        {
            0, 2, 2, 0,
            2, 1, 1, 2,
            2, 1, 1, 2,
            0, 2, 2, 0
        });

    MoldRecipeManager.instance.addRecipe(mold_pellet, 1, 1, new int[]
        {
            1
        });

    MoldRecipeManager.instance.addRecipe(mold_shell_casing, 5, 5, new int[]
        {
            0, 0, 3, 0, 0,
            0, 3, 1, 3, 0,
            3, 1, 1, 1, 3,
            0, 3, 1, 3, 0,
            0, 0, 3, 0, 0
        });

    MoldRecipeManager.instance.addRecipe(mold_gun_barrel, 3, 3, new int[]
        {
            0, 4, 0,
            4, 0, 4,
            0, 4, 0,
        });

    MoldRecipeManager.instance.addRecipe(mold_revolver_drum, 5, 5, new int[]
        {
            0, 3, 3, 3, 0,
            3, 0, 3, 0, 3,
            3, 3, 3, 3, 3,
            3, 0, 3, 0, 3,
            0, 3, 3, 3, 0
        });

    MoldRecipeManager.instance.addRecipe(mold_revolver_frame, 5, 6, new int[]
        {
            0, 2, 2, 0, 0,
            0, 2, 2, 2, 0,
            2, 2, 2, 2, 0,
            0, 2, 2, 2, 2,
            0, 0, 2, 2, 2,
            0, 0, 2, 2, 2
        });

    MoldRecipeManager.instance.addRecipe(mold_shotgun_pump, 3, 3, new int[]
        {
            4, 0, 4,
            4, 0, 4,
            4, 4, 4,
        });

    MoldRecipeManager.instance.addRecipe(mold_shotgun_frame, 6, 5, new int[]
        {
            0, 2, 2, 0, 0, 0,
            0, 2, 2, 2, 2, 0,
            0, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2,
            0, 0, 0, 2, 2, 2,
        });


    ItemStack bullet = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET);
    ItemStack bullet_hollow = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_HOLLOW);
    ItemStack bullet_jacketed = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_JACKETED);
    ItemStack bullet_casing = FoundryItems.component(ItemComponent.SubItem.AMMO_CASING);
    ItemStack pellet = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET);
    ItemStack shell_casing = FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL);
    ItemStack gun_barrel = FoundryItems.component(ItemComponent.SubItem.GUN_BARREL);
    ItemStack revolver_drum = FoundryItems.component(ItemComponent.SubItem.REVOLVER_DRUM);
    ItemStack revolver_frame = FoundryItems.component(ItemComponent.SubItem.REVOLVER_FRAME);
    ItemStack shotgun_pump = FoundryItems.component(ItemComponent.SubItem.SHOTGUN_PUMP);
    ItemStack shotgun_frame = FoundryItems.component(ItemComponent.SubItem.SHOTGUN_FRAME);
    ItemStack bullet_steel = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_STEEL);
    ItemStack pellet_steel = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_STEEL);
    ItemStack bullet_lumium = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_LUMIUM);
    ItemStack pellet_lumium = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_LUMIUM);

       
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet),
        new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_hollow),
        new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_jacketed),
        new FluidStack(FoundryFluids.liquid_copper, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_casing),
        new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet),
        new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shell_casing),
        new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_steel),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_steel),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_lumium),
        new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_lumium),
        new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET));


    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(gun_barrel),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_drum),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_frame),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_pump),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_frame),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));


    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet),
        new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_hollow),
        new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet_hollow, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_jacketed),
        new FluidStack(FoundryFluids.liquid_copper, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_bullet, new ItemStackMatcher(bullet));
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_casing),
        new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_round_casing, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet),
        new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shell_casing),
        new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET * 2), mold_shell_casing, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_steel),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_steel),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_lumium),
        new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_lumium),
        new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);

    
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(gun_barrel),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_gun_barrel, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_drum),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4), mold_revolver_drum, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_frame),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_revolver_frame, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_pump),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_shotgun_pump, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_frame),
        new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_shotgun_frame, null);

    ItemStack paper = new ItemStack(Items.PAPER);

    GameRegistry.addRecipe(
        FoundryItems.item_revolver.empty(),
        "BD",
        " F",
        'B', FoundryItems.component(ItemComponent.SubItem.GUN_BARREL), 
        'D', FoundryItems.component(ItemComponent.SubItem.REVOLVER_DRUM),
        'F', FoundryItems.component(ItemComponent.SubItem.REVOLVER_FRAME));

    GameRegistry.addRecipe(
        FoundryItems.item_shotgun.empty(),
        "BB ",
        " PF",
        'B', FoundryItems.component(ItemComponent.SubItem.GUN_BARREL), 
        'P', FoundryItems.component(ItemComponent.SubItem.SHOTGUN_PUMP),
        'F', FoundryItems.component(ItemComponent.SubItem.SHOTGUN_FRAME));


    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_hollow,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_HOLLOW), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_jacketed,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_JACKETED), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_fire,
        "B",
        "A",
        'B', "dustSmallBlaze", 
        'A', FoundryItems.item_round_hollow));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_shell,
        "PAP",
        "PGP",
        "PCP",
        'P', FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET), 
        'A', paper, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_ap,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_STEEL), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_lumium,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_LUMIUM), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_shell_ap,
        "PAP",
        "PGP",
        "PCP",
        'P', FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_STEEL), 
        'A', paper, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_shell_lumium,
        "PAP",
        "PGP",
        "PCP",
        'P', FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_LUMIUM), 
        'A', paper, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL)));

    GameRegistry.addShapelessRecipe(
        new ItemStack(FoundryItems.item_round_poison,2),
        Items.SPIDER_EYE, 
        FoundryItems.item_round_hollow,
        FoundryItems.item_round_hollow);

    GameRegistry.addShapelessRecipe(
        new ItemStack(FoundryItems.item_round_snow,2),
        Items.SNOWBALL, 
        FoundryItems.item_round_hollow,
        FoundryItems.item_round_hollow);
  }
}
