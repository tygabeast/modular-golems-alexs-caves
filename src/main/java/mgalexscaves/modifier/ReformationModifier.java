package mgalexscaves.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import mgalexscaves.ACConfig;
import mgalexscaves.ACReg;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ReformationModifier extends GolemModifier {

	private static float cap(int lv) {
		return lv * ACConfig.COMMON.reformationMax.get() * health();
	}

	private static float health() {
		return ACConfig.COMMON.reformationAbsorption.get().floatValue();
	}

	private static float heal(int lv) {
		return ACConfig.COMMON.reformationHealing.get().floatValue() * lv;
	}

	public ReformationModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	private void apply(AbstractGolemEntity<?, ?> golem, int lv) {
		golem.setAbsorptionAmount(golem.getAbsorptionAmount() + health());
		golem.repair(heal(lv));
		golem.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1f,
				1f + (golem.getRandom().nextFloat() - golem.getRandom().nextFloat()) * 0.2f);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int lv) {
		if (golem.level().isClientSide() || golem.tickCount % 20 != 0) return;
		if (golem.getAbsorptionAmount() >= cap(lv)) return;
		ItemStack stack = golem.getProjectile(ACReg.DUMMY_IRON.asStack());
		if (stack.isEmpty()) return;
		stack.shrink(1);
		apply(golem, lv);
	}

	@Override
	public InteractionResult interact(Player player, AbstractGolemEntity<?, ?> golem, InteractionHand hand, int lv) {
		ItemStack stack = player.getItemInHand(hand);
		if (!ACReg.DUMMY_IRON.get().isValid(stack)) return InteractionResult.PASS;
		if (golem.getAbsorptionAmount() >= cap(lv)) return InteractionResult.FAIL;
		if (!player.level().isClientSide()) {
			if (!player.getAbilities().instabuild) stack.shrink(1);
			apply(golem, lv);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public List<MutableComponent> getDetail(int lv) {
		return List.of(Component.translatable(getDescriptionId() + ".desc",
						Component.literal(Math.round(health()) + "").withStyle(ChatFormatting.AQUA),
						Component.literal(Math.round(heal(lv)) + "").withStyle(ChatFormatting.AQUA),
						Component.literal(Math.round(cap(lv)) + "").withStyle(ChatFormatting.AQUA))
				.withStyle(ChatFormatting.GREEN));
	}

}
