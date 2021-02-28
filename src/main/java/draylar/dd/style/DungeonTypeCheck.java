package draylar.dd.style;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public interface DungeonTypeCheck {
    boolean check(RegistryKey<Biome> key, Biome biome, BlockPos originPosition);
}
