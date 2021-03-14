import sys
import socket
from threading import Thread

from client.settings import Settings, msg_decode, msg_encode


class App:
    def __init__(self):
        self.settings = Settings()
        self.client_socket = None

    def close(self):
        if self.client_socket:
            self.client_socket.close()

    def run(self):
        try:
            self.client_socket = socket.socket()
            self.client_socket.connect((self.settings.SERVER_IP, self.settings.SERVER_PORT))
            nick = input('Enter your nick: ').strip()
            self.client_socket.send(msg_encode(nick))
            Thread(target=self.msg_receive_loop, daemon=True).start()
            self.msg_send_loop()
        except ConnectionRefusedError:
            print('Cannot connect to server.')
        except (KeyboardInterrupt, SystemExit):
            self.close()
            sys.exit()
        except Exception as e:
            self.close()
            raise e
        finally:
            self.close()

    def msg_receive_loop(self):
        while True:
            msg = self.client_socket.recv(self.settings.MSG_SIZE)

            if not msg:
                self.close()
                break

            msg = msg_decode(msg)
            print(msg)

    def msg_send_loop(self):
        while True:
            msg = input().strip()
            if msg == ':quit':
                break
            self.client_socket.send(msg_encode(msg))
