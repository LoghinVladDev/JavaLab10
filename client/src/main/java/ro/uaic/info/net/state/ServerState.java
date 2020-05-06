package ro.uaic.info.net.state;

public enum ServerState {
    WAITING_FOR_USERNAME,
    DO_NOTHING,
    UNKNOWN,
    SENDING_MATCH_LIST,
    JOIN_LOBBY_FAIL_NO_LOBBY,
    JOIN_LOBBY_FAIL_LOBBY_FULL,
    JOIN_LOBBY_SUCCESS,
    WAITING_FOR_LOBBY_NAME
}
