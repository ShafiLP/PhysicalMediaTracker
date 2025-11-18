import java.util.LinkedList;

public class Pmt {
    private final Gui GUI = new Gui(this);
    private LinkedList<Album> albumList = new LinkedList<>();

    public Pmt() {
        // Empty constructor
    }

    public Album getAlbum(int Index) {
        return albumList.get(Index);
    }

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
        // TODO
    }
}
