/*
 * 
 */
package lab7;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import heap.HashTable;
import heap.Heap;

import avl.AVL;

public class Huffman {

    protected static HashTable<String, String> table;
    
    /**
     * Builds a tree from a list of frequencies
     */
    public static AVL buildTree(Heap<CharFreq, Integer> frequencies) {
        Heap<AVL, Integer> forest = new Heap<AVL, Integer>();
        while (frequencies.size() != 0) {
            CharFreq c = frequencies.poll();
            AVL tree = new AVL();
            tree.bstInsert(c.frequency + " " + String.valueOf(c.character));
            forest.add(tree, c.frequency);
        }
        while (forest.size() > 1) {
            AVL lowest = forest.poll();
            AVL secondLowest = forest.poll();
            AVL newTree = new AVL();
            int combinedFreqs = new Scanner(lowest.root.word).nextInt() + new Scanner(secondLowest.root.word).nextInt();
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
        treeToMap(tree);
        for (int i = 0; i < input.length(); i++) {
            encoded.append(table.get(String.valueOf(input.charAt(i))));
        }
        return encoded.toString();
    }

    private static void treeToMap(AVL tree) {
        //table is <character, how to get to it>
        table = new HashTable<String, String>();
        treeToMap(tree.root, new StringBuilder());
    }

    private static void treeToMap(AVL.Node n, StringBuilder path) {
        if (n.left == null) {
            table.put(n.word, path.toString());
        }
        else {
            treeToMap(n.left, path.append(0));
            treeToMap(n.right, path.append(1));
        }
    }

    public static void main(String[] args) {
        
        String fileName = args[0];
        // get input file
        File file = new File(fileName);
        Scanner scanner;

        StringFrequency stringFreq = new StringFrequency();
        Heap<CharFreq, Integer> freqHeap;
        AVL HCT;

        // initialize scanner to read input file
        try {
            scanner = new Scanner(file).useDelimiter("//Z");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        // copy input file to string
        String inputStr = scanner.next();
        scanner.close();
        // generate char frequencies from string
        freqHeap = stringFreq.genFreq(inputStr);

        // generate HCT from frequency heap
        HCT = buildTree(freqHeap);
        System.out.println(decode(HCT, encode(HCT, inputStr)));
    }
}
