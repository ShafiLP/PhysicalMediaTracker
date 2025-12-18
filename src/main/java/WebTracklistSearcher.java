import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class WebTracklistSearcher extends Thread {
    private final IWebTracklistSearcher PARENT;
    private final String albumName;
    private final String albumArtist;

    public WebTracklistSearcher(IWebTracklistSearcher PARENT, String albumName, String albumArtist) {
        this.PARENT = PARENT;
        this.albumName = albumName;
        this.albumArtist = albumArtist;
    }

    @Override
    public void run() {
        Log.info("Started searching for track list with musicbrainz API.");
        String queryString = "artist:" + albumArtist + " AND release:" + albumName;
        String query = "https://musicbrainz.org/ws/2/release/?query=" + URLEncoder.encode(queryString, StandardCharsets.UTF_8) + "&fmt=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .header("User-Agent", "PhysicalMediaTracker/0.2.4 ( https://github.com/ShafiLP )")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException ex) {
            Log.error("No response from server.");
            return;
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray releases = json.getAsJsonArray("releases");
        if (releases == null) {
            Log.warn("No track list found.");
            return;
        }
        String releaseId = releases.get(0).getAsJsonObject().get("id").getAsString();
        String trackQuery = "https://musicbrainz.org/ws/2/release/" + releaseId + "?inc=recordings&fmt=json";

        request = HttpRequest.newBuilder()
                .uri(URI.create(trackQuery))
                .header("User-Agent", "PhysicalMediaTracker/0.2.5 ( https://github.com/ShafiLP )")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> trackResponse = null;
        try {
            trackResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException ex) {
            Log.error("No response from server.");
            return;
        }

        JsonObject releaseJson = JsonParser.parseString(trackResponse.body()).getAsJsonObject();
        JsonArray media = releaseJson.getAsJsonArray("media");

        // Check if media equals null
        if (media == null) {
            Log.warn("No media found.");
            return;
        }

        LinkedList<TrackObject> llTracks = new LinkedList<>();

        Log.print("[RESULTS FROM TRACK LIST SEARCHER]");
        for (int i = 0; i < media.size(); i++) {
            JsonArray tracks = media.get(i).getAsJsonObject().getAsJsonArray("tracks");
            for (int j = 0; j < tracks.size(); j++) {
                JsonObject track = tracks.get(j).getAsJsonObject();
                String title =  track.get("title").getAsString();
                int position = track.get("position").getAsInt();

                Log.print(position + ". " + title);
                llTracks.add(new TrackObject(position, title));
            }
        }
        Log.print("[RESULTS END]");

        PARENT.setTracklist(llTracks);
    }
}

interface IWebTracklistSearcher {
    public void setTracklist(LinkedList<TrackObject> pTracklist);
}
