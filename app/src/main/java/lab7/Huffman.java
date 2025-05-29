/*
 * 
 */
package lab7;

import heap.Heap;
import avl.AVL;

public class Huffman {
    
    public static AVL buildTree(Heap<CharFreq, Integer> frequencies) {
        Heap<AVL, Integer> forest = new Heap<AVL, Integer>();
        while (frequencies.size() != 0) {
            CharFreq c = frequencies.poll();
            AVL tree = new AVL();
            tree.bstInsert(c.chara.toString());
            forest.add(tree, c.freq);
        }
        while (forest.size() > 1) {
            AVL lowest = forest.poll();
            AVL secondLowest = forest.poll();
            AVL newTree = new AVL();
            newTree.root = newTree.new Node(null, null, lowest.root, secondLowest.root);
        }
        return forest.poll();
    }

    class CharFreq {
        Character chara;
        int freq;

        public CharFreq(char c, int f) {
            chara = c;
            freq = f;
        }
    }

    public static void main(String[] args) {
        
    }
}
