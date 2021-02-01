/**
* Object for storing a count of obj.
*/
public class Count<E> {
    
    private E obj;
    private int count;
    
    public Count(E obj, int count) {
        this.obj = obj;
        this.count = count;
    }
    
    // Adds 1 to the count
    public void incrementCount() {
        count++;
    }
    
    // Returns the current count
    public int getCount() {
        return count;
    }
    
    // Returns the object that is being counted
    public E getObj() {
        return obj;
    }

    public boolean objectEquals(E otherobj) {
        return this.getObj().equals(otherobj);
    }
    
}
