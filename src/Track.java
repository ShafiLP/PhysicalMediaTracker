import java.util.LinkedList;

public class Track {
    private String trackName;
    private LinkedList<String> trackFeatures;
    private boolean containsNullTrack;

    public Track() {
        // Empty constructor
    }

    public Track(String pName, LinkedList<String> pFeatures, boolean pNulltrack) {
        trackName = pName;
        trackFeatures = pFeatures;
        containsNullTrack = pNulltrack;
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
     * Sets/Overrides the track's features
     * @param pTrackList New track's features
     */
    public void setContainsNulltrack(boolean pNulltrack) {
        containsNullTrack = pNulltrack;
    }

    /**
     * Gets the track's features
     * @return Track's features
     */
    public boolean getContainsNulltrack() {
        return containsNullTrack;
    }
}
