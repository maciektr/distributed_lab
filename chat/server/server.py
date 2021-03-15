import sys
import socket
from threading import Thread
from datetime import datetime

from server.settings import Settings, msg_decode
from server.clients_store import ClientsStore, Client


class App:
    def __init__(self):
        self.settings = Settings()
        self.clients = ClientsStore()
        self.server_socket = None
        self.server_udp_socket = None

    def run(self):
        try:
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            self.server_socket.bind((self.settings.SERVER_IP, self.settings.SERVER_PORT))
            self.server_socket.listen(self.settings.MAX_CONN_COUNT)

            self.server_udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
            self.server_udp_socket.bind((self.settings.SERVER_IP, self.settings.SERVER_PORT))

            Thread(target=self.server_loop, daemon=True).start()
            Thread(target=self.udp_server_loop, daemon=True).start()
            self.console()
        except (KeyboardInterrupt, SystemExit):
            self.clients.close_all()
            self.server_socket.close()
            sys.exit()
        finally:
            self.clients.close_all()
            if self.server_socket:
                self.server_socket.close()
            if self.server_udp_socket:
                self.server_udp_socket.close()

    def user_handle(self, client: Client):
        try:
            nick = client.conn.recv(self.settings.MSG_SIZE)
            if not nick:
                return
            nick = msg_decode(nick)
            self.log(f'New client connected: {nick}')
            while True:
                msg = client.conn.recv(self.settings.MSG_SIZE)
                if not msg:
                    continue
                msg = f'From {nick}: {msg_decode(msg)}'
                self.clients.send_to_all(client, msg)
        except OSError:
            pass
        finally:
            self.clients.close_client(client)

    def server_loop(self):
        while True:
            conn, addr = self.server_socket.accept()
            client = Client(conn=conn, addr=addr)
            self.clients.push(client)
            Thread(target=self.user_handle, args=[client], daemon=True).start()

    def udp_server_loop(self):
        while True:
            msg, addr = self.server_udp_socket.recvfrom(self.settings.UDP_MSG_SIZE)
            msg = msg_decode(msg)
            self.clients.send_to_all_udp(msg, addr, self.server_udp_socket)

    @staticmethod
    def log(value: str):
        print(f'{str(datetime.now())}: {value}')

    def console(self):
        while True:
            cmd = input().strip()
            if cmd == ':quit':
                self.clients.close_all()
                break
