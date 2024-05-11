package com.xfcl.pokestore.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

/**
 * @author Administrator
 */
public class StatTotal {
    public static int total (Pokemon pokemon)
    {
        double g = 0;
        int[] a = {pokemon.getIVs().getStat(StatsType.HP),pokemon.getIVs().getStat(StatsType.Defence)
                ,pokemon.getIVs().getStat(StatsType.SpecialDefence),pokemon.getIVs().getStat(StatsType.Speed)
                ,pokemon.getIVs().getStat(StatsType.Attack),pokemon.getIVs().getStat(StatsType.SpecialAttack)};
        for(int i = 0; i < 6;i++)
        {
            g += a[i];
        }
        return (int)(g * 100 / 186);
    }
    public static int total2 (Pokemon pokemon)
    {
        double g = 0;
        int[] a = {pokemon.getEVs().getStat(StatsType.HP),pokemon.getEVs().getStat(StatsType.Defence)
                ,pokemon.getEVs().getStat(StatsType.SpecialDefence),pokemon.getEVs().getStat(StatsType.Speed)
                ,pokemon.getEVs().getStat(StatsType.Attack),pokemon.getEVs().getStat(StatsType.SpecialAttack)};
        for(int i = 0; i < 6;i++)
        {
            g += a[i];
        }
        return (int)(g * 100 / 510);
    }
}
