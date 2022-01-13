/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = "";
        int distance = -1;
        for (String noun : nouns) {
            int curDistance = 0;
            for (String otherNouns : nouns) {
                if (!otherNouns.equals(noun)) {
                    curDistance += net.distance(noun, otherNouns);
                }
            }
            if (curDistance > distance) {
                distance = curDistance;
                outcast = noun;
            }
        }
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
