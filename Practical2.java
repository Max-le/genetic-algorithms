import java.util.Random;
import java.io.*;

/**
 * Some very basic stuff to get you started. It shows basically how each
 * chromosome is built.
 * 
 * @author Jo Stevens
 * @version 1.0, 14 Nov 2008
 * 
 * @author Alard Roebroeck
 * @version 1.1, 12 Dec 2012
 * 
 */

public class Practical2 {

	static final String TARGET = "HELLO WORLD";
	static char[] alphabet = new char[27];
	static final boolean DEBUG = true ;


	/**
	 * @param args
	 */
	public static void main(String[] args) {


		//Size of the initial population
		int popSize = 100;
		double MUTATION_RATE = 0.5;


		//Creating the alhabet ( A to Z + space)
		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet[c - 'A'] = c;
		}
		alphabet[26] = ' ';


		Random generator = new Random(System.currentTimeMillis());
		//Create a arrays containing the objects "Individual".
		Individual[] population = new Individual[popSize];

		// we initialize the population with random characters
		if (DEBUG) System.out.println("initializing the population...");

		for (int i = 0; i < popSize; i++) {
			char[] tempChromosome = new char[TARGET.length()];
			for (int j = 0; j < TARGET.length(); j++) {
				tempChromosome[j] = alphabet[generator.nextInt(alphabet.length)]; //choose a random letter in the alphabet
			}
			population[i] = new Individual(tempChromosome);
			if (DEBUG) System.out.println(population[i].genoToPhenotype());
		}
		if (DEBUG) System.out.println("Initial population created.");

		//Create an individual with correct solution for testing purposes
		//population[19]= new Individual("HELLO WORLD".toCharArray());

	//--------------------------------------------------//
			//-----------EVOLUTION LOOP---------//
	//--------------------------------------------------//

		boolean targetFound = false ;

