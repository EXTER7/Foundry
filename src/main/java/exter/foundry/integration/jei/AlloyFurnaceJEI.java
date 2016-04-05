package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class AlloyFurnaceJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    protected final IDrawableStatic flame_drawable;
    @Nonnull
    private final List<List<ItemStack>> input;
    @Nonnull
    private final List<ItemStack> output;

    public Wrapper(IJeiHelpers helpers,@Nonnull List<List<ItemStack>> input, List<ItemStack> output)
    {
      IGuiHelper guiHelper = helpers.getGuiHelper();
      ResourceLocation furnaceBackgroundLocation = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");

      flame_drawable = guiHelper.createDrawable(furnaceBackgroundLocation, 176, 0, 14, 14);
      this.input = input;
      this.output = output;
    }

    @Nonnull
    public List<List<ItemStack>> getInputs()
    {
      return input;
    }

    @Nonnull
    public List<ItemStack> getOutputs()
    {
      return output;
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
      return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
      return Collections.emptyList();
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
  }

  static public class Category implements IRecipeCategory
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
    
    private final IJeiHelpers helpers;

    public Category(IJeiHelpers helpers)
    {
      this.helpers = helpers;
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

    }

    @Override
    public void drawAnimations(Minecraft minecraft)
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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
      IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();
      IStackHelper stack_helper = helpers.getStackHelper();

      gui_items.init(0, true, 7, 0);
      gui_items.init(1, true, 25, 0);
      gui_items.init(2, false, 85, 18);

      gui_items.setFromRecipe(0, stack_helper.toItemStackList(recipeWrapper.getInputs().get(0)));
      gui_items.setFromRecipe(1, stack_helper.toItemStackList(recipeWrapper.getInputs().get(1)));
      gui_items.setFromRecipe(2, recipeWrapper.getOutputs());
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
      return "foundry.alloyfurnace";
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
  }

  @SuppressWarnings("unchecked")
  static public List<Wrapper> getRecipes(IJeiHelpers helpers)
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IAlloyFurnaceRecipe recipe : AlloyFurnaceRecipeManager.instance.getRecipes())
    {
      List<ItemStack> input_a = recipe.getInputA().getItems();
      List<ItemStack> input_b = recipe.getInputB().getItems();

      if(!(input_a.isEmpty() || input_b.isEmpty()))
      {
        recipes.add(new Wrapper(helpers,
            Lists.newArrayList(input_a, input_b),
            Collections.singletonList(recipe.getOutput())));
      }
    }

    return recipes;
  }
}
