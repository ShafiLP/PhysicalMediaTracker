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

public class AlbumCoverSearcher extends Thread {
    public static void searchCover(AlbumCreateFrame pFrame, String pAlbumName, String pArtist) throws IOException {
        String queryString = "artist:" + pArtist + " AND release:" + pAlbumName;
        String query = "https://musicbrainz.org/ws/2/release-group/?query=" + URLEncoder.encode(queryString, StandardCharsets.UTF_8) + "&fmt=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .header("User-Agent", "PhysicalMediaTracker/0.1.5 ( https://github.com/ShafiLP )")
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
            return;
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray groups = json.getAsJsonArray("release-groups");
        if (groups == null) return;
        String releaseGroupId = groups.get(0).getAsJsonObject().get("id").getAsString();

        String coverUrl = "https://coverartarchive.org/release-group/" + releaseGroupId + "/front"; // Final URL to the album cover
        System.out.println("Cover URL: " + coverUrl);

        pFrame.setImageFromUrl(coverUrl);
    }
}
