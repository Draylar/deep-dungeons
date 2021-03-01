package draylar.dd.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {

    @Shadow public abstract World getWorld();
    @Shadow private int spawnDelay;
    @Shadow @Final private List<MobSpawnerEntry> spawnPotentials;
    @Shadow public abstract void setSpawnEntry(MobSpawnerEntry spawnEntry);
    @Shadow private int minSpawnDelay;
    @Shadow private int maxSpawnDelay;
    @Shadow private int spawnCount;
    @Shadow private int maxNearbyEntities;
    @Shadow private int requiredPlayerRange;
    @Shadow private int spawnRange;
    @Shadow @Nullable private Entity renderedEntity;

    /**
     * @reason {@link MobSpawnerLogic#fromTag} relies on the world random to select a mob spawner entry.
     * If fromTag is used to apply a tag to the spawner during world-generation, it will NPE.
     * This mixin prevents that.
     *
     * While we could use a redirect to get rid of the offending statement,
     * I'm pretty sure mixin still gives you whatever was passed in before,
     * which means the NPE would still occur.
     *
     * Sure, I could make both the conditionals false, but that seems like a pain.
     */
    @Inject(
            method = "fromTag",
            at = @At("HEAD"),
    cancellable = true)
    private void preventWorldRandomNPE(CompoundTag tag, CallbackInfo ci) {
        if(getWorld() == null) {
            this.spawnDelay = tag.getShort("Delay");
            this.spawnPotentials.clear();
            if (tag.contains("SpawnPotentials", 9)) {
                ListTag listTag = tag.getList("SpawnPotentials", 10);

                for(int i = 0; i < listTag.size(); ++i) {
                    this.spawnPotentials.add(new MobSpawnerEntry(listTag.getCompound(i)));
                }
            }

            if (tag.contains("SpawnData", 10)) {
                this.setSpawnEntry(new MobSpawnerEntry(1, tag.getCompound("SpawnData")));
            } else if (!this.spawnPotentials.isEmpty()) {
                this.setSpawnEntry(WeightedPicker.getRandom(new Random(), this.spawnPotentials));
            }

            if (tag.contains("MinSpawnDelay", 99)) {
                this.minSpawnDelay = tag.getShort("MinSpawnDelay");
                this.maxSpawnDelay = tag.getShort("MaxSpawnDelay");
                this.spawnCount = tag.getShort("SpawnCount");
            }

            if (tag.contains("MaxNearbyEntities", 99)) {
                this.maxNearbyEntities = tag.getShort("MaxNearbyEntities");
                this.requiredPlayerRange = tag.getShort("RequiredPlayerRange");
            }

            if (tag.contains("SpawnRange", 99)) {
                this.spawnRange = tag.getShort("SpawnRange");
            }

            if (this.getWorld() != null) {
                this.renderedEntity = null;
            }

            ci.cancel();
        }
    }
}
