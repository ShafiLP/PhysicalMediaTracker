import java.util.LinkedList;

public class Session {
    private LinkedList<Boolean> llListenedTracks;
    private DateTime dateTime;

    public Session(LinkedList<Boolean> llListenedTracks, DateTime dateTime) {
        this.llListenedTracks = llListenedTracks;
        this.dateTime = dateTime;
    }

    public LinkedList<Boolean> getListenedTracks() {
        return llListenedTracks;
    }

    public void setDate(String date) {
        this.dateTime.setDate(date);
    }

    public String getDate() {
        return dateTime.getDate();
    }

    public void setTime(String time) {
        this.dateTime.setTime(time);
    }
    public String getTime() {
        return dateTime.getTime();
    }

    public void setListenedTracks(LinkedList<Boolean> llListenedTracks) {
        this.llListenedTracks = llListenedTracks;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
