import java.util.Scanner;
class Process
{
    int arrivalTime;
    int burstTime;
    int waitingTime;
    int completionTime;
    int remainingTime;

    public Process(int arrivalTime, int burstTime)
    {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }
}

public class Scheduling
{
    public static void main(String[]args)
    {
        
        Scanner in = new Scanner(System.in);
        ProcessList Processes = new ProcessList();
        int i = 0;
        System.out.println("Enter Arrival and Burst time for any number of processes. Enter a character to stop.");
        while(true)
        {
            try 
            {
                System.out.print("Enter Arrival Time for process " + (i + 1) + " (or a non-integer to stop): ");
                int arrivalTime = in.nextInt();
                System.out.print("Enter Burst Time for process "+ (i + 1) + ":" );
                int burstTime = in.nextInt();
                Processes.add(new Process(arrivalTime, burstTime));
                i++;  
            } catch (Exception e) {
                in.next();
                System.out.println("Input Stopped");
                break;
            }
        }
        
        System.out.println("Choose Scheduling Algorithm: \n1. FCFS\n2. SJF(Non-Preemptive)\n3. SJF(Preemptive)");
        int choice = in.nextInt();

        switch(choice)
        {
            case 1:
                FCFS(Processes);
                break;
            case 2:
                SJFNonPreemptive(Processes);
                break;
            case 3:
                //SJFPreemptive(Processes);
                break;
            default:
                System.out.println("Invalid Choice");
        }
        in.close();
    }

    static void FCFS(ProcessList processes)
    {
        int currentTime = 0;
        int totalWaitingTime = 0;
        System.out.println("Process\tArrival\tBurst\tCompletion\tWaiting");
        for(int i = 0; i < processes.size(); i++)
        {
            Process process = processes.get(i);

            if(currentTime < process.arrivalTime)
                currentTime = process.arrivalTime;

            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.completionTime = currentTime;
            totalWaitingTime += process.waitingTime;

            System.out.println((i+1) + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" + process.completionTime + "\t\t" + process.waitingTime);
        }
        System.out.println("Average waiting time: " + (float) totalWaitingTime / processes.size());
    }

    static void SJFNonPreemptive(ProcessList processes)
    {
        int currentTime = 0, totalWaitingTime = 0;
        boolean [] isCompleted = new boolean[processes.size()];
        
        System.out.println("Process\tArrival\tBurst\tCompletion\tWaiting");

        for(int completed = 0; completed < processes.size(); completed++)
        {
            int shortestBurstTime = -1;
            for (int i = 0; i < processes.size(); i++)
            {
                Process process = processes.get(i);
                if(!isCompleted[i] && process.arrivalTime <= currentTime && (shortestBurstTime == -1 || process.burstTime < processes.get(shortestBurstTime).burstTime))
                {
                    shortestBurstTime = i;
                }
            }
            if (shortestBurstTime == -1)
            {
                currentTime++;
                continue;
            }
            
            Process process = processes.get(shortestBurstTime);
            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.completionTime = currentTime;
            totalWaitingTime += process.waitingTime;
            isCompleted[shortestBurstTime] = true;

            System.out.println((shortestBurstTime + 1) + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" + process.completionTime + "\t\t" + process.waitingTime);
        }

        System.out.println("Average waiting time: " + (float) totalWaitingTime / processes.size());
    }

    static void SJFPreemptive(ProcessList processes)
    {
        int currentTime = 0, totalWaitingTime = 0, completed = 0;
        boolean [] isCompleted = new boolean[processes.size()];
        
        System.out.println("Process\tArrival\tBurst\tCompletion\tWaiting");

        while(completed < processes.size())
        {
            int shortestBurstTime = -1;
            for (int i = 0; i < processes.size(); i++)
            {
                Process process = processes.get(i);
                if(!isCompleted[i] && process.arrivalTime <= currentTime && (shortestBurstTime == -1 || process.burstTime < processes.get(shortestBurstTime).burstTime))
                {
                    shortestBurstTime = i;
                }
            }
            if(shortestBurstTime == -1)
            {
                currentTime++;
                continue;
            }  
            Process process = processes.get(shortestBurstTime);
            process.remainingTime--;

            if(process.remainingTime == 0)
            {
                completed++;
                process.completionTime = currentTime + 1;
                process.waitingTime = process.completionTime - (process.arrivalTime + process.burstTime);
                totalWaitingTime += process.waitingTime;
                

                System.out.println((shortestBurstTime + 1) + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" + process.completionTime + "\t\t" + process.waitingTime);
            }
            currentTime++;
        }
        System.out.println("Average waiting time: " + (float) totalWaitingTime / processes.size());
    }
}
