package exter.foundry.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WordGenOre
{
  static private List<WordGenOre> ores = new ArrayList<WordGenOre>();
  
  public final int MinY;
  public final int MaxY;

  public final int Frequency;

  public final int BlockID;
  public final int BlockMeta;
  
  
  private WorldGenMinable wgm;
  
  private WordGenOre(int min_y,int max_y,int freq,int block_id,int block_meta)
  {
    if(min_y < max_y)
    {
      MinY = min_y;
      MaxY = max_y;
    } else
    {
      MinY = max_y;
      MaxY = min_y;
    }

    Frequency = freq;

    BlockID = block_id;
    BlockMeta = block_meta;
    wgm = new WorldGenMinable(BlockID, BlockMeta, 7, Block.stone.blockID);
  }

  private void GenerateOre(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
  {
    int i;
    for(i = 0; i < Frequency; i++)
    {
      int x = chunkX * 16 + random.nextInt(16);
      int y = MinY + random.nextInt(MaxY - MinY);
      int z = chunkZ * 16 + random.nextInt(16);
      wgm.generate(world, random, x, y, z);
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
    
  static public void RegisterOre(int min_y,int max_y,int freq,int block_id,int block_meta)
  {
    ores.add(new WordGenOre(min_y,max_y,freq,block_id,block_meta));
  }
}
