package exter.foundry.integration.jei;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

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
    @Nonnull
    private final List<FluidStack> input;
    @Nonnull
    private final List<FluidStack> output;

    public Wrapper(@Nonnull FluidStack output, FluidStack input_a,FluidStack input_b)
    {
      this.input = Lists.newArrayList(input_a, input_b);
      this.output = Collections.singletonList(output);
    }

    @Nonnull
    public List<List<ItemStack>> getInputs()
    {
      return Collections.emptyList();
    }

    @Nonnull
    public List<ItemStack> getOutputs()
    {
      return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
      return input;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return output;
    }


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
      // TODO Auto-generated method stub
      
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

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull Wrapper recipeWrapper)
    {
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      guiFluidStacks.init(0, true, 35 - 33, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, recipeWrapper.getFluidInputs().get(0).amount,false,tank_overlay);
      guiFluidStacks.init(1, true, 92, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, recipeWrapper.getFluidInputs().get(1).amount,false,tank_overlay);
      guiFluidStacks.init(2, false, 47, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, recipeWrapper.getFluidOutputs().get(0).amount,false,tank_overlay);

      guiFluidStacks.set(0, recipeWrapper.getFluidInputs().get(0));
      guiFluidStacks.set(1, recipeWrapper.getFluidInputs().get(1));
      guiFluidStacks.set(2, recipeWrapper.getFluidOutputs().get(0));
    }

    @Override
    public IDrawable getIcon()
    {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients)
    {
      // TODO Auto-generated method stub
      
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
      recipes.add(new Wrapper(
          recipe.getOutput(),recipe.getInputA(),recipe.getInputB()));
    }

    return recipes;
  }
}
