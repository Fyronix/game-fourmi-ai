#!/bin/sh

# Compilation, if needed
cd ..
cp -rfu src/resources/ classes/
javac -nowarn -g:none -cp lib/lawrence.jar -sourcepath src -d classes src/dev/lounge-lizard/fourmIR2000/*.java

# Program
cd classes
java -Xbootclasspath/a:../lib/lawrence.jar -Xms64m -Xmx128m dev.lounge-lizard.fourmIR2000.Main

cd ../bin
