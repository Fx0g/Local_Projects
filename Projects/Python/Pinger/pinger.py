import os

def main():
	os.system('cls')
	a = input("Enter IP to Ping: ")
	os.system('cls')
	b = input("Enter IP to Ping: "+a+".")
	os.system('cls')
	c = input("Enter IP to Ping: "+a+"."+b+".")
	os.system('cls')
	d = input("Enter IP to Ping: "+a+"."+b+"."+c+".")
		
	ip = a+"."+b+"."+c+"."+d
	os.system('ping '+ip)
	

main()