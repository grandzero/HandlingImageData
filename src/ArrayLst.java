import java.util.Arrays;

public class ArrayLst<E> {
    /**
     * This is a helper class
     * This class implemented using primitive Array
     * This class is a limited custom implemented stack-like Arraylist
     */
    private E[] theData;
    private int capacity = 100;
    private int topOfList = -1;

    /**
     * Returns the size of list
     * @return int value of size
     */
    public int size (){return topOfList+1;}

    /**
     * Public Constructor initializes data array
     */
    @SuppressWarnings("unchecked")
    public ArrayLst(){
        theData = (E[])new Object[capacity];
    }

    /**
     * Add method with one parameter
     * Adds item end of the list
     * @param item data will be added
     * @return whether item is added succesfully or not
     */
    public boolean add(E item) {
        /**
         * If item is added returns true else not
         * If capacity is full than reallocates new space
         */
        try{
        if(topOfList==theData.length-1)
        reallocate();
        ++topOfList;
        theData[topOfList] = item;
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * This helper method reallocates new space to array
     * Doubles capacity
     */
    private void reallocate(){
        capacity = capacity*2;
        theData = Arrays.copyOf(theData,capacity);
    }

    /**
     * This helper method returns element at given index
     * @param index index of element
     * @return  returns the given indexed data
     */
    public E get(int index){
        if(index < topOfList+1 && index > -1)
            return theData[index];
        else
            throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * This helper methods finds the index of given data
     * To work with this method properly
     * Data objects equals method should be implemented
     * @param item compared item
     * @return  index of item
     */
    public int find(E item){

        if(topOfList == -1)
            return -1;
        if(topOfList == 0 && item.equals(theData[0]))
            return 0;
        for(int i = 0 ; i <= topOfList; ++i){
            if(item.equals(theData[i]))
                return i;
        }
        return -1;
    }


}
