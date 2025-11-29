public class DateTime {
    private String date;
    private String time;

    /**
     * Empty constructor
     */
    public DateTime() {}

    /**
     * Constructor of DateTime class
     * @param date date
     * @param time time
     */
    public DateTime(String date, String time) {
        this.date = date;
        this.time = time;
    }

    /**
     * Gets the date String and returns it
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the time String and returns it
     * @return time
     */
    public String getTime() {
        return time;
    }
}
