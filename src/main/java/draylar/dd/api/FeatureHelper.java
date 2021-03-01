package draylar.dd.api;

import draylar.dd.style.DungeonType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Random;

public class FeatureHelper {

    public static CompoundTag createChestTag(Identifier lootTable, BlockPos pos) {
        CompoundTag chestTag = new CompoundTag();
        chestTag.putString("LootTable", lootTable.toString());
        chestTag.putInt("x", pos.getX());
        chestTag.putInt("y", pos.getY());
        chestTag.putInt("z", pos.getZ());
        return chestTag;
    }

    public static CompoundTag createSpawnerTag(BlockPos pos, DungeonType type) {
        CompoundTag spawnerTag = new CompoundTag();
        spawnerTag.putInt("x", pos.getX());
        spawnerTag.putInt("y", pos.getY());
        spawnerTag.putInt("z", pos.getZ());

        // get random mob
        List<EntityType<? extends LivingEntity>> mobs = type.getMobs();
        EntityType<? extends LivingEntity> mob = mobs.get(new Random().nextInt(mobs.size()));
        ListTag spawns = new ListTag();
        CompoundTag spawn = new CompoundTag();
        CompoundTag entityTag = new CompoundTag();
        entityTag.putString("id", Registry.ENTITY_TYPE.getId(mob).toString());
        spawn.put("Entity", entityTag);
        spawns.add(spawn);
        spawnerTag.put("SpawnPotentials", spawns);

        return spawnerTag;
    }
}
