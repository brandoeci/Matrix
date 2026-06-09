package matrix;

public class GameState {
    private volatile GameResult result = GameResult.IN_PROGRESS;

    public boolean isGameOver() {
        return result != GameResult.IN_PROGRESS;
    }

    public synchronized void setResult(GameResult r) {
        if (this.result == GameResult.IN_PROGRESS) {
            this.result = r;
        }
    }

    public GameResult getResult() {
        return result;
    }
}
