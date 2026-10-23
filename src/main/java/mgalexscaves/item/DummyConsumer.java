package mgalexscaves.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

import java.util.function.Predicate;

/**
 * Never held by anything. It only lets a golem ask "do I carry something matching this tag?"
 * through the vanilla projectile-lookup path, the same trick Modular Golems uses for its furnace dummy.
 */
public class DummyConsumer extends ProjectileWeaponItem {

	private final TagKey<Item> ammo;

	public DummyConsumer(TagKey<Item> ammo) {
		super(new Item.Properties());
		this.ammo = ammo;
	}

	public boolean isValid(ItemStack stack) {
		return stack.is(ammo);
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return this::isValid;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 0;
	}

	@Override
	protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index,
								   float velocity, float inaccuracy, float angle, LivingEntity target) {
	}

}
