@echo off
@title TwMS 113 Server Debug Mode
set CLASSPATH=.;dist\*;lib\*
java -server -Xms2g -Xmx14g server.swing.WvsCenter
pause
