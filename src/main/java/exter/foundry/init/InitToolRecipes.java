package exter.foundry.init;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class InitToolRecipes
{
  static public void init()
  {
    ItemStack extra_sticks1 = new ItemStack(Items.STICK,1);
    ItemStack extra_sticks2 = new ItemStack(Items.STICK,2);


    ItemStack mold_chestplate = FoundryItems.mold(ItemMold.SubItem.CHESTPLATE);
    ItemStack mold_pickaxe = FoundryItems.mold(ItemMold.SubItem.PICKAXE);
    ItemStack mold_axe = FoundryItems.mold(ItemMold.SubItem.AXE);
    ItemStack mold_shovel = FoundryItems.mold(ItemMold.SubItem.SHOVEL);
    ItemStack mold_hoe = FoundryItems.mold(ItemMold.SubItem.HOE);
    ItemStack mold_sword = FoundryItems.mold(ItemMold.SubItem.SWORD);
    ItemStack mold_leggings = FoundryItems.mold(ItemMold.SubItem.LEGGINGS);
    ItemStack mold_helmet = FoundryItems.mold(ItemMold.SubItem.HELMET);
    ItemStack mold_boots = FoundryItems.mold(ItemMold.SubItem.BOOTS);

    MoldRecipeManager.instance.addRecipe(mold_helmet, 4, 3, new int[]
        {
            3, 3, 3, 3,
            3, 1, 1, 3,
            3, 1, 1, 3
        });

    MoldRecipeManager.instance.addRecipe(mold_chestplate, 6, 6, new int[]
        {
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3
        });
      
    MoldRecipeManager.instance.addRecipe(mold_leggings, 6, 6, new int[]
        {
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3
        });

    MoldRecipeManager.instance.addRecipe(mold_boots, 6, 6, new int[]
        {
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            2, 3, 0, 0, 3, 2,
            3, 3, 0, 0, 3, 3,
            3, 3, 0, 0, 3, 3
        });

    MoldRecipeManager.instance.addRecipe(mold_pickaxe, 5, 5, new int[]
        {
            0, 2, 2, 2, 0,
            1, 0, 1, 0, 1,
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0
        });

    MoldRecipeManager.instance.addRecipe(mold_axe, 3, 5, new int[]
        {
            1, 2, 2,
            1, 2, 1,
            1, 0, 1,
            0, 0, 1,
            0, 0, 1
        });

    MoldRecipeManager.instance.addRecipe(mold_shovel, 3, 6, new int[]
        {
            0, 1, 0,
            1, 1, 1,
            1, 1, 1,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0
        });

    MoldRecipeManager.instance.addRecipe(mold_hoe, 3, 5, new int[]
        {
            0, 2, 2,
            1, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
        });

    MoldRecipeManager.instance.addRecipe(mold_sword, 3, 6, new int[]
        {
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            1, 2, 1,
            0, 1, 0,
        });


    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_CHESTPLATE), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_CHESTPLATE), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_PICKAXE), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, new ItemStackMatcher(extra_sticks2));
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_PICKAXE), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, new ItemStackMatcher(extra_sticks2));

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_AXE), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, new ItemStackMatcher(extra_sticks2));
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_AXE), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, new ItemStackMatcher(extra_sticks2));

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_SHOVEL), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, new ItemStackMatcher(extra_sticks2));
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_SHOVEL), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, new ItemStackMatcher(extra_sticks2));

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_SWORD), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, new ItemStackMatcher(extra_sticks1));
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_SWORD), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, new ItemStackMatcher(extra_sticks1));

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_HOE), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, new ItemStackMatcher(extra_sticks2));
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_HOE), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, new ItemStackMatcher(extra_sticks2));

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_LEGGINGS), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_LEGGINGS), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_HELMET), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_HELMET), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);

    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_BOOTS), new FluidStack(FoundryFluids.liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
    CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_BOOTS), new FluidStack(FoundryFluids.liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
  }
}
