package exter.foundry.model;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import exter.foundry.item.FoundryItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;

public class RFCModel implements IModel
{ 
  static private final class BakedItemModel implements IPerspectiveAwareModel
  {
    private final class RFCOverride extends ItemOverrideList
    {
      public RFCOverride()
      {
        super(ImmutableList.<ItemOverride>of());
      }

      @Override
      public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
      {
        FluidStack fluid = FoundryItems.item_container.getFluid(stack);
        if(fluid == null || fluid.amount == 0)
        {
          return originalModel;
        }
        int y = fluid.amount * 10 / FoundryItems.item_container.getCapacity(stack);
        if(y <= 0)
        {
          return originalModel;
        }
        ResourceLocation texture = fluid.getFluid().getStill();
        if(texture == null)
        {
          texture = TextureMap.LOCATION_MISSING_TEXTURE;
        }
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        builder.addAll(originalModel.getQuads(null, null, 0));
        builder.addAll(getQuadsForSpriteSlice(
            1, bakedTextureGetter.apply(texture), format, 
            4, 3,
            12, 3 + y,
            fluid.getFluid().getColor(),
            transform));
        
        return new SimpleBakedModel(builder.build(),particle,transforms);
      }
    }
    
    private final RFCOverride override = new RFCOverride();
    private final ImmutableList<BakedQuad> quads;
    private final TextureAtlasSprite particle;
    private final ImmutableMap<TransformType, TRSRTransformation> transforms;
    private final IBakedModel otherModel;
    private final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
    private final boolean isCulled;
    private final VertexFormat format;
    private final Optional<TRSRTransformation> transform;
    
    public BakedItemModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<TransformType, TRSRTransformation> transforms, IBakedModel otherModel, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, final VertexFormat format,Optional<TRSRTransformation> transform)
    {
      this.quads = quads;
      this.particle = particle;
      this.transforms = transforms;
      this.bakedTextureGetter = bakedTextureGetter;
      this.format = format;
      this.transform = transform;
      if(otherModel != null)
      {
        this.otherModel = otherModel;
        this.isCulled = true;
      } else
      {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for(BakedQuad quad : quads)
        {
          if(quad.getFace() == EnumFacing.SOUTH)
          {
            builder.add(quad);
          }
        }
        this.otherModel = new BakedItemModel(builder.build(), particle, transforms, this, bakedTextureGetter, format, transform);
        isCulled = false;
      }
    }

    public boolean isAmbientOcclusion()
    {
      return true;
    }

    public boolean isGui3d()
    {
      return false;
    }

    public boolean isBuiltInRenderer()
    {
      return false;
    }

