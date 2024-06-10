package Ejercicios;

import java.util.ArrayList;
import java.util.List;

class BNode<E extends Comparable<E>> {
    protected E[] keys;
    protected List<BNode<E>> childs;
    protected int count;
    int idNode;

    public BNode(int order) {
        this.keys = (E[]) new Comparable[order - 1];
        this.childs = new ArrayList<>(order);
        this.count = 0;
    }
}
