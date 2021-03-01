package draylar.dd.world;

import draylar.dd.api.*;
import draylar.dd.style.DungeonType;
import draylar.dd.style.SimpleDungeonRegistry;
import draylar.dd.world.api.SiftingStructureStart;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardDungeonStructureStart extends SiftingStructureStart {

    public StandardDungeonStructureStart(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
        super(feature, chunkX, chunkZ, box, references, seed);
    }

    @Override
    public Map<BlockPos, BlockInfo> place(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config) {
        long start = System.currentTimeMillis();
        BlockPos origin = new BlockPos(chunkX * 16, 30, chunkZ * 16);
        Map<BlockPos, BlockInfo> blocks = new HashMap<>();
        List<Room> rooms = new ArrayList<>();
        int roomCount = 15 + random.nextInt(10);
        List<Connection> connections = new ArrayList<>();
        DungeonType type = SimpleDungeonRegistry.get(registryManager.get(Registry.BIOME_KEY).getKey(biome).get(), biome, origin);

        // Initialize starting rooms at 0, 0
        for (int i = 0; i < roomCount; i++) {
            int height = 6 + random.nextInt(3);
            rooms.add(new Room(10 + random.nextInt(10), 10 + random.nextInt(10), height, Vel2D.getRandom(random)));
        }

        // Spread out rooms
        rooms.forEach(room -> {
            while(room.intercepts(rooms)) {
                room.move();
            }
        });

        // Rooms -> blocks
        rooms.forEach(room -> blocks.putAll(room.build(origin, random, type)));

        // doors
        rooms.forEach(from -> {
            Room expanded = from.expand();

            rooms.stream().filter(room -> room != from).filter(expanded::intercepts).forEach(intersects -> {
                if(connections.stream().anyMatch(connection -> connection.contains(from, intersects))) {
                    return;
                }

                connections.add(new Connection(from, intersects));

                // Find the average middle position between the two rooms
                List<BlockPos> interceptionPoints = expanded.getInterceptionPoints(intersects);
                int totalX = 0;
                int totalZ = 0;

                for (BlockPos inter : interceptionPoints) {
                    totalX += inter.getX();
                    totalZ += inter.getZ();
                }

                // 3x3 at random intersection point
                BlockPos nOrigin = new BlockPos(totalX / interceptionPoints.size(), 0, totalZ / interceptionPoints.size());

                // place
                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 3; z++) {
                        for (int y = 1; y <= 4; y++) {
                            // If we are on the edge (3) and a block has not been placed there yet, set it to a wall block.
                            if(Math.abs(x) == 3 || Math.abs(z) == 3) {
                                BlockPos checkPos = origin.add(nOrigin).add(x, y, z);
                                if(!blocks.containsKey(checkPos)) {
                                    blocks.put(origin.add(nOrigin).add(x, y, z), new BlockInfo(type.getWallBlocks().get(random.nextInt(type.getWallBlocks().size())).getDefaultState(), null));
                                }
                            } else {
                                blocks.put(origin.add(nOrigin).add(x, y, z), new BlockInfo(Blocks.AIR.getDefaultState(), null));
                            }
                        }
                    }
                }
            });
        });

        // post-processing: chests and spawners
        blocks.forEach((pos, info) -> {

            // State is NOT air.
            if(!info.state.isAir()) {
                BlockPos up = pos.up();

                // If the position above this block exists...
                if(blocks.containsKey(up)) {
                    BlockInfo upState = blocks.get(up);

                    // If it is air, this is a valid placement position.
                    if(upState.state.isAir()) {
                        if (random.nextInt(150) == 0) {
                            // chest weight logic
                            List<Identifier> pool = new ArrayList<>();
                            Map<Identifier, Double> weightedLootTables = type.getWeightedLootTables();
                            weightedLootTables.forEach((identifier, weight) -> {
                                for(int i = 0; i < weight * 100; i++) {
                                    pool.add(identifier);
                                }
                            });

                            blocks.put(up, new BlockInfo(Blocks.CHEST.getDefaultState(), FeatureHelper.createChestTag(pool.get(random.nextInt(pool.size())), up)));
                        } else if (random.nextInt(150) == 0) {
                            blocks.put(up, new BlockInfo(Blocks.SPAWNER.getDefaultState(), null));
                        }
                    }
                }
            }
        });

        // place ladder going up
        if(type.shouldPlaceLadder()) {
            Room ladderSource = rooms.get(random.nextInt(rooms.size()));
            int ladderX = origin.getX() + (int) ladderSource.getX() + (ladderSource.getWidth() / 2);
            int ladderZ = origin.getZ() + (int) ladderSource.getZ() + (ladderSource.getDepth() / 2);
            int ladderHeight = chunkGenerator.getHeightInGround(ladderX, ladderZ, Heightmap.Type.WORLD_SURFACE_WG);
            for (int i = 31; i <= ladderHeight; i++) {
                blocks.put(new BlockPos(ladderX, i, ladderZ), new BlockInfo(Blocks.LADDER.getDefaultState(), null));

                for(Direction direction : Direction.values()) {
                    if(!direction.getAxis().equals(Direction.Axis.Y)) {
                        BlockPos offset = new BlockPos(ladderX, i, ladderZ).offset(direction);

                        // ensure we are not placing over existing air
                        if(!blocks.containsKey(offset)) {
                            blocks.put(offset, new BlockInfo(type.getWallBlocks().get(random.nextInt(type.getWallBlocks().size())).getDefaultState(), null));
                        }
                    }
                }
            }

            // place explosion circle around ladder
            for(int x = -7; x <= 7; x++) {
                for(int z = -7; z <= 7; z++) {
                    if(x != 0 && z != 0) {
                        int expX = ladderX + x;
                        int expZ = ladderZ + z;
                        int expHeight = chunkGenerator.getHeightInGround(expX, expZ, Heightmap.Type.WORLD_SURFACE_WG);
                        blocks.put(new BlockPos(expX, expHeight, expZ), new BlockInfo(type.getFloorBlocks().get(random.nextInt(type.getFloorBlocks().size())).getDefaultState(), null));
                    }
                }
            }
        }

        System.out.println("Took: " + (System.currentTimeMillis() - start) + " to finish");

        return blocks;
    }
}
