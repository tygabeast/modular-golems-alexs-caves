package mgalexscaves.modifier;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import mgalexscaves.ACConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PolarizeModifier extends GolemModifier {

	private static double range(int lv) {
		return (lv + 4) * 0.2 * ACConfig.COMMON.polarizeRange.get();
	}

	private static float damage(int lv) {
		return (lv + 1) / 2f * ACConfig.COMMON.polarizeDamage.get().floatValue();
	}

	private static double force() {
		return ACConfig.COMMON.polarizeForce.get();
	}

	private static boolean pulling(AbstractGolemEntity<?, ?> golem) {
		return !(golem.isInRangedMode() || golem.hasRangeAttack());
	}

	public PolarizeModifier() {
		super(StatFilterType.MASS, 4);
	}

	@Override
	public void onClientTick(AbstractGolemEntity<?, ?> golem, int lv) {
		int mode = ACConfig.COMMON.polarizeParticles.get();
		if (mode == 0 || !golem.isAlive()) return;
		var level = golem.level();
		double r = range(lv);
		boolean pull = pulling(golem);
		if (mode == 1) {
			if (golem.tickCount % 10 != 0) return;
			if (level.getNearestPlayer(golem, 32) == null) return;
			for (int i = 0; i < 3; i++) {
				double a = golem.getRandom().nextDouble() * Math.PI * 2;
				level.addParticle(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK,
						golem.getX() + Math.cos(a) * r, golem.getY() + 0.1,
						golem.getZ() + Math.sin(a) * r, 0, 0, 0);
			}
			return;
		}
		if (golem.tickCount % 5 != 0) return;
		ParticleOptions type = pull ? ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get()
				: ACParticleRegistry.AZURE_SHIELD_LIGHTNING.get();
		int n = 12;
		double off = golem.getRandom().nextDouble() * Math.PI * 2;
		for (int i = 0; i < n; i++) {
			double a = off + i * Math.PI * 2 / n;
			level.addParticle(type, golem.getX() + Math.cos(a) * r, golem.getY() + 0.1,
					golem.getZ() + Math.sin(a) * r, 0, 0, 0);
		}
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int lv) {
		if (golem.level().isClientSide() || golem.getTarget() == null) return;
		double r = range(lv);
		boolean pull = pulling(golem);
		Vec3 look = golem.getLookAngle();
		double reach = Math.max(1, golem.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) * 0.8);
		Vec3 focus = golem.position().add(look.scale(reach));
		boolean hit = golem.tickCount % 10 == 0;
		var list = golem.level().getEntitiesOfClass(LivingEntity.class,
				golem.getBoundingBox().inflate(r), e -> e != golem && e.isAlive() && golem.canAttack(e)
						&& e.distanceToSqr(golem) <= r * r);
		for (LivingEntity e : list) {
			if (hit) e.hurt(golem.damageSources().mobAttack(golem), damage(lv));
			Vec3 dir = pull ? focus.subtract(e.position()) : e.position().subtract(golem.position());
			if (dir.lengthSqr() < 1e-4) continue;
			e.setDeltaMovement(e.getDeltaMovement().add(dir.normalize().scale(force())));
			e.hurtMarked = true;
		}
	}

	@Override
	public List<MutableComponent> getDetail(int lv) {
		return List.of(Component.translatable(getDescriptionId() + ".desc",
				Component.literal(Math.round(damage(lv)) + "").withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GREEN));
	}

}
