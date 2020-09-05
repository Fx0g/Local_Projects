from socket import *

def main():

    #server address and port
    addr = 'localhost'
    port = 9999
    cont = True
    
    c = socket(AF_INET,SOCK_STREAM)

    print("Connecting to ",addr)
    c.connect((addr,port))
    print("Connected To Server!")
    name = input("Enter your username: ")

    #get bytes from server

    print("Server:",c.recv(1024).decode())
    c.send(bytes(name,'utf-8'))
    c.send(b"hello back")

    while cont:
        message = input(name+":")
        c.send(bytes(name,'utf-8'))


main()
