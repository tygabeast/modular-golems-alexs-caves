package mgalexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import dev.xkmc.l2core.base.effects.EffectUtil;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import mgalexscaves.ACConfig;
import mgalexscaves.MGAlexsCaves;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class RadiationModifier extends GolemModifier {

	private static Holder<MobEffect> irradiated() {
		return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ACEffectRegistry.IRRADIATED.get());
	}

	public RadiationModifier() {
		super(StatFilterType.ATTACK, 2);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> golem, DamageData.Offence data, int lv) {
		var target = data.getTarget();
		var eff = irradiated();
		var old = target.getEffect(eff);
		if (old != null) {
			float bonus = 1 + (old.getAmplifier() + 1) * ACConfig.COMMON.radiationDamage.get().floatValue();
			data.addHurtModifier(DamageModifier.multTotal(bonus,
					ResourceLocation.fromNamespaceAndPath(MGAlexsCaves.MODID, "radiation")));
		}
		if (!target.level().isClientSide()) {
			EffectUtil.addEffect(target, new MobEffectInstance(eff,
					ACConfig.COMMON.radiationDuration.get(), lv * 2 - 1), golem);
		}
	}

	@Override
	public List<MutableComponent> getDetail(int lv) {
		var name = Component.translatable(irradiated().value().getDescriptionId());
		MutableComponent eff = Component.translatable("potion.withAmplifier", name,
						Component.translatable("potion.potency." + (lv * 2 - 1)))
				.copy();
		eff = Component.translatable("potion.withDuration", eff,
				Component.literal(StringUtil.formatTickDuration(ACConfig.COMMON.radiationDuration.get(), 20)));
		eff = eff.withStyle(ChatFormatting.RED);
		var pct = Component.literal(Math.round(ACConfig.COMMON.radiationDamage.get() * 100) + "%")
				.withStyle(ChatFormatting.AQUA);
		return List.of(Component.translatable(getDescriptionId() + ".desc", eff, pct).withStyle(ChatFormatting.GREEN));
	}

}
