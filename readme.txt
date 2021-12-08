(c)2021 Lounge Lizarddev


1) MAIN COMMANDS
#################

How to run the program:
-----------------------
WITH ANT:
   $: ant run
WITHOUT ANT:
   $: cd bin
   $: sh fourmIR2000.sh
   OR
   $: cd classes
   $: java -Xbootclasspath/a:../lib/lawrence.jar -Xms64m -Xmx128m dev.lounge-lizard.fourmIR2000.Main

Note that you can omit the "-Xms64m -Xmx128m" options. This is done to improve
the gameplay with BIGS maps.


How to compile the program:
---------------------------
WITH ANT:
   $: ant compile
WITHOUT ANT:
   $: cp -rfu src/resources/ classes/
   $: javac -nowarn -g:none -cp lib/lawrence.jar -sourcepath src -d classes src/dev/lounge-lizard/fourmIR2000/*.java


How to build the Jar File:
--------------------------
   $: ant dist


How to build Javadoc:
---------------------
   $: ant javadoc


How to clean classes and Jar File:
----------------------------------
   $: ant clean


      
2) DOCUMENTATION
#################

The documentation could be found in the folder 'docs'.
Content:
 - user.pdf    user manual
 - dev.pdf     developper manual
 - api         developper javadoc 

 
3) TESTS
#########

Some test levels could be found in the folder 'levels'.
Content:
 - big.lvl             wide map
 - laby_100x100.lvl    big labyrinth of 100 tiles X 100 tiles
 - real_life.lvl       for a well balanced game
 - samantha.lvl        just for fun
 - lounge-lizard.lvl            idem

