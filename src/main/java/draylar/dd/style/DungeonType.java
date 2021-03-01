package draylar.dd.style;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonType {

    private final List<Block> wallBlocks = new ArrayList<>();
    private final List<Block> floorBlocks = new ArrayList<>();
    private final Map<Identifier, Double> weightedLootTables = new HashMap<>();
    private List<EntityType<? extends LivingEntity>> mobs = new ArrayList<>();
    private boolean placeLadder = false;

    public DungeonType() {

    }

    public DungeonType addWallBlock(Block block) {
        this.wallBlocks.add(block);
        return this;
    }

    public DungeonType addFloorBlock(Block block) {
        this.floorBlocks.add(block);
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

    public DungeonType withSpawnType(EntityType<? extends LivingEntity> sup) {
        this.mobs.add(sup);
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

    public List<EntityType<? extends LivingEntity>> getMobs() {
        return mobs;
    }

    public Map<Identifier, Double> getWeightedLootTables() {
        return weightedLootTables;
    }

    public boolean shouldPlaceLadder() {
        return placeLadder;
    }
}
