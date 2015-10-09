package exter.foundry.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WordGenOre
{
  static private List<WordGenOre> ores = new ArrayList<WordGenOre>();
  
  public final int min_y;
  public final int max_y;

  public final int frequency;

  public final IBlockState block;
  
  
  private WorldGenMinable wgm;
  
  private WordGenOre(int min,int max,int freq,IBlockState state)
  {
    if(min < max)
    {
      min_y = min;
      max_y = max;
    } else
    {
      min_y = max;
      max_y = min;
    }

    frequency = freq;

    block = state;
    wgm = new WorldGenMinable(state, 7);
  }

  private void GenerateOre(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
    int i;
    for(i = 0; i < frequency; i++)
    {
      int x = chunkX * 16 + random.nextInt(16);
      int y = min_y + random.nextInt(max_y - min_y);
      int z = chunkZ * 16 + random.nextInt(16);
      wgm.generate(world, random, new BlockPos(x,y,z));
    }
  }
  
  static public void Generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
    if(!(chunkGenerator instanceof ChunkProviderGenerate))
    {
      return;
    }
    for(WordGenOre wgo:ores)
    {
      wgo.GenerateOre(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
    }
  }
    
  static public void RegisterOre(int min_y,int max_y,int freq,IBlockState state)
  {
    ores.add(new WordGenOre(min_y,max_y,freq,state));
  }
}
