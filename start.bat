@echo off
@title ZSRC_Server
set path=jdk\bin;%SystemRoot%\system32;%SystemRoot%;%SystemRoot%
java -cp "zsrc.jar;lib/*" gui.Start
pause