#! /usr/bin/python
 
import usbtmc
import sys
import time

freq = -1.0
modPulseWidth = -1.0
rfPulseWidth = -0.5
echo = False
sigGen =  usbtmc.Instrument(2391, 11271)

freq = float(sigGen.ask("SOURCE1:FREQUENCY?"))
mpw = float(sigGen.ask("SOURCE1:FUNCTION:PULSE:WIDTH?")) * 1000.0
rpw = float(sigGen.ask("SOURCE2:FUNCTION:PULSE:WIDTH?")) * 1000.0
phase = float(sigGen.ask("SOURCE2:PHASE?"))
modRiseTime = (phase / (-0.36 * freq)) - mpw

print "-frq " + str(freq) + " -mrt " + str(modRiseTime) + " -rpw " + str(rpw) + " -mod " + sigGen.ask("OUTPUT1?") + " -rf " + sigGen.ask("OUTPUT2?")
print "0"
