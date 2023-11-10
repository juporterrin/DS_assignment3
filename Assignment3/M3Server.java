import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class M3Server {
    private static final int PORT = 8083;
    private static CouncilMember member;

    private static List<Integer> sequenceNumberList = new ArrayList<>();

    private static  int responseCount = 0;

    private static int delay = 0;


    private static Random random = new Random();
    private static int sequenceNumber = random.nextInt(90000) + 10000;
    Proposal proposal = new Proposal(sequenceNumber,null,"M3");

    public static void main(String[] args) {
        if(sequenceNumber < 20000){
            sequenceNumber  = sequenceNumber + 30000;
        }
        if(sequenceNumber > 80000){
            sequenceNumber  = sequenceNumber - 30000;
        }

        if(args.length > 0){
            try{
                if(Objects.equals(args[0], "delay")){
                    delay = 8000;
                }
            }catch(NumberFormatException e){
                System.out.println("Please enter 'delay' if you want M3 to respond with a delay");
            }
        }
        System.out.println("The initial sequence number is: " + sequenceNumber);

        // 创建一个单一的 Scanner 对象来读取键盘输入
        Scanner scanner = new Scanner(System.in);

        // 启动监听键盘输入的线程并传递 Scanner 对象
        Thread keyboardInputThread = new Thread(new KeyboardInputListener(scanner));
        keyboardInputThread.start();

        startServer();
    }

    static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("M3's server started on port " + PORT);
            System.out.println("The initial sequence number is: " + sequenceNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Scanner scanner = null;
                Thread clientThread = new Thread(new ClientHandler(clientSocket, scanner)); // 传递 Scanner 对象
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if (input.equals("sendPrepareMessage")) {
                    System.out.println("Enter the Message:");
                    String message = readProposalFromConsole(scanner); // 传递 Scanner 对象
                    if (message != null) {


                        informProposalToOtherMembers(message, 8081, "M3",sequenceNumber);
                        informProposalToOtherMembers(message, 8082, "M3",sequenceNumber);
                        informProposalToOtherMembers(message, 8084, "M3",sequenceNumber);
                        informProposalToOtherMembers(message, 8085, "M3",sequenceNumber);
                        informProposalToOtherMembers(message, 8086, "M3",sequenceNumber);
                        informProposalToOtherMembers(message, 8087, "M3",sequenceNumber);
                        informProposalToOtherMembers(message, 8088, "M3",sequenceNumber);
                        informProposalToOtherMembers(message, 8089, "M3",sequenceNumber);
                        sequenceNumber =  sequenceNumber + 30000;

                        System.out.println("Successfully sent!");
                    } else {
                        System.out.println("No proposal entered.");
                    }

                }
                if (input.equals("sendProposal")){
                    System.out.println("Enter The Proposal: ");
                    String proposal = readProposalFromConsole(scanner); // 传递 Scanner 对象

                    if (proposal != null) {
                        informLearnerToCreateProposal("M3",proposal,sequenceNumber);
                        System.out.println("Proposal Successfully sent!");
                    } else {
                        System.out.println("No proposal entered.");
                    }

                }
                if(input.equals("goOffline")){System.exit(0);
                }
            }
        }

        private String readProposalFromConsole(Scanner scanner) { // 传递 Scanner 对象
            String proposal = scanner.nextLine();
            return proposal;
        }
    }
    protected static void informProposalToOtherMembers(String message,int port, String proposer, int sequenceNumber) {
        try (Socket m2Socket = new Socket("localhost", port);
             PrintWriter out = new PrintWriter(m2Socket.getOutputStream(), true)) {
            out.println(8083);//此服务器端口
            out.println(message);
            out.println(Integer.toString(sequenceNumber));
            out.println(proposer);
            System.out.println("Sent prepare message "+message + "to Server on the port: "+port +"!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void informLearnerToCreateProposal(String proposer, String proposal, int sequenceNumber) {
        try (Socket m2Socket = new Socket("localhost", 8090);
             PrintWriter out = new PrintWriter(m2Socket.getOutputStream(), true)) {
            out.println("M3Proposal");
            out.println(sequenceNumber);
            out.println(proposer);
            out.println(proposal);
            System.out.println("Sent a request to Learner to create a proposal for M3 ");
            System.out.println("The proposal is " + proposal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
    private static int getMaxSequenceNumber() {
        int maxSequenceNumber = sequenceNumber; // Initialize with a minimum value

        for (int sequenceNumber : sequenceNumberList) {
            if (sequenceNumber > maxSequenceNumber) {
                maxSequenceNumber = sequenceNumber;
            }
        }

        return maxSequenceNumber;
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private Scanner scanner;

        public ClientHandler(Socket clientSocket, Scanner scanner) {
            this.clientSocket = clientSocket;
            this.scanner = scanner;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                int port = Integer.parseInt(in.readLine());//8081
                String message = in.readLine();
                int sequenceNumber = Integer.parseInt(in.readLine());
                String proposer = in.readLine();

                int maxSequenceNumber = getMaxSequenceNumber();
                System.out.println("Message received and processed from port "+ port+", saying: " + message);
                //检查当前提案号是否大于最大提案号
                //是返回响应
                System.out.println("Max sequenceNumber in history is : ："+maxSequenceNumber);
                System.out.println("Currently received sequenceNumber is: ：" + sequenceNumber);

                try {
                    Thread.sleep(delay);
                    if(delay != 0) {
                        System.out.println("M3 delaying.....");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(sequenceNumber > maxSequenceNumber ){
                    sequenceNumberList.add(sequenceNumber);
                    System.out.println(true);

                    //向learner发送投票
                    System.out.println("Current sequenceNumber is largest in history, considering to accept");
                    sendVoteToLearner("M3Vote","I am M3, I am considering your request" + proposer,proposer);
                }else{
                    System.out.println("I am considering to reject the proposal, since the current sequenceNumber is not max");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
