package com.xfcl.pokestore.util;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.nbt.CompressedStreamTools;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 */
public class PokeUtil {
    public static Pokemon getPokemonByFile(File file) throws IOException {
        return Pixelmon.pokemonFactory.create(CompressedStreamTools.read(file));
    }

}
