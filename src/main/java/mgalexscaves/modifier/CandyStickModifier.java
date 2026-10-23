package mgalexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MeltedCaramelEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class CandyStickModifier extends GolemModifier {

	public CandyStickModifier() {
		super(StatFilterType.ATTACK, 2);
	}

	@Override
	public void onHurtTarget(AbstractGolemEntity<?, ?> golem, DamageData.Offence data, int level) {
		LivingEntity target = data.getTarget();
		var world = target.level();
		if (world.isClientSide()) return;
		MeltedCaramelEntity caramel = new MeltedCaramelEntity(ACEntityRegistry.MELTED_CARAMEL.get(), world);
		Vec3 pos = ACMath.getGroundBelowPosition(world, target.position().add(0, 0.02, 0));
		caramel.setPos(pos.x, pos.y, pos.z);
		caramel.setDespawnsIn(100 * level);
		world.addFreshEntity(caramel);
	}

}
