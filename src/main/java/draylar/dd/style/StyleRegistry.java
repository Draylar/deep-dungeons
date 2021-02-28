package draylar.dd.style;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class StyleRegistry {

    private static final Style FALLBACK_STYLE = new Style().addWallBlock(Blocks.COBBLESTONE).addWallBlock(Blocks.MOSSY_COBBLESTONE).addLootTable(new Identifier("chests/simple_dungeon"), 1.0);
    private static final Map<StyleRequirement, Pair<Integer, Style>> BIOME_STYLES = new HashMap<>();

    public static void registerBiomeStyle(StyleRequirement requirement, int priority, Style style) {
        BIOME_STYLES.put(requirement, new Pair<>(priority, style));
    }

    public static Style get(RegistryKey<Biome> key, Biome biome, BlockPos origin) {
        Map<Integer, List<Style>> pools = new HashMap<>();

        for (Map.Entry<StyleRequirement, Pair<Integer, Style>> entry : BIOME_STYLES.entrySet()) {
            StyleRequirement styleRequirement = entry.getKey();
            Style style = entry.getValue().getRight();
            int priority = entry.getValue().getLeft();

            if(styleRequirement.check(key, biome, origin)) {
                if(!pools.containsKey(priority)) {
                    pools.put(priority, new ArrayList<>());
                }

                pools.get(entry.getValue().getLeft()).add(style);
            }
        }

        if(pools.isEmpty()) {
            return FALLBACK_STYLE;
        } else {
            List<Style> highestPriorityPool = new ArrayList<>();
            int highestPriority = -999;

            for (Map.Entry<Integer, List<Style>> entry : pools.entrySet()) {
                Integer priority = entry.getKey();
                List<Style> pool = entry.getValue();

                // If the priority for this pool is higher than the current highest priority pool,
                //   replace the pool with our new one.
                if(priority > highestPriority) {
                    highestPriorityPool = pool;
                }
            }

            return highestPriorityPool.get(new Random().nextInt(highestPriorityPool.size()));
        }
    }

    private StyleRegistry() {
        // NO-OP
    }
}
