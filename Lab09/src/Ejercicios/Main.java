package Ejercicios;

public class Main {
    public static void main(String[] args) {
        try {
            // Cambia la ruta del archivo según sea necesario
            String filename = "C:/Users/Yoslao/Desktop/Proyectos Eclipse/Lab09/src/Ejercicios/arbol.txt";
            BTree<Integer> bTree = BTree.building_Btree(filename);

            // Imprime la estructura del árbol B construido
            System.out.println("B-tree construido correctamente:");
            printBTree(bTree.root, 0);

        } catch (ItemNotFound e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static <E extends Comparable<E>> void printBTree(BNode<E> node, int level) {
        if (node != null) {
            System.out.print("Nivel " + level + ": ");
            for (int i = 0; i < node.count; i++) {
                System.out.print(node.keys[i]);
                if (i < node.count - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
            for (BNode<E> child : node.childs) {
                printBTree(child, level + 1);
            }
        }
    }
}