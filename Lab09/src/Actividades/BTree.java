package Actividades;

public class BTree<E extends Comparable<E>> {
	private BNode<E> root; //raiz del arbol
	private int orden; //orden del arbol B
	private boolean up; //indicador de si el arbol necesita un ajuste hacia arriba
	private BNode<E> nDes; //nuevo nodo que se crea cuando se divide un nodo lleno

	//Constructor
	public BTree(int orden) {
		this.orden = orden;
		this.root = null;
	}

	//Verificar si el arbol esta vacio
	public boolean isEmpty() {
		return this.root == null;
	}

	//Insertar una clave en el arbol
	public void insert(E cl) {
		up = false; //resetea el indicador de ajuste hacia arriba
		E mediana; //valor mediano que puede subir
		BNode<E> pnew; //nuevo nodo que puede convertirse en la nueva raiz
		mediana = push(this.root, cl); //inserta la clave y obtiene la mediana si hay ajuste
		if (up) { //si hay ajuste hacia arriba
			pnew = new BNode<E>(this.orden); //crea un nuevo nodo
			pnew.count = 1; //inicializa la cuenta de claves del nuevo nodo
			pnew.keys.set(0, mediana); //establece la mediana en el nuevo nodo
			pnew.childs.set(0, this.root); //establece la raiz antigua como primer hijo
			pnew.childs.set(1, nDes); //establece el nuevo nodo dividido como segundo hijo
			this.root = pnew; //el nuevo nodo se convierte en la nueva raiz
		}
	}

	//Metodo recursivo para insertar una clave y dividir los nodos
	private E push(BNode<E> current, E cl) {
		int pos[] = new int[1]; //posicion donde se debe insertar la clave
		E mediana; //valor mediano que puede subir
		if (current == null) { //si el nodo actual es nulo (hoja)
			up = true; //necesita ajuste hacia arriba
			nDes = null; //no hay nodo derecho aun
			return cl; //devuelve la clave que se insertara
		} else {
			boolean fl; //indicador de si se encontro la clave
			fl = current.searchNode(cl, pos); //busca la posicion de la clave en el nodo actual
			if (fl) { //si la clave ya existe
				System.out.println("Item duplicado\n"); //mensaje de clave duplicada
				up = false; //no hay ajuste hacia arriba
				return null; //no devuelve ninguna clave
			}
			mediana = push(current.childs.get(pos[0]), cl); //inserta recursivamente en el hijo correspondiente
			if (up) { //si hay ajuste hacia arriba
				if (current.nodeFull(this.orden - 1)) //si el nodo esta lleno
					mediana = dividedNode(current, mediana, pos[0]); //divide el nodo
				else {
					up = false; //no hay ajuste hacia arriba
					putNode(current, mediana, nDes, pos[0]); //inserta la clave en el nodo actual
				}
			}
			return mediana; //devuelve la mediana
		}
	}

	//Metodo para insertar una clave y un nodo derecho en un nodo dado
	private void putNode(BNode<E> current, E cl, BNode<E> rd, int k) {
		int i;
		for (i = current.count - 1; i >= k; i--) {
			current.keys.set(i + 1, current.keys.get(i)); //desplaza las claves a la derecha
			current.childs.set(i + 2, current.childs.get(i + 1)); //desplaza los hijos a la derecha
		}
		current.keys.set(k, cl); //inserta la clave en la posicion k
		current.childs.set(k + 1, rd); //inserta el nodo derecho en la posicion k+1
		current.count++; //incrementa el contador de claves
	}

