#evilzone unpacker challenge
#by methran

import patoolib
import os
os.chdir = (os.path.dirname(os.path.realpath(__file__)))

for ID in range(999,0,-1):
    print(ID)
    patoolib.extract_archive("flag_"+str(ID)+".tar.gz")
