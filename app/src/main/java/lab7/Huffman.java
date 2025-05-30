/*  Daniel Perterson && Naythan Ramos | CSCI 241
 *  Lab 7 - 5.29.25
 * 
 *  Huffman.java
 * 
 *  implements a huffman coding tree for text file compression
 * 
 *  also includes a small main class for demonstrating functionality of HCT
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
            tree.bstInsert(c.toString());
            forest.add(tree, c.frequency);
        }
        while (forest.size() > 1) {
            AVL lowest = forest.poll();
            AVL secondLowest = forest.poll();
            AVL newTree = new AVL();
            int combinedFreqs = 0;
            if (lowest.root.left == null) {
                combinedFreqs += Integer.valueOf(lowest.root.word.substring(2));
            }
            else {
                combinedFreqs += Integer.valueOf(lowest.root.word);
            }
            if (secondLowest.root.left == null) {
                combinedFreqs += Integer.valueOf(secondLowest.root.word.substring(2));
            }
            else {
                combinedFreqs += Integer.valueOf(secondLowest.root.word);
            }
            newTree.bstInsert(String.valueOf(combinedFreqs));
            if (lowest.root.left == null) {
                newTree.root.left = newTree.new Node(String.valueOf(lowest.root.word.charAt(0)));
            }
            else {
                newTree.root.left = lowest.root;
            }
            if (secondLowest.root.left == null) {
                newTree.root.right = newTree.new Node(String.valueOf(secondLowest.root.word.charAt(0)));
            }
            else {
                newTree.root.right = secondLowest.root;
            }
            forest.add(newTree, combinedFreqs);
        }
        AVL p = forest.poll();
        return p;
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

    /** Enocdes an input string given a Huffman coding tree */
    public static String encode(AVL tree, String input) {
        StringBuilder encoded = new StringBuilder();
        treeToMap(tree);
        for (int i = 0; i < input.length(); i++) {
            encoded.append(table.get(String.valueOf(input.charAt(i))));
        }
        return encoded.toString();
    }

    /** Converts a Huffman coding tree into a map for encoding */
    private static void treeToMap(AVL tree) {
        table = new HashTable<String, String>();
        treeToMap(tree.root, "");
    }

    /** Converts a Huffman coding tree into a map for encoding */
    private static void treeToMap(AVL.Node n, String path) {
        if (n.left == null) {
            table.put(n.word, path);
        }
        else {
            treeToMap(n.left, path + "0");
            treeToMap(n.right, path + "1");
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
        // the input string always has an extra newline char at the end for some reason, so we remove that
        inputStr = inputStr.substring(0, inputStr.length()-1);
        // generate char frequencies from string
        freqHeap = stringFreq.genFreq(inputStr);

        // generate HCT from frequency heap
        HCT = buildTree(freqHeap);
        String en = encode(HCT, inputStr);
        String de = decode(HCT, en);
        
        // for input lengths <= 100 chars, print strings
        if (inputStr.length() <= 100) {
            System.out.println("Input String: " + inputStr);
            System.out.println("Encoded String: " + en);
            System.out.println("Decoded String: " + de);
        }

        // general info
        System.out.println("Decoded Equals Input: " + inputStr.equals(de));

        double compRatio = ((float)en.length()/(float)inputStr.length()/8.0);
        System.out.println("Compression Ratio: " + compRatio);

    }
}
