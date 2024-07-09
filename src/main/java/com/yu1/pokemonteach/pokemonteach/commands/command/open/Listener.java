package com.yu1.pokemonteach.pokemonteach.commands.command.open;

import com.aystudio.core.bukkit.util.inventory.GuiModel;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Objects;

import static com.yu1.pokemonteach.pokemonteach.commands.MainCommand.INSTANCE;

public interface Listener {
    File file = new File(INSTANCE.getDataFolder(), "gui.yml");
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    static void open(GuiModel inv, Player player, Pokemon pokemon, int page)
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
                String itemName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
                if(Objects.requireNonNull(data.getString("items.up.name")).replace("&", "§").equals(Objects.requireNonNull(itemName)))
                {
                    // 如果当前页是第一页就返回
                    if(page == 1)  return;
                    GuiOpen.openIt(player, pokemon, page - 1);
                }
                if(Objects.requireNonNull(data.getString("items.down.name")).replace("&", "§").equals(Objects.requireNonNull(itemName)))
                {
                    GuiOpen.openIt(player, pokemon, page + 1);
                }
                pixelmonLearnEvent(itemStack, player, pokemon, itemName);
            }
        });
    }
    static void pixelmonLearnEvent(ItemStack itemStack, Player player, Pokemon pokemon, String itemName){
        if(itemStack.getType().toString().contains("PIXELMON"))
        {
            // 所需的数量
            int needCount = INSTANCE.getConfig().getInt("Settings.num");
            boolean hasFlag = false;
            for(ItemStack item : player.getInventory().getContents()){
                if(item != null && item.getType() != Material.AIR && item.getItemMeta().hasLore()){
                    for(String lore : Objects.requireNonNull(item.getItemMeta()).getLore()){
                        if(lore.contains(INSTANCE.getConfig().getString("Settings.lore").replace("&", "§")) && item.getAmount() >= needCount)
                        {
                            item.setAmount(item.getAmount() - needCount);
                            player.sendMessage(INSTANCE.getConfig().getString("Messages.deleteSuccess").replace("&", "§"));
                            hasFlag = true;
                            break;
                        }
                    }
                    if(hasFlag) break;
                }
            }
            if(!hasFlag){
                player.sendMessage(INSTANCE.getConfig().getString("Messages.deleteFail").replace("&", "§"));
                return;
            }
            String fontPrefix = data.getString("Cd.name").split("%AttackName%")[0];
            String attackName = itemName.replace("§", "&")
                    .replace('_', ' ')
                    .replace(fontPrefix, "");
            Attack attack = new Attack(attackName);
            if (pokemon.getMoveset().size() >= 4) {
                EntityPlayerMP playerMP = ((CraftPlayer)player).getHandle();
                LearnMoveController.sendLearnMove(playerMP, pokemon.getUUID(), attack.getActualMove());
            } else {
                pokemon.getMoveset().add(attack);
            }
            if(INSTANCE.getConfig().getBoolean("Settings.addFlag"))
            {
                pokemon.addSpecFlag("untradeable");
                pokemon.addSpecFlag("unbreedable");
            }
        }
    }
}
