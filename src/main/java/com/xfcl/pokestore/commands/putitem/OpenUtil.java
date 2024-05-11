package com.xfcl.pokestore.commands.putitem;

import com.aystudio.core.bukkit.util.inventory.GuiModel;
import com.aystudio.core.pixelmon.api.sprite.SpriteHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.xfcl.pokestore.commands.command.Open;
import com.xfcl.pokestore.util.PokeUtil;
import com.xfcl.pokestore.util.StatTotal;
import net.minecraft.nbt.CompressedStreamTools;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 */
public class OpenUtil extends Open {

    public OpenUtil(CommandSender sender, String[] args) throws IOException {
        super(sender, args);
    }

    public static void putItem(GuiModel inv)
    {
        inv.setCloseRemove(true);
        for (String key : data.getConfigurationSection("items").getKeys(false)) {
            String newKey = "items." + key + ".";
            ItemStack itemStack = new ItemStack(Material.valueOf(data.getString(newKey + "type")), 1, (short) data.getInt(newKey + "data"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(data.getString(newKey + "name").replace("&", "§"));
            List<String> lore = new ArrayList<>();
            data.getStringList(newKey + "lore").forEach((l) -> lore.add(l.replace("&", "§")));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            if (data.getStringList(newKey + "slots").isEmpty()) {
                inv.setItem(data.getInt(newKey + "slot"), itemStack);
            } else {
                data.getIntegerList(newKey + "slots").forEach((slot) -> inv.setItem(slot, itemStack));
            }
        }
    }

    public static void listener(GuiModel inv,Player player)
    {
        // 打开界面
        inv.openInventory(player);
        //注册监听器
        inv.registerListener(INSTANCE);
        //点击监听事件
        inv.execute(e -> {
            e.setCancelled(true);
            if (e.getClickedInventory() == e.getInventory()) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    return;
                }
                for(int i = 1; i <= 6; i++)
                {
                    if(Objects.requireNonNull(OpenUtil.data.getString("items.add" + i + ".name")).replace("&", "§").equals(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName()))
                    {
                        try {
                            new Add(i,player);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                if("PIXELMON_PIXELMON_SPRITE".equals(itemStack.getType().name()))
                {
                    try {
                        new Take(e.getSlot(),player);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
    public static void putPokemon(GuiModel inv,Player player) throws IOException {
        int g = INSTANCE.getConfig().getInt("Settings.start");
        String filePath = INSTANCE.getDataFolder() + "/data/" + player.getUniqueId() + "/";
        File directory = new File(filePath);
        // 使用listFiles()获取目录中的文件和子目录
        File[] files = directory.listFiles();
        // 如果files不为空，计算文件数量（不包括子目录）
        if (files != null) {
            for (File file : files) {
                Pokemon pokemon = PokeUtil.getPokemonByFile(file);
                SpriteHelper sprite = new SpriteHelper();
                ItemStack itemStack = sprite.getSpriteItem(pokemon);
                ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                itemMeta.setDisplayName(Objects.requireNonNull(data.getString("Item.name"))
                        .replace("%pokemon_name%",pokemon.getLocalizedName())
                        .replace("&", "§"));
                List<String> lore = new ArrayList<>();
                data.getStringList("Item.lore").forEach((l) -> lore.add(l
                        .replace("%Move1%", pokemon.getMoveset().get(0) != null ? pokemon.getMoveset().get(0).getActualMove().getLocalizedName() : "无")
                        .replace("%Move2%", pokemon.getMoveset().get(1) != null ? pokemon.getMoveset().get(1).getActualMove().getLocalizedName() : "无")
                        .replace("%Move3%", pokemon.getMoveset().get(2) != null ? pokemon.getMoveset().get(2).getActualMove().getLocalizedName() : "无")
                        .replace("%Move4%", pokemon.getMoveset().get(3) != null ? pokemon.getMoveset().get(3).getActualMove().getLocalizedName() : "无")
                        .replace("%Gender%", pokemon.getGender().getLocalizedName())
                        .replace("%Growth%", pokemon.getGrowth().getLocalizedName())
                        .replace("%Nature%", pokemon.getNature().getLocalizedName())
                        .replace("%Ability%", pokemon.getAbility().getLocalizedName())
                        .replace("%Shiny%", pokemon.isShiny() ?"是" : "否")
                        .replace("%EVS_Attack%", pokemon.getEVs().getStat(StatsType.Attack) + "")
                        .replace("%EVS_Defence%", pokemon.getEVs().getStat(StatsType.Defence) + "")
                        .replace("%EVS_SpecialAttack%", pokemon.getEVs().getStat(StatsType.SpecialAttack) + "")
                        .replace("%EVS_SpecialDefence%", pokemon.getEVs().getStat(StatsType.SpecialDefence) + "")
                        .replace("%EVS_Speed%", pokemon.getEVs().getStat(StatsType.Speed) + "")
                        .replace("%EVS_HP%", pokemon.getEVs().getStat(StatsType.HP) + "")
                        .replace("%EVS_SUM%", StatTotal.total2(pokemon) + "")
                        .replace("%IVS_Attack%", pokemon.getIVs().getStat(StatsType.Attack) + "")
                        .replace("%IVS_Defence%", pokemon.getIVs().getStat(StatsType.Defence) + "")
                        .replace("%IVS_SpecialAttack%", pokemon.getIVs().getStat(StatsType.SpecialAttack) + "")
                        .replace("%IVS_SpecialDefence%", pokemon.getIVs().getStat(StatsType.SpecialDefence) + "")
                        .replace("%IVS_Speed%", pokemon.getIVs().getStat(StatsType.Speed) + "")
                        .replace("%IVS_HP%", pokemon.getIVs().getStat(StatsType.HP) + "")
                        .replace("%IVS_SUM%",StatTotal.total(pokemon) + "")
                        .replace("%Level%", pokemon.getLevel() + "")
                        .replace("&", "§")));
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                inv.setItem(g,itemStack);
                g++;
            }
        }
    }
}
