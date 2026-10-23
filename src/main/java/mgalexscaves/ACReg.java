package mgalexscaves;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.modulargolems.content.item.upgrade.CraftMaterialItem;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import mgalexscaves.effect.AtomicBoostEffect;
import mgalexscaves.item.DummyConsumer;
import mgalexscaves.modifier.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Item;

/**
 * Everything is registered through Modular Golems' own registrate and under the
 * "modulargolems" namespace, so ids match the old built-in integration
 * (modulargolems:polarize, modulargolems:candy_construct, ...).
 */
public class ACReg {

	public static final ItemEntry<Item> CANDY_MIXTURE, MAGNETIC_ALLOY;
	public static final ItemEntry<CraftMaterialItem> CANDY_CONSTRUCT, MAGNETIC_CONSTRUCT, NUCLEAR_CONSTRUCT;
	public static final ItemEntry<DummyConsumer> DUMMY_IRON, DUMMY_URANIUM;

	public static final SimpleEntry<MobEffect> EFF_ATOMIC;

	public static final Val<CandyStickModifier> STICKY_CARAMEL;
	public static final Val<CandyShootModifier> GUM_SHOOTER;
	public static final Val<FreeMoveModifier> FREE_MOVE;
	public static final Val<PolarizeModifier> POLARIZE;
	public static final Val<ReformationModifier> REFORMATION;
	public static final Val<RadiationModifier> RADIATION;
	public static final Val<AtomicFuelingModifier> ATOMIC_FUELING;

	static {
		var r = ModularGolems.REGISTRATE;
		CANDY_MIXTURE = r.item("candy_mixture", Item::new).register();
		MAGNETIC_ALLOY = r.item("magnetic_alloy", Item::new).register();
		CANDY_CONSTRUCT = r.item("candy_construct", CraftMaterialItem::new).register();
		MAGNETIC_CONSTRUCT = r.item("magnetic_construct", CraftMaterialItem::new).register();
		NUCLEAR_CONSTRUCT = r.item("nuclear_construct", CraftMaterialItem::new).register();

		DUMMY_IRON = r.item("dummy_iron_consumer", p -> new DummyConsumer(
				TagKey.create(net.minecraft.core.registries.Registries.ITEM,
						net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "ingots/iron")))).register();
		DUMMY_URANIUM = r.item("dummy_uranium_consumer", p -> new DummyConsumer(
				TagKey.create(net.minecraft.core.registries.Registries.ITEM,
						net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "nuggets/uranium")))).register();

		EFF_ATOMIC = new SimpleEntry<>(r.effect("atomic_boost",
						() -> new AtomicBoostEffect(MobEffectCategory.BENEFICIAL, 0x7CFF4D),
						"Increase golem attack damage and movement speed")
				.register());

		STICKY_CARAMEL = GolemModifiers.reg("sticky_caramel", CandyStickModifier::new,
				"Put molten caramel on attack targets");
		GUM_SHOOTER = GolemModifiers.reg("gum_shooter", CandyShootModifier::new,
				"Shoot gumballs at targets dealing %s damage");
		FREE_MOVE = GolemModifiers.reg("free_movement", FreeMoveModifier::new,
				"Golem will not be stuck by blocks or caramel");
		POLARIZE = GolemModifiers.reg("polarize", PolarizeModifier::new,
				"Pull enemies in melee mode and push enemies away in ranged/standing mode, deal %s electrical damage constantly.");
		REFORMATION = GolemModifiers.reg("reformation", ReformationModifier::new,
				"Consume iron ingot to gain %s absorption and heal %s. Golem may hold at most %s absorption");
		RADIATION = GolemModifiers.reg("radiation", RadiationModifier::new,
				"Inflict %s to attack targets. Damage to radiated target increase by %s per radiation level.");
		ATOMIC_FUELING = GolemModifiers.reg("atomic_fueling", AtomicFuelingModifier::new,
				"Consume [%s] to heal %s HP, and boost attack and speed by %s for %s seconds. Makes golem immune to irradiated effect.");
	}

	public static void register() {
		// class initialisation performs all registration
	}

}
