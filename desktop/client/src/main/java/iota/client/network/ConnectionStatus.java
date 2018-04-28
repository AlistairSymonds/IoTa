package iota.client.network;

public enum ConnectionStatus {
    NOT_CONNECTED,
    FIRST_CONNECT, //connection started, no data has been sent yet
    SENDING_MAGIC_BYTE,
    WAITING_FOR_MAGIC_BYTE_PLUS_ONE, //
    SENDING_EMPTY_CAPSULE,
    WAITING_FOR_EMPTY_CAPSULE,
    CONNECTED,
    ERROR
}
