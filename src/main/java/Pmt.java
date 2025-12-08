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
    private LinkedList<Album> llAlbums;
    private SortType sortType = SortType.LAST_ADDED;
    private SortOrder sortOrder = SortOrder.DESCENDING;

    /**
     * Constructor of Pmt (Physical Media Tracker) class
     */
    public Pmt() {
        // Read settings
        try {
            settings = Settings.readSettings();
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        if (settings == null) settings = new Settings(); // Default settings

        // Load album data
        llAlbums = readAlbumsFromJson(settings.getDataPath());

        // Initialise GUI
        GUI = new Gui(this, settings);
    }

    /**
     * Gets an Album object from local LinkedList<Album> and returns it
     * @param Index Index of requested Album object
     * @return Album object of the given index
     */
    public Album getAlbum(int Index) {
        return llAlbums.get(Index);
    }

    /**
     * Gets the local LinkedList<Album> that contains all albums and returns it
     * @return LinkedList<Album> with all albums
     */
    public LinkedList<Album> getAlbums() {
        return llAlbums;
    }

    /**
     * Gets the local LinkedList<Album>, sorts it by local parameters sortType & sortOrder and returns it
     * @return LinkedList<Album> with all albums, sorted by local parameters sortType & sortOrder
     */
    public LinkedList<Album> sortAlbums() {
        switch (sortType) {
            case SortType.NAME -> {
                LinkedList<Album> llSorted = new LinkedList<>(llAlbums);
                llSorted.sort(Comparator.comparing(Album::getAlbumName));
                if (sortOrder == SortOrder.DESCENDING) return llSorted.reversed();
                return llSorted;
            }
            case SortType.ARTIST ->  {
                LinkedList<Album> llSorted = new LinkedList<>(llAlbums);
                llSorted.sort(Comparator.comparing(Album::getAlbumArtist, String.CASE_INSENSITIVE_ORDER));
                if (sortOrder == SortOrder.DESCENDING) return llSorted.reversed();
                return llSorted;
            }
            case SortType.YEAR -> {
                LinkedList<Album> llSorted = new LinkedList<>(llAlbums);
                llSorted.sort(Comparator.comparing(Album::getReleaseYear));
                if (sortOrder == SortOrder.DESCENDING) return llSorted.reversed();
                return llSorted;
            }
            case SortType.LAST_LISTENED -> {
                LinkedList<Album> llSorted = new LinkedList<>(llAlbums);
                llSorted.sort(
                        Comparator.comparing((Album a) -> a.getSessions().isEmpty()
                                                ? null
                                                : a.getSessions().getLast().getDate(),
                                        Comparator.nullsLast(Comparator.naturalOrder())
                                )
                                .thenComparing(
                                        (Album a) -> a.getSessions().isEmpty()
                                                ? null
                                                : a.getSessions().getLast().getTime(),
                                        Comparator.nullsLast(Comparator.naturalOrder())
                                )
                );
                if (sortOrder == SortOrder.DESCENDING) return llSorted.reversed();
                return llSorted;
            }
            case SortType.LAST_ADDED ->  {
                if (sortOrder == SortOrder.DESCENDING) return llAlbums.reversed();
                return llAlbums;
            }
            case null, default -> {
                return llAlbums;
            }
        }

    }

    /**
     * Gets the local LinkedList<Album>, sorts it by given parameters sortType & sortOrder and returns it
     * @param sortType Type of what the list should be sorted by
     * @param sortOrder Order of the sorted album (ascending or descending)
     * @return LinkedList<Album> with all albums, sorted by given parameters sortType & sortOrder
     */
    public LinkedList<Album> sortAlbums(SortType sortType, SortOrder sortOrder) {
        this.sortType = sortType;
        this.sortOrder = sortOrder;

        return sortAlbums();
    }

    /**
     * Gets the local LinkedList<Album>, sorts it by given parameter sortType & local parameter sortOrder and returns it
     * @param sortType Type of what the list should be sorted by
     * @return LinkedList<Album> with all albums, sorted by given parameter sortType & local parameter sortOrder
     */
    public LinkedList<Album> sortAlbums(SortType sortType) {
        this.sortType = sortType;

        return sortAlbums();
    }

    /**
     * Gets the local LinkedList<Album>, sorts it by local parameter sortType & given parameter sortOrder and returns it
     * @param sortOrder Order of the sorted album (ascending or descending)
     * @return LinkedList<Album> with all albums, sorted by local parameter sortType & given parameter sortOrder
     */
    public LinkedList<Album> sortAlbums(SortOrder sortOrder) {
        this.sortOrder = sortOrder;

        return sortAlbums();
    }

    /**
     * Adds an Album object to local LinkedList<Album> and adds it to the saved ones inside JSON save file
     * @param pAlbum Album object to add to List and save to JSON
     */
    public void addAlbum(Album pAlbum) {
        llAlbums.addLast(pAlbum);

        // Save as JSON file
        addAlbumToJson(pAlbum, settings.getDataPath());

        System.out.println("Added album " + pAlbum.getAlbumName() + " by " + pAlbum.getAlbumArtist() + " to JSON."); // DEBUG
        GUI.updateAlbums();
    }

    /**
     * Replaced an Album object in local LinkedList<Album> with a new one and saves the changes inside JSON save file
     * @param pAlbumBefore Album object to replace in List and JSON
     * @param pAlbumAfter Replacement Album object
     */
    public void editAlbum(Album pAlbumBefore, Album pAlbumAfter) {
        // Search for album in llAlbums
        for (int i = 0; i < llAlbums.size(); i++) {
            if (llAlbums.get(i).getAlbumName().equals(pAlbumBefore.getAlbumName()) & llAlbums.get(i).getAlbumArtist().equals(pAlbumBefore.getAlbumArtist())) {
                // Override album
                llAlbums.set(i, pAlbumAfter);

                // Override in JSON file
                overrideAlbumsInJson(llAlbums, settings.getDataPath());
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
        // Search for album in llAlbums
        for (int i = 0; i < llAlbums.size(); i++) {
            if (llAlbums.get(i).getAlbumName().equals(pAlbum.getAlbumName()) & llAlbums.get(i).getAlbumArtist().equals(pAlbum.getAlbumArtist())) {
                // Remove from LinkedList
                llAlbums.remove(i);

                // Remove from JSON file
                overrideAlbumsInJson(llAlbums, settings.getDataPath());
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

        for (Album album : llAlbums) {
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

    /**
     * Updates path to album data, loads it and updates GUI
     * @param pPath New path to album data
     */
    public void setDataPath(String pPath) {
        settings.setDataPath(pPath);
        Settings.writeSettings(settings);
        llAlbums = readAlbumsFromJson(settings.getDataPath());
        GUI.updateAlbums();
    }
}