	//Metodo para dividir un nodo lleno
	private E dividedNode(BNode<E> current, E cl, int k) {
		BNode<E> rd = nDes; //nodo derecho
		int i, posMdna;
		posMdna = (k <= this.orden / 2) ? this.orden / 2 : this.orden / 2 + 1; //calcula la posicion de la mediana
		nDes = new BNode<E>(this.orden); //crea un nuevo nodo derecho
		for (i = posMdna; i < this.orden - 1; i++) {
			nDes.keys.set(i - posMdna, current.keys.get(i)); //copia las claves al nuevo nodo derecho
			nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1)); //copia los hijos al nuevo nodo derecho
		}
		nDes.count = (this.orden - 1) - posMdna; //establece el contador de claves del nuevo nodo derecho
		current.count = posMdna; //actualiza el contador de claves del nodo actual
		if (k <= this.orden / 2)
			putNode(current, cl, rd, k); //inserta la clave en el nodo actual si esta en la primera mitad
		else
			putNode(nDes, cl, rd, k - posMdna); //inserta la clave en el nuevo nodo derecho si esta en la segunda mitad
		E median = current.keys.get(current.count - 1); //la mediana es la ultima clave del nodo actual
		nDes.childs.set(0, current.childs.get(current.count)); //copia el hijo del nodo actual al nuevo nodo derecho
		current.count--; //decrementa el contador de claves del nodo actual
		return median; //devuelve la mediana
	}

	//Metodo para convertir el arbol en una cadena de texto
	public String toString() {
		String s = "";
		if (isEmpty())
			s += "BTree is empty...";
		else {
			s += "Id.Nodo\tClaves Nodo\tId.Padre\tId.Hijos\n";
			s += writeTree(this.root, null); //escribe el arbol comenzando desde la raiz
		}
		return s;
	}

	//Metodo recursivo para escribir el arbol en una cadena de texto
	private String writeTree(BNode<E> current, BNode<E> parent) {
		StringBuilder sb = new StringBuilder();
		sb.append(current.idNode).append("\t(");
		for (int i = 0; i < current.count; i++) {
			sb.append(current.keys.get(i)); //añade las claves del nodo actual
			if (i < current.count - 1) {
				sb.append(", "); //añade una coma entre las claves
			}
		}
		sb.append(")\t");
		if (parent == null) {
			sb.append("--\t"); //indica que no hay padre para la raiz
		} else {
			sb.append("[").append(parent.idNode).append("]\t"); //añade el id del padre
		}
		sb.append("[");
		boolean hasChildren = false;
		for (int i = 0; i <= current.count; i++) {
			if (current.childs.get(i) != null) {
				if (hasChildren) {
					sb.append(", "); //añade una coma entre los hijos
				}
				sb.append(current.childs.get(i).idNode); //añade el id del hijo
				hasChildren = true;
			}
		}
		sb.append("]\n");
		for (int i = 0; i <= current.count; i++) {
			if (current.childs.get(i) != null) {
				sb.append(writeTree(current.childs.get(i), current)); //llama recursivamente para escribir los hijos
			}
		}
		return sb.toString();
	}
	
	//Metodo para buscar un elemento en el arbol
	public boolean search(E cl) {
		return searchRecursive(this.root, cl); //llama al metodo recursivo de busqueda
	}

	//Metodo recursivo para busqueda
	private boolean searchRecursive(BNode<E> current, E cl) {
		if (current == null) { //si el nodo actual es nulo

			return false; //retorna falso (elemento no encontrado)
		}
		int[] pos = new int[1]; //arreglo para almacenar la posicion
		boolean found = current.searchNode(cl, pos); //busca el elemento en el nodo actual
		if (found) { //si se encontró el elemento
			System.out.println(cl + " se encuentra en el nodo " + current.idNode + " en la posicion " + pos[0]); //muestra un mensaje con la informacion de la posicion
			return true; //retorna verdadero
		} else { //si el elemento no se encuentra en el nodo actual
			return searchRecursive(current.childs.get(pos[0]), cl); //realiza la busqueda en el hijo correspondiente
		}
	}
	
	// Método para eliminar un elemento del árbol
	public void delete(E key) {
	    if (root == null) {
	        System.out.println("El árbol está vacío.");
	        return;
	    }
	    delete(root, key); // Llama al método de eliminación recursivo
	    if (root.count == 0) {
	        // Si la raíz queda vacía después de la eliminación, actualiza la raíz
	        if (root.childs.get(0) == null) {
	            root = null;
	        } else {
	            root = root.childs.get(0);
	        }
	    }
	}

	// Método de eliminación recursivo
	private boolean delete(BNode<E> node, E key) {
	    int pos[] = new int[1];
	    boolean found = node.searchNode(key, pos); // Busca la posición del elemento en el nodo

	    if (found) {
	        if (node.childs.get(pos[0]) == null) {
	            removeKey(node, pos[0]); // Si el elemento está en una hoja, simplemente elimínalo
	            return true;
	        } else {
	            E pred = getPredecessor(node, pos[0]); // Si no está en una hoja, obtén el predecesor
	            node.keys.set(pos[0], pred); // Reemplaza el elemento con el predecesor
	            return delete(node.childs.get(pos[0]), pred); // Elimina recursivamente el predecesor
	        }
	    } else {
	        if (node.childs.get(pos[0]) == null) {
	            return false;
	        } else {
	            boolean isDeleted = delete(node.childs.get(pos[0]), key); // Busca en el hijo correspondiente
	            if (node.childs.get(pos[0]).count < (orden - 1) / 2) {
	                fix(node, pos[0]); // Realiza una corrección si es necesario
	            }
	            return isDeleted;
	        }
	    }
	}

	// Método para eliminar una clave de un nodo
	private void removeKey(BNode<E> node, int index) {
	    for (int i = index; i < node.count - 1; i++) {
	        node.keys.set(i, node.keys.get(i + 1)); // Desplaza las claves hacia la izquierda
	    }
	    node.keys.set(node.count - 1, null); // Borra la última clave
	    node.count--; // Decrementa el contador de claves
	}

	// Método para obtener el predecesor de un elemento en un nodo
	private E getPredecessor(BNode<E> node, int index) {
	    BNode<E> current = node.childs.get(index);
	    while (current.childs.get(current.count) != null) {
	        current = current.childs.get(current.count); // Desciende al hijo más a la derecha
	    }
	    return current.keys.get(current.count - 1); // Devuelve la última clave
	}

	// Método para corregir el árbol si un nodo se queda con muy pocas claves después de la eliminación
	private void fix(BNode<E> parent, int index) {
	    if (index > 0 && parent.childs.get(index - 1).count > (orden - 1) / 2) {
	        borrowFromLeft(parent, index); // Intenta pedir prestado de un hermano izquierdo
	    } else if (index < parent.count && parent.childs.get(index + 1).count > (orden - 1) / 2) {
	        borrowFromRight(parent, index); // Intenta pedir prestado de un hermano derecho
	    } else {
	        if (index > 0) {
	            merge(parent, index - 1); // Fusiona con el hermano izquierdo
	        } else {
	            merge(parent, index); // Fusiona con el hermano derecho
	        }
	    }
	}
	
	// Método para pedir prestado claves de un hermano izquierdo
	private void borrowFromLeft(BNode<E> parent, int index) {
	    BNode<E> leftSibling = parent.childs.get(index - 1); // Obtén el hermano izquierdo
	    BNode<E> node = parent.childs.get(index); // Nodo actual

	    // Hacer espacio para la clave que se va a transferir
	    for (int i = node.count; i > 0; i--) {
	        node.keys.set(i, node.keys.get(i - 1)); // Desplaza las claves hacia la derecha
	    }
	    for (int i = node.count + 1; i > 0; i--) {
	        node.childs.set(i, node.childs.get(i - 1)); // Desplaza los hijos hacia la derecha
	    }

	    // Transferir la clave del padre al nodo actual
	    node.keys.set(0, parent.keys.get(index - 1));
	    node.count++;

	    // La clave del hermano izquierdo pasa al padre
	    parent.keys.set(index - 1, leftSibling.keys.get(leftSibling.count - 1));
	    leftSibling.keys.set(leftSibling.count - 1, null); // Elimina la clave transferida
	    leftSibling.count--;

	    // Si el nodo actual tiene hijos, también transferir el hijo más a la derecha del hermano izquierdo
	    if (!leftSibling.nodeEmpty()) {
	        node.childs.set(0, leftSibling.childs.get(leftSibling.count + 1));
	        leftSibling.childs.set(leftSibling.count + 1, null); // Elimina el hijo transferido
	    }
	}

	// Método para pedir prestado claves de un hermano derecho
	private void borrowFromRight(BNode<E> parent, int index) {
	    BNode<E> rightSibling = parent.childs.get(index + 1); // Obtén el hermano derecho
	    BNode<E> node = parent.childs.get(index); // Nodo actual

	    // Transferir la clave del hermano derecho al nodo actual
	    node.keys.set(node.count, parent.keys.get(index)); 
	    node.count++;

	    // La clave del hermano derecho pasa al padre
	    parent.keys.set(index, rightSibling.keys.get(0));

	    // Elimina la clave transferida del hermano derecho
	    removeKey(rightSibling, 0);

	    // Si el nodo actual tiene hijos, también transferir el hijo más a la izquierda del hermano derecho
	    if (!rightSibling.nodeEmpty()) {
	        node.childs.set(node.count, rightSibling.childs.get(0));
	        for (int i = 0; i <= rightSibling.count; i++) {
	            rightSibling.childs.set(i, rightSibling.childs.get(i + 1));
	        }
	        rightSibling.childs.set(rightSibling.count, null); // Elimina el hijo transferido
	    }
	}

	// Método para fusionar un nodo con su hermano
	private void merge(BNode<E> parent, int index) {
	    BNode<E> leftSibling = parent.childs.get(index); // Obtén el hermano izquierdo
	    BNode<E> rightSibling = parent.childs.get(index + 1); // Obtén el hermano derecho

	    // Transfiere la clave del padre al hermano izquierdo
	    leftSibling.keys.set(leftSibling.count, parent.keys.get(index));
	    leftSibling.count++;

	    // Transfiere las claves y los hijos del hermano derecho al hermano izquierdo
	    for (int i = 0; i < rightSibling.count; i++) {
	        leftSibling.keys.set(leftSibling.count + i, rightSibling.keys.get(i));
	        leftSibling.childs.set(leftSibling.count + i, rightSibling.childs.get(i));
	    }
	    leftSibling.childs.set(leftSibling.count + rightSibling.count, rightSibling.childs.get(rightSibling.count));

	    leftSibling.count += rightSibling.count; // Actualiza el contador de claves del hermano izquierdo

	    // Elimina el hermano derecho
	    for (int i = index + 1; i < parent.count; i++) {
	        parent.keys.set(i - 1, parent.keys.get(i));
	        parent.childs.set(i, parent.childs.get(i + 1));
	    }
	    parent.keys.set(parent.count - 1, null); // Borra la clave transferida del padre
	    parent.childs.set(parent.count, null); // Borra la referencia al hermano derecho
	    parent.count--;
	}
}