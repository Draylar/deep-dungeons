package draylar.dd.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class FeatureHelper {

    public static CompoundTag createChestTag(Identifier lootTable, BlockPos pos) {
        CompoundTag chestTag = new CompoundTag();
        chestTag.putString("LootTable", lootTable.toString());
        chestTag.putInt("x", pos.getX());
        chestTag.putInt("y", pos.getY());
        chestTag.putInt("z", pos.getZ());
        return chestTag;
    }
}
