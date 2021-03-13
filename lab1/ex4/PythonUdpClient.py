import socket;

serverIP = "127.0.0.1"
serverPort = 9008
msg = "Ping Python Udp!"
# msg = 'żółta gęś'

print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(bytes(f'P{msg}', 'cp1250'), (serverIP, serverPort))

resp = client.recv(128).decode('cp1250')
print('Response', resp)
