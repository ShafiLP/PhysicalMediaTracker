import java.util.LinkedList;

public class Track {
    private String trackName;
    private LinkedList<String> trackFeatures;
    private int trackNumber;

    public Track() {
        // Empty constructor
    }

    public Track(String pName, LinkedList<String> pFeatures, int pTrackNumb) {
        trackName = pName;
        trackFeatures = pFeatures;
        trackNumber = pTrackNumb;
    }

    /**
     * Sets/Overrides the track's name
     * @param pTrackList New track name
     */
    public void setTrackName(String pName) {
        trackName = pName;
    }

    /**
     * Gets the track's name
     * @return Track's name
     */
    public String getTrackName() {
        return trackName;
    }

    /**
     * Sets/Overrides the track's features
     * @param pTrackList New track's features
     */
    public void setTrackFeatures(LinkedList<String> pFeatures) {
        trackFeatures = pFeatures;
    }

    /**
     * Gets the track's features
     * @return Track's features
     */
    public LinkedList<String> getTrackFeatures() {
        return trackFeatures;
    }

    /**
     * Sets/Overrides the track's number (index)
     * @param pTrackList New track's number (index)
     */
    public void setTrackNumber(int pNumb) {
        trackNumber = pNumb;
    }

    /**
     * Gets the track's number (index)
     * @return Track's number (index)
     */
    public int getTrackNumber() {
        return trackNumber;
    }
}
