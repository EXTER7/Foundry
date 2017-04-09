package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.gui.GuiMetalCaster;
import exter.foundry.recipes.manager.CastingRecipeManager;
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
import net.minecraftforge.fluids.FluidStack;


public class CastingJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final ICastingRecipe recipe;

    public Wrapper(ICastingRecipe recipe)
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
      IItemMatcher extra = recipe.getInputExtra();
      List<ItemStack> extra_items = extra != null?extra.getItems():Collections.<ItemStack>emptyList();
      ingredients.setInputs(FluidStack.class, Collections.singletonList(recipe.getInput()));
      ingredients.setInputLists(ItemStack.class, ImmutableList.of(
          Collections.singletonList(recipe.getMold()),
          extra_items));
      ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Override
    public boolean equals(Object other)
    {
      return recipe == other;
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
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/caster.png");


      IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 53, 24, 17);
      arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/caster.png");
      background = guiHelper.createDrawable(location, 38, 16, 68, 54);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
      localizedName = Translator.translateToLocal("gui.jei.casting");

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
      arrow.draw(minecraft, 22, 35);
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
      return "foundry.casting";
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
      IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();
      IGuiFluidStackGroup gui_fluids = recipeLayout.getFluidStacks();

      gui_items.init(0, false, 47, 34);
      gui_items.init(1, true, 27, 4);
      gui_items.init(2, true, 47, 4);
      gui_fluids.init(3, true, 1, 5, 16, GuiMetalCaster.TANK_HEIGHT, FoundryAPI.CASTER_TANK_CAPACITY,false,tank_overlay);
      gui_items.set(0, ingredients.getOutputs(ItemStack.class).get(0));
      gui_items.set(1, ingredients.getInputs(ItemStack.class).get(0));
      gui_items.set(2, ingredients.getInputs(ItemStack.class).get(1));
      gui_fluids.set(3, ingredients.getInputs(FluidStack.class).get(0));
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
      return "foundry.casting";
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
      return "foundry.casting";
    }
  }

  static public List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(ICastingRecipe recipe : CastingRecipeManager.instance.getRecipes())
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
