from dataclasses import dataclass

from utils import msg_encoder, msg_decoder


@dataclass(frozen=True)
class Settings:
    SERVER_IP = '127.0.0.1'
    SERVER_PORT = 8080
    MSG_ENCODING = 'utf-8'
    MSG_SIZE = 1024
    MAX_CONN_COUNT = 128


msg_encode = msg_encoder(Settings().MSG_ENCODING)
msg_decode = msg_decoder(Settings().MSG_ENCODING)
