from socket import *

def main():
    #host and port
    host = 'localhost'
    port = 9999
    bool = 1
    msgs = True
    
    s = socket(AF_INET,SOCK_STREAM)
    s.bind((host,port))
    s.listen()
    print("Waiting For Incoming Connections...")


    while bool == 1:
        c,addr = s.accept()
        print("Connected with ",addr)
        c.send(b"Hello!")
        username = c.recv(1024)
        print(username.decode().capitalize()+":",c.recv(1023).decode())
        while msgs:
            if s.recv(1024).decode() == "stop":
                msgs = False
            print(s.recv(1024).decode())

if __name__ == '__main__':
    main()