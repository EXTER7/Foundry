package gregtechmod.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Slot_Armor extends Slot {
    final int mArmorType;
    final EntityPlayer mPlayer;
    
    public GT_Slot_Armor(Container par1ContainerPlayer, IInventory par2IInventory, int par3, int par4, int par5, int par6, EntityPlayer aPlayer) {
        super(par2IInventory, par3, par4, par5);
        mArmorType = par6;
        mPlayer = aPlayer;
    }
    
    public int getSlotStackLimit() {
        return 1;
    }
    
    public boolean isItemValid(ItemStack aStack) {
        return aStack != null && aStack.getItem() != null && aStack.getItem().isValidArmor(aStack, mArmorType, mPlayer);
    }
}
