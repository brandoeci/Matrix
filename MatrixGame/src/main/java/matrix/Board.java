package matrix;

import java.util.*;

public class Board {

    public static final char EMPTY = '.';
    public static final char WALL  = '#';
    public static final char NEO   = 'N';
    public static final char AGENT = 'A';
    public static final char PHONE = 'T';

    private final int size;
    private final char[][] grid;
    private final Random random = new Random();
    private final GameState state;

    private int neoRow, neoCol;
    private final List<int[]> agents = new ArrayList<>();
    private final List<int[]> phones = new ArrayList<>();

    public Board(int size, int numAgents, int numPhones, GameState state) {
        this.size = size;
        this.grid = new char[size][size];
        this.state = state;
        for (char[] row : grid) Arrays.fill(row, EMPTY);
        placeWalls();
        placePhones(numPhones);
        placeNeo();
        placeAgents(numAgents);
    }

    private void placeWalls() {
        int count = (int)(size * size * 0.15);
        int placed = 0;
        while (placed < count) {
            int r = random.nextInt(size);
            int c = random.nextInt(size);
            if (grid[r][c] == EMPTY) {
                grid[r][c] = WALL;
                placed++;
            }
        }
    }

    private void placePhones(int n) {
        for (int i = 0; i < n; i++) {
            int[] pos = randomEmpty();
            grid[pos[0]][pos[1]] = PHONE;
            phones.add(pos);
        }
    }

    private void placeNeo() {
        int[] pos = randomEmpty();
        neoRow = pos[0];
        neoCol = pos[1];
        grid[neoRow][neoCol] = NEO;
    }

    private void placeAgents(int n) {
        for (int i = 0; i < n; i++) {
            int[] pos = randomEmpty();
            grid[pos[0]][pos[1]] = AGENT;
            agents.add(pos);
        }
    }

    private int[] randomEmpty() {
        int r, c;
        do {
            r = random.nextInt(size);
            c = random.nextInt(size);
        } while (grid[r][c] != EMPTY);
        return new int[]{r, c};
    }

    public synchronized void print() {
        System.out.print("  ");
        for (int c = 0; c < size; c++) System.out.print(c % 10 + " ");
        System.out.println();
        for (int r = 0; r < size; r++) {
            System.out.print(r % 10 + " ");
            for (int c = 0; c < size; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println("[N]=Neo  [A]=Agente  [T]=Telefono  [#]=Pared");
    }

    private int[] bfsNextStep(int fromR, int fromC, int toR, int toC, boolean esNeo) {
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        boolean[][] visitado = new boolean[size][size];
        int[][] padreR = new int[size][size];
        int[][] padreC = new int[size][size];
        for (int[] fila : padreR) Arrays.fill(fila, -1);

        Queue<int[]> cola = new LinkedList<>();
        cola.add(new int[]{fromR, fromC});
        visitado[fromR][fromC] = true;

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int r = actual[0];
            int c = actual[1];

            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];

                if (nr < 0 || nr >= size || nc < 0 || nc >= size) continue;
                if (visitado[nr][nc]) continue;
                if (grid[nr][nc] == WALL) continue;
                if (esNeo && grid[nr][nc] == AGENT) continue;

                visitado[nr][nc] = true;
                padreR[nr][nc] = r;
                padreC[nr][nc] = c;

                if (nr == toR && nc == toC) {
                    int cr = toR;
                    int cc = toC;
                    while (padreR[cr][cc] != fromR || padreC[cr][cc] != fromC) {
                        int tr = padreR[cr][cc];
                        int tc = padreC[cr][cc];
                        cr = tr;
                        cc = tc;
                    }
                    return new int[]{cr, cc};
                }

                cola.add(new int[]{nr, nc});
            }
        }
        return null;
    }

    private int[] closestAgent() {
        int[] best = null;
        int bestDist = Integer.MAX_VALUE;
        for (int[] a : agents) {
            int dist = Math.abs(a[0] - neoRow) + Math.abs(a[1] - neoCol);
            if (dist < bestDist) { bestDist = dist; best = a; }
        }
        return best;
    }

    private int[] bestPhone() {
        if (phones.isEmpty()) return null;
        int[] agenteCercano = closestAgent();
        int[] mejor = null;
        int mejorPuntaje = Integer.MIN_VALUE;
        for (int[] p : phones) {
            int distNeo = Math.abs(p[0] - neoRow) + Math.abs(p[1] - neoCol);
            int distAgente = agenteCercano == null ? 0 :
                    Math.abs(p[0] - agenteCercano[0]) + Math.abs(p[1] - agenteCercano[1]);
            int puntaje = distAgente - distNeo;
            if (puntaje > mejorPuntaje) { mejorPuntaje = puntaje; mejor = p; }
        }
        return mejor;
    }

    public synchronized char moveNeo() {
        if (state.isGameOver()) return 'M';

        int[] target = bestPhone();
        if (target == null) return 'M';

        int[] next = bfsNextStep(neoRow, neoCol, target[0], target[1], true);

        if (next == null) {
            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
            int[] agente = closestAgent();
            int mejorDist = -1;
            for (int[] d : dirs) {
                int nr = neoRow + d[0];
                int nc = neoCol + d[1];
                if (nr < 0 || nr >= size || nc < 0 || nc >= size) continue;
                if (grid[nr][nc] == WALL || grid[nr][nc] == AGENT) continue;
                int dist = agente == null ? 0 : Math.abs(nr - agente[0]) + Math.abs(nc - agente[1]);
                if (dist > mejorDist) { mejorDist = dist; next = new int[]{nr, nc}; }
            }
        }

        if (next == null) return 'M';

        char dest = grid[next[0]][next[1]];
        if (dest == AGENT) return 'C';

        grid[neoRow][neoCol] = EMPTY;
        neoRow = next[0];
        neoCol = next[1];

        if (dest == PHONE) {
            grid[neoRow][neoCol] = NEO;
            return 'W';
        }

        grid[neoRow][neoCol] = NEO;
        return 'M';
    }

    public synchronized boolean moveAgent(int index) {
        if (state.isGameOver()) return false;
        if (index >= agents.size()) return false;

        int[] pos = agents.get(index);
        int[] next = bfsNextStep(pos[0], pos[1], neoRow, neoCol, false);
        if (next == null) return false;

        char dest = grid[next[0]][next[1]];

        boolean phoneWasHere = false;
        for (int[] p : phones) {
            if (p[0] == pos[0] && p[1] == pos[1]) { phoneWasHere = true; break; }
        }
        grid[pos[0]][pos[1]] = phoneWasHere ? PHONE : EMPTY;

        agents.set(index, next);
        grid[next[0]][next[1]] = AGENT;

        return dest == NEO;
    }

    public synchronized int getAgentCount() { return agents.size(); }
}