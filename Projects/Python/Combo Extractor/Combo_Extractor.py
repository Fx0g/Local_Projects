#returns email and pass from a text file
import re

def get_mail_pass():
    pattern = "\S+@\S+" #pattern to get mail:pass string
    combos = []
    file_name = input("enter source textfile name:")
    data_in = open(file_name,"r+")
    data_out = open("combos.txt","w+")
    each = [lines for lines in data_in.readlines() if lines.strip()]
    for l in each:
        combos.append((re.findall(pattern,l))[0])
    for c in combos:
        data_out.write(str(c)+"\n")
    data_in.close()
    data_out.close()
    return "successfully extracted "+str(len(combos))+" combos!"

print(get_mail_pass())
