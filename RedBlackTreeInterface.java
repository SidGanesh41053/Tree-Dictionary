import java.util.ArrayList;

import P2Individual.SortedCollectionInterface;

public interface RedBlackTreeInterface<T extends Comparable<T>> extends SortedCollectionInterface<T> {

    public boolean insert(T data) throws NullPointerException, IllegalArgumentException;

    public boolean remove(T data) throws NullPointerException, IllegalArgumentException;
    
    // searches through the tree for matching data to return the value of
    public T search(T data) throws NullPointerException, IllegalArgumentException;
    
    // returns the subtree where the specified T would be the root (used for the autocomplete feature) 
    public ArrayList<T> getSubtree(T data);
    
    public boolean contains(T data);
    
    public int size();
    
    public boolean isEmpty();
  }
  