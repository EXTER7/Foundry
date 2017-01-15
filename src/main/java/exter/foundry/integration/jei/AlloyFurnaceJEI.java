package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
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

public class AlloyFurnaceJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    protected final IDrawableStatic flame_drawable;
    @Nonnull
    private final IAlloyFurnaceRecipe recipe;

    public Wrapper(IJeiHelpers helpers,@Nonnull IAlloyFurnaceRecipe recipe)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      ResourceLocation furnaceBackgroundLocation = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");

      flame_drawable = guiHelper.createDrawable(furnaceBackgroundLocation, 176, 0, 14, 14);
      this.recipe = recipe;
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
      ingredients.setInputLists(ItemStack.class, ImmutableList.of(
          recipe.getInputA().getItems(),
          recipe.getInputB().getItems()));
      ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }
  }

  static public class Category implements IRecipeCategory<Wrapper>
  {

    protected final ResourceLocation background_location;
    @Nonnull
    protected final IDrawableAnimated flame;
    @Nonnull
    protected final IDrawableAnimated arrow;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localized_name;
    

    public Category(IJeiHelpers helpers)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      background_location = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");

      IDrawableStatic flameDrawable = guiHelper.createDrawable(background_location, 176, 0, 14, 14);
      flame = guiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

      IDrawableStatic arrowDrawable = guiHelper.createDrawable(background_location, 176, 14, 24, 17);
      arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");
      background = guiHelper.createDrawable(location, 30, 16, 110, 54);
      localized_name = Translator.translateToLocal("gui.jei.alloyfurnace");
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
      flame.draw(minecraft, 18, 20);
      arrow.draw(minecraft, 50, 19);
    }

    @Nonnull
    @Override
    public String getTitle()
    {
      return localized_name;
    }

    @Nonnull
    @Override
    public String getUid()
    {
      return "foundry.alloyfurnace";
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

      gui_items.init(0, true, 7, 0);
      gui_items.init(1, true, 25, 0);
      gui_items.init(2, false, 85, 18);

      gui_items.set(0, ingredients.getInputs(ItemStack.class).get(0));
      gui_items.set(1, ingredients.getInputs(ItemStack.class).get(1));
      gui_items.set(2, ingredients.getOutputs(ItemStack.class).get(0));
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
      return "foundry.alloyfurnace";
    }
  }

  static public List<Wrapper> getRecipes(IJeiHelpers helpers)
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IAlloyFurnaceRecipe recipe : AlloyFurnaceRecipeManager.instance.getRecipes())
    {
      List<ItemStack> input_a = recipe.getInputA().getItems();
      List<ItemStack> input_b = recipe.getInputB().getItems();

      if(!(input_a.isEmpty() || input_b.isEmpty()))
      {
        recipes.add(new Wrapper(helpers,recipe));
      }
    }

    return recipes;
  }
}
