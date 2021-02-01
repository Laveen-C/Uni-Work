/**
* A generic implementation of the IList iterface.
*/
public class Set<E> implements ISet<E> {
    
    private MyArrayList<E> array = new MyArrayList<>();

    // INCOMPLETE.
    public boolean add(E element) {
        // Adds element to the list when it does not already exist.
        // Returns true on success and false otherwise.
        return false;
    }
    
    // INCOMPLETE.
    public String toString() {
        // Returns a string representation of this Set object.
        return null;
    }
    
    public void clear() {
        array.clear();
    }

    public boolean contains(E element) {
        return array.contains(element);
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public boolean remove(E element) {
        return array.remove(element);
    }

    public int size() {
        return array.size();
    }
    
}