		while (targetFound == false) {
			final boolean DEBUG = false ; 
			
			int position_target = 0 ;
		//Search the population if there's an individual matching the target
			for (int i = 0 ; i < popSize ; i++) {
				if (TARGET.equals(population[i].genoToPhenotype())){
					targetFound = true ; 
					System.out.println("TARGET FOUND !!! ");
					position_target = i ; 
				}
				else{
		 		//System.out.println("Target not found. Continue searching...") ; 
				}

			}

		//Calculate and assign fitness for every individual 
			for (int i = 0; i < population.length; i++) {
				population[i].setFitness(Practical2.calcFitness(population[i]));
			}
		//Just DEBUG info in a external txt file
			if (DEBUG) {	
				String log = new String(); 
				for (int i = 0; i < population.length; i++) {
					log = log + population[i].genoToPhenotype() + " " + population[i].getFitness() +"\n";
				}
				try {
					PrintWriter writer = new PrintWriter("log/init_population.txt", "UTF-8");
					writer.println(log);
					writer.close(); 

				}
				catch( FileNotFoundException e) {
					System.out.println("ERROR !  File not found.");

				}
				catch (UnsupportedEncodingException e ){
					System.out.println("ERROR !  UnsupportedEncodingException returned.");

				}
			}

		//Sort the population according to their fitness.
			HeapSort.sort(population);
			//Show fittest individual
			System.out.println("Best one : "+ population[0].genoToPhenotype()+ " "+population[0].getFitness());


		//Just DEBUG info in a external txt file
			if (DEBUG) {	
				String log = new String(); 
				for (int i = 0; i < population.length; i++) {
					log = log + population[i].genoToPhenotype() + " " + population[i].getFitness() +"\n";
				}
				try {
					PrintWriter writer = new PrintWriter("log/init_population_sorted.txt", "UTF-8");
					writer.println(log);
					writer.close(); 

				}
				catch( FileNotFoundException e) {
					System.out.println("ERROR !  File not found.");

				}
				catch (UnsupportedEncodingException e ){
					System.out.println("ERROR !  UnsupportedEncodingException returned.");

				}
			}

		//Creates a mating pool : a subset of the population ( 25 %) that are elected to reproduce.
		//Mating pool = elite of the population
			Individual[] matingPool = new Individual[popSize/4];

		//Adds to the top 25% to the pool
			for (int i = 0 ; i < matingPool.length ;i++ ) {
				matingPool[i] = population[i];	
			}

		//Just DEBUG info in a external txt file
			if (DEBUG) {	
				String log = new String(); 
				for (int i = 0; i < matingPool.length; i++) {
					log = log + matingPool[i].genoToPhenotype() + " " + matingPool[i].getFitness() +"\n";
				}
				try {
					PrintWriter writer = new PrintWriter("log/Mating_pool.txt", "UTF-8");
					writer.println(log);
					writer.close(); 

				}
				catch( FileNotFoundException e) {
					System.out.println("ERROR !  File not found.");

				}
				catch (UnsupportedEncodingException e ){
					System.out.println("ERROR !  UnsupportedEncodingException returned.");

				}
			}


		//-------------REPRODUCTION PHASE--------------//


		//REMPLACER la liste population[] par les children fait par la matingPool
		//The fresher generation. 

		//Individuals picked for reproduction
			Individual[] nextgen = new Individual[popSize];

			for (int i = 0 ;i<popSize ;i++ ) {


				Individual mate1 = select(matingPool); 
				Individual mate2 = select(matingPool) ;

				if (DEBUG) System.out.println("Individuals selected for reproduction  : \n"+ mate1.genoToPhenotype() + "\n"+ mate2.genoToPhenotype()+"\n---------"); 

		//Crossover : creating the child
				Individual child = crossover(mate1, mate2);
				if (DEBUG) System.out.println("Before mutation  :\n"+child.genoToPhenotype());
		//mutate the child
				child = mutation(child, MUTATION_RATE) ; 
				if (DEBUG)  System.out.println("After mutation : \n"+child.genoToPhenotype()); 


		//children are the new generation, they replace the population
				population[i] = child ; 
			}



		//Recalculate and assign fitness for every individual 
			for (int i = 0; i < population.length; i++) {
				population[i].setFitness(calcFitness(population[i]));
			}

		//Just DEBUG info in a external txt file
			if (DEBUG) {	
				String log = new String(); 
				for (int i = 0; i < population.length; i++) {
					log = log + population[i].genoToPhenotype() + " " + population[i].getFitness() +"\n";
				}
				try {
					PrintWriter writer = new PrintWriter("log/population_nextgen.txt", "UTF-8");
					writer.println(log);
					writer.close(); 

				}
				catch( FileNotFoundException e) {
					System.out.println("ERROR !  File not found.");

				}
				catch (UnsupportedEncodingException e ){
					System.out.println("ERROR !  UnsupportedEncodingException returned.");

				}
			}
		}//END WHILE LOOP




