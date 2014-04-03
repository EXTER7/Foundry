package forestry.api.core;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Provides icons, needed in some interfaces, most notably for bees and trees. 
 */
public interface IIconProvider {
	
	@SideOnly(Side.CLIENT)
	IIcon getIcon(short texUID);
	
	@SideOnly(Side.CLIENT)
	void registerIcons(IIconRegister register);

}
