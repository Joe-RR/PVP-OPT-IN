package com.rerezd.pvpoptin.globals;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class globals {

    public static File configdir = FabricLoader.getInstance().getConfigDirectory();
    public static File modconfigdir=new File(configdir,"pvpoptin");
    public static File agroFile = new File(modconfigdir.getPath(), "AgroPlayers.txt");
    public static Logger Log = LogManager.getLogger("[PvpOptIn] ");

    private List<UUID> agro = new ArrayList<UUID>();

    public List<UUID> getAgroPlayers (){
        return agro;
    }

    public boolean isPlayerAgro(ServerPlayerEntity player){

        if(agro.contains(player.getUuid())){
            return true;
        }else{
            return false;
        }

    }

    public void addAgro (ServerPlayerEntity player){
        agro.add(player.getUuid());
        try {
            this.listToFile(agro);
        } catch (IOException e) {
            Log.error("Uh oh, stinky. Couldn't save agro file.");
        }
    }

    public void removeAgro (ServerPlayerEntity player){
        if(agro.contains(player.getUuid())){
            agro.remove(player.getUuid());
            try {
                this.listToFile(agro);
            } catch (IOException e) {
                Log.error("Uh oh, stinky. Couldn't save agro file.");
            }
        }else{
            //idk alert the player, just not going to do anything cause they're already not agro
        }

    }



    public static Text addStyle(MutableText text, String color){

        switch(color){

            case("Yellow"):
                return text.formatted(Formatting.YELLOW);

            case("Aqua"):
                return text.formatted(Formatting.AQUA);

            case("Gold"):
                return text.formatted(Formatting.GOLD);

            default:
                return text.formatted(Formatting.WHITE);
        }

    }
    //config related things

    public void onLoad()  {
        if(!modconfigdir.exists()) {
            createconfigdir(modconfigdir);
        }else{
            if(!agroFile.exists()){
                try {
                    agroFile.createNewFile();
                } catch (IOException e) {
                    Log.error("Uh oh, stinky. Couldn't create agro file.");
                }
            }

            try {
                this.fileToList(agroFile);
            } catch (IOException e) {
                Log.error("Uh oh, stinky. Couldn't load agro file.");
            }
        }

    }

    public void listToFile(List<UUID> list) throws IOException {
        FileWriter writer = new FileWriter(agroFile);
        for(UUID uuid: list) {
            writer.write(uuid.toString() + System.lineSeparator());
        }
        writer.close();
    }

    public void fileToList(File agroFile) throws IOException {
        Scanner s = new Scanner(agroFile);
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()){
            agro.add(UUID.fromString(s.next()));
        }
        s.close();
    }

    public void createconfigdir(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }



}
