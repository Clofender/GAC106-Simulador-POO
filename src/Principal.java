import java.util.Scanner;

public class Principal{
  
  public static void main(String[] args){
    /*Scanner scanner = new Scanner(System.in);
    System.out.println("Escolha o mapa (1 ou 2): ");
    int mapa = scanner.nextInt();
    scanner.close();
*/    int mapa = 1; // Escolha do mapa pode ser feita aqui diretamente
    Simulator simulator = new Simulator(mapa);
    simulator.runLongSimulation();
    //simulator.simulate(300);
  }
}
