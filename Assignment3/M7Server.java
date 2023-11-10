import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class M7Server {
    private static final int PORT = 8087;

    private static List<Integer> sequenceNumberList = new ArrayList<>();

    private static int delay = 0;

    private static Random random = new Random();
    private static int sequenceNumber = random.nextInt(90000) + 10000;

    public static void main(String[] args) {

        if(sequenceNumber < 20000){
            sequenceNumber  = sequenceNumber + 30000;
        }
        if(sequenceNumber > 80000){
            sequenceNumber  = sequenceNumber - 30000;
        }

        if(args.length > 0){
            try{
                delay = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                System.out.println("Please Provide Valid delay Time!");
            }
        }

        startServer();
    }

    static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("M7's server started on port " + PORT);
            System.out.println("The initial sequence number is: " + sequenceNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getMaxSequenceNumber() {
        int maxSequenceNumber = sequenceNumber; // Initialize with a minimum value

        for (int sequenceNumber : sequenceNumberList) {
            if (sequenceNumber > maxSequenceNumber) {
                maxSequenceNumber = sequenceNumber;
            }
        }

        return maxSequenceNumber;
    }
    /**
     * send current acceptor's vote to learner
     *
     * @param flag socket read flag
     * @param voteMsg a message about vote
     * @param proposer who is going to be voted
     */
    protected static void sendVoteToLearner(String flag, String voteMsg, String proposer) {
        try (Socket m2Socket = new Socket("localhost", 8090);
             PrintWriter out = new PrintWriter(m2Socket.getOutputStream(), true)) {
            out.println(flag);
            out.println(voteMsg);
            out.println(proposer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                int port = Integer.parseInt(in.readLine());//8082
                String message = in.readLine();
                int sequenceNumber = Integer.parseInt(in.readLine());
                String proposer = in.readLine();

                int maxSequenceNumber = getMaxSequenceNumber();
                System.out.println("Message received and processed from port "+ port+", saying: " + message);
                // Check if the current sequenceNumber is greater than the maximum sequenceNumber
                // If yes, return a response

                System.out.println("Max sequenceNumber in history is : ："+maxSequenceNumber);
                System.out.println("Currently received sequenceNumber is: ：" + sequenceNumber);
                try {
                    Thread.sleep(delay);
                    if(delay != 0) {
                        System.out.println("M7 delaying.....");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(sequenceNumber > maxSequenceNumber ){
                    sequenceNumberList.add(sequenceNumber);
                    System.out.println("Current sequenceNumber is largest in history, considering to accept");
                    sendVoteToLearner("M7Vote","I am M7, I vote for " + proposer,proposer);
                }else{
                    System.out.println("I am considering to reject the proposal, since the current sequenceNumber is not max");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
