#! /usr/bin/python
 
import usbtmc
import sys

idvendor = int(sys.argv[1])
idproduct = int(sys.argv[2])
command = sys.argv[3]

usbInst =  usbtmc.Instrument(idvendor, idproduct)
usbInst.write(command)
print "0"

