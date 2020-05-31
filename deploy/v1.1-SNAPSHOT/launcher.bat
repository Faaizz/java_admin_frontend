::SETUP SAVE FILE

@ECHO OFF

::Check if save folder exists
IF EXIST "settings" (
    ::IF IT DOES, NAVIGATE IN AND CHECK IF save file DOESN'T EXIST
    CD settings
    IF NOT EXIST "main.json" (
        ::CREATE main.json FILE
        copy NUL main.json

    )

) ELSE (
    ::IF IT DOESN'T
    ::CREATE THE settings DIRECTORY  
    MKDIR settings
    ::ENTER THE DIRECTORY
    CD settings
    ::CREATE main.json FILE
    copy NUL main.json

)

::NAVIAGATE OUT OF settings FOLDER
CD ../

::============================================================

::SET TO USE LOCAL JAVA FX SDK
SET module_path="windows\javafx-sdk-11.0.2\lib" 

::LAUNCH APPLICATION
java -jar --module-path %module_path% --add-modules=javafx.controls,javafx.fxml java_admin_frontend-1.1-SNAPSHOT.jar