import java.util.*;

public class SalesmanGenome implements Comparable<SalesmanGenome> {
    List<Integer> genome;
    int[][] travelPrices;
    int startingCity;
    int numberOfCities;
    int fitness;

    /**
     * User defined Genome size
     * @param permutationOfCities List<Integer> ways to traverse through the nodes
     * @param numberOfCities int vertices
     * @param travelPrices int[][] graph
     * @param startingCity int starting node
     */
    public SalesmanGenome(List<Integer> permutationOfCities, int numberOfCities, int[][] travelPrices, int startingCity){
        genome = permutationOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        fitness = this.calculateFitness();  // Cost of taking a certain path
    }

    /**
     * This method will calculate the actual cost of a chosen path
     * @return int total cost of a path
     */
    public int calculateFitness(){
        int fitness = 0;
        int currentCity = startingCity;
        // Calculate the cost of a chosen path
        for ( int gene : genome) {
            fitness += travelPrices[currentCity][gene];
            currentCity = gene;
        }

        // Add the cost of going back to the starting node to complete the Hamiltonian Cycle
        // The genome is missing the starting city and the starting index of 0,
        // hence minus 2 from the node count
        fitness += travelPrices[genome.get(numberOfCities-2)][startingCity];
        return fitness;
    }

    /**
     * This method generates a random genome that are permutations of the list of cities,
     * except the starting city. Therefore, the method adds them all to a list and shuffles
     * @return List
     */
    private List<Integer> randomSalesman(){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < numberOfCities; i++) {
            if(i != startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    /**
     * Getter method for Genome
     * @return List of genomes (solution path)
     */
    public List<Integer> getGenome() {
        return genome;
    }

    /**
     * Getter method for fitness
     * @return int fitness of a certain path
     */
    public int getFitness() {
        return fitness;
    }

    /**
     * Override toString method to return the path of the fittest route
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path: [");
        sb.append(startingCity);
        for (int gene: genome) {
            sb.append(", ");
            sb.append(gene);
        }
        sb.append(", ");
        sb.append(startingCity);
        sb.append("]");
        sb.append("\nLength: ");
        sb.append(this.fitness);
        return sb.toString();
    }

    /**
     * Overrides compareTo method so that it can work with SalesmanGenome objects
     * @param o Object
     * @return int state of comparison
     */
    @Override
    public int compareTo(SalesmanGenome o) {
        return Integer.compare(this.fitness, o.getFitness());
    }

    /**
     * Random genome size Genetic Algorithm Constructor
     * @param numberOfCities int vertices
     * @param travelPrices int[][] graph
     * @param startingCity int starting node
     */
    public SalesmanGenome(int numberOfCities, int[][] travelPrices, int startingCity){
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        genome = randomSalesman();
        fitness = this.calculateFitness();
    }
}

/*
    Reference:
    #1: https://stackabuse.com/traveling-salesman-problem-with-genetic-algoriths-in-java/
    #2: https://towardsdatascience.com/introduction-to-genetic-algorithms-including-example-code-e396e98d8bf3
 */
