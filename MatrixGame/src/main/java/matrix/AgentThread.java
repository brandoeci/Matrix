package matrix;

import java.util.concurrent.CyclicBarrier;

public class AgentThread extends Thread {

    private final Board board;
    private final CyclicBarrier barrier;
    private final GameState state;
    private final int index;

    public AgentThread(int index, Board board, CyclicBarrier barrier, GameState state) {
        this.index = index;
        this.board = board;
        this.barrier = barrier;
        this.state = state;
    }

    @Override
    public void run() {
        while (!state.isGameOver()) {
            try { barrier.await(); } catch (Exception e) { return; }

            if (state.isGameOver()) return;

            boolean caught = board.moveAgent(index);
            if (caught) state.setResult(GameResult.AGENTS_WIN);

            try { barrier.await(); } catch (Exception e) { return; }
        }
    }
}
