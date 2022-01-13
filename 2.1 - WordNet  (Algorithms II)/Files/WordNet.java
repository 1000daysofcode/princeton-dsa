/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private HashMap<Integer, String> matches = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> nouns;
    private Digraph hnGraph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        this.nouns = makeSsMap(synsets);
        this.hnGraph = makeHnGraph(hypernyms);
    }

    private HashMap<String, ArrayList<Integer>> makeSsMap(String synsets) {
        In ss = new In(synsets);
        HashMap<String, ArrayList<Integer>> nounList = new HashMap<>();

        while (ss.hasNextLine()) {
            String[] synset = ss.readLine().split(",");

            int id = Integer.parseInt(synset[0]);
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(id);

            String[] nounsList;
            if (synset[1].contains(" ")) nounsList = synset[1].split(" ");
            else nounsList = new String[] { synset[1] };

            for (String noun : nounsList) {
                if (nounList.containsKey(noun)) nounList.get(noun).add(id);
                else nounList.put(noun, temp);
                // matches.put(id, noun);
            }
            matches.put(id, synset[1]);
        }
        return nounList;
    }

    private Digraph makeHnGraph(String hypernyms) {
        In hn = new In(hypernyms);

        int hnSS = 0;
        Digraph dg = new Digraph(matches.size());

        while (hn.hasNextLine()) {
            String[] hypernym = hn.readLine().split(",");
            int v = Integer.parseInt(hypernym[0]);
            for (int i = 1; i < hypernym.length; i++) {
                dg.addEdge(v, Integer.parseInt(hypernym[i]));
            }
            hnSS++;
        }
        checkDAG(dg);
        return dg;
    }

    private void checkDAG(Digraph dg) {
        DirectedCycle dcdg = new DirectedCycle(dg);
        if (dcdg.hasCycle()) throw new IllegalArgumentException("Has cycle");
        int roots = 0;
        for (int i = 0; i < dg.V(); i++) {
            if (!dg.adj(i).iterator().hasNext()) roots++;
            if (roots > 1) throw new IllegalArgumentException("Not rooted");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        // return allNouns;
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        // return allNouns.contains(word);
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(hnGraph);
        ArrayList<Integer> v = nouns.get(nounA); // get key based on noun
        ArrayList<Integer> w = nouns.get(nounB); // get key based on noun
        int length = sap.length(v, w);
        return length;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(hnGraph);
        ArrayList<Integer> v = nouns.get(nounA); // get key based on noun
        ArrayList<Integer> w = nouns.get(nounB); // get key based on noun
        int a = sap.ancestor(v, w);
        // String ancestor = ssMap.get(a)[0];
        String ancestor = matches.get(a);
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // WordNet wn = new WordNet(args[0], args[1]);
        // StdOut.println("Done");
    }
}
