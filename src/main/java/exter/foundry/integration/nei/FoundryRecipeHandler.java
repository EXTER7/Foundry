package exter.foundry.integration.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import exter.foundry.item.ItemRefractoryFluidContainer;

public abstract class FoundryRecipeHandler  extends TemplateRecipeHandler
{
  public static Point TANK_OVERLAY = new Point(176, 0);

  public abstract class CachedFoundryRecipe extends TemplateRecipeHandler.CachedRecipe
  {

    public int createdAt;

    public CachedFoundryRecipe()
    {
      createdAt = cycleticks;
    }

    public List<FluidTank> getTanks()
    {
      List<FluidTank> result = Lists.newArrayList();
      if(getTank() != null)
      {
        result.add(getTank());
      }
      return result;
    }

    public FluidTank getTank()
    {
      return null;
    }

    public int getAgeTicks()
    {
      return cycleticks - createdAt;
    }

    @Override
    public PositionedStack getResult()
    {
      return null;
    }
  }

  public static class FluidTank
  {
    public FluidStack fluid;
    public int capacity;
    public Rectangle position;

    public FluidTank(FluidStack fluid, int capacity, Rectangle position)
    {
      this.fluid = fluid;
      this.capacity = capacity;
      this.position = position;
    }
  }

  public static class ProgressBar
  {

    public int posX;
    public int posY;
    public int texX;
    public int texY;
    public int maxWidth;
    public int height;

    public int minValue;
    public int maxValue;

    public ProgressBar(int posX, int posY, int texX, int texY, int maxWidth, int height, int minValue, int maxValue)
    {
      this.posX = posX;
      this.posY = posY;
      this.texX = texX;
      this.texY = texY;
      this.maxWidth = maxWidth;
      this.height = height;
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    public Rectangle asRectangle()
    {
      return new Rectangle(posX, posY, maxWidth, height);
    }
  }


  public abstract Rectangle getRecipeRect();

  public abstract ItemStack getMachineItem();

  @Override
  public boolean mouseClicked(GuiRecipe gui, int button, int recipe)
  {
    CachedFoundryRecipe foundryRecipe = (CachedFoundryRecipe) arecipes.get(recipe);
    if(button == 1 && isMouseOver(getRecipeRect(),gui,recipe))
    {
      return GuiCraftingRecipe.openRecipeGui("item", getMachineItem());
    }
    for(FluidTank tank : foundryRecipe.getTanks())
    {
      if(isMouseOver(tank.position, gui, recipe))
      {
        if(button == 0)
        {
          return GuiCraftingRecipe.openRecipeGui("liquid", tank.fluid);
        }
        if(button == 1)
        {
          return GuiUsageRecipe.openRecipeGui("liquid", tank.fluid);
        }
      }
    }
    return super.mouseClicked(gui, button, recipe);
  }

  @Override
  public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
  {
    CachedFoundryRecipe foundryRecipe = (CachedFoundryRecipe) arecipes.get(recipe);
    for(FluidTank tank : foundryRecipe.getTanks())
    {
      if(isMouseOver(tank.position, gui, recipe))
      {
        currenttip.add(tank.fluid.getLocalizedName());
        currenttip.add(String.valueOf(EnumChatFormatting.GRAY) + tank.fluid.amount + " mB");
      }
    }
    return super.handleTooltip(gui, currenttip, recipe);
  }

  protected void drawTanks(List<FluidTank> tanks, int drawMultiplier, Point overlayLocation)
  {
    FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
    for(FluidTank tank : tanks)
    {
      int times = 1;
      if(tank.fluid.amount != 0)
      {
        times = drawMultiplier % (tank.capacity / tank.fluid.amount) + 1;
      }
      int drawHeight = times * tank.fluid.amount * tank.position.height / tank.capacity;
      
      int color = tank.fluid.getFluid().getColor(tank.fluid);
      float red = (float) (color >> 16 & 255) / 255.0F;
      float green = (float) (color >> 8 & 255) / 255.0F;
      float blue = (float) (color & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, 1.0f);
      drawFluidVertical(tank.position.x, tank.position.y + tank.position.height, tank.position.width, drawHeight, tank.fluid.getFluid());
      GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
      GuiDraw.changeTexture(getGuiTexture());
      GuiDraw.drawTexturedModalRect(tank.position.x, tank.position.y, overlayLocation.x, overlayLocation.y, tank.position.width, tank.position.height);
    }
  }

