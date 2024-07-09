package com.yu1.pokemonteach.pokemonteach.commands.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetTest {
    public GetTest(CommandSender sender){
        Player player = (Player) sender;
        player.sendMessage(player.getInventory().getItemInMainHand().getData().toString());
    }

}
