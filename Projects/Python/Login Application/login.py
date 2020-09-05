#simple login framework
import os 

if not os.path.exists("db.txt"):
    file =  open("db.txt","w+")

def write(username,password):
    print(username,password)
    db = open("db.txt","r+")

    for line in db.readlines():
        if str(username+" "+password) in line:
            return "exists"
    db.close()
    db = open("db.txt","a")
    db.write(username + " " + password + "\n")

def get():
    username = input("Enter Username: ")
    password = input("Enter Password: ")

    write(username,password)

get()
