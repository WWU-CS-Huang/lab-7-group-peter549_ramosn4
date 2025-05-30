/*  Naythan Ramos | CSCI 241
 *  Lab 7 - 5.29.25
 *  
 *  WordFrequency.java
 *  
 *  given an input string, calculate the frequency of each letter in the string
 * 
 *  store characters and their frequencies in a node as a value in a heap, with an associated priority equal to the frequency
 * 
 */

package lab7;

import heap.Heap;
import heap.HashTable;
import heap.AList;

public class StringFrequency {

    Heap<CharFreq, Integer> freqHeap = new Heap<CharFreq, Integer>();
    HashTable<Character, Integer> freqTable = new HashTable<Character, Integer>();
    AList<Character> freqList = new AList<Character>();

    /*  convert an input string 
     * 
     */
    public Heap<CharFreq, Integer> genFreq(String inputStr) {
        genFreqTable(inputStr);
        genFreqHeap();
        return freqHeap;
    }
    /*  put character in input string into hash table
     *  key in table corresponds to character
     *  value in table corresponds to frequency
     *  runtime: O(n), AList append can be an O(u) (u is # of unique chars) operation as well if the list needs to be
     *  resized, but this will likely only happen 4-5 times max for any input string
     */
    private void genFreqTable(String inputStr) {
        // invaraint: inputStr[0..i] has been processed into freqTable
        // unique characters in inputStr[0..1] have distinct nodes in freqTable, with values corresponding
        // to # of occurrences of character
        for (int i = 0; i < inputStr.length(); i++) {
            // get selected char
            char curChar = inputStr.charAt(i);
            //System.out.println(curChar);
            // if selected char is already in table, origFreq will = previous frequency of char
            // if selected char is not already in table, origFreq = 0
            int origFreq = 0;
            if (freqTable.containsKey(curChar)) {
                origFreq = freqTable.remove(curChar);               
            }
            // if this is the first occurrence of curChar, add it to the list of unique chars
            // we need to do this to maintain what keys are in our table
            if (origFreq == 0) {
                freqList.append(curChar);
            }
            // create new entry in table with updated frequency (1 or origFreq+1)
            freqTable.put(curChar, origFreq + 1);
        }
    }

    /*  convert freqTable into a heap
     *  precondition: freqTable has already been populated
     *  runtime: ulog(u) where u is # of unique characters in string
     *  does mean that worst case runtime could be > O(n), not sure how to get around this
     *  but for an average case n >>> u so this is negligible
     */
    private void genFreqHeap() {
        for (int i = 0; i < freqList.size(); i ++) {
            // get selected char/key from freqList
            char curChar = freqList.get(i);
            // get frequency corresponding to that char from freqTable
            int freq = freqTable.remove(curChar);
            // now that we have the selected char and its frequency, create new node in heap with these
            // create CharFreq with current char and freq so both are accessible later
            CharFreq curCharFreq = new CharFreq(curChar, freq);
            // add new entry to heap with curCharFreq as value and freq as priority
            freqHeap.add(curCharFreq, freq);
            //System.out.println(curCharFreq.toString());
        }
    }
}
