package draylar.dd.style;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

import java.util.*;

public class DungeonType {

    private final List<Block> wallBlocks = new ArrayList<>();
    private final List<Block> floorBlocks = new ArrayList<>();
    private final Map<Identifier, Double> weightedLootTables = new HashMap<>();
    private final List<EntityType<?>> mobs = new ArrayList<>();
    private boolean placeLadder = false;

    public DungeonType() {

    }

    public DungeonType addWallBlock(Block... blocks) {
        this.wallBlocks.addAll(Arrays.asList(blocks));
        return this;
    }

    public DungeonType addFloorBlock(Block... blocks) {
        this.floorBlocks.addAll(Arrays.asList(blocks));
        return this;
    }

    public DungeonType addLootTable(Identifier id, double weight) {
        this.weightedLootTables.put(id, weight);
        return this;
    }

    public DungeonType placeLadder(boolean place) {
        this.placeLadder = place;
        return this;
    }

    public DungeonType addSpawnable(EntityType<?>... mobs) {
        this.mobs.addAll(Arrays.asList(mobs));
        return this;
    }

    public List<Block> getWallBlocks() {
        return wallBlocks;
    }

    public List<Block> getFloorBlocks() {
        if(floorBlocks.isEmpty()) {
            return wallBlocks;
        }

        return floorBlocks;
    }

    public List<EntityType<?>> getMobs() {
        return mobs;
    }

    public Map<Identifier, Double> getWeightedLootTables() {
        return weightedLootTables;
    }

    public boolean shouldPlaceLadder() {
        return placeLadder;
    }
}
