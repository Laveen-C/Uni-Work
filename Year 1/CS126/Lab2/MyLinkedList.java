/**
* A generic implementation of the IList iterface, that uses LinkElements.
*/
public class MyLinkedList<E> implements IList<E> {
    
    MyLinkedListElement<E> head;
    
    public MyLinkedList() {
        this.head = null;
    }
    
    // INCOMPLETE.
    public boolean isEmpty() {
        // Returns whether the list is empty.
        return false;
    }
    
    // INCOMPLETE.
    public boolean add(E element) {
        // Adds an element to the head of the list.
        MyLinkedListElement<E> temp = new MyLinkedListElement<>(element);
        
        // if the list is not empty, point the new link to head
        if (head != null) {
            temp.setNext(head);
        }
        // update the head
        head = temp;
        
        return true;
    }
    
    // INCOMPLETE.
    public int size() {
        // Returns the number of elements in stored in this list.
        return 0;
    }
    
    // INCOMPLETE.
    public String toString() {
        // Returns a string representation of this list.
        return "";
    }
    
    // INCOMPLETE.
    public boolean addToTail(E element) {
        // Adds element to tail of the list
        return false;
    }
    
    // INCOMPLETE.
    public E removeFromHead() {
        // Removes and returns the head element
        return null;
    }
    
    
    
    public E get(int index) {
        // Gets the element at index in the list
        MyLinkedListElement<E> ptr = head;
        for (int i=size()-1;i>index;i--) {
            ptr = ptr.getNext();
        }
        return ptr.getValue();
    }

    public int indexOf(E element) {
        // Gets the index of element in the list
        MyLinkedListElement<E> ptr = head;
        int i=0;
        while (ptr != null) {
            if (element.equals(ptr.getValue())) {
                return i;
            }
            i++;
            ptr = ptr.getNext();
        }
        return -1;
    }

    public E set(int index, E element) {
        // Sets element at index in the list
        MyLinkedListElement<E> ptr = head;
        for (int i=0;i<index;i++) {
            ptr = ptr.getNext();
        }
        E ret = ptr.getNext().getValue();
        MyLinkedListElement<E> newlink = new MyLinkedListElement<>(element);
        newlink.setNext(ptr.getNext().getNext());
        ptr.setNext(newlink);
        return ret;
    }
    
    public void clear() {
        // Clears the list
        head = null;
    }
    
    public boolean contains(E element) {
        // Returns whether the element exists in the list
        return indexOf(element) != -1;
    }

    public boolean remove(E element) {
        // Removes elemenet from the list
        if (isEmpty()) return false;
        MyLinkedListElement<E> ptr = head;
        while (ptr.getNext().getNext() != null) {
            if (element.equals(ptr.getNext().getValue())) {
                ptr.setNext(ptr.getNext().getNext());
                return true;
            }
            ptr = ptr.getNext();
        }
        if (element.equals(ptr.getNext().getValue())) {
            ptr.setNext(null);
            return true;
        }
        return false;
    }

    
}
