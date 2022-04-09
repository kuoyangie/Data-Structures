import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class DynamicProgrammingApproach {
    private final int N, start;
    private final int[][] distance;
    private final List<Integer> tour = new ArrayList<>();
    private int minTourCost = Integer.MAX_VALUE;
    private boolean ranSolver = false;

    public DynamicProgrammingApproach(int start, int[][] distance) {
        N = distance.length;

        if (N <= 2) throw new IllegalStateException("N <= 2 not yet supported.");
        if (N != distance[0].length) throw new IllegalStateException("Matrix must be square (n x n)");
        if (start < 0 || start >= N) throw new IllegalArgumentException("Invalid start node.");

        this.start = start;
        this.distance = distance;
    }

    // Returns the optimal tour for the traveling salesman problem.
    public List<Integer> getTour() {
        if (!ranSolver) solve();
        return tour;
    }

    // Returns the minimal tour cost.
    public int getTourCost() {
        if (!ranSolver) solve();
        return minTourCost;
    }

    // Solves the traveling salesman problem and caches solution.
    public void solve() {
        if (ranSolver) return;
        final int END_STATE = (1 << N) - 1;
        int[][] memo = new int[N][1 << N];
        // Add all outgoing edges from the starting node to memo table.
        for (int end = 0; end < N; end++) {
            if (end == start) continue;
            memo[end][(1 << start) | (1 << end)] = distance[start][end];
        }
        // The combinations function generates all bit sets
        // of size N with r bits set to 1. i.e.
        // combinations (3, 4) = {0111, 1011, 1101, 1110}
        // This bitmask method is useful to store state representation
        // and can be cached easily
        // Reference: https://stackoverflow.com/questions/33527127/dynamic-programming-approach-to-tsp-in-java
        for (int r = 3; r <= N; r++) {
            for (int subset : combinations(r, N)) {
                if (notIn(start, subset)) continue;
                for (int next = 0; next < N; next++) {
                    if (next == start || notIn(next, subset)) continue;
                    // The subset state is without the next node
                    int subsetWithoutNext = subset ^ (1 << next);
                    int minDist = Integer.MAX_VALUE;
                    for (int end = 0; end < N; end++) {
                        if (end == start || end == next || notIn(end, subset)) continue;
                        int newDistance = memo[end][subsetWithoutNext] + distance[end][next];
                        if (newDistance < minDist) {
                            minDist = newDistance;
                        }
                    }
                    memo[next][subset] = minDist;
                }
            }
        }
        // Connect tour back to starting node and minimize cost.
        for (int i = 0; i < N; i++) {
            if (i == start) continue;
            int tourCost = memo[i][END_STATE] + distance[i][start];
            if (tourCost < minTourCost) {
                minTourCost = tourCost;
            }
        }
        int lastIndex = start;
        int state = END_STATE;
        tour.add(start);
        // Reconstruct TSP path from memo table.
        for (int i = 1; i < N; i++) {
            int index = -1;
            for (int j = 0; j < N; j++) {
                if (j == start || notIn(j, state)) continue;
                if (index == -1) index = j;
                int prevDist = memo[index][state] + distance[index][lastIndex];
                int newDist = memo[j][state] + distance[j][lastIndex];
                if (newDist < prevDist) {
                    index = j;
                }
            }
            tour.add(index);
            // Shift the state
            state = state ^ (1 << index);
            lastIndex = index;
        }

        // Adding the start node to finish the cycle,
        // then reverse the collection to get the correct order
        // Set ranSolver to true to end the loop
        tour.add(start);
        Collections.reverse(tour);
        ranSolver = true;
    }

    /**
     * This method returns true if the ith bit in 'subset' is not a set
     *
     * @param elem   int
     * @param subset int
     * @return boolean
     */
    private static boolean notIn(int elem, int subset) {
        return ((1 << elem) & subset) == 0;
    }

    /**
     * This method generates all bit sets of size n where r bits
     * are set to one. The result is returned as a list of integer masks.
     * @param r int
     * @param n int
     * @return List masked integers
     */
    public static List<Integer> combinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();
        combinations(0, 0, r, n, subsets);
        return subsets;
    }

    /**
     * This method recursively calls itself to generate bit sets
     *
     * @param set     int
     * @param at      int
     * @param r       int
     * @param n       int
     * @param subsets List
     */
    private static void combinations(int set, int at, int r, int n, List<Integer> subsets) {

        // Return early if there are more elements left to select than what is available.
        int elementsLeftToPick = n - at;
        if (elementsLeftToPick < r) return;

        // We selected 'r' elements, so we found a valid subset!
        if (r == 0) {
            subsets.add(set);
        } else {
            for (int i = at; i < n; i++) {
                // Try including this element
                // flip on ith bit to see the number of ways to permute
                // when the set contains the next node
                set |= 1 << i;
                combinations(set, i + 1, r - 1, n, subsets);
                // Backtrack and flip off ith bit
                // the '~' means complementary of 'x'
                set &= ~(1 << i);
            }
        }
    }
}

/*
    Reference:
    #1 Dynamic Programming for the TSP: https://www.youtube.com/watch?v=cY4HiiFHO1o
    #2 Bitwise Operations: https://www.youtube.com/watch?v=1qa0zvcdHXI
    #3 Bitwise Operations: https://www.programiz.com/java-programming/bitwise-operators
    #4 Bitwise Caching: https://stackoverflow.com/questions/33527127/dynamic-programming-approach-to-tsp-in-java
    #5 Bitwise Operation: https://www.programiz.com/java-programming/bitwise-operators#unsigned-right-shift
 */