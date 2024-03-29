package ro.uaic.info.net.state;

public enum ServerState {
    WAITING_FOR_USERNAME,
    DO_NOTHING,
    UNKNOWN,
    SENDING_MATCH_LIST,
    JOIN_LOBBY_FAIL_NO_LOBBY,
    JOIN_LOBBY_FAIL_LOBBY_FULL,
    JOIN_LOBBY_SUCCESS,
    WAITING_FOR_LOBBY_NAME,
    LEAVE_LOBBY_SUCCESS,
    GAME_START_FAILED,
    GAME_START_SUCCESS_WHITE,
    GAME_START_SUCCESS_BLACK,
    WHITE_PLAYER_TURN,
    BLACK_PLAYER_TURN,
    WAITING_FOR_PIECE_LOCATION,
    SENDING_BOARD,
    WHITE_PLAYER_VICTORY,
    BLACK_PLAYER_VICTORY
}
