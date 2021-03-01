package draylar.dd.style;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class SimpleDungeonRegistry {

    private static final DungeonType FALLBACK_DUNGEON = new DungeonType()
            .addWallBlock(Blocks.COBBLESTONE).addWallBlock(Blocks.MOSSY_COBBLESTONE)
            .withSpawnType(EntityType.ZOMBIE).withSpawnType(EntityType.SKELETON).withSpawnType(EntityType.SPIDER)
            .addLootTable(new Identifier("chests/simple_dungeon"), 1.0);

    private static final Map<DungeonTypeCheck, Pair<Integer, DungeonType>> BIOME_DUNGEON_TYPES = new HashMap<>();

    public static void register(DungeonTypeCheck requirement, int priority, DungeonType type) {
        BIOME_DUNGEON_TYPES.put(requirement, new Pair<>(priority, type));
    }

    public static DungeonType get(RegistryKey<Biome> key, Biome biome, BlockPos origin) {
        Map<Integer, List<DungeonType>> pools = new HashMap<>();

        for (Map.Entry<DungeonTypeCheck, Pair<Integer, DungeonType>> entry : BIOME_DUNGEON_TYPES.entrySet()) {
            DungeonTypeCheck styleRequirement = entry.getKey();
            DungeonType style = entry.getValue().getRight();
            int priority = entry.getValue().getLeft();

            if(styleRequirement.check(key, biome, origin)) {
                if(!pools.containsKey(priority)) {
                    pools.put(priority, new ArrayList<>());
                }

                pools.get(entry.getValue().getLeft()).add(style);
            }
        }

        if(pools.isEmpty()) {
            return FALLBACK_DUNGEON;
        } else {
            List<DungeonType> highestPriorityPool = new ArrayList<>();
            int highestPriority = -999;

            for (Map.Entry<Integer, List<DungeonType>> entry : pools.entrySet()) {
                Integer priority = entry.getKey();
                List<DungeonType> pool = entry.getValue();

                // If the priority for this pool is higher than the current highest priority pool,
                //   replace the pool with our new one.
                if(priority > highestPriority) {
                    highestPriorityPool = pool;
                }
            }

            return highestPriorityPool.get(new Random().nextInt(highestPriorityPool.size()));
        }
    }

    private SimpleDungeonRegistry() {
        // NO-OP
    }
}
