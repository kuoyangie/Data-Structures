import java.util.ArrayList;

public class NaiveApproach {
    public int naiveApproachTSP(int[][] graph, int s, int vertices) {

        ArrayList<Integer> vertex = new ArrayList<>();
        // Initializing array list with symmetric fashion
        for (int i = 0; i < vertices; i++)
            if (i != s)
                vertex.add(i);

        int min_path = Integer.MAX_VALUE;

        do {
            int curPathWeight = 0;
            int k = s;
            // Populate the array list with available vertices' weights
            for (Integer integer : vertex) {
                curPathWeight += graph[k][integer];

                k = integer;
            }

            min_path = Math.min(min_path, curPathWeight);
        } while (findNextPermutation(vertex));

        return min_path;
    }

    public static void swap(ArrayList<Integer> data, int left, int right) {
        int temp = data.get(left);
        data.set(left, data.get(right));
        data.set(right, temp);
    }

    public static void reverse(ArrayList<Integer> data, int left, int right) {
        while (left < right) {
            int temp = data.get(left);
            data.set(left++, data.get(right));
            data.set(right--, temp);
        }
    }

    public static boolean findNextPermutation(ArrayList<Integer> data) {
        // If the data size contains only 1 vertex, stop the search
        // for the next permutation is impossible
        if (data.size() <= 1)
            return false;
        int last = data.size() - 2;
        // Find the longest non-increasing suffix and find the pivot
        while (last >= 0) {
            if (data.get(last) < data.get(last + 1)) break;
            last--;
        }
        // Returning false if the search is complete
        if (last < 0)
            return false;

        int nextGreater = data.size() - 1;
        // Find the right-most successor to the pivot
        for (int i = data.size() - 1; i > last; i--) {
            if (data.get(i) > data.get(last)) {
                nextGreater = i;
                break;
            }
        }
        // Swap the successor and the pivot
        swap(data, nextGreater, last);
        // Reverse suffix
        reverse(data, last + 1, data.size() - 1);
        return true;
    }
}
