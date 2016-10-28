#! /usr/bin/python
 
import usbtmc
import sys
import math
import time

freq = 1.0
modRiseTime = 1.0
modPulseWidth = 0.100
modVolt = 3.0
rfPulseWidth = 0.5
rfVolt = 1.0
echo = False
sigGen =  usbtmc.Instrument(2391, 11271)
tekScope =  usbtmc.Instrument(1689, 927)

noArg = (len(sys.argv) - 1) / 2 
if noArg > 0:
   for iarg in range(0, noArg):
      if sys.argv[1 + 2 * iarg] == '-frq':
         freq = float(sys.argv[1 + 2 * iarg + 1])
      if sys.argv[1 + 2 * iarg] == '-mrt':
         modRiseTime = float(sys.argv[1 + 2 * iarg + 1])
      if sys.argv[1 + 2 * iarg] == '-rpw':
         rfPulseWidth = float(sys.argv[1 + 2 * iarg + 1])
      if sys.argv[1 + 2 * iarg] == '-ech':
         if sys.argv[1 + 2 * iarg + 1] ==  'true': echo = True
if rfPulseWidth > 2.0: rfPulseWidth = 2.0
if freq > 14.0: freq = 14.0

sigGen.write("OUTPUT1 0")
sigGen.write("OUTPUT2 0")
time.sleep(1)

tekScope.write("*RST")
if echo: print(tekScope.ask("*IDN?"))
cmd = "CH1:PROBE:GAIN 1"
if echo: print cmd
tekScope.write(cmd)
cmd = "CH1:VOLTS " + str(modVolt * 0.5)
if echo: print cmd
tekScope.write(cmd)
cmd = "CH1:OFFSET " + str(modVolt)
if echo: print cmd
tekScope.write(cmd)
cmd = "CH2:PROBE:GAIN 1"
if echo: print cmd
tekScope.write(cmd)
cmd = "CH2:VOLTS " + str(rfVolt * 0.5)
if echo: print cmd
tekScope.write(cmd)
cmd = "CH2:OFFSET " + str(rfVolt)
if echo: print cmd
tekScope.write(cmd)
cmd = "TRIGGER:A:EDGE:SOURCE CH1"
if echo: print cmd
tekScope.write(cmd)
cmd = "TRIGGER:A:LEVEL:CH1 " + str(0.25 * modVolt)
if echo: print cmd
tekScope.write(cmd)
cmd = "TRIGGER:A:MODE NORMAL"
if echo: print cmd
tekScope.write(cmd)
cmd = "SELECT:CH1 ON"
if echo: print cmd
tekScope.write(cmd)
cmd = "SELECT:CH2 ON"
if echo: print cmd
tekScope.write(cmd)

horScale10 = round(math.log10((modPulseWidth + modRiseTime + rfPulseWidth)/ 8.0))
horScale10 = math.pow(10.0,horScale10)
horScale = ((modPulseWidth + modRiseTime + rfPulseWidth) / 8.0) / horScale10;
horScale2 = 2.0
if horScale > 4.0: 
   horScale2 = 10.0
else:
   if horScale > 2.0:
      horScale2 = 4.0
horScale2 = 0.001 * horScale2 * horScale10

cmd = "HORIZONTAL:SCALE " + str(horScale2)
if echo: print cmd
tekScope.write(cmd)
cmd = "HORIZONTAL:DELAY:TIME " + str(horScale2 * 4.0)
if echo: print cmd
tekScope.write(cmd)


if echo: print(sigGen.ask("*IDN?"))
sigGen.write("*RST")
cmd = "SOURCE1:APPLY:PULSE " + str(freq) + "," + str(modVolt) + "," + str(0.5 * modVolt)
if echo: print cmd
sigGen.write(cmd)
cmd = "SOURCE1:FUNCTION:PULSE:WIDTH " + str(modPulseWidth * 0.001)
if echo: print cmd
sigGen.write(cmd)
cmd = "SOURCE2:APPLY:PULSE " + str(freq) + "," + str(rfVolt) + "," + str(0.5 * rfVolt)
if echo: print cmd
sigGen.write(cmd)
cmd = "SOURCE2:FUNCTION:PULSE:WIDTH " + str(rfPulseWidth * 0.001)
if echo: print cmd
sigGen.write(cmd)
sigGen.write("SOURCE2:PHASE:SYNCHRONIZE")
phase = -0.36 * (modRiseTime + modPulseWidth) * freq
cmd = "SOURCE2:PHASE " + str(phase)
if echo: print cmd
sigGen.write(cmd)
sigGen.write("OUTPUT1 0")
sigGen.write("OUTPUT2 0")
time.sleep(1)
tekScope.write("TRIGGER FORCE")
print "0"

