package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.interfaces.ICoverable;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.interfaces.IMetaTileEntity;
import gregtechmod.api.metatileentity.BaseMetaPipeEntity;
import gregtechmod.api.metatileentity.MetaPipeEntity;
import gregtechmod.api.util.GT_Utility;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Icon;

public abstract class GT_MetaPipeEntity_Frame extends MetaPipeEntity {
	
	public GT_MetaPipeEntity_Frame(int aID, String mName, String mNameRegional) {
		super(aID, mName, mNameRegional);
	}
	
	public GT_MetaPipeEntity_Frame() {
		
	}
	
	@Override public String getDescription() {return "Just something you can put a Cover or CFoam on.";}
	@Override public int getTextureIndex(byte aSide, byte aConnections, boolean aActive, boolean aRedstone) {return 3;}
	
	@Override public final boolean isSimpleMachine()				{return true;}
	@Override public final boolean isFacingValid(byte aFacing)		{return false;}
	@Override public final boolean isValidSlot(int aIndex)			{return false;}
    @Override public final int getInvSize()							{return 0;}
    @Override public final boolean renderInside()					{return true;}
	@Override public final float getThickNess() {return 1.0F;}
	@Override public final void saveNBTData(NBTTagCompound aNBT) {}
	@Override public final void loadNBTData(NBTTagCompound aNBT) {}
	@Override public final boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack) {return false;}
	@Override public final boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack) {return false;}
}