import java.util.*;

class Main {
    public static void main(String[] args) {
        // Calling member functions for customizable numbers of vertices
        // Then populate the 2D array with user-given number of vertices and randomized weights (5 - 30)
        optionList();
        int duplicates = 0;  // Repeated Vertices (A-A)
        boolean hasRan = false;
        boolean continueProgram = true;

        int vertices = getVerticesNum();
        int[][] graph = getMatrix(vertices);
        printMatrix(graph);

        do {
            // Prompt the user for new random set of cities
            if (hasRan) {
                char decision = getYesOrNo();
                if (decision == 'y') {
                    vertices = getVerticesNum();
                    graph = getMatrix(vertices);
                    printMatrix(graph);
                }
            }

            char modeSelect = getMode();
            switch (modeSelect) {
                case 'g':
                    try{
                        char selectionType = getSelectionType();
                        if (selectionType == 'r'){
                            System.out.println("\nAttempting to solve the TSP using ROULETTE approach in genetic algorithm...");
                            Ubermensch genesis = new Ubermensch(vertices, SelectionType.ROULETTE, graph, 0, 0);
                            long start = System.nanoTime();
                            SalesmanGenome fittest = genesis.optimize();
                            System.out.println(fittest);
                            long finish = System.nanoTime();
                            System.out.println("Time taken to solve the problem: " + (finish - start) + " nanoseconds.\n");
                        }
                        else {
                            System.out.println("\nAttempting to solve the TSP using TOURNAMENT approach in genetic algorithm...");
                            Ubermensch genesis = new Ubermensch(vertices, SelectionType.TOURNAMENT, graph, 0, 0);
                            long start = System.nanoTime();
                            SalesmanGenome fittest = genesis.optimize();
                            System.out.println(fittest);
                            long finish = System.nanoTime();
                            System.out.println("Time taken to solve the problem: " + (finish - start) + " nanoseconds.\n");
                        }
                        hasRan = true;
                    } catch (OutOfMemoryError oom) {
                        oom.printStackTrace();
                        System.out.println("Warning! The JVM is out of memory (Java Heap Space).");
                        System.out.println("There are too many vertices!");
                        hasRan = false;
                    }
                    break;
                case 'n':
                    try{
                        System.out.println("\nAttempting to solve the TSP using naive approach...");
                        NaiveApproach naiveSolve = new NaiveApproach();
                        long start = System.nanoTime();
                        int resultMinWeight = naiveSolve.naiveApproachTSP(graph, duplicates, vertices);
                        long finish = System.nanoTime();
                        System.out.println("The minimum cost of the solution is " + resultMinWeight + ".");
                        System.out.println("Time taken to solve the problem: " + (finish - start) + " nanoseconds.\n");
                        hasRan = true;
                    }catch (OutOfMemoryError oom){
                        oom.printStackTrace();
                        System.out.println("Warning! The JVM is out of memory (Java Heap Space).");
                        System.out.println("There are too many vertices!");
                        hasRan = false;
                    }
                    break;
                case 'd':
                    try{
                        System.out.println("\nAttempting to solve the TSP using Dynamic Programming approach...");
                        long start = System.nanoTime();
                        DynamicProgrammingApproach DPApproach = new DynamicProgrammingApproach(0, graph);
                        System.out.println("Path: " + DPApproach.getTour());
                        long finish = System.nanoTime();
                        System.out.println("Length: " + DPApproach.getTourCost());
                        System.out.println("Time taken to solve the problem: " + (finish - start) + " nanoseconds.\n");
                        hasRan = true;
                    }
                    catch (ArrayIndexOutOfBoundsException a){
                        a.printStackTrace();
                        System.out.println("Warning! Array Index is out of bound!");
                        System.out.println("There are too many vertices!");
                        hasRan = false;
                    }
                    catch (OutOfMemoryError oom){
                        oom.printStackTrace();
                        System.out.println("Warning! The JVM is out of memory (Java Heap Space).");
                        System.out.println("There are too many vertices!");
                        hasRan = false;
                    }
                    break;
                case 'h': optionList(); break;
                case 'x': continueProgram = false;
            }
        } while (continueProgram);
    }

