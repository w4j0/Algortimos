package Actividades;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
	protected ArrayList<E> keys;
	protected ArrayList<BNode<E>> childs;
	protected int count;
	protected static int nextId = 0;
    protected int idNode;
	public BNode (int n){
		this.keys = new ArrayList<E>(n);
		this.childs = new ArrayList<BNode<E>>(n+1);
		this.count = 0;
		this.idNode = nextId++;
		for(int i=0; i < n; i++){
			this.keys.add(null);
			this.childs.add(null);
		}
	}
	
	//Validar si el nodo actual esta lleno
	public boolean nodeFull (int i) {
		return count == i;
	}
	//Validar si el nodo actual esta vacio
	public boolean nodeEmpty () {
		return count == 0;
	}
	
	//Buscar en el nodo actual el elemento, si se encuentra se retorna true
	//y la posicion en la que se encuentra, caso contrario, retorna false
	//y la posicion del hijo al que debe de descender
	public boolean searchNode (E key, int[] pos) {
		int item = 0;
		for(E llave : keys) { //recorrer las claves del nodo
			if(item >= count) break;
			if(llave.compareTo(key) == 0) { //comparar si el dato a buscar esta dentro del nodo
				pos[0] = item;
				return true;
			}
			else if(llave.compareTo(key) > 0) {//el dato no se encuentra en el nodo
				pos[0] = item;
				return false;
			}
			item++;
		}
		pos[0] = count;
		return false;
	}
	
	//Retornar la claves encontradas en el nodo
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Node ID: ").append(idNode).append(" Keys: ");
		for(int i = 0; i > count; i++) {
			sb.append(keys.get(i)).append(" ");
		}
		return sb.toString();
	}
}