/*Write a program in C/C++/Java to simulate the Banker’s algorithm for 
deadlock avoidance. Consider at least 3 processes in the system, with 4 
resource classes having at least one resource instance for each class. 
Assume the values for Available, Allocation, MAX, and request from 
a particular process from your side. Program must reflect two cases, 
where a safe sequence exists for one and safe sequence does not exist 
for another. */

public class BankersAlgorithm {

    static final int PROCESSES = 3;
    static final int RESOURCES = 4;

    static int[] available = {1, 1, 2, 1};

    static int[][] allocation = {
        {0, 1, 0, 0},
        {2, 0, 0, 1},
        {1, 1, 2, 0}
    };

    static int[][] max = {
        {1, 2, 1, 1},
        {2, 1, 1, 2},
        {1, 1, 3, 2}
    };

    static int[] request = {1, 0, 1, 0};

    public static void main(String[] args) {
        System.out.println("Initial State:");
        displayState();

        int process = 1;
        System.out.println("\nProcess " + process + " makes a request: ");
        displayArray(request);

        if (requestResources(process, request)) {
            System.out.println("\nRequest can be granted. System is in a safe state.");
            System.out.println("Safe Sequence: ");
            displaySafeSequence();
        } else {
            System.out.println("\nRequest cannot be granted. No safe sequence exists.");
        }
    }

    public static boolean requestResources(int process, int[] request) {
        for (int i = 0; i < RESOURCES; i++) {
            if (request[i] > max[process][i] - allocation[process][i]) {
                System.out.println("Error: Process has exceeded its maximum claim.");
                return false;
            }
        }

        for (int i = 0; i < RESOURCES; i++) {
            if (request[i] > available[i]) {
                System.out.println("Error: Resources are not available for the request.");
                return false;
            }
        }

        for (int i = 0; i < RESOURCES; i++) {
            available[i] -= request[i];
            allocation[process][i] += request[i];
        }

        if (isSafeState()) {
            return true;
        } else {
            for (int i = 0; i < RESOURCES; i++) {
                available[i] += request[i];
                allocation[process][i] -= request[i];
            }
            return false;
        }
    }

    public static boolean isSafeState() {
        int[] work = available.clone();
        boolean[] finish = new boolean[PROCESSES];

        int count = 0;
        while (count < PROCESSES) {
            boolean found = false;

            for (int p = 0; p < PROCESSES; p++) {
                if (!finish[p]) {
                    int j;
                    for (j = 0; j < RESOURCES; j++) {
                        if (max[p][j] - allocation[p][j] > work[j]) {
                            break;
                        }
                    }
                    if (j == RESOURCES) {
                        for (int k = 0; k < RESOURCES; k++) {
                            work[k] += allocation[p][k];
                        }
                        finish[p] = true;
                        found = true;
                        count++;
                    }
                }
            }

            if (!found) {
                return false;
            }
        }
        return true;
    }

    public static void displaySafeSequence() {
        int[] work = available.clone();
        boolean[] finish = new boolean[PROCESSES];

        for (int count = 0; count < PROCESSES; count++) {
            for (int p = 0; p < PROCESSES; p++) {
                if (!finish[p]) {
                    int j;
                    for (j = 0; j < RESOURCES; j++) {
                        if (max[p][j] - allocation[p][j] > work[j]) {
                            break;
                        }
                    }
                    if (j == RESOURCES) {
                        for (int k = 0; k < RESOURCES; k++) {
                            work[k] += allocation[p][k];
                        }
                        finish[p] = true;
                        System.out.print("P" + p + " ");
                    }
                }
            }
        }
        System.out.println();
    }

    public static void displayState() {
        System.out.println("Available Resources: ");
        displayArray(available);

        System.out.println("\nAllocation Matrix: ");
        displayMatrix(allocation);

        System.out.println("\nMax Matrix: ");
        displayMatrix(max);
    }

    public static void displayArray(int[] array) {
        for (int val : array) {
            System.out.print(val + " ");
        }
        System.out.println();
    }

    public static void displayMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}
