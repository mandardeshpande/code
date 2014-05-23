#!/bin/sh
# this example batch script requests many processors...
# for more info on requesting specific nodes see
# "man pbs_resources"
#PBS -V
#PBS -l nodes=node5:ppn=2
#PBS -N pong
#PBS -j oe
#PBS -q batch
cd $PBS_O_WORKDIR
NCORES=`wc -w < $PBS_NODEFILE`
echo "Pong using $NCORES cores..."
echo "PBS_O_WORKDIR: $PBS_O_WORKDIR"

###openmp###mpirun --mca orte_rsh_agent rsh -np 6 -hostfile $PBS_NODEFILE ./pong
echo " RUNTIME - START " '/bin/date'

mpirun -np 2 -hostfile $PBS_NODEFILE --nooversubscribe ./ring 1

echo " RUNTIME - END " '/bin/date/'
