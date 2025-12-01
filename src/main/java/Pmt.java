import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class Pmt {
    private final Gui GUI;
    private Settings settings;
    private LinkedList<Album> albumList;

    /**
     * Constructor of Pmt (Physical Media Tracker) class
     */
    public Pmt() {
        albumList = readAlbumsFromJson("saveData\\data.json");
        try {
            settings = Settings.readSettings();
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        if (settings == null) settings = new Settings(); // Default settings

        GUI = new Gui(this, settings);
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
     * Gets the local LinkedList<Album>, sorts it by album name alphabetically and returns it
     * @return LinkedList<Album> with all albums, sorted by album name
     */
    public LinkedList<Album> sortByName() {
        LinkedList<Album> llSorted = new LinkedList<>(albumList);
        llSorted.sort(Comparator.comparing(Album::getAlbumName));
        return llSorted;
    }

    /**
     * Gets the local LinkedList<Album>, sorts it by album artists alphabetically and returns it
     * @return LinkedList<Album> with all albums, sorted by album artists
     */
    public LinkedList<Album> sortByArtist() {
        LinkedList<Album> llSorted = new LinkedList<>(albumList);
        llSorted.sort(Comparator.comparing(Album::getAlbumArtist, String.CASE_INSENSITIVE_ORDER));
        return llSorted;
    }

    /**
     * Gets the local LinkedList<Album>, sorts it by album release years and returns it
     * @return LinkedList<Album> with all albums, sorted by release years
     */
    public LinkedList<Album> sortByRelease() {
        LinkedList<Album> llSorted = new LinkedList<>(albumList);
        llSorted.sort(Comparator.comparing(Album::getReleaseYear));
        return llSorted;
    }

    /**
     * Adds an Album object to local LinkedList<Album> and adds it to the saved ones inside JSON save file
     * @param pAlbum Album object to add to List and save to JSON
     */
    public void addAlbum(Album pAlbum) {
        albumList.addLast(pAlbum);

        // Save as JSON file
        addAlbumToJson(pAlbum, "saveData\\data.json");

        System.out.println("Added album " + pAlbum.getAlbumName() + " by " + pAlbum.getAlbumArtist() + " to JSON."); // DEBUG
        GUI.updateAlbums();
    }

    /**
     * Replaced an Album object in local LinkedList<Album> with a new one and saves the changes inside JSON save file
     * @param pAlbumBefore Album object to replace in List and JSON
     * @param pAlbumAfter Replacement Album object
     */
    public void editAlbum(Album pAlbumBefore, Album pAlbumAfter) {
        // Search for album in albumList
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getAlbumName().equals(pAlbumBefore.getAlbumName()) & albumList.get(i).getAlbumArtist().equals(pAlbumBefore.getAlbumArtist())) {
                // Override album
                albumList.set(i, pAlbumAfter);

                // Override in JSON file
                overrideAlbumsInJson(albumList, "saveData\\data.json");
                System.out.println("Changed album " + pAlbumAfter.getAlbumName() + " by " + pAlbumAfter.getAlbumArtist() + " in JSON."); // DEBUG
                
                GUI.updateAlbums();
                return;
            }
        }

        // If no matching album was found
        System.out.println("No matching album found to change.");
    }

    /**
     * Removes an Album object from local LinkedList<Album> and JSON save file
     * @param pAlbum Album object to remove from List and JSON
     */
    public void deleteAlbum(Album pAlbum) {
        // Search for album in albumList
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getAlbumName().equals(pAlbum.getAlbumName()) & albumList.get(i).getAlbumArtist().equals(pAlbum.getAlbumArtist())) {
                // Remove from LinkedList
                albumList.remove(i);

                // Remove from JSON file
                overrideAlbumsInJson(albumList, "savaData\\data.json");
                System.out.println("Removed album " + pAlbum.getAlbumName() + " by " + pAlbum.getAlbumArtist() + " from JSON."); // DEBUG
                
                GUI.updateAlbums();
                return;
            }
        }

        // If no matching album was found
        System.out.println("No matching album found to delete.");
    }

    /**
     * Searches the given String in local LinkedList<Album> and returns matches as a LinkedList<Album>
     * Matches can contain album names and artists
     * @param pSearch Term to search in List
     * @return LinkedList<Album> with matches in album name or artist. If no matches, return empty List
     */
    public LinkedList<Album> searchForAlbum(String pSearch) {
        pSearch = pSearch.trim().toLowerCase();
        LinkedList<Album> llResults = new LinkedList<>();

        for (Album album : albumList) {
            if (album.getAlbumName().trim().toLowerCase().contains(pSearch) || album.getAlbumArtist().trim().toLowerCase().contains(pSearch))
                llResults.add(album);
        }

        return llResults;
    }

    //** METHODS TO SAVE AND LOAD ALBUM OBJECTS FROM A JSON FILE*/

    /**
     * Adds an Album object to the data of an already existing JSON file
     * @param pAlbum Album object to be added to existing file
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
