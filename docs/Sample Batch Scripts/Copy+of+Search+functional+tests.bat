:TAExecute0
@echo off 
set JAVA_TOOL_OPTIONS=
set _JAVA_OPTIONS=
cls 
title TestArchitect - Command Line Tool 
"C:\Program Files\LogiGear\TestArchitect\jre\bin\java.exe" -jar -Xmx512m "C:\Program Files\LogiGear\TestArchitect\binclient\TACommandLine.jar" /exechost localhost /execport 53600 /rshost WIN-FLB11GGPAFM.localdomain /rsport 53400 /lsaddr  "checkLicenseByTrialKey" /lsport  "14101" /lsusr  "" /dbtype  "javadb" /dbname "SampleRepository" /uid "administrator" /pwd "04542414A4DC3D9D0CEB2B0A2BDB1" /proid 11fkqmi8av /srvid b2gyccouoboz /sessionid  "cb26bebb-27e3-4ff4-b79c-008478c6b0e6" /var  "" /resultname  "Copy of Search functional tests"  /comment "" /mod "\Copy of Search functional tests	initial{#@}TC 01{#@}TC 02{#@}final	b2hkd0sifrfn"   /openresult "no" /toolname "TestArchitect Automation Playback" /toolscript "{INSTALL_DIR}/binclient/taplayback.exe" /toolpath "{INSTALL_DIR}/binclient/taplayback.exe" /toolcmd "" /versions "" /delay "0" /redunlsaddr  "" /redunlsport  "14101" /xupath  "C:\tempp\TestResults" /exporthtmlpath  "" /exportxmlpath  "" /uploadresulttorepos  "" /testsetid  "" /uploadedfiletype  "" /timetraveling  "" /udf "build number		" /capturecond  "" /numofinteraction  "" /exportscreenshotcond  "0"
exit
