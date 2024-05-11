package com.xfcl.pokestore.commands.putitem;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.xfcl.pokestore.commands.MainCommand;
import com.xfcl.pokestore.util.FileConut;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Administrator
 */
public class Add extends MainCommand {
    public Add(int slot,Player player) throws IOException {
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        Pokemon pokemon = storage.get(slot - 1);
        if(pokemon == null)
        {
            player.sendMessage(INSTANCE.getConfig().getString("Messages.noPokemon").replace("&", "§"));
            return;
        }
        if(Objects.requireNonNull(pokemon).isEgg())
        {
            player.sendMessage(INSTANCE.getConfig().getString("Messages.noEgg").replace("&", "§"));
            return;
        }
        String filePath = INSTANCE.getDataFolder() + "/data/" + player.getUniqueId() + "/";
        int count = FileConut.getCount(filePath);
        if(count >= INSTANCE.getConfig().getInt("Settings.max"))
        {
            player.sendMessage(INSTANCE.getConfig().getString("Messages.noSpace").replace("&", "§"));
            return;
        }
        File poke = new File(filePath , count + 1 + ".poke");
        NBTTagCompound nbt = new NBTTagCompound();
        Objects.requireNonNull(pokemon).writeToNBT(nbt);
        CompressedStreamTools.safeWrite(nbt,poke);
        storage.set(slot - 1,null);
        player.sendMessage(INSTANCE.getConfig().getString("Messages.addSuccess").replace("&", "§"));
        player.closeInventory();
    }
}
