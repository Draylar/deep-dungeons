package draylar.dd.style;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonType {

    private final List<Block> wallBlocks = new ArrayList<>();
    private final List<Block> floorBlocks = new ArrayList<>();
    private final Map<Identifier, Double> weightedLootTables = new HashMap<>();

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

    public List<Block> getWallBlocks() {
        return wallBlocks;
    }

    public List<Block> getFloorBlocks() {
        if(floorBlocks.isEmpty()) {
            return wallBlocks;
        }

        return floorBlocks;
    }

    public Map<Identifier, Double> getWeightedLootTables() {
        return weightedLootTables;
    }
}
