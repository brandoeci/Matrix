package matrix;

import java.util.concurrent.CyclicBarrier;

public class Game {

    private final Board board;
    private final GameState state;
    private final int numAgents;
    private final int delayMs;

    public Game(int size, int numAgents, int numPhones, int delayMs) {
        this.state = new GameState();
        this.board = new Board(size, numAgents, numPhones, state);
        this.numAgents = numAgents;
        this.delayMs = delayMs;
    }

    public void start() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(numAgents + 2);

        NeoThread neo = new NeoThread(board, barrier, state);
        neo.setDaemon(true);
        neo.start();

        for (int i = 0; i < numAgents; i++) {
            AgentThread agent = new AgentThread(i, board, barrier, state);
            agent.setDaemon(true);
            agent.start();
        }

        int turn = 0;
        System.out.println("=== TURNO INICIAL ===");
        board.print();
        Thread.sleep(delayMs);

        while (!state.isGameOver()) {
            turn++;

            try { barrier.await(); } catch (Exception e) { break; }
            try { barrier.await(); } catch (Exception e) { break; }

            System.out.println("=== TURNO " + turn + " ===");
            board.print();

            if (state.isGameOver()) break;

            Thread.sleep(delayMs);
        }

        System.out.println("=== FIN DEL JUEGO ===");
        board.print();

        if (state.getResult() == GameResult.NEO_WINS) {
            System.out.println("NEO GANO - Llego al telefono!");
        } else {
            System.out.println("LOS AGENTES GANARON - Atraparon a Neo!");
        }

        try { barrier.await(); } catch (Exception ignored) {}
        try { barrier.await(); } catch (Exception ignored) {}
    }
}
