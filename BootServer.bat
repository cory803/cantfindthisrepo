@echo off
title RuneLive
COLOR 2F
"C:/Program Files (x86)/java/jdk1.8.0_25/bin/java.exe" -server -Xmx1024m -cp bin;lib/* com.runelive.GameServer
pause