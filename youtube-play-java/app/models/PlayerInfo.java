package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.WebSocket;

public class PlayerInfo {
    public Integer playerId;
//    public String status;
//    public String thumbnailUrl;
//    public String videoId;
    public WebSocket.Out<JsonNode> outSocket;
}
