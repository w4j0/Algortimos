package Actividades;

public class Principal {
	 public static void main(String[] args) {
	        BNode<Integer> node = new BNode<>(5);
	        node.keys.set(0, 10);
	        node.keys.set(1, 20);
	        node.keys.set(3, 30);
	        node.keys.set(2, 25);
	        node.keys.set(4, 48);
	        node.count = 4;

	        int[] pos = new int[1];
	        boolean found = node.searchNode(20, pos);
	        System.out.println("Busqueda para la clave 20: encontrada = " + found + ", posicion = " + pos[0]);

	        found = node.searchNode(50, pos);
	        System.out.println("Busqueda para la clabe 50: encontrada = " + found + ", posicion = " + pos[0]);
	        
	        BTree<Integer> btree = new BTree<>(5); //Asumiendo un orden de 4 para el B-Tree

	        //Insertar los valores de los nodos de la imagen
	        int[] valores = {31, 12, 19, 41, 57, 63, 3, 10, 13, 16, 22, 25, 28, 33, 38, 40, 49, 52, 55, 60, 62, 67, 70, 72};
	        for (int valor : valores) {
	            btree.insert(valor);
	        }

	        // Imprimir la tabla resultante
	        System.out.println(btree);
	        
	        //Buscar valores
	        found = btree.search(52);
	        System.out.println(found);
	        
	        found = btree.search(3);
	        System.out.println(found);
	        
	        found = btree.search(40);
	        System.out.println(found);
	        
	        found = btree.search(58);
	        System.out.println(found);
	        
	        btree.delete(52);
	        btree.delete(3);
	        btree.delete(40);
	        btree.delete(58);
	        
	        System.out.println("\nBúsqueda después de eliminar elementos:");
	        System.out.println("¿Se encuentra 52? " + btree.search(52));
	        System.out.println("¿Se encuentra 3? " + btree.search(3));
	        System.out.println("¿Se encuentra 40? " + btree.search(40));
	        
	 }
}