    public TextureAtlasSprite getParticleTexture()
    {
      return particle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
      return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
      return override;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
      if(side == null)
      {
        return quads;
      }
      return ImmutableList.of();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
    {
      Pair<? extends IBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, type);
      if(type == TransformType.GUI && !isCulled && pair.getRight() == null)
      {
        return Pair.of(otherModel, null);
      } else if(type != TransformType.GUI && isCulled)
      {
        return Pair.of(otherModel, pair.getRight());
      }
      return pair;
    }
  }

  static private final class SimpleBakedModel implements IPerspectiveAwareModel
  {
    private final ImmutableList<BakedQuad> quads;
    private final TextureAtlasSprite particle;
    private final ImmutableMap<TransformType, TRSRTransformation> transforms;
    
    public SimpleBakedModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle,ImmutableMap<TransformType, TRSRTransformation> transforms)
    {
      this.quads = quads;
      this.particle = particle;
      this.transforms = transforms;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
      return true;
    }

    @Override
    public boolean isGui3d()
    {
      return false;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
      return false;
    }

    public TextureAtlasSprite getParticleTexture()
    {
      return particle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
      return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
      return ItemOverrideList.NONE;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
      if(side == null)
      {
        return quads;
      }
      return ImmutableList.of();
    }
    
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
    {
      return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, type);
    }
  }


  @Override
  public IBakedModel bake(IModelState state, final VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
  {
    Optional<TRSRTransformation> transform = state.apply(Optional.<IModelPart>absent());
    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
    builder.addAll(getQuadsForSprite(0, bakedTextureGetter.apply(texture_bg), format, transform));
    builder.addAll(getQuadsForSprite(2, bakedTextureGetter.apply(texture_fg), format, transform));
    
    ImmutableMap<TransformType, TRSRTransformation> map = IPerspectiveAwareModel.MapWrapper.getTransforms(state);
    return new BakedItemModel(builder.build(), bakedTextureGetter.apply(texture_fg), map, null, bakedTextureGetter, format,transform);
  }
  
  @Override
  public Collection<ResourceLocation> getDependencies()
  {
    return ImmutableList.of();
  }

  @Override
  public Collection<ResourceLocation> getTextures()
  {
    return ImmutableList.of(texture_fg,texture_bg);
  }

  @Override
  public IModelState getDefaultState()
  {
    return DEFAULT_STATE;
  }

  public static ImmutableList<BakedQuad> getQuadsForSprite(int tint, TextureAtlasSprite sprite, VertexFormat format, Optional<TRSRTransformation> transform)
  {
    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

    int uMax = sprite.getIconWidth();
    int vMax = sprite.getIconHeight();

    BitSet faces = new BitSet((uMax + 1) * (vMax + 1) * 4);
    for(int f = 0; f < sprite.getFrameCount(); f++)
    {
      int[] pixels = sprite.getFrameTextureData(f)[0];
      boolean ptu;
      boolean[] ptv = new boolean[uMax];
      Arrays.fill(ptv, true);
      for(int v = 0; v < vMax; v++)
      {
        ptu = true;
        for(int u = 0; u < uMax; u++)
        {
          boolean t = isTransparent(pixels, uMax, vMax, u, v);
          if(ptu && !t) // left - transparent, right - opaque
          {
            addSideQuad(builder, faces, format, transform, EnumFacing.WEST, tint, sprite, uMax, vMax, u, v);
          }
          if(!ptu && t) // left - opaque, right - transparent
          {
            addSideQuad(builder, faces, format, transform, EnumFacing.EAST, tint, sprite, uMax, vMax, u, v);
          }
          if(ptv[u] && !t) // up - transparent, down - opaque
          {
            addSideQuad(builder, faces, format, transform, EnumFacing.UP, tint, sprite, uMax, vMax, u, v);
          }
          if(!ptv[u] && t) // up - opaque, down - transparent
          {
            addSideQuad(builder, faces, format, transform, EnumFacing.DOWN, tint, sprite, uMax, vMax, u, v);
          }
          ptu = t;
          ptv[u] = t;
        }
        if(!ptu) // last - opaque
        {
          addSideQuad(builder, faces, format, transform, EnumFacing.EAST, tint, sprite, uMax, vMax, uMax, v);
        }
      }
      // last line
      for(int u = 0; u < uMax; u++)
      {
        if(!ptv[u])
        {
          addSideQuad(builder, faces, format, transform, EnumFacing.DOWN, tint, sprite, uMax, vMax, u, vMax);
        }
      }
    }

    builder.add(buildQuad(format, transform, EnumFacing.NORTH, sprite, tint,
        0, 0, 7.5f / 16f - 0.0002f * tint, sprite.getMinU(), sprite.getMaxV(),
        0, 1, 7.5f / 16f - 0.0002f * tint, sprite.getMinU(), sprite.getMinV(),
        1, 1, 7.5f / 16f - 0.0002f * tint, sprite.getMaxU(), sprite.getMinV(),
        1, 0, 7.5f / 16f - 0.0002f * tint, sprite.getMaxU(), sprite.getMaxV(),
        0xFFFFFFFF ));
    builder.add(buildQuad(format, transform, EnumFacing.SOUTH, sprite, tint,
        0, 0, 8.5f / 16f + 0.0002f * tint, sprite.getMinU(), sprite.getMaxV(),
        1, 0, 8.5f / 16f + 0.0002f * tint, sprite.getMaxU(), sprite.getMaxV(),
        1, 1, 8.5f / 16f + 0.0002f * tint, sprite.getMaxU(), sprite.getMinV(),
        0, 1, 8.5f / 16f + 0.0002f * tint, sprite.getMinU(), sprite.getMinV(),
        0xFFFFFFFF ));
      return builder.build();
  }

  static public ImmutableList<BakedQuad> getQuadsForSpriteSlice(int tint, TextureAtlasSprite sprite, VertexFormat format, int min_x, int min_y, int max_x, int max_y, int color, Optional<TRSRTransformation> transform)
  {
    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

    float min_u = sprite.getInterpolatedU(min_x);
    float min_v = sprite.getInterpolatedV(min_y);
    float max_u = sprite.getInterpolatedU(max_x);
    float max_v = sprite.getInterpolatedV(max_y);

    float x1 = (float)min_x / 16;
    float y1 = (float)min_y / 16;
    float x2 = (float)max_x / 16;
    float y2 = (float)max_y / 16;
    
    float z1 = 7.5f / 16f - 0.0002f * tint;
    float z2 = 8.5f / 16f + 0.0002f * tint;

    
    builder.add(buildQuad(format, transform, EnumFacing.NORTH, sprite, tint,
        x1, y1, z1, min_u, max_v,
        x1, y2, z1, min_u, min_v,
        x2, y2, z1, max_u, min_v,
        x2, y1, z1, max_u, max_v,
        color));

    builder.add(buildQuad(format, transform, EnumFacing.SOUTH, sprite, tint,
        x1, y1, z2, min_u, max_v,
        x2, y1, z2, max_u, max_v,
        x2, y2, z2, max_u, min_v,
        x1, y2, z2, min_u, min_v,
        color));

    return builder.build();
  }

  private static boolean isTransparent(int[] pixels, int uMax, int vMax, int u, int v)
  {
    return (pixels[u + (vMax - 1 - v) * uMax] >> 24 & 0xFF) == 0;
  }

  private static void addSideQuad(ImmutableList.Builder<BakedQuad> builder, BitSet faces, VertexFormat format, Optional<TRSRTransformation> transform, EnumFacing side, int tint, TextureAtlasSprite sprite, int uMax, int vMax, int u, int v)
  {
    int si = side.ordinal();
    if(si > 4)
    {
      si -= 2;
    }
    int index = (vMax + 1) * ((uMax + 1) * si + u) + v;
    if(!faces.get(index))
    {
      faces.set(index);
      builder.add(buildSideQuad(format, transform, side, tint, sprite, u, v));
    }
  }

  private static BakedQuad buildSideQuad(VertexFormat format, Optional<TRSRTransformation> transform, EnumFacing side, int tint, TextureAtlasSprite sprite, int u, int v)
  {
    final float eps0 = 30e-5f;
    final float eps1 = 45e-5f;
    final float eps2 = .5f;
    final float eps3 = .5f;
    float x0 = (float) u / sprite.getIconWidth();
    float y0 = (float) v / sprite.getIconHeight();
    float x1 = x0, y1 = y0;
    float z1 = 7.5f / 16f - eps1 - 0.0002f * tint;
    float z2 = 8.5f / 16f + eps1 + 0.0002f * tint;
    switch(side)
    {
      case WEST:
        z1 = 8.5f / 16f + eps1 + 0.0002f * tint;
        z2 = 7.5f / 16f - eps1 - 0.0002f * tint;
      case EAST:
        y1 = (v + 1f) / sprite.getIconHeight();
        break;
      case DOWN:
        z1 = 8.5f / 16f + eps1 + 0.0002f * tint;
        z2 = 7.5f / 16f - eps1 - 0.0002f * tint;
      case UP:
        x1 = (u + 1f) / sprite.getIconWidth();
        break;
      default:
        throw new IllegalArgumentException("can't handle z-oriented side");
    }
    float u0 = 16f * (x0 - side.getDirectionVec().getX() * eps3 / sprite.getIconWidth());
    float u1 = 16f * (x1 - side.getDirectionVec().getX() * eps3 / sprite.getIconWidth());
    float v0 = 16f * (1f - y0 - side.getDirectionVec().getY() * eps3 / sprite.getIconHeight());
    float v1 = 16f * (1f - y1 - side.getDirectionVec().getY() * eps3 / sprite.getIconHeight());
    switch(side)
    {
      case WEST:
      case EAST:
        y0 -= eps1;
        y1 += eps1;
        v0 -= eps2 / sprite.getIconHeight();
        v1 += eps2 / sprite.getIconHeight();
        break;
      case DOWN:
      case UP:
        x0 -= eps1;
        x1 += eps1;
        u0 += eps2 / sprite.getIconWidth();
        u1 -= eps2 / sprite.getIconWidth();
        break;
      default:
        throw new IllegalArgumentException("can't handle z-oriented side");
    }
    switch(side)
    {
      case WEST:
        x0 += eps0;
        x1 += eps0;
        break;
      case EAST:
        x0 -= eps0;
        x1 -= eps0;
        break;
      case DOWN:
        y0 -= eps0;
        y1 -= eps0;
        break;
      case UP:
        y0 += eps0;
        y1 += eps0;
        break;
      default:
        throw new IllegalArgumentException("can't handle z-oriented side");
    }
    return buildQuad(
        format, transform, side.getOpposite(), sprite, tint,
        x0, y0, z1, sprite.getInterpolatedU(u0), sprite.getInterpolatedV(v0),
        x1, y1, z1, sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1),
        x1, y1, z2, sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1),
        x0, y0, z2, sprite.getInterpolatedU(u0), sprite.getInterpolatedV(v0),
        0xFFFFFFFF );
  }

  private static final BakedQuad buildQuad(VertexFormat format, Optional<TRSRTransformation> transform, EnumFacing side, TextureAtlasSprite sprite, int tint, float x0, float y0, float z0, float u0, float v0, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3, int color)
  {
    UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
    builder.setQuadTint(tint);
    builder.setQuadOrientation(side);
    builder.setTexture(sprite);
    putVertex(builder, format, transform, side, x0, y0, z0, u0, v0, color);
    putVertex(builder, format, transform, side, x1, y1, z1, u1, v1, color);
    putVertex(builder, format, transform, side, x2, y2, z2, u2, v2, color);
    putVertex(builder, format, transform, side, x3, y3, z3, u3, v3, color);
    return builder.build();
  }

  private static void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, Optional<TRSRTransformation> transform, EnumFacing side, float x, float y, float z, float u, float v, int color)
  {
    Vector4f vec = new Vector4f();
    for(int e = 0; e < format.getElementCount(); e++)
    {
      switch(format.getElement(e).getUsage())
      {
        case POSITION:
          if(transform.isPresent())
          {
            vec.x = x;
            vec.y = y;
            vec.z = z;
            vec.w = 1;
            transform.get().getMatrix().transform(vec);
            builder.put(e, vec.x, vec.y, vec.z, vec.w);
          } else
          {
            builder.put(e, x, y, z, 1);
          }
          break;
        case COLOR:
        {
          float r = (float) (color & 0xFF) / 255;
          float g = (float) ((color >>> 8) & 0xFF) / 255;
          float b = (float) ((color >>> 16) & 0xFF) / 255;
          float a = (float) ((color >>> 24) & 0xFF) / 255;
          builder.put(e, r, g, b, a);
        }
          break;
        case UV:
          if(format.getElement(e).getIndex() == 0)
          {
            builder.put(e, u, v, 0f, 1f);
            break;
          }
        case NORMAL:
          builder.put(e, (float) side.getFrontOffsetX(), (float) side.getFrontOffsetY(), (float) side.getFrontOffsetZ(), 0f);
          break;
        default:
          builder.put(e);
          break;
      }
    }
  }

  static public class Loader implements ICustomModelLoader
  {
    static public Loader instance = new Loader();

    private Loader()
    {

    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {

    }

    public boolean accepts(ResourceLocation modelLocation)
    {
      return modelLocation.getResourceDomain().equals("foundry")
          && (modelLocation.getResourcePath().equals("container") || modelLocation.getResourcePath().endsWith("/container"));
    }

    public IModel loadModel(ResourceLocation modelLocation)
    {
      return RFCModel.instance;
    }
  }
  
  public static final RFCModel instance = new RFCModel();

  private final ResourceLocation texture_fg;
  private final ResourceLocation texture_bg;

  private RFCModel()
  {
    texture_fg = new ResourceLocation("foundry", "items/container_foreground");
    texture_bg = new ResourceLocation("foundry", "items/container_background");
  }
  
  static private final IModelState DEFAULT_STATE;
  
  static {
    TRSRTransformation tprh = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
        new Vector3f(0f, 4.0f / 16, 0.5f / 16),
        TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, -90, -55)),
        new Vector3f(0.65f, 0.65f, 0.65f),
        null));

    TRSRTransformation tplh = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
        new Vector3f(0f, 4.0f / 16, 0.5f / 16),
        TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 90, 55)),
        new Vector3f(0.65f, 0.65f, 0.65f),
        null));

    TRSRTransformation fprh = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
        new Vector3f(1.13f / 16, 3.2f / 16, 1.13f / 16),
        TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, -90, 25)),
        new Vector3f(0.58f, 0.58f, 0.58f),
        null));
    
    TRSRTransformation fplh = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
        new Vector3f(1.13f / 16, 3.2f / 16, 1.13f / 16),
        TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 90, -25)),
        new Vector3f(0.58f, 0.58f, 0.58f),
        null));

    TRSRTransformation gnd = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
        new Vector3f(1f / 16, 1f / 16, 0),
        TRSRTransformation.quatFromXYZ(0, 0, 0),
        new Vector3f(0.58f, 0.58f, 0.58f),
        null));

    DEFAULT_STATE = new SimpleModelState(ImmutableMap
        .of(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, tplh,
            ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, tprh,
            ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, fplh,
            ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, fprh,
            ItemCameraTransforms.TransformType.GROUND, gnd));
  }

}
