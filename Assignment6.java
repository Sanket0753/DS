

// BullyAlgorithm.java

import java.util.Scanner;

public class BullyAlgorithm {
    static boolean[] state = new boolean[5];
    static int coordinator = 5;

    public static void up(int up) {
        if (state[up - 1]) {
            System.out.println("Process " + up + " is already up.");
        } else {
            state[up - 1] = true;
            System.out.println("Process " + up + " is up.");

            // If the newly up process has a higher ID than the current coordinator,
            // initiate an election
            if (up > coordinator) {
                System.out.println("Process " + up + " initiates an election.");
                election(up);
            }
        }
    }

    public static void down(int down) {
        if (!state[down - 1]) {
            System.out.println("Process " + down + " is already down.");
        } else {
            state[down - 1] = false;
            System.out.println("Process " + down + " is down.");
            if (down == coordinator) {
                System.out.println("Coordinator (Process " + down + ") is down.");
                election(down);
            }
        }
    }

    public static void mess(int mess) {
        if (!state[mess - 1]) {
            System.out.println("Process " + mess + " is down.");
        } else {
            if (mess == coordinator) {
                System.out.println("Coordinator (Process " + coordinator + ") received the message: OK");
            } else {
                System.out.println("Process " + mess + " sends a message.");
            }
        }
    }

    public static void election(int initiator) {
        System.out.println("Election initiated by Process " + initiator);
        for (int i = initiator + 1; i <= 5; i++) {
            if (state[i - 1]) {
                System.out.println("Election message sent from Process " + initiator + " to Process " + i);
            }
        }
        // If no higher priority process responds, declare the initiator as the new coordinator
        coordinator = initiator;
        System.out.println("Process " + coordinator + " becomes the new coordinator.");
        System.out.println("Coordinator message sent from Process " + coordinator + " to all.");
    }

    public static void main(String[] args) {
        int choice;
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 5; ++i) {
            state[i] = true;
        }
        System.out.println("5 active processes are:");
        System.out.println("Process up  = p1 p2 p3 p4 p5");
        System.out.println("Process 5 is coordinator");
        do {
            System.out.println(".........");
            System.out.println("1. Up a process.");
            System.out.println("2. Down a process.");
            System.out.println("3. Send a message.");
            System.out.println("4. Exit.");
            choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Bring process up:");
                    int up = sc.nextInt();
                    if (up > 5) {
                        System.out.println("Invalid process number.");
                        break;
                    }
                    up(up);
                    break;
                }
                case 2: {
                    System.out.println("Bring down any process:");
                    int down = sc.nextInt();
                    if (down > 5) {
                        System.out.println("Invalid process number.");
                        break;
                    }
                    down(down);
                    break;
                }
                case 3: {
                    System.out.println("Which process will send message:");
                    int mess = sc.nextInt();
                    if (mess > 5) {
                        System.out.println("Invalid process number.");
                        break;
                    }
                    mess(mess);
                    break;
                }
            }
        } while (choice != 4);
    }
}



// RingAlgorithm.java


import java.util.Scanner;

public class RingAlgorithm {

    public static void main(String[] args) {

        int i, j;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of processes: ");
        int numberOfProcesses = scanner.nextInt();

        Process[] processes = new Process[numberOfProcesses];

        // Object initialization
        for (i = 0; i < processes.length; i++)
            processes[i] = new Process();

        // Getting input from users
        for (i = 0; i < numberOfProcesses; i++) {
            processes[i].index = i;
            System.out.println("Enter the ID of process " + (i + 1) + ": ");
            processes[i].id = scanner.nextInt();
            processes[i].state = "active";
            processes[i].hasSentMessage = false;
        }
        
        // Sorting the processes based on their IDs
        for (i = 0; i < numberOfProcesses - 1; i++) {
            for (j = 0; j < numberOfProcesses - i - 1; j++) {
                if (processes[j].id > processes[j + 1].id) {
                    Process temp = processes[j];
                    processes[j] = processes[j + 1];
                    processes[j + 1] = temp;
                }
            }
        }

        System.out.println("Processes in sorted order:");
        for (i = 0; i < numberOfProcesses; i++) {
            System.out.println("[" + processes[i].index + "] " + processes[i].id);
        }

        // Initiating coordinator selection
        int initiatorIndex;
        while (true) {
            System.out.println("\n1. Initiate Election\n2. Quit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter the index of the process initiating the election: ");
                    initiatorIndex = scanner.nextInt();
                    initiateElection(processes, initiatorIndex, numberOfProcesses);
                    break;
                case 2:
                    System.out.println("Program terminated.");
                    return;
                default:
                    System.out.println("Invalid response.");
            }
        }
    }

    public static void initiateElection(Process[] processes, int initiatorIndex, int numberOfProcesses) {
        System.out.println("Election initiated by Process " + processes[initiatorIndex].id);
        processes[initiatorIndex].hasSentMessage = true;

        int tempIndex = initiatorIndex;
        int nextIndex = (initiatorIndex + 1) % numberOfProcesses;
        while (nextIndex != initiatorIndex) {
            if (processes[nextIndex].state.equals("active") && !processes[nextIndex].hasSentMessage) {
                System.out.println("Process " + processes[initiatorIndex].id + " sends message to Process " + processes[nextIndex].id);
                processes[nextIndex].hasSentMessage = true;
                tempIndex = nextIndex;
            }
            nextIndex = (nextIndex + 1) % numberOfProcesses;
        }

        System.out.println("Process " + processes[tempIndex].id + " sends message to Process " + processes[initiatorIndex].id);
        System.out.println("Process " + processes[initiatorIndex].id + " becomes the coordinator.");
        processes[initiatorIndex].state = "inactive";
    }
}

class Process {
    public int index;           // Index of the process
    public int id;              // ID of the process
    public boolean hasSentMessage;  // Flag to indicate whether the process has sent a message
    public String state;        // Indicates whether the process is active or inactive
}
