package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;


import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.gui.GuiMeltingCrucible;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.tileentity.TileEntityMeltingCrucible;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class MeltingJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final List<List<ItemStack>> input;
    @Nonnull
    private final List<FluidStack> output;
    @Nonnull
    private final IDrawable heat;
    
    private final int melting_point;
    

    public Wrapper(IJeiHelpers helpers,@Nonnull List<ItemStack> input, FluidStack output, int melting_point)
    {
      this.input = Collections.singletonList(input);
      this.output = Collections.singletonList(output);
      this.melting_point = melting_point;
      ResourceLocation background_location = new ResourceLocation("foundry", "textures/gui/metalsmelter.png");

      heat = helpers.getGuiHelper().createDrawable(background_location, 176, 53,
          (melting_point * 100 - TileEntityMeltingCrucible.HEAT_MIN) * 54 / (TileEntityMeltingCrucible.HEAT_MAX - TileEntityMeltingCrucible.HEAT_MIN), 12);

    }

    @Nonnull
    public List<List<ItemStack>> getInputs()
    {
      return input;
    }

    @Nonnull
    public List<ItemStack> getOutputs()
    {
      return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
      return Collections.emptyList();
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
      heat.draw(minecraft,11,41);
      minecraft.fontRendererObj.drawString(melting_point + " K", 14, 28, 0);
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
      return false;
    }
  }

  static public class Category implements IRecipeCategory
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

    private final IJeiHelpers helpers;

    public Category(IJeiHelpers helpers)
    {
      this.helpers = helpers;
      IGuiHelper guiHelper = helpers.getGuiHelper();
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/metalsmelter.png");


      IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 78, 24, 17);
      this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/metalsmelter.png");
      background = guiHelper.createDrawable(location, 30, 16, 94, 54);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
      localizedName = Translator.translateToLocal("gui.jei.melting");

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
      arrow.draw(minecraft, 49, 7);
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
      return "foundry.melting";
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
      IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

      guiItemStacks.init(0, true, 24, 6);
      guiFluidStacks.init(1, false, 77, 6, 16, GuiMeltingCrucible.TANK_HEIGHT, FoundryAPI.ICF_TANK_CAPACITY,false,tank_overlay);
      guiItemStacks.setFromRecipe(0, helpers.getStackHelper().toItemStackList(recipeWrapper.getInputs().get(0)));
      guiFluidStacks.set(1, recipeWrapper.getFluidOutputs().get(0));
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
      return "foundry.melting";
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

  static public List<Wrapper> getRecipes(IJeiHelpers helpers)
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(IMeltingRecipe recipe : MeltingRecipeManager.instance.getRecipes())
    {
      List<ItemStack> input = JEIHelper.toItemStackList(recipe.getInput());

      if(!input.isEmpty())
      {
        recipes.add(new Wrapper(helpers,input, recipe.getOutput(), recipe.getMeltingPoint()));
      }
    }

    return recipes;
  }
}
