import java.util.LinkedList;

public class Album {
    private LinkedList<Track> trackList;
    private String albumName;
    private String albumArtist;
    private int releaseYear;
    private String albumCoverPath;
    private String whereBought;
    private boolean containsNulltrack;
    private String[] genre;

    private boolean onVinyl;
    private boolean onCd;
    private boolean onCassette;

    private LinkedList<Session> llSessions = new LinkedList<>();

    public Album() {
        // Empty constructor
    }

    public Album(LinkedList<Track> pTrackList, String pAlbumName, String pAlbumArtist, int pReleaseYear, String pCoverPath,
    String pWhereBought, boolean pContainsNulltrack, boolean pOnVinyl, boolean pOnCd, boolean pOnCassette, String[] pGenre) {
        trackList = pTrackList;
        albumName = pAlbumName;
        albumArtist = pAlbumArtist;
        releaseYear = pReleaseYear;
        albumCoverPath = pCoverPath;
        whereBought = pWhereBought;
        containsNulltrack = pContainsNulltrack;
        onVinyl = pOnVinyl;
        onCd = pOnCd;
        onCassette = pOnCassette;
        genre = pGenre;
    }

    /**
     * Sets/Overrides the track list
     * @param pTrackList New track list
     */
    public void setTrackList(LinkedList<Track> pTrackList) {
        trackList = pTrackList;
    }

    /**
     * Gets the track list
     * @return Album's track list
     */
    public LinkedList<Track> getTrackList() {
        return trackList;
    }

    /**
     * Sets/Overrides the album name
     * @param pAlbumName New album name
     */
    public void setAlbumName(String pAlbumName) {
        albumName = pAlbumName;
    }

    /**
     * Gets the album's name
     * @return Album's name
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets/Overrides the album's artist
     * @param pAlbumArtist New artist of the album
     */
    public void setAlbumArtist(String pAlbumArtist) {
        albumArtist = pAlbumArtist;
    }

    /**
     * Gets the album's artist
     * @return Album's artist
     */
    public String getAlbumArtist() {
        return albumArtist;
    }

    /**
     * Sets/Overrides the album's release year
     * @param pReleaseYear New release year
     */
    public void setReleaseYear(int pReleaseYear) {
        releaseYear = pReleaseYear;
    }

    /**
     * Gets the album's release year
     * @return Album's release year
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets/Overrides where the physical medium was bought
     * @param pWhereBought New place where the album was bought
     */
    public void setWhereBought(String pWhereBought) {
        whereBought = pWhereBought;
    }

    /**
     * Gets where the album was bought
     * @return Place where the album was bought
     */
    public String getWhereBought() {
        return whereBought;
    }

    /**
     * Sets/Overrides if album contains a nulltrack
     * @param pNulltrack New boolean if album contains a nulltrack
     */
    public void setContainsNulltrack(boolean pNulltrack) {
        containsNulltrack = pNulltrack;
    }

    /**
     * Gets if album contains a nulltrack
     * @return boolean if album contains a nulltrack
     */
    public boolean containsNulltrack() {
        return containsNulltrack;
    }

    /**
     * Sets/Overrides path to the album's cover
     * @param pPath New path to album's cover
     */
    public void setCoverPath(String pPath) {
        albumCoverPath = pPath;
    }

    /**
     * Gets path to album's cover
     * @return Path to album's cover
     */
    public String getCoverPath() {
        return albumCoverPath;
    }

    public void setOnVinyl(boolean onVinyl) {
        this.onVinyl = onVinyl;
    }

    public boolean isOnVinyl() {
        return onVinyl;
    }

    public void setOnCd(boolean onCd) {
        this.onCd = onCd;
    }

    public boolean isOnCd() {
        return onCd;
    }

    public void setOnCassette(boolean onCassette) {
        this.onCassette = onCassette;
    }

    public boolean isOnCassette() {
        return onCassette;
    }

    /**
     * Gets the listen count of the album and returns it
     * @return Listen count of the album
     */
    public int getListenCount() {
        return llSessions.size();
    }

    public String[] getGenres() {
        return genre;
    }

    public void setGenres(String[] genre) {
        this.genre = genre;
    }

    /**
     * Adds a session when a user listened to the album to the album data
     * @param pSession Session object to add at the end of sessions LinkedList
     */
    public void addSession(Session pSession) {
        llSessions.add(pSession);
    }

    public void setSessions(LinkedList<Session> pSessions) {
        llSessions = pSessions;
    }

    /**
     * Gets the LinkedList that contains the session data and returns it
     * @return LinkedList with all album's session objects, containing date, time and listened tracks
     */
    public LinkedList<Session> getSessions() {
        return  llSessions;
    }

    /**
     * Removes a Session object from the album's data
     * @param pIdx Index of the Session object to remove
     */
    public void removeSession(int pIdx) {
        // Remove listen count from each track
        for (int i = 0; i < llSessions.get(pIdx).getListenedTracks().size(); i++) {
            if (llSessions.get(pIdx).getListenedTracks().get(i)) trackList.get(i).decreaseListenCount();
        }
        // Remove listen count from album
        llSessions.remove(pIdx);
    }
}
