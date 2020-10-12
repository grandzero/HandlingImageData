import java.awt.desktop.SystemEventListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class ComponentFinder {

    private ArrayLst<String> records = new ArrayLst<String>();
    private int size = 0;
    private int width = 0;
    private int[] input;
    private boolean[] visitedIndexes;
    private int componentCounter = 0;

    /**
     * Public constructor gets a filename and
     * calculates components and assigns all values
     * @param filename as String
     */
    public ComponentFinder(String filename){

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);

            }
            reader.close();
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }
        /**
         * Gettting total number of inputs.
         */
        String nLine = records.get(0);
        size = (nLine.length()/2) * records.size();
        /**
         * Adding all inputs to the integer array
         * This part is building the int array
         * that includes 0 and 1 values
         */
        nLine = records.get(0);
        width = (nLine.length())/2; // number of columns
        input = new int[size]; // initalizing array size
        //visited array has an important role to find number of components
        visitedIndexes = new boolean[size]; //visited array is for all visited 1's at array
        int index = 0;
        for(int i = 0; i<records.size(); ++i){
            nLine = records.get(i);
            for(int j = 0; j<nLine.length(); j++){
                if(nLine.charAt(j)=='0') {
                    input[index] = 0; // If current input of string is 0 than adds that index at int array 0
                    ++index;
                }else if(nLine.charAt(j)=='1'){
                    input[index] = 1; // If current input of string is 1 than adds that index at int array 0
                    ++index;
                }else if(nLine.charAt(j) !=' ')
                    throw new InputMismatchException();
            }
        }

        /**
         * Travesing all inputs while finding components
         */
        for(int i = 0; i<size ; ++i){
            if(input[i]==1 && !visitedIndexes[i]){
                visitedIndexes[i] = true; // All visited indexes signs as visited
                findComponent(i); // this method finds components and increases global component counter
            }
        }



    }

    /**
     * This method is for test purposes
     */
    public void printIntArray(){

        for(int i = 0 ; i<records.size(); ++i) {
            for (int j = 0; j < width; ++j)
                System.out.print(input[(i*width) + j]);
            System.out.print("\n");
        }

    }

    /**
     * This method finds all components using stack
     * Adds all components items indexses to visited
     * And updates the component counter
     * @param id index of 1 as integer
     */
    private void findComponent(int id){
        Stack<Integer> indexStack = new Stack<Integer>();
        int index = id; // id
        indexStack.push(index);

        while(!indexStack.isEmpty()){
           input[index] = componentCounter+1;
            index = indexStack.peek();
            visitedIndexes[index] = true;
            int expCounter = 0 ;
            if(index - width < 0 || input[index - width] == 0 || visitedIndexes[index - width])
                expCounter++;// chekcs up of given index if it's exist and value is 1 than adds to stack
            if((index + 1) % width == 0 || index + 1 == size || input[index + 1] == 0 || visitedIndexes[index + 1])
                expCounter++;// chekcs up of given index if it's exist and value is 1 than adds to stack
            if(index + width > size || input[index + width] == 0 || visitedIndexes[index + width])
                expCounter++;// chekcs up of given index if it's exist and value is 1 than adds to stack
            if(index - 1 < 0 || index % width == 0 || input[index - 1] == 0 || visitedIndexes[index - 1])
                expCounter++;// chekcs up of given index if it's exist and value is 1 than adds to stack
             if(expCounter == 4)
                 indexStack.pop();
             else {
                 /**
                  * This part checks all neighbors iff all of them are 0 or visited then pops that index from stack
                  */
                 if (index - width >= 0 && input[index - width] == 1 && !visitedIndexes[index - width]){
                     indexStack.push(index - width);
                 }
                 if ((index + 1) % width != 0 && index + 1 != size && input[index + 1] == 1 && !visitedIndexes[index + 1]){
                     indexStack.push(index + 1);
                 }
                 if (index + width < size && input[index + width] == 1 && !visitedIndexes[index + width]){
                     indexStack.push(index + width);
                 }

                 if (index - 1 >= 0 && index % width != 0 && input[index - 1] == 1 && !visitedIndexes[index - 1]) {
                     indexStack.push(index - 1);
                 }
             }
        }
        ++componentCounter;


    }

    /**
     * This method returns calculated component size
     * @return integer value of components
     */
    public int getComponentCounter(){return componentCounter;}

}
