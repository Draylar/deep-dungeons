package draylar.dd.registry;

import draylar.dd.DeepDungeons;
import draylar.dd.world.api.SiftingStructureGenerator;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;

public class DeepDungeonPieces {

    public static final StructurePieceType STANDARD = register("standard", SiftingStructureGenerator::new);

    public static void init() {

    }

    public static StructurePieceType register(String id, StructurePieceType type) {
        return  Registry.register(Registry.STRUCTURE_PIECE, DeepDungeons.id(id), type);
    }
}
