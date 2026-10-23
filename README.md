# Modular Golems: Alex's Caves Restored (source)

Code addon for **Modular Golems 3.1.43** + the **Alex's Caves** fork on **NeoForge 21.1.x / Minecraft 1.21.1**.
Restores what the old 2.7.4 built-in integration had:

* Materials: Candy, Magnetic, Nuclear (stats, textures, Create sequenced-assembly recipes, guide entries)
* Items: candy_mixture, candy_construct, magnetic_alloy, magnetic_construct, nuclear_construct (+ two hidden dummy items)
* Abilities (ids unchanged, under `modulargolems:`): sticky_caramel, gum_shooter, free_movement, polarize,
  reformation, radiation, atomic_fueling; effect `atomic_boost`
* Config `config/mg_alexscaves-common.toml` (the old values, moved out of Modular Golems' config)

## Build
1. Put in `libs/`: `modulargolems-3.1.43.jar`, your `alexscaves-*.jar`, `l2library-3.0.8.jar`.
2. `python3 setup_libs.py`  (extracts L2Core, Registrate, L2Serial, L2DamageTracker, mob_weapon_api from the jar-in-jar folders)
3. `gradle build`  (JDK 21, Gradle 8.8+). Output: `build/libs/mg_alexscaves-2.0.6.jar`

## Install
Replace the previous data-only `mg_alexscaves-1.0.0.jar` with this one (same mod id). Needs Modular Golems, Alex's Caves.
Existing golems keep working; the new modifiers are on the materials, so golems built from Candy/Magnetic/Nuclear parts after
the update get them (the material config is read live).

## Licence
LGPL-2.1 (see `LICENSE`), copyright Tygabeast, because parts of the code were ported from Modular Golems 2.7.4. See `NOTICE.md` for the
third-party material (textures copied from 2.7.4) and credits.

## Status
Ability code is a port from decompiled 2.7.4 bytecode. Polarize's pull/push maths and the sounds are reconstructed
approximations. Compiles cleanly against NeoForge 21.1.256, Modular Golems 3.1.43 and the Alex's Caves fork and has been playtested by the author.
There is no Gradle wrapper: add one from the NeoForge MDK (`gradle wrapper`) if you want `./gradlew`.
