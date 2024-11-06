/*
 * Write a program using C/C++/Java to simulate the priority scheduling (pre-emptive as well as 
non-preemptive approach) and RR, CPU scheduling algorithms. The scenario is: user may 
input n processes with respective CPU burst time and arrival time (also take the priority number 
in case of priority scheduling). System will ask the user to select the type of algorithm from the 
list mentioned above. System should display the waiting time for each process, average waiting 
time for the whole system, and final execution sequence.
 */
import java.util.Scanner;
class Process
{
    int arrivalTime;
    int burstTime;
    int waitingTime;
    int completionTime;
    int priority;
    int remainingTime;

    public Process(int arrivalTime, int burstTime, int priority)
    {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }
}

public class MoreScheduling
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        ProcessList processes = new ProcessList();
        int i = 0;

        System.out.println("Enter Arrival, Burst, and Priority for processes. Enter a non-integer to stop.");
        while (true) {
            try 
            {
                System.out.print("Arrival Time for process " + (i + 1) + ": ");
                int arrivalTime = in.nextInt();
                System.out.print("Burst Time for process " + (i + 1) + ": ");
                int burstTime = in.nextInt();
                System.out.print("Priority for process " + (i + 1) + ": ");
                int priority = in.nextInt();
                processes.add(new Process(arrivalTime, burstTime, priority));
                i++;
            } catch (Exception e) {
                in.next();
                System.out.println("Input Stopped.");
                break;
            }
        }

        System.out.println("Choose Scheduling Algorithm:\n1. Priority (Non-Preemptive)\n2. Priority (Preemptive)\n3. Round Robin");
        int choice = in.nextInt();

        switch (choice)
        {
            case 1:
                priorityNonPreemptive(processes);
                break;
            case 2:
                priorityPreemptive(processes);
                break;
            case 3:
                System.out.print("Enter time quantum for Round Robin: ");
                int quantum = in.nextInt();
                roundRobin(processes, quantum);
                break;
            default:
                System.out.println("Invalid Choice.");
        }
        in.close();
    }

    static void priorityNonPreemptive(ProcessList processes) {
        int currentTime = 0, totalWaitingTime = 0;
        boolean[] isCompleted = new boolean[processes.size()];
        StringBuilder sequence = new StringBuilder();

        System.out.println("Process\tArrival\tBurst\tPriority\tCompletion\tWaiting");

        for (int completed = 0; completed < processes.size(); completed++) {
            int highestPriorityIndex = -1;

            for (int i = 0; i < processes.size(); i++) {
                Process process = processes.get(i);
                if (!isCompleted[i] && process.arrivalTime <= currentTime &&
                    (highestPriorityIndex == -1 || process.priority < processes.get(highestPriorityIndex).priority)) {
                    highestPriorityIndex = i;
                }
            }

            if (highestPriorityIndex == -1) {
                currentTime++;
                continue;
            }

            Process process = processes.get(highestPriorityIndex);
            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.completionTime = currentTime;
            totalWaitingTime += process.waitingTime;
            isCompleted[highestPriorityIndex] = true;
            sequence.append((highestPriorityIndex + 1)).append(" ");

            System.out.println((highestPriorityIndex + 1) + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" + process.priority +
                               "\t\t" + process.completionTime + "\t\t" + process.waitingTime);
        }

        System.out.println("Average waiting time: " + (float) totalWaitingTime / processes.size());
        System.out.println("Execution Sequence: " + sequence);
    }

    static void priorityPreemptive(ProcessList processes) {
        int currentTime = 0, totalWaitingTime = 0, completed = 0;
        boolean[] isCompleted = new boolean[processes.size()];
        StringBuilder sequence = new StringBuilder();

        System.out.println("Process\tArrival\tBurst\tPriority\tCompletion\tWaiting");

        while (completed < processes.size()) {
            int highestPriorityIndex = -1;

            for (int i = 0; i < processes.size(); i++) {
                Process process = processes.get(i);
                if (!isCompleted[i] && process.arrivalTime <= currentTime &&
                    (highestPriorityIndex == -1 || process.priority < processes.get(highestPriorityIndex).priority)) {
                    highestPriorityIndex = i;
                }
            }

            if (highestPriorityIndex == -1) {
                currentTime++;
                continue;
            }

            Process process = processes.get(highestPriorityIndex);
            sequence.append((highestPriorityIndex + 1)).append(" ");
            process.remainingTime--;

            if (process.remainingTime == 0) {
                completed++;
                process.completionTime = currentTime + 1;
                process.waitingTime = process.completionTime - process.arrivalTime - process.burstTime;
                totalWaitingTime += process.waitingTime;
                isCompleted[highestPriorityIndex] = true;

                System.out.println((highestPriorityIndex + 1) + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" + process.priority +
                                   "\t\t" + process.completionTime + "\t\t" + process.waitingTime);
            }
            currentTime++;
        }

        System.out.println("Average waiting time: " + (float) totalWaitingTime / processes.size());
        System.out.println("Execution Sequence: " + sequence);
    }

    static void roundRobin(ProcessList processes, int quantum) {
        int currentTime = 0, totalWaitingTime = 0, completed = 0;
        StringBuilder sequence = new StringBuilder();

        System.out.println("Process\tArrival\tBurst\tCompletion\tWaiting");

        while (completed < processes.size()) {
            boolean allProcessed = true;

            for (int i = 0; i < processes.size(); i++) {
                Process process = processes.get(i);

                if (process.remainingTime > 0) {
                    allProcessed = false;
                    sequence.append(i + 1).append(" ");

                    if (process.remainingTime > quantum) {
                        currentTime += quantum;
                        process.remainingTime -= quantum;
                    } else {
                        currentTime += process.remainingTime;
                        process.waitingTime = currentTime - process.arrivalTime - process.burstTime;
                        process.completionTime = currentTime;
                        totalWaitingTime += process.waitingTime;
                        process.remainingTime = 0;
                        completed++;

                        System.out.println((i + 1) + "\t" + process.arrivalTime + "\t" + process.burstTime +
                                           "\t" + process.completionTime + "\t\t" + process.waitingTime);
                    }
                }
            }

            if (allProcessed) break;
        }

        System.out.println("Average waiting time: " + (float) totalWaitingTime / processes.size());
        System.out.println("Execution Sequence: " + sequence);
    }
}
