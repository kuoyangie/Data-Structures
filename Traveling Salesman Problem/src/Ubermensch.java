import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Ubermensch {
    private final int generationSize;   // number of genomes/individuals in each generation
    private final int genomeSize;       // Equals to number of nodes minus one (Chromosome length)
    private final int numberOfCities;
    private final int reproductionSize;
    // Number of genomes who will be selected to reproduce to make the next generation
    // The maximum number of generations to program will evolve before terminating
    // in case there is no convergence before then.
    private final int maxIterations;
    private final float mutationRate;   // Frequency of mutations when creating a new generation
    private final int tournamentSize;
    private final SelectionType selectionType;
    private final int[][] travelPrices;
    private final int startingCity;
    // Fitness of the best genome has to reach according to the objective function
    // for the program to terminate early. Setting a fitness value can shorten the program
    private final int targetFitness;

    /**
     * Constructor for Übermensch (Genetic Algorithm Data Fields)
     * @param numberOfCities int number of vertices
     * @param selectionType enum Two solving methods (roulette, tournament)
     * @param travelPrices int[][] graph
     * @param startingCity int starting position
     * @param targetFitness int
     */
    public Ubermensch(int numberOfCities, SelectionType selectionType, int[][] travelPrices, int startingCity, int targetFitness){
        this.numberOfCities = numberOfCities;
        this.genomeSize = numberOfCities - 1;
        this.selectionType = selectionType;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.targetFitness = targetFitness;

        generationSize = 5000;
        reproductionSize = 200;
        maxIterations = 1000;
        mutationRate = 0.1f;
        tournamentSize = 40;
    }

    public List<SalesmanGenome> initialPopulation(){
        List<SalesmanGenome> population = new ArrayList<>();
        for(int i = 0; i < generationSize; i++){
            population.add(new SalesmanGenome(numberOfCities, travelPrices, startingCity));
        }
        return population;
    }

    /**
     * This method will use the selected Salesman Genome (potential solution)
     * and put it into the selected mutation method (ROULETTE / TOURNAMENT)
     * @param population List <SalesmanGenome>
     * @return List
     */
    public List<SalesmanGenome> selection(List<SalesmanGenome> population){
        List<SalesmanGenome> selected = new ArrayList<>();
        for(int i = 0; i < reproductionSize; i++){
            if(selectionType == SelectionType.ROULETTE){
                selected.add(rouletteSelection(population));
            }
            else if(selectionType == SelectionType.TOURNAMENT){
                selected.add(tournamentSelection(population));
            }
        }
        return selected;
    }

    /**
     * This method will calculate the fittest path using roulette approach
     * @param population List genome of a path
     * @return SalesmanGenome
     */
    public SalesmanGenome rouletteSelection(List<SalesmanGenome> population){
        int totalFitness = population.stream().map(SalesmanGenome::getFitness).mapToInt(Integer::intValue).sum();

        // Picking a random value to set a point on the roulette wheel
        Random random = new Random();
        int selectedValue = random.nextInt(totalFitness);

        // Since TSP is looking for the minimum value, the method needs to use reciprocal value
        // So the probability of selecting a genome would be inversely proportional to its fitness
        // Hence, the smaller the fitness value the higher the probability
        float recValue = (float) 1 / selectedValue;

        // Add up the values until reaching recValue, and pick the genome that crossed the threshold
        float currentSum = 0;
        for(SalesmanGenome genome : population){
            currentSum += (float) 1 / genome.getFitness();
            if(currentSum >= recValue){
                return genome;
            }
        }

        // If no genome crossed the threshold inside the for loop, choose one genome randomly
        int selectRandom = random.nextInt(generationSize);
        return population.get(selectRandom);
    }

    /**
     * Helper function to pick n random elements from the population for the tournament
     * @param list List genome Population
     * @param n int decided tournament size
     * @param <E> SalesmanGenome
     * @return List<E>
     */
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n) return null;

        for (int i = length - 1; i >= length - n; --i)
        {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    /**
     * Deterministic tournament - the best genome always wins
     * @param population List
     * @return List
     */
    public SalesmanGenome tournamentSelection (List<SalesmanGenome> population){
        List<SalesmanGenome> selected = pickNRandomElements(population,tournamentSize);
        return Collections.min(selected);
    }

    /**
     * if the mutationRate passes the threshold, swap the two cities in the genome (mutate).
     * Otherwise, return the original genome
     * @param salesman SalesmanGenome
     * @return SalesmanGenome
     */
    public SalesmanGenome mutate(SalesmanGenome salesman){
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Integer> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new SalesmanGenome(genome, numberOfCities, travelPrices, startingCity);
        }
        return salesman;
    }

    /**
     * Using a generational algorithm to make an entirely new population
     * @param population genome
     * @return List new generation
     */
    public List<SalesmanGenome> createGeneration(List<SalesmanGenome> population){
        List<SalesmanGenome> generation = new ArrayList<>();
        int currentGenerationSize = 0;

        // Creation of the new population requires the following steps:
        // #1 Selection
        // #2 Crossover
        // #3 Mutation
        // #4 Compute fitness
        while(currentGenerationSize < generationSize){
            List<SalesmanGenome> parents = pickNRandomElements(population,2);
            List<SalesmanGenome> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize += 2;
        }
        return generation;
    }

    /**
     * This method is an atypical demonstration of crossover,
     * because each swapped genome is a permutation of the list of cities,
     * If the method uses the conventional crossover method, it will result in visiting some cities twice
     * while missing some cities, violating the definition of Hamiltonian Cycles.
     * Therefore, in the TSP, the method uses Partially Mapped Crossover (PMX henceforth) that
     * Swap ith element of one of the parents with the element equivalent in value to the ith element of the other
     * Doing so, the method preserve the properties of permutations.
     * @param parents List
     * @return List an arraylist of SalesmanGenome Objects
     */
    public List<SalesmanGenome> crossover(List<SalesmanGenome> parents){
        // housekeeping
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize);
        List<SalesmanGenome> children = new ArrayList<>();

        // copy parental genomes - so that the program wouldn't modify in case they were
        // chosen to participate in crossover multiple times
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

        // creating child 1
        for(int i = 0; i < breakpoint; i++){
            int newVal;
            newVal = parent2Genome.get(i);
            Collections.swap(parent1Genome,parent1Genome.indexOf(newVal),i);
        }
        children.add(new SalesmanGenome(parent1Genome,numberOfCities,travelPrices,startingCity));
        parent1Genome = parents.get(0).getGenome(); // resetting the edited parent

        // creating child 2
        for(int i = breakpoint; i < genomeSize; i++){
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new SalesmanGenome(parent2Genome, numberOfCities, travelPrices, startingCity));

        return children;
    }

    /**
     * This method will terminate under following conditions:
     * #1: The number of generations has reached maxIterations
     * #2: The best genome's path is lower than the target path length
     * @return SalesmanGenome the best/minimum path
     */
    public SalesmanGenome optimize(){
        List<SalesmanGenome> population = initialPopulation();
        SalesmanGenome globalBestGenome = population.get(0);
        for(int i = 0; i < maxIterations; i++){
            List<SalesmanGenome> selected = selection(population);
            population = createGeneration(selected);
            globalBestGenome = Collections.min(population);
            if(globalBestGenome.getFitness() < targetFitness)
                break;
        }
        return globalBestGenome;
    }

    public void printGeneration(List<SalesmanGenome> generation){
        for(SalesmanGenome genome : generation){
            System.out.println(genome);
        }
    }
}