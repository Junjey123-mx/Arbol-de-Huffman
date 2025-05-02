//Colaborador: Vernel Josue Hernández Cáceres
//Carne; 24584.

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class HuffmanCompresor {

    private static Map<Character, String> huffmanCodes = new HashMap<>();
    private static Huffman root;

    public static void main(String[] args) throws IOException {
        // Leer archivo
        String input = readFile("original.txt");

        // Generar árbol
        Map<Character, Integer> frequency = buildFrequencyMap(input);
        root = buildTree(frequency);
        buildCodes(root, "");

        // Codificar texto
        String encoded = encode(input);
        writeCompressedAsText("comprimido.txt", encoded, huffmanCodes);

        // Decodificar
        String decoded = decode(encoded);
        writeFile("descomprimido.txt", decoded);
    }

    static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(new File(path).toPath()));
    }

    static void writeFile(String path, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(data);
        writer.close();
    }

    static void writeCompressedAsText(String path, String bitString, Map<Character, String> codes) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write("CODES:\n");
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            if (entry.getKey() == '\n') {
                writer.write("\\n: " + entry.getValue() + "\n");
            } else if (entry.getKey() == '\r') {
                writer.write("\\r: " + entry.getValue() + "\n");
            } else if (entry.getKey() == '\t') {
                writer.write("\\t: " + entry.getValue() + "\n");
            } else {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
        }
        writer.write("\nDATA:\n");
        writer.write(bitString);
        writer.close();
    }

    static Map<Character, Integer> buildFrequencyMap(String input) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : input.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        return freq;
    }

    static Huffman buildTree(Map<Character, Integer> freqMap) {
        PriorityQueue<Huffman> pq = new PriorityQueue<>(new Huffman.FrequencyComparator());
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new Huffman(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Huffman left = pq.poll();
            Huffman right = pq.poll();
            Huffman merged = new Huffman('\0', left.frequency + right.frequency);
            merged.left = left;
            merged.right = right;
            pq.add(merged);
        }
        return pq.poll();
    }

    static void buildCodes(Huffman node, String code) {
        if (node == null) return;
        if (node.character != '\0') {
            huffmanCodes.put(node.character, code);
        }
        buildCodes(node.left, code + "0");
        buildCodes(node.right, code + "1");
    }

    static String encode(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append(huffmanCodes.get(c));
        }
        return sb.toString();
    }

    static String decode(String encoded) {
        StringBuilder result = new StringBuilder();
        Huffman current = root;
        for (int i = 0; i < encoded.length(); i++) {
            current = (encoded.charAt(i) == '0') ? current.left : current.right;
            if (current.left == null && current.right == null) {
                result.append(current.character);
                current = root;
            }
        }
        return result.toString();
    }
}
