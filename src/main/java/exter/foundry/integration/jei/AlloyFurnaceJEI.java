package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.JEIManager;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.StackUtil;
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

    public Wrapper(@Nonnull List<List<ItemStack>> input,List<ItemStack> output)
    {
      IGuiHelper guiHelper = JEIManager.guiHelper;
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
    public List<FluidStack> getFluidInputs() {
      return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
      return Collections.emptyList();
    }


    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
      
    }

    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
      
    }
  }
  
  static public class Category implements IRecipeCategory {
    private static final int inputSlot_a = 0;
    private static final int inputSlot_b = 1;
//    private static final int fuelSlot = 2;
    private static final int outputSlot = 2;

    protected final ResourceLocation backgroundLocation;
    @Nonnull
    protected final IDrawableAnimated flame;
    @Nonnull
    protected final IDrawableAnimated arrow;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public Category() {
      IGuiHelper guiHelper = JEIManager.guiHelper;
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");

      IDrawableStatic flameDrawable = guiHelper.createDrawable(backgroundLocation, 176, 0, 14, 14);
      flame = guiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

      IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 14, 24, 17);
      this.arrow = JEIManager.guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/alloyfurnace.png");
      background = JEIManager.guiHelper.createDrawable(location, 30, 16, 110, 54);
      localizedName = Translator.translateToLocal("gui.jei.alloyfurnace");

    }


    @Override
    @Nonnull
    public IDrawable getBackground() {
      return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void drawAnimations(Minecraft minecraft) {
      flame.draw(minecraft, 18, 20);
      arrow.draw(minecraft, 50, 19);
    }

    @Nonnull
    @Override
    public String getTitle() {
      return localizedName;
    }

    @Nonnull
    @Override
    public String getUid() {
      return "foundry.alloyfurnace";
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

      guiItemStacks.init(inputSlot_a, true, 7, 0);
      guiItemStacks.init(inputSlot_b, true, 25, 0);
      guiItemStacks.init(outputSlot, false, 85, 18);

      guiItemStacks.setFromRecipe(inputSlot_a, StackUtil.toItemStackList(recipeWrapper.getInputs().get(inputSlot_a)));
      guiItemStacks.setFromRecipe(inputSlot_b, StackUtil.toItemStackList(recipeWrapper.getInputs().get(inputSlot_b)));
      guiItemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());
    }
  }
  
  static public class Handler implements IRecipeHandler<Wrapper>
  {
    @Override
    @Nonnull
    public Class<Wrapper> getRecipeClass() {
      return Wrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
      return "foundry.alloyfurnace";
    }

    @Override
    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull Wrapper recipe) {
      return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull Wrapper recipe) {
      return recipe.getInputs().size() != 0 && recipe.getOutputs().size() > 0;
    }
  }
  
  @SuppressWarnings("unchecked")
  public static List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for (IAlloyFurnaceRecipe recipe:AlloyFurnaceRecipeManager.instance.getRecipes()) {
      List<ItemStack> input_a = JEIHelper.toItemStackList(recipe.getInputA());
      List<ItemStack> input_b = JEIHelper.toItemStackList(recipe.getInputB());
      
      if(!(input_a.isEmpty() || input_b.isEmpty()))
      {
        recipes.add(new Wrapper(Lists.newArrayList(input_a,input_b),Collections.singletonList(recipe.getOutput())));
      }

    }

    return recipes;
  }
}