		// do your own cool GA here
		/**
		 * Some general programming remarks and hints:
		 * - A crucial point is to set each individual's fitness (by the setFitness() method) before sorting. When is an individual fit? 
		 * 	How do you encode that into a double (between 0 and 1)? -- CHECK
		 * - Decide when to stop, that is: when the algorithm has converged. And make sure you  terminate your loop when it does.
		 * - print the whole population after convergence and print the number of generations it took to converge.
		 * - print lots of output (especially if things go wrong).
		 * - work in an orderly and structured fashion (use tabs, use methods,..)
		 * - DONT'T make everything private. This will only complicate things. Keep variables local if possible
		 * - A common error are mistakes against pass-by-reference (this means that you pass the 
		 * 	address of an object, not a copy of the object to the method). There is a deepclone method included in the
		 *  Individual class.Use it!
		 * - You can compare your chromosome and your target string, using for eg. TARGET.charAt(i) == ...
		 * - Check your integers and doubles (eg. don't use ints for double divisions).
		 */

	}// END MAIN



	/*Give a fitness score to an individual between 0 and 1. 
	The function function is the percentage of characters 
	that match the target ( must be same character and same position).
	*/
	public static double calcFitness(Individual individu){
		double fitness = 0 ; 
		for (int i = 0 ; i < individu.chromosome.length; i++) {
			//Checks if the character at position i is the same in target
			if (individu.chromosome[i] == TARGET.charAt(i) ){
				fitness = fitness + 1.0/individu.chromosome.length;
			}
		}
		return fitness ;
	}



		//Select two parents at random in the mating pool

	//Selects an individual at random in an array ( the population )
	public static Individual select(Individual[] population) {
		final boolean DEBUG = false; 
		Random ran = new Random(); 
		int x = ran.nextInt(population.length-1)  ;
		if (DEBUG) System.out.println(population.length);
		if (DEBUG) System.out.println("x : "+x);	
		return population[x];
	}

	/**Performs crossover from two parents, returns a newborn.
	It simply takes the first half of parent1 and second half from parent2 and merges the two parts.*/
	public static Individual crossover(Individual parent1, Individual parent2) {
		final boolean DEBUG = false; 

		String dna1 = parent1.genoToPhenotype() ;
		String dna2 = parent2.genoToPhenotype() ; 
		String newDNA = new String(); 

		final int LENGTH_OF_DNA = dna1.length();
		if (DEBUG) System.out.println("LENGTH_OF_DNA : "+ LENGTH_OF_DNA);

		// DNA as form of char list instead of String
		char[] chromosome1 = parent1.getChromosome();
		char[] chromosome2 = parent2.getChromosome();
		char[] newchromosome = new char[LENGTH_OF_DNA];

		Random ran = new Random(); 
		for (int i =0 ;i < LENGTH_OF_DNA ;i++ ) {
			double x = ran.nextDouble(); 
			if(x< 0.5){
				if (DEBUG) System.out.println("Copying Character from first parent...");
				newchromosome[i] = chromosome1[i];
			}
			else {
				if (DEBUG) System.out.println("Copying Character from second parent...");
				newchromosome[i] = chromosome2[i];
			}
			
		}
	
			//Create the child
		Individual child = new Individual(newchromosome); 
		if (DEBUG)  System.out.println("new child : " + child.genoToPhenotype());
		return child; 
	}

	/**Exposes the child to gamma rays ( may result as an alteration of the DNA ...)
	@param individu Indivudual that we are gonna expose to gamma rays and may mutate
	@param rate Chance of a letter is altered ( between 0 and 1 )
	*/
	public static Individual mutation(Individual individu, double rate){
		final boolean DEBUG = false; 
		if (DEBUG ) System.out.println("Trying to mutate "+individu.genoToPhenotype() + " with rate "+ rate);


		//Creating an alhabet ( A to Z + space) ( code reused )
		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet[c - 'A'] = c;
		}
		alphabet[26] = ' ';

		Random ran = new Random();
		char[] dna = individu.getChromosome();
		char[] newDNA = individu.getChromosome();
		for (int i = 0 ; i< dna.length ; i++ ) {
			double x = ran.nextDouble(); 
			if (DEBUG) System.out.println("x = "+ x);
			if (x < rate) {

				//Mutation ! 
				

				//Gives a random letter
				newDNA[i] =  alphabet[ran.nextInt(alphabet.length)]; 
				if (DEBUG ) System.out.println("mutation !  "+(i+1)+"nd element" +" becomes "+newDNA[i]);

			} 
			else {
				//No mutation
				newDNA[i] = dna[i];
			}
			i++;
			
		}//end for loop
		Individual mutatedIndividual = new Individual(newDNA);

		if (DEBUG)	System.out.println("Mutated individual : "+ mutatedIndividual.genoToPhenotype());
		return mutatedIndividual;


	}// end mutation method



}


