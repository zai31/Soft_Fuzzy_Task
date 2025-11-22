@echo off
echo Building Fuzzy Logic Library...
if not exist "target" mkdir target
javac -d target -sourcepath src/main/java src/main/java/fuzzy/**/*.java src/main/java/casestudy/*.java
if %ERRORLEVEL% EQU 0 (
    echo Build successful!
    echo.
    echo To run the Patient Triage System:
    echo   java -cp target casestudy.PatientTriageSystem
    echo.
    echo To run the comprehensive demo:
    echo   java -cp target casestudy.PatientTriageDemo
) else (
    echo Build failed!
    exit /b 1
)

