package me.mrpeterss.terrain;


// a link is data that the player will get from inputting a key with a command that will tell the plugin the information needed.

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.IOException;

public class Link {

    //the bounding box in lat and lon;
    public BoundingBox selectionBBox;

    //the zoom level at which the selection should be processed;
    public int selectionZoomLvl;

    //hyperlink is the input key; player is the player who initiated the request
    public Link(String hyperlink, Player player) throws IOException {
        setVars(hyperlink,player);
    }

    public void setVars(String hyperlink, Player player) throws IOException {
        //get the response of the request
        JSONObject response = Utils.getJsonFromLink("http://localhost:3000/api/link/"+hyperlink);

        if (response.toJSONString().equalsIgnoreCase("bad_key")) {
            //uh oh
            Utils.sendError(player, "You inputted a bad key, check the website to ensure that your key is correct.");
            return;
        }


        //bounding box as a json obj
        JSONObject json_box = (JSONObject) response.get("bbox");

        //set the variables
        selectionBBox = new BoundingBox((Float) json_box.get("east"), (Float) json_box.get("south"), (Float) json_box.get("west"), (Float) json_box.get("north"));
        selectionZoomLvl = (int) json_box.get("zoom");

    }
}
