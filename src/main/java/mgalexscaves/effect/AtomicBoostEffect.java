package mgalexscaves.effect;

import dev.xkmc.l2core.base.effects.api.InherentEffect;
import mgalexscaves.MGAlexsCaves;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AtomicBoostEffect extends InherentEffect {

	public AtomicBoostEffect(MobEffectCategory category, int color) {
		super(category, color);
		var id = ResourceLocation.fromNamespaceAndPath(MGAlexsCaves.MODID, "atomic_boost");
		addAttributeModifier(Attributes.ATTACK_DAMAGE, id, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, id, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

}
