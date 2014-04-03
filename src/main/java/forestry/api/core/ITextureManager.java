package forestry.api.core;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ITextureManager {

	void registerIconProvider(IIconProvider provider);

	IIcon getIcon(short texUID);

	IIcon getDefault(String ident);
}
