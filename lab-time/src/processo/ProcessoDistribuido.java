package processo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProcessoDistribuido extends Thread {
    private final int id;
    private final int port;
    private final List<Integer> allPorts;
    private final List<Integer> otherPorts;
    private final Random random = new Random();

    public ProcessoDistribuido(int id, int port, List<Integer> allPorts) {
        this.id = id;
        this.port = port;
        this.allPorts = allPorts;
        this.otherPorts = new ArrayList<>(allPorts);
        this.otherPorts.remove((Integer) port); 
    }

    @Override
    public void run() {
        new Thread(this::listen).start(); 
        while (true) {
            try {
                Thread.sleep(random.nextInt(3000) + 1000);

                int destPort = otherPorts.get(random.nextInt(otherPorts.size()));
                int destId = allPorts.indexOf(destPort) + 1;

                long timestamp = System.currentTimeMillis();
                String msg = id + ":Mensagem:" + timestamp;

                try (Socket socket = new Socket("localhost", destPort);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                    out.println(msg);
                }

                System.out.printf("[Envio] P%d -> P%d | Físico: %d\n", id, destId, timestamp);

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
                long timestamp = System.currentTimeMillis();

                String[] parts = received.split(":");
                int senderId = Integer.parseInt(parts[0]);

                System.out.printf("[Recebido] P%d <- P%d | Físico: %d\n", id, senderId, timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
