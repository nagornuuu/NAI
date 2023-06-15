import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Node> nodes = readInputFile();
        Node root = buildHuffmanTree(nodes);
        Map<Character, String> codes = encode(root);

        codes.forEach((letter, code) -> System.out.println(letter + ": " + code));
    }

    private static List<Node> readInputFile() {
        List<Node> nodes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("huffman.txt"))) {
            int n = Integer.parseInt(reader.readLine());

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                char letter = parts[0].charAt(0); //pobiernie pierwszego znaku z pierwszej części
                int frequency = Integer.parseInt(parts[1]); //pobranie częstotliwości
                Node node = new Node(letter, frequency);
                nodes.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodes;
    }

    private static Node buildHuffmanTree(List<Node> nodes) {
        //changes
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> {
            if (n1.frequency == n2.frequency) {
                return Character.compare(n1.letter, n2.letter); // Alphabetical sorting when frequencies are equal
            }
            return Integer.compare(n1.frequency, n2.frequency); // Sort based on frequencies
        });

        pq.addAll(nodes);

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            //changes
            pq.add(new Node((char) addCharacter(left.letter, right.letter), left.frequency + right.frequency, left, right));
        }


        return pq.poll();
    }
 /*
        (Variant 2) PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> {
            if (n1.frequency == n2.frequency) {
                return Character.compare(n1.letter, n2.letter); // Alphabetical sorting when frequencies are equal
            }
            return Integer.compare(n1.frequency, n2.frequency); // Sort based on frequencies
        });

       (Variant 1) PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.frequency));

    */
    private static Map<Character, String> encode(Node root) {
        Map<Character, String> codes = new HashMap<>();

        helperToEncode(root, "", codes);

        return codes;
    }

    private static void helperToEncode(Node node, String code, Map<Character, String> codes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null && Character.isLetter(node.letter)) {
            codes.put(node.letter, code);
            return;
        } // jezeli jest liściem i jest literą to dodajemy do mapy

        helperToEncode(node.left, code + "0", codes);
        helperToEncode(node.right, code + "1", codes);
    }

    private static class Node {
        char letter;
        int frequency;
        Node left;
        Node right;

        Node(char letter, int frequency) {
            this.letter = letter;
            this.frequency = frequency;
        }

        Node(char letter, int frequency, Node left, Node right) {
            this.letter = letter;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
    }
    //changes
    private static int addCharacter(char letter1, char letter2) {
        return letter1 + letter2;
    }
}
