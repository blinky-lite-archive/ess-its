#! /usr/bin/python
 
import usbtmc
import sys

modOn = False
rfOn = False
sigGen =  usbtmc.Instrument(2391, 11271)

noArg = (len(sys.argv) - 1) / 2 
if noArg > 0:
   for iarg in range(0, noArg):
      if sys.argv[1 + 2 * iarg] == '-mod':
         if sys.argv[1 + 2 * iarg + 1] ==  'on': modOn = True
      if sys.argv[1 + 2 * iarg] == '-rf':
         if sys.argv[1 + 2 * iarg + 1] ==  'on': rfOn = True

if modOn:
   sigGen.write("OUTPUT1 1")
else:
   sigGen.write("OUTPUT1 0")
if rfOn:
   sigGen.write("OUTPUT2 1")
else:
   sigGen.write("OUTPUT2 0")
print "0"

