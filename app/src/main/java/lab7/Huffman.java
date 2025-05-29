/*
 * 
 */
package lab7;

import heap.HashTable;
import heap.Heap;

import avl.AVL;

public class Huffman {
    
    /**
     * Builds a tree from a list of frequencies
     */
    public static AVL buildTree(Heap<StrFreq, Integer> frequencies) {
        Heap<AVL, Integer> forest = new Heap<AVL, Integer>();
        while (frequencies.size() != 0) {
            StrFreq c = frequencies.poll();
            AVL tree = new AVL();
            tree.bstInsert(c.str);
            forest.add(tree, c.freq);
        }
        while (forest.size() > 1) {
            AVL lowest = forest.poll();
            AVL secondLowest = forest.poll();
            AVL newTree = new AVL();
            int combinedFreqs = Integer.valueOf(lowest.root.word) + Integer.valueOf(secondLowest.root.word);
            newTree.root = newTree.new Node(String.valueOf(combinedFreqs), null, lowest.root, secondLowest.root);
            forest.add(newTree, combinedFreqs);
        }
        return forest.poll();
    }

    /**
     * Decodes a BitString given a tree
     */
    public static String decode(AVL tree, String bits) {
        StringBuilder original = new StringBuilder();
        AVL.Node n = tree.root;
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '0') {
                n = n.left;
                if (n.left == null) {
                    original.append(n.word);
                    n = tree.root;
                }
            }
            else if (bits.charAt(i) == '1') {
                n = n.right;
                if (n.left == null) {
                    original.append(n.word);
                    n = tree.root;
                }
            }
        }
        return original.toString();
    }

    public static String encode(AVL tree, String input) {
        StringBuilder encoded = new StringBuilder();
        HashTable<String, String> table = treeToMap(tree);
        for (int i = 0; i < input.length(); i++) {
            encoded.append(table.get(String.valueOf(input.charAt(i))));
        }
        return encoded.toString();
    }

    private static HashTable<String, String> treeToMap(AVL tree) {
        //table is <character, how to get to it>
        HashTable<String, String> table = new HashTable<String, String>();
        treeToMap(tree.root, table, new StringBuilder());
        return table;
    }

    private static void treeToMap(AVL.Node n, HashTable<String, String> table, StringBuilder path) {
        if (n.left == null) {
            table.put(n.word, path.toString());
        }
        else {
            treeToMap(n.left, table, path.append(0));
            treeToMap(n.right, table, path.append(1));
        }
    }

    /**
     * Inner container class which contains a character represented as a String and the frequency of that String
     */
    class StrFreq {
        String str;
        int freq;

        public StrFreq(String s, int f) {
            str = s;
            freq = f;
        }
    }

    public static void main(String[] args) {
        
    }
}
