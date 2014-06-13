#!/usr/bin/sh
recur_fun()
{
for i in `ls -ltr | grep '^d'| awk '{print $9}'` 
do
echo $i;
cd $i;
ls
pwd
recur_fun
cd ..
pwd
done
} 
recur_fun