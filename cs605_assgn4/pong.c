#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include "mpi.h"

#define MB 1024*1024
#define ROOT 0
#define ONE 1

/* this application has 2 processes...  */
int main (int argc, char* argv[])
{
	int rank, proc_count, i, err, iter=10, left_partner_rank,right_partner_rank;
	int megad, ndoubles,pack_size,min_data_size,max_data_size,step_size;
	long nbytes;
	double *token; char cptr[100];
	double time_start, time_finish;
	static double time_total;

	MPI_Status status;

	gethostname(cptr,100);

	err = MPI_Init(&argc, &argv);
	if (err != MPI_SUCCESS)
	{
		printf("MPI initialization error\n"); return 0;
	}

	
/* Record your process count and local process rank */
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &proc_count);

	if (rank == ROOT)
	{
		if (argc < 3)
		{
			printf("Not the correct number of parameters, please try again\n");
			MPI_Finalize();
			return 0;
		}
	}

	min_data_size = atoi(argv[1]);
	max_data_size = atoi(argv[2]);
	step_size = atoi(argv[3]);
	


	MPI_Bcast(&min_data_size, 1, MPI_INT, 0, MPI_COMM_WORLD);
  	MPI_Bcast(&max_data_size, 1, MPI_INT, 0, MPI_COMM_WORLD);
   	MPI_Bcast(&step_size, 1, MPI_INT, 0, MPI_COMM_WORLD);
   //	MPI_Bcast(&iterations, 1, MPI_INT, 0, MPI_COMM_WORLD);



	/* echo info to STDOUT */
	printf("process %d: on host (%s)\n", rank, cptr);

	

/* Use Barrier command to sync all the processes*/


	 left_partner_rank = (rank+proc_count-1)%proc_count;
	 right_partner_rank = (rank+1)%proc_count;

	 for(pack_size=min_data_size; pack_size <= max_data_size ; pack_size = pack_size + step_size)
	 {
	 	ndoubles = pack_size*MB;
		nbytes = ndoubles*sizeof(double);
		token = (double *) malloc( (size_t) nbytes);

		time_start = 0.0;
		time_finish = 0.0;
		time_total = 0.0;

		if (!token)
		{
			printf("process %d: malloc() of %ld bytes failed; exiting...\n", rank, nbytes);
			MPI_Finalize();
			return 0;
		}

		
		if (rank == ROOT)
		{
			printf("process %d: malloc()ed %ld bytes\n", rank, nbytes);
		}
	
	 	
		for(i=0 ; i<iter ; i++) /* computes the number of iterations*/
		{
			MPI_Barrier(MPI_COMM_WORLD);

			if (rank == 0) 
			{
				time_start = MPI_Wtime();
				MPI_Send(token, ndoubles, MPI_DOUBLE, right_partner_rank, 0, MPI_COMM_WORLD);
				MPI_Recv(token, ndoubles, MPI_DOUBLE, left_partner_rank, 0, MPI_COMM_WORLD, &status);
				time_finish = MPI_Wtime();

				time_total += time_finish - time_start;
				printf("Time for iternation %d = %f \n",i+1,time_total);
			}

			else /* when the process is not a root process*/
			{ 
				MPI_Recv(token, ndoubles, MPI_DOUBLE, left_partner_rank, 0, MPI_COMM_WORLD, &status);
				MPI_Send(token, ndoubles, MPI_DOUBLE, right_partner_rank, 0, MPI_COMM_WORLD);
				
			}
		}	

		if (rank == ROOT) 
			{
				printf("total time for send()s and recv()s: %f seconds\n", time_total);
				printf("send rate= %f Mbytes/second\n", iter*2*((double) (nbytes/(MB)))/(time_total));
			}
	}

	

	MPI_Finalize();
	return 0;
}