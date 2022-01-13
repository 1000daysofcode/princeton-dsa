/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;


public class BaseballElimination {
    private final String[] names;
    private final HashMap<String, Integer> lookup;
    private final HashMap<String, ArrayList<String>> elim;
    private final int numTeams;
    private final int[] wins, loss, rem;
    private final int[][] gamesRem;
    private int allWins;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In teams = new In(filename);
        numTeams = Integer.parseInt(teams.readLine());

        names = new String[numTeams];
        lookup = new HashMap<>();
        elim = new HashMap<>();

        gamesRem = new int[numTeams][numTeams];

        wins = new int[numTeams];
        loss = new int[numTeams];
        rem = new int[numTeams];

        for (int i = 0; i < numTeams; i++) {
            String name = teams.readString();
            int won = teams.readInt(), lost = teams.readInt(), left = teams.readInt();

            names[i] = name;
            lookup.put(name, i);

            wins[i] = won;
            loss[i] = lost;
            rem[i] = left;

            for (int j = 0; j < numTeams; j++) {
                gamesRem[i][j] = teams.readInt();
            }
        }
        addAllEliminations();
    }

    private void addAllEliminations() {

        // Process trivial eliminations
        for (String each : teams()) {
            ArrayList<String> eliminators = trivElim(each);
            if (eliminators == null) continue;
            else {
                elim.put(each, eliminators);
            }
        }

        // process nontrivial eliminations
        int psblCombos = ((numTeams) * (numTeams - 1)) / 2,
                numVertices = 2 + psblCombos + numTeams;
        int s = numVertices - 2, t = numVertices - 1;
        FlowNetwork flowN;

        // Init FlowNetwork with FlowEdges for each team
        for (int team = 0; team < numTeams; team++) {
            if (!isEliminated(names[team])) {
                flowN = makeFlowNetwork(names[team]);
                FordFulkerson ff = new FordFulkerson(flowN, s, t);

                // continue if not eliminated
                boolean eliminated;
                int val = (int) ff.value();
                eliminated = val != allWins;

                // add to eliminations if eliminated
                if (eliminated) {
                    ArrayList<String> reasonForElim = new ArrayList<>();
                    for (int elimTeam = 0; elimTeam < numTeams; elimTeam++)
                        if (team != elimTeam) {
                            if (ff.inCut(psblCombos + elimTeam)) {
                                reasonForElim.add(names[elimTeam]);
                            }
                        }
                    elim.putIfAbsent(names[team], reasonForElim);
                }
            }
        }
    }

    // trivial eliminations
    private ArrayList<String> trivElim(String team) {
        int maxScorePos = wins(team) + remaining(team);
        ArrayList<String> reasonForElim = new ArrayList<>();

        for (String each : teams()) {
            if (!each.equals(team)) {
                if (wins(each) > maxScorePos) reasonForElim.add(each);
            }
        }
        if (reasonForElim.isEmpty()) return null;
        return reasonForElim;
    }

    // flow network for complex eliminations
    private FlowNetwork makeFlowNetwork(String each) {
        int psblCombos = (numTeams) * (numTeams - 1) / 2;
        int numVertices = 2 + psblCombos + numTeams;
        FlowNetwork flowN = new FlowNetwork(numVertices);

        allWins = 0;

        int s = numVertices - 2, t = numVertices - 1, wr = wins(each) + remaining(each), gv = 0;

        // Make flowEdges of
        for (int i = 0; i < numTeams; i++) {
            for (int j = i + 1; j < numTeams; j++) {
                // source s to games
                flowN.addEdge(new FlowEdge(s, gv, gamesRem[i][j]));

                double posInf = Double.POSITIVE_INFINITY;

                // games to teams
                flowN.addEdge(new FlowEdge(gv, psblCombos + i, posInf));
                flowN.addEdge(new FlowEdge(gv, psblCombos + j, posInf));

                allWins += gamesRem[i][j];
                gv++;
            }
        }

        // teams to sink t
        for (int i = psblCombos; i < s; i++) {
            int index = i - psblCombos;

            flowN.addEdge(new FlowEdge(i, t, wr - wins[index]));
        }
        return flowN;
    }

    // number of teams
    public int numberOfTeams() {
        return numTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return lookup.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!lookup.containsKey(team))
            throw new IllegalArgumentException("No such team");
        return wins[lookup.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!lookup.containsKey(team))
            throw new IllegalArgumentException("No such team");
        return loss[lookup.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!lookup.containsKey(team))
            throw new IllegalArgumentException("No such team");
        return rem[lookup.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!lookup.containsKey(team1) || !lookup.containsKey(team2))
            throw new IllegalArgumentException("No such team");
        return gamesRem[lookup.get(team1)][lookup.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!lookup.containsKey(team))
            throw new IllegalArgumentException("No such team");
        return elim.containsKey(team);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!lookup.containsKey(team))
            throw new IllegalArgumentException("No such team");
        if (isEliminated(team)) return elim.get(team);
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
