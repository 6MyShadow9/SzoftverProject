package project.UI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Saver {

    private List<SavedData> loadExistingData(ObjectMapper objectMapper) {
        List<SavedData> existingData = new ArrayList<>();

        try {
            File file = new File("game1/target/classes/Data/history.json");
            Logger.debug("Search for existing file...");
            if (file.exists() && file.length() != 0) {
                Logger.debug("Existing file found!");
                existingData = objectMapper.registerModule(new JavaTimeModule())
                        .readValue(Saver.class.getResourceAsStream("/Data/history.json"),
                                new TypeReference<List<SavedData>>() {
                                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return existingData;
    }

    private void saveData(ObjectMapper objectMapper, List<SavedData> data) throws Exception{
        Logger.debug("Data before saving: " + data);
        try {
            File file = new File("game1/target/classes/Data/history.json");
            List<SavedData> existingData = new ArrayList<>();
            if (file.exists() && file.length() != 0) {
                existingData = objectMapper.readValue(file, new TypeReference<List<SavedData>>() {});
            }
            existingData.addAll(data); // Append new data to existing data
            FileWriter writer = new FileWriter(file);
            objectMapper.writeValue(writer, existingData);
            Logger.info("Saving successful!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveGame(String Player1Name, String Player2Name, String WinnerName, int AmountOfMoves) {

        var objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());

        List<SavedData> data = loadExistingData(objectMapper);

        var forSave = new SavedData();
        forSave.setPlayer1Name(Player1Name);
        forSave.setPlayer2Name(Player2Name);
        forSave.setWinnerName(WinnerName);
        forSave.setAmountOfMoves(AmountOfMoves-1);
        forSave.setDateOfGame(LocalDate.now());


        data.add(forSave);


        try {
            saveData(objectMapper, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
