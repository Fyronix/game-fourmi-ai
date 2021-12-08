@REM Compilation, if needed
cd ..
mkdir classes\resources
xcopy src\resources classes\resources /E /Q /Y
javac -nowarn -g:none -cp lib\lawrence.jar -sourcepath src -d classes src\fr\dev\lounge-lizard\FourmIR2000\*.java

@REM Program
cd classes
java -Xbootclasspath/a:..\lib\lawrence.jar -Xms64m -Xmx128m dev.lounge-lizard.fourmIR2000.Main
cd ..\bin
