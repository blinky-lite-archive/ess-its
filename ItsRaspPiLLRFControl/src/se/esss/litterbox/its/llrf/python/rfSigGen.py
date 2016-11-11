#! /usr/bin/python
 
import usbtmc
import sys
import math
import time


rfSigGen =  usbtmc.Instrument(2733, 72)
rfSigGen.write("*RST")
print(rfSigGen.ask("*IDN?"))
rfSigGen.write("FREQ 350MHZ")
rfSigGen.write("POW -10")
rfSigGen.write("OUTP ON")
rfSigGen.write("PULM:SOUR EXT")
rfSigGen.write("PULM:STAT ON")
