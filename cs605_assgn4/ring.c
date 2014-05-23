#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include "mpi.h"
                                               
#define ROOT  0
#define MB 1024*1024   

int main (int argc, char* argv[])
{
  int my_id, num_procs, ierr, other;
  int megad, ndoubles;
  long nbytes;
  double *token;
  char host[100];
  MPI_Status status;
  double bandwidth;

  double start_time, end_time, total_time;

  gethostname(host,100);

// Initinilize a process
  ierr = MPI_Init(&argc, &argv);
  if (ierr != MPI_SUCCESS)
  {
    printf("MPI initialization error\n"); return 0;
  }

// find the rank of each process
  MPI_Comm_rank(MPI_COMM_WORLD, &my_id);
  MPI_Comm_size(MPI_COMM_WORLD, &num_procs); // find the no.of processes in the process group

  megad = atoi(argv[1]);
  ndoubles = megad*MB;
  nbytes = ndoubles*sizeof(double);

  token = (double *) malloc( (size_t) nbytes);

  if(num_procs <= 2) 
  {
     if(my_id == ROOT) 
     {
       printf("Number of processes should be more than 2\n");
     }
     return 0;
  }

  if( my_id == 0)
  {
    start_time = MPI_Wtime();
    MPI_Send(token,ndoubles, MPI_DOUBLE, 1,0, MPI_COMM_WORLD);
    MPI_Recv(token,ndoubles, MPI_DOUBLE, num_procs - 1, 0,MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    end_time = MPI_Wtime();
    total_time = end_time - start_time;
  }

  
  if(my_id != 0)
  {
    MPI_Recv(token, ndoubles, MPI_DOUBLE, my_id - 1, 0,MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    MPI_Send(token,ndoubles, MPI_DOUBLE, (my_id + 1) % num_procs,0, MPI_COMM_WORLD);
    
    printf("Process %d received token %d from process %d\n",my_id, nbytes, my_id - 1);
  }
  

  
   
  if(my_id == 0)
  {
    printf("Total time required is %f \n", total_time);
    bandwidth = num_procs*8*((double) (nbytes/(MB))/(total_time));
    printf("bandwidth in Mbits/seconds is  %f \n", bandwidth);
    
  }


  

// Wrap up everything
  MPI_Finalize();
  return 0;
}

