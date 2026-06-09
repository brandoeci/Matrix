package matrix;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("MATRIX THE GAME");

        int size = 0;
        while (size < 2 || size > 7) {
            System.out.print("Tamano del tablero (2 a 7): ");
            try { size = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { size = 0; }
            if (size < 2 || size > 7) System.out.println("Valor invalido.");
        }

        int maxAgents = Math.max(1, (size * size) / 6);
        int agents = 0;
        while (agents < 1 || agents > maxAgents) {
            System.out.print("cantidad de Agentes (1 a " + maxAgents + "): ");
            try { agents = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { agents = 0; }
            if (agents < 1 || agents > maxAgents) System.out.println("Valor invalido.");
        }

        int maxPhones = Math.max(1, (size * size) / 8);
        int phones = 0;
        while (phones < 1 || phones > maxPhones) {
            System.out.print("Cantidad de Telefonos (1 a " + maxPhones + "): ");
            try { phones = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { phones = 0; }
            if (phones < 1 || phones > maxPhones) System.out.println("Valor invalido.");
        }

        int delay = 0;
        while (delay < 100 || delay > 3000) {
            System.out.print("delay entre turnos en ms para ve mejor los turnos de 100 a 3000 (recomendado 800): ");
            try { delay = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { delay = 0; }
            if (delay < 100 || delay > 3000) System.out.println("Valor invalido.");
        }

        Game game = new Game(size, agents, phones, delay);
        game.start();
    }
}