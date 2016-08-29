package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
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


public class CastingTableJEI
{

  public class Wrapper implements IRecipeWrapper
  {
    private final String name;
    @Nonnull
    private final List<FluidStack> input_fluid;
    @Nonnull
    private final List<ItemStack> output;
    @Nonnull
    private final List<List<ItemStack>> input;

    public Wrapper(String name,@Nonnull ItemStack output, FluidStack input)
    {
      this.name = name;
      this.input_fluid = Collections.singletonList(input);
      this.input = Collections.singletonList(Collections.singletonList(table_item));
      this.output = Collections.singletonList(output);
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
      return input_fluid;
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

  public class Category implements IRecipeCategory<Wrapper>
  {

    protected final ResourceLocation backgroundLocation;
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    
    private IJeiHelpers helpers;

    public Category(IJeiHelpers helpers)
    {
      this.helpers = helpers;
      IGuiHelper guiHelper = helpers.getGuiHelper();
      backgroundLocation = new ResourceLocation("foundry", "textures/gui/casting_table_jei.png");

      ResourceLocation location = new ResourceLocation("foundry", "textures/gui/casting_table_jei.png");
      background = guiHelper.createDrawable(location, 0, 0, 74, 59);
      localizedName = Translator.translateToLocal("gui.jei.casting_table." + name);

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
      return "foundry.casting_table." + name;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull Wrapper recipeWrapper)
    {
      IGuiItemStackGroup gui_items = recipeLayout.getItemStacks();
      IGuiFluidStackGroup gui_fluids = recipeLayout.getFluidStacks();
      IStackHelper stack_helper = helpers.getStackHelper();

      gui_items.init(0, false, 53, 20);
      gui_items.init(1, true, 3, 39);
      gui_fluids.init(2, true, 4, 4, 16, 24, recipeWrapper.input_fluid.get(0).amount,false,null);

      gui_items.setFromRecipe(0, stack_helper.toItemStackList(recipeWrapper.getOutputs().get(0)));
      gui_items.set(1, table_item);
      gui_fluids.set(2, recipeWrapper.getFluidInputs().get(0));
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
      return "foundry.casting_table";
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
      return "foundry.casting_table." + recipe.name;
    }
  }
  
  private final ItemStack table_item;
  private final ICastingTableRecipe.TableType type;
  private final String name;
  
  public CastingTableJEI(BlockCastingTable.EnumTable table)
  {
    table_item = FoundryBlocks.block_casting_table.asItemStack(table);
    name = table.name;
    type = table.type;
  }

  public List<Wrapper> getRecipes()
  {
    List<Wrapper> recipes = new ArrayList<Wrapper>();

    for(ICastingTableRecipe recipe : CastingTableRecipeManager.instance.getRecipes())
    {
      if(recipe.getTableType() == type)
      {
        ItemStack output = recipe.getOutput();

        if(output != null)
        {
          recipes.add(new Wrapper(name,output,recipe.getInput()));
        }
      }
    }

    return recipes;
  }
}
