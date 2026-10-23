package mgalexscaves.modifier;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import mgalexscaves.ACConfig;
import mgalexscaves.ACReg;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public class AtomicFuelingModifier extends GolemModifier {

	private static int time() {
		return ACConfig.COMMON.atomicDuration.get();
	}

	private static float heal() {
		return ACConfig.COMMON.atomicHeal.get().floatValue();
	}

	/** L2Complements is optional; look its curse effect up by id instead of linking against it. */
	private static MobEffect curse() {
		return BuiltInRegistries.MOB_EFFECT.getOptional(
				ResourceLocation.fromNamespaceAndPath("l2complements", "curse")).orElse(null);
	}

	public AtomicFuelingModifier() {
		super(StatFilterType.HEALTH, 2);
	}

	private void perform(AbstractGolemEntity<?, ?> golem, int lv) {
		float max = golem.getMaxHealth();
		MobEffect curse = curse();
		if (curse != null && golem.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(curse))) {
			golem.removeEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(curse));
		}
		golem.repair(max * heal() * lv);
		Holder<MobEffect> boost = ACReg.EFF_ATOMIC.holder();
		var old = golem.getEffect(boost);
		int dur = old == null ? 0 : old.getDuration();
		golem.addEffect(new MobEffectInstance(boost, dur + time(), lv));
		golem.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1f,
				1f + (golem.getRandom().nextFloat() - golem.getRandom().nextFloat()) * 0.2f);
	}

	@Override
	public void onAiStep(AbstractGolemEntity<?, ?> golem, int lv) {
		if (golem.level().isClientSide() || golem.tickCount % 20 != 0) return;
		if (golem.getHealth() > golem.getMaxHealth() * (1 - heal() * lv)) return;
		ItemStack stack = golem.getProjectile(ACReg.DUMMY_URANIUM.asStack());
		if (stack.isEmpty()) return;
		stack.shrink(1);
		perform(golem, lv);
	}

	@Override
	public InteractionResult interact(Player player, AbstractGolemEntity<?, ?> golem, InteractionHand hand, int lv) {
		ItemStack stack = player.getItemInHand(hand);
		if (!ACReg.DUMMY_URANIUM.get().isValid(stack)) return InteractionResult.PASS;
		if (golem.getHealth() >= golem.getMaxHealth()) return InteractionResult.FAIL;
		if (!player.level().isClientSide()) {
			if (!player.getAbilities().instabuild) stack.shrink(1);
			perform(golem, lv);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRegisterGoals(AbstractGolemEntity<?, ?> golem, int lv, BiConsumer<Integer, Goal> addGoal) {
		golem.effectImmunity.add(ACEffectRegistry.IRRADIATED.get());
		MobEffect curse = curse();
		if (curse != null) golem.effectImmunity.add(curse);
	}

	@Override
	public List<MutableComponent> getDetail(int lv) {
		var name = ACItemRegistry.URANIUM_SHARD.get().getDefaultInstance().getHoverName();
		var healTxt = Component.literal(Math.round(heal() * 100 * lv) + "%").withStyle(ChatFormatting.AQUA);
		var boostTxt = Component.literal("20%").withStyle(ChatFormatting.AQUA);
		var secs = Component.literal(time() / 20 + "").withStyle(ChatFormatting.AQUA);
		return List.of(Component.translatable(getDescriptionId() + ".desc", name, healTxt, boostTxt, secs)
				.withStyle(ChatFormatting.GREEN));
	}

}
