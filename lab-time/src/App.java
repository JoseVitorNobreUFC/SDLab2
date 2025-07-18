import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processo.*;

public class App {
    // Usando ProcessoDistribuido desordenado
    
    public static void main(String[] args) {
        List<Integer> ports = Arrays.asList(5001, 5002, 5003);

        for (int i = 0; i < ports.size(); i++) {
            int id = i + 1;
            int port = ports.get(i);
            new ProcessoDistribuido(id, port, ports).start();
        }
    }

    // Usando ProcessoDistribuido com o relogio lógico de Lamport

    // public static void main(String[] args) {
    //     List<Integer> ports = Arrays.asList(5001, 5002, 5003);

    //     for (int i = 0; i < ports.size(); i++) {
    //         int id = i + 1;
    //         int port = ports.get(i);
    //         new ProcessoDistribuidoLamport(id, port, ports).start();
    //     }
    // }

    // Usando ProcessoDistribuido com o relogio vetorial

    // public static void main(String[] args) {
    //     List<Integer> ports = Arrays.asList(5001, 5002, 5003);

    //     for (int i = 0; i < ports.size(); i++) {
    //         int id = i + 1;
    //         int port = ports.get(i);
    //         new ProcessoDistribuidoVetorial(id, port, ports).start();
    //     }
    // }
}