  protected void drawProgressBar(ProgressBar bar, int value)
  {
    int width = (value - bar.minValue) * bar.maxWidth / (bar.maxValue - bar.minValue);
    GuiDraw.drawTexturedModalRect(bar.posX, bar.posY, bar.texX, bar.texY, width, bar.height);
  }
  
  protected Object asItemStackOrList(Object item)
  {
    if(item instanceof String)
    {
      for(String oreName : OreDictionary.getOreNames())
      {
        if(oreName.equals(item))
        {
          return OreDictionary.getOres(oreName);
        }
      }
      return null;
    }
    return item;
  }

  protected FluidStack getFluidStackFor(Object input)
  {
    if(input instanceof FluidStack)
    {
      return (FluidStack) input;
    }
    if(input instanceof ItemStack)
    {
      ItemStack stack = (ItemStack) input;
      Item stack_item = stack.getItem();
      if(stack_item instanceof ItemRefractoryFluidContainer)
      {
        return ((ItemRefractoryFluidContainer) stack.getItem()).getFluid(stack);
      }
      if(stack_item instanceof ItemBlock)
      {
        Block block = ((ItemBlock)stack_item).block;
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
        if(fluid != null)
        {
          return new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
        }
      }
      return FluidContainerRegistry.getFluidForFilledItem(stack);
    }
    return null;
  }

  protected boolean isEmptyOre(Object item)
  {
    if(item instanceof String)
    {
      for(String oreName : OreDictionary.getOreNames())
      {
        if(oreName.equals(item))
        {
          return OreDictionary.getOres(oreName).size() == 0;
        }
      }
      return true;
    }
    return false;
  }


  public boolean isMouseOver(Rectangle rect, GuiRecipe gui, int recipe)
  {
    Point offset = gui.getRecipePosition(recipe);
    
    //Workaround to access GUI protected fields.
    int gui_guiLeft = (Integer)ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, 4);
    int gui_guiTop = (Integer)ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, 5);
    
    Rectangle absoluteLoc = new Rectangle(rect.x + offset.x + gui_guiLeft, rect.y + offset.y + gui_guiTop, rect.width, rect.height);
    return absoluteLoc.contains(GuiDraw.getMousePosition());
  }

  public void drawRectWithIconAligned(Rectangle rect, TextureAtlasSprite icon, boolean alignLeft, boolean alignTop)
  {
    float minU = alignLeft ? icon.getInterpolatedU(0) : icon.getInterpolatedU(16 - rect.width);
    float minV = alignTop ? icon.getInterpolatedV(0) : icon.getInterpolatedV(16 - rect.height);
    float maxU = alignLeft ? icon.getInterpolatedU(rect.width) : icon.getInterpolatedU(16);
    float maxV = alignTop ? icon.getInterpolatedV(rect.height) : icon.getInterpolatedV(16);
    double z = GuiDraw.gui.getZLevel();

    WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(rect.x, rect.y + rect.height, z, minU, maxV);
    tessellator.addVertexWithUV(rect.x + rect.width, rect.y + rect.height, z, maxU, maxV);
    tessellator.addVertexWithUV(rect.x + rect.width, rect.y, z, maxU, minV);
    tessellator.addVertexWithUV(rect.x, rect.y, z, minU, minV);
    tessellator.finishDrawing();
  }

  public void drawFluidVertical(int left, int bottom, int width, int height, Fluid fluid)
  {
    FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
    int toDraw = height;
    TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill().toString());

    while(toDraw >= 16)
    {
      drawRectWithIconAligned(new Rectangle(left, bottom - 16, width, 16), icon, true, false);
      bottom -= 16;
      toDraw -= 16;
    }
    if(toDraw > 0)
    {
      drawRectWithIconAligned(new Rectangle(left, bottom - toDraw, width, toDraw), icon, true, false);
    }
  }
}
