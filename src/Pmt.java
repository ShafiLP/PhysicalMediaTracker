import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Pmt {
    private final Gui GUI;
    private LinkedList<Album> albumList;

    /**
     * Constructor of Pmt (Physical Media Tracker) class
     */
    public Pmt() {
        albumList = readAlbumsFromJson("saveData\\data.json");
        GUI = new Gui(this);
    }

    /**
     * Gets an Album object from local LinkedList<Album> and returns it
     * @param Index Index of requested Album object
     * @return Album object of the given index
     */
    public Album getAlbum(int Index) {
        return albumList.get(Index);
    }

    /**
     * Gets the local LinkedList<Album> that contains all albums and returns it
     * @return LinkedList<Album> with all albums
     */
    public LinkedList<Album> getAlbumList() {
        return albumList;
    }

    /**
     * Adds an Album object to local LinkedList<Album> and adds it to the saved ones inside JSON save file
     * @param pAlbum Album object to add to List and save to JSON
     */
    public void addAlbum(Album pAlbum) {
        albumList.addLast(pAlbum);

        // DEBUG
        for(int i = 0; i < pAlbum.getTrackList().size(); i++) {
            System.out.println(pAlbum.getTrackList().get(i).getTrackNumber() + ": " + pAlbum.getTrackList().get(i).getTrackName());
        }
        System.out.println("Name: " + pAlbum.getAlbumName());
        System.out.println("Artist: " + pAlbum.getAlbumArtist());
        System.out.println("Release: " + pAlbum.getReleaseYear());
        System.out.println("Cover: " + pAlbum.getCoverPath());
        System.out.println("Where: " + pAlbum.getWhereBought());
        System.out.println("Nulltrack: " + pAlbum.containsNulltrack());

        // Save as JSON file
        addAlbumToJson(pAlbum, "saveData\\data.json");
    }

    //** METHODS TO SAVE AND LOAD ALBUM OBJECTS FROM A JSON FILE*/

    /**
     * Adds an Album object to the data of an already existing JSON file
     * @param pAlbum Album object to be added to exisitng file
     * @param pPath Path to JSON file that contains data
     */
    private void addAlbumToJson(Album pAlbum, String pPath) {
        LinkedList<Album> llAlbums = readAlbumsFromJson(pPath);

        llAlbums.addLast(pAlbum);
        overrideAlbumsInJson(llAlbums, pPath);
    }

    /**
     * Reads all Album objects from a JSON file and returns them as a LinkedList
     * @param pPath Path to JSON file that contains data
     * @return LinkedList<Album> with all albums from given JSON file path
     */
    private LinkedList<Album> readAlbumsFromJson(String pPath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LinkedList<Album> llAlbums = new LinkedList<>();

        try (FileReader reader = new FileReader(pPath)) {
            llAlbums = gson.fromJson(reader, new TypeToken<LinkedList<Album>>(){}.getType());

            // If file is empty
            if (llAlbums == null) {
                llAlbums = new LinkedList<>();
            }
        } catch (Exception e) {
            // If file doesn't exist
            llAlbums = new LinkedList<>();
        }

        return llAlbums;
    }

    /**
     * Overrides all Album objects inside a JSON file with new ones from a given LinkedList<Album>
     * @param pAlbums New Album objects that should override the already existing ones inside JSON file
     * @param pPath Path to JSON file that contains Album objects
     */
    private void overrideAlbumsInJson(LinkedList<Album> pAlbums, String pPath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(pPath)) {
            gson.toJson(pAlbums, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
