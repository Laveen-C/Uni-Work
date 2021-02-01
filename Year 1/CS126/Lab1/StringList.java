/**
* An (simple and broken) array list implementation that is capable of holding Strings.
*/
public class StringList {

    private String[] array;
    private int size;
    private int capacity;
    
    public StringList() {
        this.capacity = 100;
        this.array = new String[capacity];
        this.size = 0;
    }

    public boolean add(String element) {
        this.array[size++] = element;
        return true;
    }

    public boolean contains(String element) {
        return this.indexOf(element) >= 0;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return size;
    }

    public String get(int index) {
        return this.array[index];
    }

    public int indexOf(String element) {
        for (int i=0;i<this.size();i++) {
            if (element.equals(this.get(i))) {
                return i;
            }
        }
        return -1;
    }

}
