package com.xfcl.pokestore.commands.putitem;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.xfcl.pokestore.commands.MainCommand;
import com.xfcl.pokestore.util.PokeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author Administrator
 */
public class Take extends MainCommand {
    public Take(int slot,Player player,int page) throws IOException {
        int start = INSTANCE.getConfig().getInt("Settings.start");
        int position = (slot - start + 1) * page;
        String baseDir = INSTANCE.getDataFolder() + "/data/" + player.getUniqueId() + "/";
        File poke = new File(baseDir, position + ".poke");
        Pokemon pokemon = PokeUtil.getPokemonByFile(poke);
        poke.delete();
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        storage.add(pokemon);

        // 文件扩展名
        String extension = ".poke";

        // 获取目录中的所有文件
        List<Path> files = new ArrayList<>();
        try {
            Files.list(Paths.get(baseDir))
                    .filter(path -> path.toString().endsWith(extension))
                    .forEach(files::add);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 自定义比较器，基于文件名中的数字进行排序
        Comparator<Path> numericSort = (f1, f2) -> {
            String name1 = f1.getFileName().toString();
            String name2 = f2.getFileName().toString();
            int index1 = name1.indexOf('.');
            int index2 = name2.indexOf('.');

            // 提取数字部分，假设数字始终在扩展名之前
            String num1Str = name1.substring(0, index1);
            String num2Str = name2.substring(0, index2);

            // 将字符串转换为整数进行比较
            int num1 = Integer.parseInt(num1Str);
            int num2 = Integer.parseInt(num2Str);

            return Integer.compare(num1, num2);
        };
        files.sort(numericSort);
        List<Path> filesToRename = new ArrayList<>();
        for (Path file : files) {
            String fileNameWithoutExt = file.getFileName().toString().substring(0, file.getFileName().toString().length() - extension.length());
            int fileNumber = Integer.parseInt(fileNameWithoutExt);
            if (fileNumber > position) {
                filesToRename.add(file);
            }
        }

        // 重命名文件
        for (Path file : filesToRename) {
            String fileNameWithoutExt = file.getFileName().toString().substring(0, file.getFileName().toString().length() - extension.length());
            int fileNumber = Integer.parseInt(fileNameWithoutExt);
            int newNameNumber = fileNumber - 1;
            Path target = Paths.get(baseDir, String.format("%d%s", newNameNumber, extension));
            try {
                Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ps open " + player.getName() + " " + page);
    }
}
