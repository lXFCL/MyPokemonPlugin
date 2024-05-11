package com.xfcl.pokestore.commands.putitem;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.xfcl.pokestore.commands.MainCommand;
import com.xfcl.pokestore.util.PokeUtil;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 */
public class Take extends MainCommand {
    public Take(int slot,Player player) throws IOException {
        int position = slot - INSTANCE.getConfig().getInt("Settings.start") + 1;
        String filePath = INSTANCE.getDataFolder() + "/data/" + player.getUniqueId() + "/";
        File poke = new File(filePath, position + ".poke");
        Pokemon pokemon = PokeUtil.getPokemonByFile(poke);
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        storage.add(pokemon);
        poke.delete();
        File directory = new File(filePath);
        // 使用listFiles()获取目录中的文件和子目录
        File[] files = directory.listFiles();
        // 如果files不为空，计算文件数量（不包括子目录）
        if (files != null) {
            for (File file : files) {
                if(Integer.parseInt(file.getName().replace(".poke", "")) > position)
                {
                    file.renameTo(new File(filePath, (Integer.parseInt(file.getName().replace(".poke", "")) - 1) + ".poke"));
                }
            }
        }
        player.closeInventory();
    }
}
