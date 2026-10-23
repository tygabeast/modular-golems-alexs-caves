package mgalexscaves;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(MGAlexsCaves.MODID)
public class MGAlexsCaves {

	public static final String MODID = "mg_alexscaves";

	public MGAlexsCaves(IEventBus bus, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, ACConfig.SPEC);
		// Modular Golems is ordered BEFORE this mod, so its registrates already exist.
		ACReg.register();
	}

}
