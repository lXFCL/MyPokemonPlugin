package com.yu1.pokemonteach.pokemonteach.util;

import com.pixelmonmod.pixelmon.enums.technicalmoves.ITechnicalMove;
import org.bukkit.inventory.ItemStack;

public interface CdItem {

    static ItemStack getCdItemByProperty(String property)
    {
        ITechnicalMove move = ITechnicalMove.getMoveFor("tm_gen8",3);
        switch (property){
            case "火":
            case "Fire":
                return PokeUtil.getCdItem(move);
            case "水":
            case "Water":
                move = ITechnicalMove.getMoveFor("tm_gen8",33);
                return PokeUtil.getCdItem(move);
            case "草":
            case "Grass":
                move = ITechnicalMove.getMoveFor("tm_gen8",11);
                return PokeUtil.getCdItem(move);
            case "电":
            case "Electric":
                move = ITechnicalMove.getMoveFor("tm_gen8",14);
                return PokeUtil.getCdItem(move);
            case "冰":
            case "Ice":
                move = ITechnicalMove.getMoveFor("tm_gen8",4);
                return PokeUtil.getCdItem(move);
            case "毒":
            case "Poison":
                move = ITechnicalMove.getMoveFor("tm_gen4",84);
                return PokeUtil.getCdItem(move);
            case "地面":
            case "Ground":
                move = ITechnicalMove.getMoveFor("tm_gen2",31);
                return PokeUtil.getCdItem(move);
            case "飞行":
            case "Flying":
                move = ITechnicalMove.getMoveFor("tm_gen3",40);
                return PokeUtil.getCdItem(move);
            case "钢":
            case "Steel":
                move = ITechnicalMove.getMoveFor("tm_gen3",23);
                return PokeUtil.getCdItem(move);
            case "龙":
            case "Dragon":
                move = ITechnicalMove.getMoveFor("tm_gen2",24);
                return PokeUtil.getCdItem(move);
            case "恶":
            case "Dark":
                move = ITechnicalMove.getMoveFor("tm_gen7",59);
                return PokeUtil.getCdItem(move);
            case "幽灵":
            case "Ghost":
                move = ITechnicalMove.getMoveFor("tm_gen2",3);
                return PokeUtil.getCdItem(move);
            case "格斗":
            case "Fighting":
                move = ITechnicalMove.getMoveFor("tm_gen1",18);
                return PokeUtil.getCdItem(move);
            case "一般":
            case "Normal":
                move = ITechnicalMove.getMoveFor("tm_gen1",10);
                return PokeUtil.getCdItem(move);
            case "虫":
            case "Bug":
                move = ITechnicalMove.getMoveFor("tm_gen2",49);
                return PokeUtil.getCdItem(move);
            case "超能力":
            case "Psychic":
                move = ITechnicalMove.getMoveFor("tm_gen8",61);
                return PokeUtil.getCdItem(move);
            case "岩石":
            case "Rock":
                move = ITechnicalMove.getMoveFor("tm_gen4",80);
                return PokeUtil.getCdItem(move);
            case "妖精":
            case "Fairy":
                move = ITechnicalMove.getMoveFor("tm_gen8",29);
                return PokeUtil.getCdItem(move);
        }
        return null;
    }
}
