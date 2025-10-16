@echo off
echo Starting Monevo Banking System...

set JAVA_FX_PATH=C:\Users\Kaelo\Desktop\Java SDK\openjfx-24_windows-x64_bin-sdk\javafx-sdk-24\lib
java --module-path "%JAVA_FX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "target\classes" com.examplemonevo.App

pause