package mgalexscaves.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import mgalexscaves.ACConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.BiConsumer;

public class CandyShootModifier extends GolemModifier {

	public CandyShootModifier() {
		super(StatFilterType.HEAD, 1);
	}

	@Override
	public void onRegisterGoals(AbstractGolemEntity<?, ?> golem, int level, BiConsumer<Integer, Goal> addGoal) {
		addGoal.accept(2, new GumShootAttackGoal(20, 2, 16, golem, level));
	}

	@Override
	public List<MutableComponent> getDetail(int level) {
		int dmg = Math.round((float) (ACConfig.COMMON.candyDamage.get() * level));
		return List.of(Component.translatable(getDescriptionId() + ".desc",
				Component.literal(dmg + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GREEN));
	}

}
