package draylar.dd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.Arrays;
import java.util.List;

@Config(name = "deepdungeons")
public class DeepDungeonsConfig implements ConfigData {
    public List<String> standardDungeonBiomeBlacklist = Arrays.asList("minecraft:the_void");
    public int standardDungeonSpacing = 24;
    public int standardDungeonSeparation = 12;
}
