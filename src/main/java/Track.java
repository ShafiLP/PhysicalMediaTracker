public class Track {
    private String trackName;
    private int trackNumber;
    private int listenCount;

    public Track() {
        // Empty constructor
    }

    public Track(String pName, int pTrackNumb) {
        trackName = pName;
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

    /**
     * Increases the listen count by one
     */
    public void incraseListenCount() {
        listenCount++;
    }

    public void setListenCount(int pListenCount) {
        listenCount = pListenCount;
    }

    /**
     * Gets the current listen count of the track and returns it
     * @return listen count of the track
     */
    public int getListenCount() {
        return listenCount;
    }

    public void increaseListenCount() {
        listenCount++;
    }

    public void decreaseListenCount() {
        listenCount--;
    }
}
