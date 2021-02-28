package draylar.dd.api;

import draylar.dd.style.DungeonType;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

public class Room {

    private double x = 0;
    private double z = 0;
    private final int width;
    private final int depth;
    private final int height;
    private final Vel2D velocity;

    public Room(int width, int depth, int height, Vel2D velocity) {
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.velocity = velocity;
    }

    public Room(double x, double z, int width, int depth, int height, Vel2D velocity) {
        this.x = x;
        this.z = z;
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.velocity = velocity;
    }

    public boolean intercepts(List<Room> room) {
        return room.stream().filter(r -> r != this).anyMatch(this::intercepts);
    }

    public Room expand() {
        return new Room(x - 1, z - 1, width + 2, depth + 2, height, velocity);
    }

    public List<BlockPos> getInterceptionPoints(Room room) {
        List<BlockPos> otherPositions = room.toPositions();
        return toPositions().stream().filter(otherPositions::contains).collect(Collectors.toList());
    }

    public boolean intercepts(Room room) {
        double x1min = x;
        double x1max = x + width;
        double x2min = room.getX();
        double x2max = room.getX() + room.getWidth();

        double z1min = z;
        double z1max = z + depth;
        double z2min = room.getZ();
        double z2max = room.getZ() + room.depth;

        return x1min < x2max && x2min < x1max && z1min < z2max && z2min < z1max;
    }

    public Map<BlockPos, BlockInfo> build(BlockPos origin, Random random, DungeonType style) {
        Map<BlockPos, BlockInfo> blocks = new HashMap<>();

        int minX = (int) x;
        int maxX = minX + width;
        int minZ = (int) z;
        int maxZ = minZ + depth;

        for (int i = minX; i <= maxX; i++) {
            for (int w = minZ; w <= maxZ; w++) {
                for (int y = 0; y <= height; y++) {
                    // If the block is on the edges, place stone. Otherwise, place air.
                    if (i == minX || i == maxX || w == minZ || w == maxZ || y == 0 || y == height) {
                        if(y == 0 || y == height) {
                            blocks.put(origin.add(i, y, w), new BlockInfo(style.getFloorBlocks().get(random.nextInt(style.getFloorBlocks().size())).getDefaultState(), null));
                        } else {
                            blocks.put(origin.add(i, y, w), new BlockInfo(style.getWallBlocks().get(random.nextInt(style.getWallBlocks().size())).getDefaultState(), null));
                        }
                    } else {
                        blocks.put(origin.add(i, y, w), new BlockInfo(Blocks.CAVE_AIR.getDefaultState(), null));
                    }
                }
            }
        }

        return blocks;
    }

    public List<BlockPos> toPositions() {
        List<BlockPos> positions = new ArrayList<>();

        int minX = (int) x;
        int maxX = minX + width;
        int minZ = (int) z;
        int maxZ = minZ + depth;

        for (int i = minX; i <= maxX; i++) {
            for (int m = minZ; m <= maxZ; m++) {
                positions.add(new BlockPos(i, 0, m));
            }
        }

        return positions;
    }

    public void move() {
        x += velocity.getX();
        z += velocity.getZ();
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }
}
