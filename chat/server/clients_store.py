import socket

from server.settings import msg_encode


class Client:
    def __init__(self, conn: socket.socket):
        self.conn = conn

    def close(self):
        if self.conn:
            self.conn.close()

    def __eq__(self, other):
        if isinstance(other, Client):
            return self.conn == other.conn

        if isinstance(other, socket.socket):
            return self.conn == other

        return False

    def send(self, msg):
        self.conn.send(msg_encode(msg))


class ClientsStore:
    def __init__(self):
        self.store = []

    def push(self, value: Client):
        self.store.append(value)

    def close_all(self):
        map(lambda c: c.close(), self.store)
        self.store = []

    def close_client(self, client):
        client.close()
        if client in self.store:
            self.store.remove(client)

    def send_to_all(self, sender: Client, msg: str):
        for client in self.store:
            if client == sender:
                continue
            try:
                client.send(msg)
            except BrokenPipeError:
                self.close_client(client)
