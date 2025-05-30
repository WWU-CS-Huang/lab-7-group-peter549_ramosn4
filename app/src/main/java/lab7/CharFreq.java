/*  Naythan Ramos && ___ | CSCI 241
 *  Lab 7 - 5.29.25
 * 
 *  CharFreq.java
 * 
 *  node type thing, just stores a char and int value for heap entries
 * 
 */

package lab7;

public class CharFreq {
    char character;
    int frequency;

    public CharFreq(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public String toString() {
        return String.valueOf(character) + " " + String.valueOf(frequency);
    }
}
