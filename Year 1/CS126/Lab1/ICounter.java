/**
 * An interface for a generic list.
 */
public interface ICounter<E> {
    
    // Adds element to the list when it does not already exist.
    // Returns true on success and false otherwise.
    public boolean add(E element);

    // Returns the count of an item (the number of times it was added to the list).
    public int getCount(E elemnt);

    // Returns the total count of all items in the list.
    public int size();
        
    // Clears (empties) the list.
    public void clear();
    
    // Returns true when element is in the list, false otherwise.
    public boolean contains(E element);
    
    // Returns true when the list contains no elements.
    public boolean isEmpty();
    
    // Removes an element form the list. 
    // Returns true on success, false if the element was not found.
    public boolean remove(E element);
       
}