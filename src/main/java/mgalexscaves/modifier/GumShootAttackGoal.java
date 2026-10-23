package mgalexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.entity.item.GumballEntity;
import dev.xkmc.mob_weapon_api.util.ShootUtils;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import mgalexscaves.ACConfig;
import net.minecraft.world.entity.LivingEntity;

public class GumShootAttackGoal extends BaseRangedAttackGoal {

	public GumShootAttackGoal(int wait, int near, int far, AbstractGolemEntity<?, ?> golem, int lv) {
		super(wait, near, far, golem, lv);
	}

	@Override
	protected boolean performAttack(LivingEntity target) {
		var level = golem.level();
		GumballEntity ball = new GumballEntity(level, golem);
		var eye = golem.getEyePosition();
		ball.setPos(eye.x, eye.y, eye.z);
		ball.setDamage((float) (ACConfig.COMMON.candyDamage.get() * lv));
		ShootUtils.shootAimHelper(target, ball, 1.5f, 0.08f);
		level.addFreshEntity(ball);
		return true;
	}

}
