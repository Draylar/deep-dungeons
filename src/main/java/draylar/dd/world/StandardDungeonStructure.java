package draylar.dd.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class StandardDungeonStructure extends StructureFeature<DefaultFeatureConfig> {

    public StandardDungeonStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return StandardDungeonStructureStart::new;
    }
}
