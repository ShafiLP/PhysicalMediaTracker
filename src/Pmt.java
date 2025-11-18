import java.util.LinkedList;

public class Pmt {
    private final Gui GUI = new Gui(this);
    private LinkedList<Album> albumList;

    public Pmt() {
        // Empty constructor
    }

    public Album getAlbum(int Index) {
        return albumList.get(Index);
    }
}