    /**
     * This function will get a number &gt; 3 from the user
     * Will throw exception if given an invalid input
     * @return int number of vertices
     */
    public static int getVerticesNum(){
        Scanner input = new Scanner(System.in);
        boolean mismatchInput = false;
        int verticesNum = 0;

        do {
            System.out.print("Please enter a number greater than 3 for this problem: ");
            try {
                verticesNum = Integer.parseInt(input.nextLine());
                mismatchInput = verticesNum > 3;
            } catch (Exception e) {
                System.out.print("Invalid input. ");
            }
        }while(!mismatchInput);

        return verticesNum;
    }

    /**
     * This function will return a matrix populated with randomized weights
     * The size of the matrix is determined by the passed in number of vertices
     * The weight has an upper bound of 30 and a lower bound of 5
     * @param vertices int number of vertices
     * @return int[][] populated 2-Dimensional matrix with randomized weights
     */
    public static int[][] getMatrix(int vertices) {
        int[][] grid = new int[vertices][vertices];
        int upperBound = 30;
        int lowerBound = 10;
        Random rand = new Random();
        rand.setSeed(System.nanoTime());

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < i; j++) {
                int random_int = (int)Math.floor(Math.random()*(upperBound - lowerBound + 1) + lowerBound);
                grid[i][j] = random_int;
                grid[j][i] = random_int;
            }
        }
        return grid;
    }

    /**
     * This function will print out a list of options
     */
    public static void optionList(){
        System.out.println("""
                N - Naive Approach
                D - Dynamic Programming Approach.
                G - Genetic Algorithm.
                H - Print Option List.
                X - Exit Program.
                """);
    }

    /**
     * This method will prompt the user to choose the selection type from the genetic algorithm
     * @return char selected type (R for roulette and T for Tournament)
     */
    public static char getSelectionType() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter selection type (R for roulette and T for tournament): ");
        String selectedMode = scanner.next();
        selectedMode = selectedMode.toLowerCase();
        char mode = selectedMode.charAt(0);

        while (mode != 'r' && mode != 't') {
            System.out.print("Invalid input. Please enter R for roulette and T for tournament: ");
            selectedMode = scanner.next();
            selectedMode = selectedMode.toLowerCase();
            mode = selectedMode.charAt(0);
        }
        return mode;
    }

    /**
     * This method will prompt the user for an answer whether he wants to work with a new graph or not
     * @return char Yes or No
     */
    public static char getYesOrNo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to start with a new map(Y/N)? ");
        String selectedMode = scanner.next();
        selectedMode = selectedMode.toLowerCase();
        char mode = selectedMode.charAt(0);

        while (mode != 'y' && mode != 'n') {
            System.out.print("Invalid input. Please enter Y for yes or N for no: ");
            selectedMode = scanner.next();
            selectedMode = selectedMode.toLowerCase();
            mode = selectedMode.charAt(0);
        }
        return mode;
    }

    /**
     * This method will get the command from the user, also validates input
     * @return char
     */
    public static char getMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter command (enter H for command list): ");
        String selectedMode = scanner.next();
        selectedMode = selectedMode.toLowerCase();
        char mode = selectedMode.charAt(0);

        while (mode != 'n' && mode != 'd' && mode != 'g' && mode != 'h' && mode != 'x') {
            System.out.print("Invalid input. Please enter the command to see the specified method, or hit 'H' for command list.");
            selectedMode = scanner.next();
            selectedMode = selectedMode.toLowerCase();
            mode = selectedMode.charAt(0);
        }
        return mode;
    }

    /**
     * This method will print out the generated matrix
     * @param graph int[][] Random generated 2D array
     */
    public static void printMatrix(int[][] graph){
        System.out.println("Transforming graph into matrix...");
        System.out.println("---------------------------------");
        for (int[] row : graph){
            System.out.println(Arrays.toString(row));
        }
        System.out.println("---------------------------------");
    }
}