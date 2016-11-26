package exter.foundry.integration.jei;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.gui.GuiAlloyingCrucible;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class AlloyingCrucibleJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    private final IAlloyingCrucibleRecipe recipe;

    public Wrapper(IAlloyingCrucibleRecipe recipe)
    {
      this.recipe = recipe;
    }

    @Deprecated
    @Override
    public List<List<ItemStack>> getInputs()
    {
      return null;
    }

    @Deprecated
    @Override
    public List<ItemStack> getOutputs()
    {
      return null;
    }

    @Deprecated
    @Override
    public List<FluidStack> getFluidInputs()
    {
      return null;
    }

    @Deprecated
    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return null;
    }


    @Deprecated
    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
      return null;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
      
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
      return false;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
      ingredients.setOutput(FluidStack.class, recipe.getOutput());
      ingredients.setInputs(FluidStack.class, ImmutableList.of(recipe.getInputA(),recipe.getInputB()));
    }
  }

  static public class Category implements IRecipeCategory<Wrapper>
  {

    protected final ResourceLocation backgroundLocation;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawable tank_overlay;

    public Category(IJeiHelpers helpers)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/alloyingcrucible.png");

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/alloyingcrucible.png");
      background = guiHelper.createDrawable(location, 33, 43, 110, 39);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 35);
      localizedName = Translator.translateToLocal("gui.jei.alloyingcrucible");
    }

    @Override
    @Nonnull
    public IDrawable getBackground()
    {
      return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {

    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Nonnull
    @Override
    public String getTitle()
    {
      return localizedName;
    }

    @Nonnull
    @Override
    public String getUid()
    {
      return "foundry.alloyingcrucible";
    }

    @Deprecated
    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull Wrapper recipeWrapper)
    {

    }

    @Override
    public IDrawable getIcon()
    {
      return null;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients)
    {
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      FluidStack out = ingredients.getOutputs(FluidStack.class).get(0);
      List<FluidStack> in_a = ingredients.getInputs(FluidStack.class).get(0);
      List<FluidStack> in_b = ingredients.getInputs(FluidStack.class).get(1);
      
      guiFluidStacks.init(0, true, 35 - 33, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, out.amount,false,tank_overlay);
      guiFluidStacks.init(1, true, 92, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, in_a.get(0).amount,false,tank_overlay);
      guiFluidStacks.init(2, false, 47, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, in_b.get(0).amount,false,tank_overlay);

      guiFluidStacks.set(0, in_a);
      guiFluidStacks.set(1, in_b);
      guiFluidStacks.set(2, out);
    }
  }

  static public class Handler implements IRecipeHandler<Wrapper>
  {
    @Override
    @Nonnull
    public Class<Wrapper> getRecipeClass()
    {
      return Wrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
      return "foundry.alloyingcrucible";
    }

    @Override
    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull Wrapper recipe)
    {
      return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull Wrapper recipe)
    {
      return true;
    }

    @Override
    public String getRecipeCategoryUid(Wrapper recipe)
    {
      return "foundry.alloyingcrucible";
    }
  }

  static public List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IAlloyingCrucibleRecipe recipe : AlloyingCrucibleRecipeManager.instance.getRecipes())
    {
      recipes.add(new Wrapper(recipe));
    }

    return recipes;
  }
}
