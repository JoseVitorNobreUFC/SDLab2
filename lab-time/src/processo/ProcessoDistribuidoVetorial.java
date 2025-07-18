package processo;
import java.io.*;
import java.net.*;
import java.util.*;

public class ProcessoDistribuidoVetorial extends Thread {
    private final int id; 
    private final int port;
    private final List<Integer> allPorts;
    private final List<Integer> otherPorts;
    private final int numProcesses;
    private final int[] vectorClock;
    private final Random random = new Random();

    public ProcessoDistribuidoVetorial(int id, int port, List<Integer> allPorts) {
        this.id = id;
        this.port = port;
        this.allPorts = allPorts;
        this.otherPorts = new ArrayList<>(allPorts);
        this.otherPorts.remove((Integer) port);
        this.numProcesses = allPorts.size();
        this.vectorClock = new int[numProcesses];
    }

    @Override
    public void run() {
        new Thread(this::listen).start();

        while (true) {
            try {
                Thread.sleep(random.nextInt(3000) + 1000);

                vectorClock[id - 1]++;

                int destPort = otherPorts.get(random.nextInt(otherPorts.size()));
                int destId = allPorts.indexOf(destPort) + 1;
                long physicalTime = System.currentTimeMillis();

                StringBuilder message = new StringBuilder();
                message.append(id).append(":");
                for (int i = 0; i < numProcesses; i++) {
                    message.append(vectorClock[i]);
                    if (i < numProcesses - 1) message.append(",");
                }

                try (Socket socket = new Socket("localhost", destPort);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                    out.println(message.toString());
                }

                System.out.printf("[Envio] P%d -> P%d | Vetor: %s | Físico: %d\n",
                        id, destId, Arrays.toString(vectorClock), physicalTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String received = in.readLine();
                long physicalTime = System.currentTimeMillis();

                String[] parts = received.split(":");
                int senderId = Integer.parseInt(parts[0]);
                String[] clockParts = parts[1].split(",");

                int[] receivedVector = new int[numProcesses];
                for (int i = 0; i < numProcesses; i++) {
                    receivedVector[i] = Integer.parseInt(clockParts[i]);
                }

                for (int i = 0; i < numProcesses; i++) {
                    vectorClock[i] = Math.max(vectorClock[i], receivedVector[i]);
                }

                vectorClock[id - 1]++;

                System.out.printf("[Recebido] P%d <- P%d | Vetor: %s | Físico: %d\n",
                        id, senderId, Arrays.toString(vectorClock), physicalTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

