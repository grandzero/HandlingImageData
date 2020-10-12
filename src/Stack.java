import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack<E> {
    /**
     * This generic class uses primitive array as data
     * If array is full then it reallocates new space for array
     * This class is a Stack implementation using array
     * To follow the top of array there is a topOfStack variable
     * And capacity is 100 before reallocating
     */

    private E[] theData; //Primitive data object array
    private int topOfStack = -1; // index of top of stack - last inserted element since Stack is LIFO
    private int capacity = 100; // starting capacity

    /**
     * This method returns the size of Stack
     * Using top of stack index
     * @return topOfStack start from -1 so size is topOfStack+1
     */
    public int getSize (){return topOfStack+1;}

    /**
     * Public Constructor initializes the data array
     */
    @SuppressWarnings("unchecked")
    public Stack(){
        theData = (E[])new Object[capacity];
    }

    /**
     * Method checks whether stack is empty or not
     * @return boolean value
     */
    public boolean isEmpty(){ return topOfStack==-1;}

    /**
     * This method returns top element of stack w/o deleting it
     * @return top data
     */
    public E peek(){
        return theData[topOfStack];
    }

    /**
     * This methods removes top element of stack and
     * returns it
     * @return top data
     */
    public E pop(){
        if (isEmpty())
            throw new EmptyStackException();
        else
            return theData[topOfStack--];
    }

    /**
     * Adds element to stack
     * if capaicty is full than reallocates and adds element
     * @param obj Element that will add to stack
     * @return returns given elements
     */
    public E push(E obj){
        if(topOfStack==theData.length-1)
            reallocate();
        ++topOfStack;
        theData[topOfStack] = obj;
        return obj;
    }

    /**
     * This method reallocates new space for data array
     * Doubles capacity
     */
    private void reallocate(){
        capacity = capacity*2;
        theData = Arrays.copyOf(theData,capacity);
    }
}


