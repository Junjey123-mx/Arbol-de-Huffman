//Colaborador: Vernel Josue Hernández Cáceres
//Carne; 24584.

import java.util.*;

public class Huffman {
    char character;
    int frequency;
    Huffman left, right;

    Huffman(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public static class FrequencyComparator implements Comparator<Huffman> {
        public int compare(Huffman x, Huffman y) {
            return x.frequency - y.frequency;
        }
    }
}
