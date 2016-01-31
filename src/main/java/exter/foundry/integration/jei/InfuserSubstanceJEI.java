package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.tileentity.TileEntityFoundryPowered;
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
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;


public class InfuserSubstanceJEI
{

  static public class Wrapper implements IRecipeWrapper
  {
    @Nonnull
    private final List<List<ItemStack>> input;
    @Nonnull
    private final InfuserSubstance output;
    @Nonnull
    private final IDrawable substance_drawable;
    
    private final int energy;
    
    public Wrapper(IJeiHelpers helpers,@Nonnull InfuserSubstance output, @Nonnull List<ItemStack> input, int energy)
    {
      this.input = Collections.singletonList(input);
      this.output = output;
      this.substance_drawable = JEIHelper.getSubstanceDrawable(helpers, output);
      this.energy = energy;
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
      return Collections.emptyList();
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
      if(substance_drawable != null)
      {
        JEIHelper.setSubstanceGLColor(output);
        substance_drawable.draw(minecraft, 56, 2);
        GlStateManager.resetColor();
      }
      minecraft.fontRendererObj.drawString(energy / TileEntityFoundryPowered.RATIO_RF + " RF", 0, 38, 0);
    }

    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public List<String> getTooltipStrings(int x, int y)
    {
      if(x >= 56 && x <= 66 && y >= 2 && y <= 49)
      {
        return Lists.newArrayList(
            StatCollector.translateToLocal("substance." + output.type),
            output.amount + " mL");
      }
      return null;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
      drawInfo(minecraft, recipeWidth, recipeHeight);
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
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawable tank_overlay;
    @Nonnull
    protected final IDrawableAnimated arrow;
    
    private final IJeiHelpers helpers;
    
    public Category(IJeiHelpers helpers)
    {
      this.helpers = helpers;
      IGuiHelper guiHelper = helpers.getGuiHelper();
      background_location = new ResourceLocation("foundry", "textures/gui/infuser.png");

      IDrawableStatic arrowDrawable = guiHelper.createDrawable(background_location, 176, 53, 24, 17);
      arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/infuser.png");
      background = guiHelper.createDrawable(location, 15, 41, 137, 51);
      tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 47);
      localizedName = Translator.translateToLocal("gui.jei.infuser.substance");
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
      arrow.draw(minecraft, 27, 18);
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
      return "foundry.infuser.substance";
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

      guiItemStacks.init(0, true, 3, 17);
      guiItemStacks.setFromRecipe(0, helpers.getStackHelper().toItemStackList(recipeWrapper.getInputs().get(0)));
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
      return "foundry.infuser.substance";
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

    for(IInfuserSubstanceRecipe recipe : InfuserRecipeManager.instance.getSubstanceRecipes())
    {
      List<ItemStack> input = JEIHelper.toItemStackList(recipe.getInput());
      if(input.size() > 0)
      {
        recipes.add(new Wrapper(helpers,
            recipe.getOutput(),
            input,
            recipe.getEnergyNeeded()));
      }
    }

    return recipes;
  }
}
