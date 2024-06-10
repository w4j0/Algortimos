package Ejercicios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTree<E extends Comparable<E>> {
    protected BNode<E> root;
    private int order;

    public BTree(int order) {
        this.order = order;
        this.root = null;
    }

    public static <E extends Comparable<E>> BTree<E> building_Btree(String filename) throws ItemNotFound, IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            int order = Integer.parseInt(reader.readLine().trim());
            BTree<E> tree = new BTree<>(order);

            Map<Integer, BNode<E>> nodes = new HashMap<>();
            Map<Integer, Integer> nodeLevels = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int level = Integer.parseInt(parts[0].trim());
                int idNode = Integer.parseInt(parts[1].trim());
                BNode<E> node = new BNode<>(order);
                node.idNode = idNode;

                List<E> keys = new ArrayList<>();
                for (int i = 2; i < parts.length; i++) {
                    if (!parts[i].equals("null")) {
                        keys.add((E) (Comparable) Integer.parseInt(parts[i].trim()));
                    }
                }
                node.keys = keys.toArray((E[]) new Comparable[keys.size()]);
                node.count = keys.size();

                nodes.put(idNode, node);
                nodeLevels.put(idNode, level);

                if (level == 0) {
                    tree.root = node;
                }
            }

            for (BNode<E> node : nodes.values()) {
                int idNode = node.idNode;
                int level = nodeLevels.get(idNode);
                if (level > 0) {
                    int parentNodeId = findParentId(nodeLevels, idNode, level, order);
                    BNode<E> parentNode = nodes.get(parentNodeId);
                    if (parentNode != null) {
                        parentNode.childs.add(node);
                    }
                }
            }

            if (!tree.validateBTree()) {
                throw new ItemNotFound("El árbol no cumple las propiedades de un B-Tree");
            }

            return tree;

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static int findParentId(Map<Integer, Integer> nodeLevels, int idNode, int level, int order) {
        for (int id : nodeLevels.keySet()) {
            if (nodeLevels.get(id) == level - 1 && id <= (idNode - 1) / (order - 1)) {
                return id;
            }
        }
        return -1;
    }

    private boolean validateBTree() {
        return validateBTree(this.root, -1) != -1;
    }

    private int validateBTree(BNode<E> node, int expectedHeight) {
        if (node == null) return 0;

        int minKeys = (int) Math.ceil((double) order / 2) - 1;
        int maxKeys = order - 1;

        if (node.count < minKeys || node.count > maxKeys) {
            return -1;
        }

        int currentHeight = -1;
        for (BNode<E> child : node.childs) {
            int height = validateBTree(child, expectedHeight);

            if (height == -1) {
                return -1;
            }

            if (currentHeight == -1) {
                currentHeight = height;
            } else if (currentHeight != height) {
                return -1;
            }
        }

        if (expectedHeight == -1) {
            return currentHeight + 1;
        } else if (expectedHeight != currentHeight + 1) {
            return -1;
        }

        return currentHeight + 1;
    }
}
