import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Learner {
    private static final int PORT = 8090;
    public static Map<String, Proposal> proposalMap;

    private final int majority = 5;

    private static int countM1 = 0;
    private static int countM2 = 0;
    private static int countM3 = 0;

    public Learner() {
        proposalMap = new HashMap<>();
        startServer();
    }

    public static void main(String[] args) {
        // Create a single Scanner object to read keyboard input
        Scanner scanner = new Scanner(System.in);

        // Start a thread to listen for keyboard input and pass the Scanner object
        Thread keyboardInputThread = new Thread(new KeyboardInputListener(scanner));
        keyboardInputThread.start();
        new Learner();
    }

    static class KeyboardInputListener implements Runnable {
        private Scanner scanner;

        public KeyboardInputListener(Scanner scanner) {
            this.scanner = scanner;
        }

        @Override
        public void run() {
            while (true) {
                String input = scanner.nextLine();
                if (input.equals("winner")) {
                    System.out.println("Count M1: " + countM1);
                    System.out.println("Count M2: " + countM2);
                    System.out.println("Count M3: " + countM3);

                    if (countM1 > 5) {
                        System.out.println("Winner is M1");
                    } else if (countM2 > 5) {
                        System.out.println("Winner is M2");
                    } else if (countM3 > 5) {
                        System.out.println("Winner is M3");
                    }
                    if (countM1 <= 5 && countM2 <= 5 && countM3 <= 5) {
                        System.out.println("There is no winner since no proposals have more than 5 votes, please keep proposing!");
                    }
                }
            }
        }

        private String readProposalFromConsole(Scanner scanner) {
            // Function to read a proposal from the console
            return scanner.nextLine();
        }
    }

    void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Learner's server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Scanner scanner = null;
                Thread clientThread = new Thread(new ClientHandler(clientSocket, scanner));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket, Scanner scanner) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                String proposerFlag = in.readLine();

                if (proposerFlag.equals("M1Proposal")) {
                    // Handle M1's proposal
                    String sequenceNumberString = in.readLine();
                    int sequenceNumber = Integer.parseInt(sequenceNumberString);
                    String proposer = in.readLine();
                    String proposalContent = in.readLine();

                    Proposal proposal = new Proposal(sequenceNumber, proposalContent, proposer);
                    proposalMap.put(sequenceNumberString, proposal);

                    // Process the proposal
                    processProposal(proposal);
                } if (proposerFlag.equals("M2Proposal")) {
                    // Handle M2's proposal
                    String sequenceNumberString = in.readLine();
                    int sequenceNumber = Integer.parseInt(sequenceNumberString);
                    String proposer = in.readLine();
                    String proposalContent = in.readLine();

                    Proposal proposal = new Proposal(sequenceNumber, proposalContent, proposer);
                    proposalMap.put(sequenceNumberString, proposal);

                    // Process the proposal
                    processProposal(proposal);
                } if (proposerFlag.equals("M3Proposal")) {
                    // Handle M3's proposal
                    String sequenceNumberString = in.readLine();
                    int sequenceNumber = Integer.parseInt(sequenceNumberString);
                    String proposer = in.readLine();
                    String proposalContent = in.readLine();

                    Proposal proposal = new Proposal(sequenceNumber, proposalContent, proposer);
                    proposalMap.put(sequenceNumberString, proposal);

                    // Process the proposal
                    processProposal(proposal);
                } if (proposerFlag.equals("M1Vote")) {
                    // Handle M1's vote
                    String voteMsg = in.readLine(); // I am M1
                    String proposer = in.readLine();
                    System.out.println("M1 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        System.out.println(" ");
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        countM3++;
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M2Vote")) {
                    // Handle M2's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M2 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        System.out.println(" ");
                    } else if (proposer.equals("M3")) {
                        countM3++;
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M3Vote")) {
                    // Handle M3's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M3 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        System.out.println(" ");
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M4Vote")) {
                    // Handle M4's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M4 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        countM3++;
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M5Vote")) {
                    // Handle M5's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M5 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        countM3++;
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M6Vote")) {
                    // Handle M6's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M6 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        countM3++;
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M7Vote")) {
                    // Handle M7's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M7 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        countM3++;
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M8Vote")) {
                    // Handle M8's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M8 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        System.out.println(" ");
                    } else {
                        System.out.println("I give up voting!");
                    }
                } if (proposerFlag.equals("M9Vote")) {
                    // Handle M9's vote
                    String voteMsg = in.readLine();
                    String proposer = in.readLine();
                    System.out.println("M9 voted for: " + proposer);
                    if (proposer.equals("M1")) {
                        countM1++;
                    } else if (proposer.equals("M2")) {
                        countM2++;
                    } else if (proposer.equals("M3")) {
                        System.out.println(" ");
                    } else {
                        System.out.println("M9 give up voting!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processProposal(Proposal proposal) {
            // Process the received proposal
            System.out.println("Received proposal from " + proposal.getProposer() + ": " + proposal.getContent());
            // Print all proposals in the proposalMap
            System.out.println("All Proposals:");
            System.out.println("****************************************************");
            for (Map.Entry<String, Proposal> entry : proposalMap.entrySet()) {
                String sequenceNumber = entry.getKey();
                Proposal storedProposal = entry.getValue();
                System.out.println("Proposer: " + storedProposal.getProposer());
                System.out.println("Proposal Content: " + storedProposal.getContent());
                System.out.println("Sequence Number: " + storedProposal.getSequenceNumber());
                System.out.println("                                                ");
            }
            System.out.println("****************************************************");
            System.out.println("                                                    ");
        }
    }
}
