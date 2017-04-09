package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.gui.GuiMetalAtomizer;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;


public class AtomizerJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final IAtomizerRecipe recipe;

    private static final FluidStack WATER = new FluidStack(FluidRegistry.WATER,50);
    
    public Wrapper(@Nonnull IAtomizerRecipe recipe)
    {
      this.recipe = recipe;
    }

    @Override
    @Deprecated
    public List<List<ItemStack>> getInputs()
    {
      return null;
    }

    @Override
    @Deprecated
    public List<ItemStack> getOutputs()
    {
      return null;
    }

    @Override
    @Deprecated
    public List<FluidStack> getFluidInputs()
    {
      return null;
    }

    @Override
    @Deprecated
    public List<FluidStack> getFluidOutputs()
    {
      return null;
    }


    @Override
    @Deprecated
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
      ingredients.setInputs(FluidStack.class, ImmutableList.of(recipe.getInput(), WATER));
      ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }
  }

  static public class Category implements IRecipeCategory<Wrapper>
  {

    protected final ResourceLocation backgroundLocation;
    @Nonnull
    protected final IDrawableAnimated arrow;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawable tank_overlay;

    public Category(IJeiHelpers helpers)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/atomizer.png");


      IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 53, 24, 17);
      arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/atomizer.png");
      background = guiHelper.createDrawable(location, 8, 19, 133, 51);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
      localizedName = Translator.translateToLocal("gui.jei.atomizer");
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
      arrow.draw(minecraft, 52, 18);
    }

    @Override
    @Deprecated
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
      return "foundry.atomizer";
    }

    @Override
    @Deprecated
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
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      guiItemStacks.init(0, false, 77, 17);
      guiFluidStacks.init(1, true, 31, 2, 16, GuiMetalAtomizer.TANK_HEIGHT, FoundryAPI.ATOMIZER_TANK_CAPACITY,false,tank_overlay);
      guiFluidStacks.init(2, true, 115, 2, 16, GuiMetalAtomizer.TANK_HEIGHT, FoundryAPI.ATOMIZER_TANK_CAPACITY,false,tank_overlay);
      guiItemStacks.set(0, ingredients.getOutputs(ItemStack.class).get(0));
      guiFluidStacks.set(1, ingredients.getInputs(FluidStack.class).get(0));
      guiFluidStacks.set(2, ingredients.getInputs(FluidStack.class).get(1));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
      return Collections.emptyList();
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
      return "foundry.atomizer";
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
      return "foundry.atomizer";
    }
  }

  static public List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IAtomizerRecipe recipe : AtomizerRecipeManager.instance.getRecipes())
    {
      ItemStack output = recipe.getOutput();

      if(output != null)
      {
        recipes.add(new Wrapper(recipe));
      }
    }

    return recipes;
  }
}
