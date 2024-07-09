package com.yu1.pokemonteach.pokemonteach.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.yu1.pokemonteach.pokemonteach.commands.MainCommand.INSTANCE;

public interface SelfSkills {
    File file = new File(INSTANCE.getDataFolder(), "skill.yml");
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    static ArrayList<Attack> getCheckSkills(Pokemon pokemon){
        ArrayList<Attack> skills = new ArrayList<>();
        for(String pokemonName : data.getConfigurationSection("").getKeys(false)){
            if(Objects.equals(pokemonName, pokemon.getLocalizedName())){
                List<String> skillList = data.getStringList(pokemonName);
                for(String skillName : skillList){
                    Attack attack = new Attack(skillName);
                    skills.add(attack);
                }
            }
        }
        return skills;
    }
}
