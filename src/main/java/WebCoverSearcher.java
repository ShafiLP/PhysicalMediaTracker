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

public class WebCoverSearcher extends Thread {
    private final IWebCoverSearcher PARENT;
    private final String albumName;
    private final String albumArtist;

    public WebCoverSearcher(IWebCoverSearcher PARENT, String albumName, String albumArtist) {
        this.PARENT = PARENT;
        this.albumName = albumName;
        this.albumArtist = albumArtist;
    }

    @Override
    public void run() {
        String queryString = "artist:" + albumArtist + " AND release:" + albumName;
        String query = "https://musicbrainz.org/ws/2/release-group/?query=" + URLEncoder.encode(queryString, StandardCharsets.UTF_8) + "&fmt=json";

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
            System.out.println("[ERROR] No response from server.");
            PARENT.setCoverFromUrl(null);
            return;
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray groups = json.getAsJsonArray("release-groups");
        if (groups == null) {
            System.out.println("[WARNING] No album cover found.");
            PARENT.setCoverFromUrl(null);
            return;
        }
        String releaseGroupId = groups.get(0).getAsJsonObject().get("id").getAsString();

        String coverUrl = "https://coverartarchive.org/release-group/" + releaseGroupId + "/front"; // Final URL to the album cover
        System.out.println("Cover URL: " + coverUrl);

        PARENT.setCoverFromUrl(coverUrl);
    }
}

interface IWebCoverSearcher {
    public void setCoverFromUrl(String coverUrl);
}
