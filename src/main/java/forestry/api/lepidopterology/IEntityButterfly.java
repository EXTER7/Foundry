package forestry.api.lepidopterology;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;

import forestry.api.genetics.IIndividual;

public interface IEntityButterfly extends IAnimals {

	void changeExhaustion(int change);

	int getExhaustion();

	IButterfly getButterfly();

	/**
	 * @return The entity as an EntityCreature to save casting.
	 */
	EntityCreature getEntity();

	IIndividual getPollen();

	void setPollen(IIndividual pollen);
}
