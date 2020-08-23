@echo off
del "app\build\outputs\apk\debug\output.json" /f /q 
gradlew assembleDebug > NUL && gradlew push > NUL