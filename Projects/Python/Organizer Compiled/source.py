import os
import re
import shutil
import time

print("Started!")
time.sleep(3)

def main():
    path = (os.path.dirname(os.path.realpath(__file__)))
    os.chdir(path)
    extensions,folders,files = [],[],[]
    pattern = "\.+[a-zA-Z0-9]+"

    if not os.path.exists("file".upper()):
        os.mkdir("FILE".upper())

    for item in os.listdir():
        if os.path.isfile(item):
            if item != os.path.basename(__file__):
                if len(re.findall(pattern,item)) != 0:
                    files.append(item)
                    extensions.append(re.findall(pattern,item)[-1])
                else:
                    shutil.move(item,"FILE")
        else:
            folders.append(item)

    for extension in extensions:
        if not os.path.exists(extension.replace(".","")):
            os.mkdir((str(extension).replace(".","")).upper())
    
    for file in files:
        if not (os.path.exists(os.path.realpath(str(re.findall(pattern,file)[-1]).replace(".",""))+"\\"+file)):
            if ((str(re.findall(pattern,file)[-1]).replace(".","").upper())) in folders:
                if not os.path.exists(os.path.realpath(str(re.findall(pattern,file)[-1]).replace(".",""))+"'\'"+file):
                    shutil.move(file,os.path.realpath(str(re.findall(pattern,file)[-1]).replace(".","")))
        else:
            os.unlink(file)

for _ in range(2):
    main()