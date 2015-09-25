package exter.foundry.api.registry;

import net.minecraft.item.ItemStack;

@Deprecated
public interface IItemRegistry
{
  /**
   * Get a block/item by it's name
   * @param Name of the block/item
   * @return ItemStack of the item
   */
  @Deprecated
  public ItemStack GetItem(String name);
  
  /**
   * List of valid names:
   * 
   * 
   * Machines:
   * "blockMachineICF"
   * "blockMachineCaster"
   * "blockMachineAlloyMixer"
   * "blockMachineInfuser"
   * "blockMachineAlloyFurnace"
   * 
   * Metal Blocks:
   * "blockMetalIron"
   * "blockMetalGold"
   * "blockMetalCopper"
   * "blockMetalTin"
   * "blockMetalBronze"
   * "blockMetalElectrum"
   * "blockMetalInvar"
   * "blockMetalNickel"
   * "blockMetalZinc"
   * "blockMetalBrass"
   * "blockMetalSilver"
   * "blockMetalSteel"
   * "blockMetalLead"
   * 
   * Slabs:
   * "blockSlabIron"
   * "blockSlabGold"
   * "blockSlabCopper"
   * "blockSlabTin"
   * "blockSlabBronze"
   * "blockSlabElectrum"
   * "blockSlabInvar"
   * "blockSlabNickel"
   * "blockSlabZinc"
   * "blockSlabBrass"
   * "blockSlabSilver"
   * "blockSlabSteel"
   * "blockSlabLead"
   * 
   * Stairs:
   * "blockStairsIron"
   * "blockStairsGold"
   * "blockStairsCopper"
   * "blockStairsTin"
   * "blockStairsBronze"
   * "blockStairsElectrum"
   * "blockStairsInvar"
   * "blockStairsNickel"
   * "blockStairsZinc"
   * "blockStairsBrass"
   * "blockStairsSilver"
   * "blockStairsSteel"
   * "blockStairsLead"
   * 
   * Components:
   * "blockRefractoryCasing"
   * "itemStoneGear"
   * "itemHeatingCoil"
   * "itemRefractoryClay,
   * "itemRefractoryBrick"
   * "itemBlankClayMold"
   * 
   * Molds:
   * "itemIngotMold"
   * "itemChestplateMold"
   * "itemPickaxeMold"
   * "itemBlockMold"
   * "itemAxeMold"
   * "itemSwordMold"
   * "itemShovelMold"
   * "itemHoeMold"
   * "itemLeggingsMold"
   * "itemHelmetMold"
   * "itemBootsMold"
   * "itemGearMold"
   * "itemCableMold"
   * "itemCasingMold"
   * "itemSlabMold"
   * "itemStairsMold"
   * "itemPlateMold"
   * 
   * Clay molds:
   * "itemClayIngotMold"
   * "itemClayChestplateMold"
   * "itemClayPickaxeMold"
   * "itemClayBlockMold"   
   * "itemClayAxeMold"
   * "itemClaySwordMold"
   * "itemClayShovelMold"
   * "itemClayHoeMold"
   * "itemClayLeggingsMold"
   * "itemClayHelmetMold"
   * "itemClayBootsMold"
   * "itemClayGearMold"
   * "itemClayCableMold"
   * "itemClayCasingMold"
   * "itemClaySlabMold"
   * "itemClayStairsMold"
   * "itemClayPlateMold"
   * 
   * Refractory Fluid Container
   * "itemRefractoryFluidContainer"
   */
}
