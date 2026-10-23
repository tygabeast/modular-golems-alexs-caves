package mgalexscaves;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Replacement for the Alex's Caves entries that used to live in Modular Golems' own common config. */
public class ACConfig {

	public static final ModConfigSpec SPEC;
	public static final ACConfig COMMON;

	static {
		var pair = new ModConfigSpec.Builder().configure(ACConfig::new);
		COMMON = pair.getLeft();
		SPEC = pair.getRight();
	}

	public final ModConfigSpec.DoubleValue candyDamage;
	public final ModConfigSpec.IntValue caramelDuration;
	public final ModConfigSpec.DoubleValue polarizeRange;
	public final ModConfigSpec.DoubleValue polarizeDamage;
	public final ModConfigSpec.DoubleValue polarizeForce;
	public final ModConfigSpec.IntValue polarizeParticles;
	public final ModConfigSpec.DoubleValue reformationAbsorption;
	public final ModConfigSpec.DoubleValue reformationHealing;
	public final ModConfigSpec.IntValue reformationMax;
	public final ModConfigSpec.IntValue radiationDuration;
	public final ModConfigSpec.DoubleValue radiationDamage;
	public final ModConfigSpec.DoubleValue atomicHeal;
	public final ModConfigSpec.IntValue atomicDuration;

	private ACConfig(ModConfigSpec.Builder b) {
		b.push("alexscaves");
		candyDamage = b.comment("Damage of gumballs shot by Gum Shooter, per level")
				.defineInRange("candyDamage", 4.0, 1, 100);
		caramelDuration = b.comment("Duration of caramel left by Sticky Caramel, in ticks, per level")
				.defineInRange("caramelDuration", 200, 1, 10000);
		polarizeRange = b.comment("Range of Polarize")
				.defineInRange("polarizeRange", 5.0, 1, 40);
		polarizeDamage = b.comment("Damage of Polarize")
				.defineInRange("polarizeDamage", 2.0, 0, 100);
		polarizeForce = b.comment("Pull/push force of Polarize")
				.defineInRange("polarizeForce", 0.1, 0, 1);
		polarizeParticles = b.comment("Polarize visual effect: 0 = off, 1 = light (default, a few small sparks, no large bolts), 2 = heavy (the old ring of large lightning bolts, expensive)")
				.defineInRange("polarizeParticles", 1, 0, 2);
		reformationAbsorption = b.comment("Absorption gained per iron ingot with Reformation")
				.defineInRange("reformationAbsorption", 10.0, 1, 100);
		reformationHealing = b.comment("Healing per iron ingot with Reformation, per level")
				.defineInRange("reformationHealing", 2.0, 0, 100);
		reformationMax = b.comment("Max number of iron ingots worth of absorption, per level")
				.defineInRange("reformationMax", 2, 1, 10);
		radiationDuration = b.comment("Duration of irradiated effect applied by Radiation, in ticks")
				.defineInRange("radiationDuration", 200, 20, 20000);
		radiationDamage = b.comment("Extra damage per irradiated level against irradiated targets (0.2 = 20%)")
				.defineInRange("radiationDamage", 0.2, 0, 1);
		atomicHeal = b.comment("Fraction of max health healed per uranium shard, per level")
				.defineInRange("atomicHeal", 0.1, 0, 1);
		atomicDuration = b.comment("Duration of Atomic Boost per uranium shard, in ticks")
				.defineInRange("atomicDuration", 200, 20, 20000);
		b.pop();
	}

}
