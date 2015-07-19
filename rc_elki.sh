#Sasha's scrpt for recompilling the elki library - it saves the old version in lib/old_elki

rm lib/old_elki/elki.jar 
cd mllib/src/main/scala/org/apache/spark/mllib/elki
mv elki.jar /localdisk/ashanina/git_repos/spark/lib/old_elki
make elki
cd /localdisk/ashanina/git_repos/spark
