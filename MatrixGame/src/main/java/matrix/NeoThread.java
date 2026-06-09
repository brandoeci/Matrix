package matrix;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class NeoThread extends Thread {

    private final Board board;
    private final CyclicBarrier barrier;
    private final GameState state;

    public NeoThread(Board board, CyclicBarrier barrier, GameState state) {
        this.board = board;
        this.barrier = barrier;
        this.state = state;
    }

    @Override
    public void run() {
        while (!state.isGameOver()) {
            try { barrier.await(); } catch (Exception e) { return; }

            if (state.isGameOver()) return;

            char result = board.moveNeo();
            if (result == 'W') state.setResult(GameResult.NEO_WINS);
            else if (result == 'C') state.setResult(GameResult.AGENTS_WIN);

            try { barrier.await(); } catch (Exception e) { return; }
        }
    }
}
