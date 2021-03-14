
def msg_encode(msg: str, encoding: str = 'UTF-8'):
    return bytes(msg, encoding)


def msg_encoder(encoding: str = 'UTF-8'):
    def __encode(msg: str):
        return msg_encode(msg, encoding)
    return __encode


def msg_decode(msg, encoding: str = 'UTF-8'):
    return str(msg, encoding)


def msg_decoder(encoding: str = 'UTF-8'):
    def __decode(msg):
        return msg_decode(msg, encoding)
    return __decode
