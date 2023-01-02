package me.mrpeterss.terrain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    //get a JSONObject from a http req url
    public static JSONObject getJsonFromLink(String url) throws IOException {
        String jsonString = makeHttpRequest(url);

        // Create a JSONObject from the JSON string
        return (JSONObject) JSONValue.parse(jsonString);
    }

    private static String makeHttpRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method and properties
        connection.setRequestMethod("GET");
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoInput(true);

        // Make the request and get the response code
        connection.connect();
        int responseCode = connection.getResponseCode();

        // Read the response body if the response was successful (200)
        if (responseCode == 200) {
            InputStream inputStream = connection.getInputStream();
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } else {
            return "bad_key";
        }
    }

    public static void sendError(Player player, String msg) {
        player.sendMessage(colorString("&d&b[&bMapGen&d&b] &c" + msg));
    }

    public static void sendGood(Player player, String msg) {
        player.sendMessage(colorString("&d&b[&bMapGen&d&b] &a&b" + msg));
    }
    public static void sendMeh(Player player, String msg) {
        player.sendMessage(colorString("&d&b[&bMapGen&d&b] &e" + msg));
    }

    public static String colorString(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
