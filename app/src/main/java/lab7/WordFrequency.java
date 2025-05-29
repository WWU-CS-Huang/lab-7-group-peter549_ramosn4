/*  Naythan Ramos | CSCI 241
 *  Lab 7 - 5.29.25
 *  
 *  WordFrequency.java
 *  
 *  given an input string, calculate the frequency of each letter in the string
 * 
 */

package main.java.lab7;

import heap.Heap;
import heap.HashTable;
import heap.AList;

public class WordFrequency {

    Heap freqHeap = new Heap();
    HashTable freqTable = new HashTable();
    AList freqList = new AList();

    /*  convert an input string 
     * 
     */
    public Heap genFreq(String inputStr) {
        genFreqTable(inputStr);
        genFreqHeap();
        return freqHeap;
    }
    /*  put character in input string into hash table
     *  key in table corresponds to character
     *  value in table corresponds to frequency
     */
    private void genFreqTable(String inputStr) {
        // invaraint: inputStr[0..i] has been processed into freqTable
        // unique characters in inputStr[0..1] have distinct nodes in freqTable, with values corresponding
        // to # of occurrences of character
        for (int i = 0; i < inputStr.length(); i ++) {
            // get selected char
            char curChar = inputStr.charAt(i);
            // if selected char is already in table, origFreq will = previous frequency of char
            // if selected char is not already in table, origFreq = 0
            int origFreq = freqTable.remove(curChar);
            // if this is the first occurrence of curChar, add it to the list of unique chars
            // we need to do this to maintain what keys are in our table
            if (origFreq == 0) {
                freqList.append(curChar);
            }
            // create new entry in table with updated frequency (1 or origFreq+1)
            freqTable.add(curChar, origFreq+1);
        }
    }

    /*  convert freqTable into a heap
     *  precondition: freqTable has already been populated
     */
    private void genFreqHeap() {
        for (int i = 0; i < freqList.size; i ++) {
            // get selected char/key from freqList
            char curChar = freqList.get(i);
            // get frequency corresponding to that char from freqTable
            int freq = freqTable.remove(curChar);
            // now that we have the selected char and its frequency, create new node in heap with these
            // create CharFreq with current char and freq so both are accessible later
            CharFreq curCharFreq = CharFreq(curChar, freq);
            // add new entry to heap with curCharFreq as value and freq as priority
            freqHeap.add(curCharFreq, freq);
        }
    }
}
