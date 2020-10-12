import java.io.BufferedReader;
import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.InputMismatchException;


public class Calculator {
    //ArrayLst is a custom implemented arraylist class but with limited properties
    private ArrayLst<String> inputs = new ArrayLst<String>();
    private StringBuilder postfix = new StringBuilder();
    private Stack<String> operators = new Stack<String>();
    private Stack<Double> result = new Stack<Double>();
    private ArrayLst<String> variables = new ArrayLst<String>();
    private ArrayLst<Double> values = new ArrayLst<Double>();
    public Calculator(String path){

    String exp = getInfixExpression(path);
    System.out.println(exp);


    String temp = convertToPostfix(exp);
    System.out.println(temp);
    System.out.println("result of evalpostfix = " +evalPostfix(temp));
    }

    /**
     * This method reads file and  sets required data
     * @param filename
     * @return
     */
    private String getInfixExpression(String filename) {
        String line = "FIrst value";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            while ((line = reader.readLine()) != null)
            {
                if(line != null )
                inputs.add(line); // Taking all lines to the list
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }
        /**
         * Class saves variables and values in a arraylist
         * when one of the variables comes to turn it evaluates its value
         */
        extractValues(); //First rows until empty line is values
        return inputs.get(inputs.size()-1);
    }

    /**
     * This is a helper method to extract variables and
     * their values from string input data
     */
    private void extractValues(){
        int i = 0;
        /**
         * Looks until the empty line
         * Adds all variables to variables list
         * and adds all values of these variables to values list
         */
        while(inputs.get(i).length()>1){
            String[] line = inputs.get(i).split(" ");
             variables.add(line[0]);
             values.add(Double.parseDouble(line[2]));
            ++i;
        }

    }
    /**
     * This method evaluates postifx expression
     */
    private double evalPostfix(String postfix){
        String[] steps = postfix.split(",");
        for(int i = 0; i<steps.length; ++i){

            if(isOperand(steps[i])){ //checks cureent step whether it is operand or not
                if(isNumeric(steps[i])){ //checks wheter its a numeric value or a variable
                    result.push(Double.parseDouble(steps[i])); //if numeric value then adds to the steck
                }else {
                    pushValues(steps[i]); //if it's a variable than calls pushValues method to push real values
                }
                }else if(!isOperand(steps[i])){//if it's not operand then it should be operator
                /**
                 * If it's a operator then calls evalute operators
                 * Some operators such cos,sin and abs are unary operators
                 * And other primitive operators ( + , - , * , /) are binary operators
                 */
                evaluateOperator(steps[i]);
                }
        }

        /**
         * After the evaluation ends, only result stays at result stack
         */
        return result.pop();
    }

    /**
     * This is a helper method that takes variable and
     * finds it's real value and pushes it intu result stack
     * @param step
     */
    private void pushValues(String step) {
        int index  = variables.find(step); //finds index of given variables
            result.push(values.get(index)); //according to that index it pushes the value
    }

    /**
     * This helper method takes operator as parameter
     * And calculates what that operator does
     * cos,sin and abs operators takes only one operand
     * because of this these operators pops only one value from result stack
     * @param step operator type as String object
     */
    private void evaluateOperator(String step) {
        double temp1 = 0;
        double temp2 = 0;
        if(step.equals("+")){
            //Evaluates binary addition operation
            temp1 = result.pop();
            temp2 = result.pop();
            result.push(temp1+temp2);
        }
        if(step.equals("-")){
            //Evaluates binary subtraction operation
            temp1 = result.pop();
            temp2 = result.pop();
            result.push(temp1-temp2);
        }
        if(step.equals("*")){
            //Evaluates binary multiplication operation
            temp1 = result.pop();
            temp2 = result.pop();
            result.push(temp1*temp2);
        }
        if(step.equals("/")){
            //Evaluates binary division operation
            temp1 = result.pop();
            temp2 = result.pop();
            result.push(temp1/temp2);
        }
        if(step.equals("sin(")){
            //Evaluates unary sinus operation
            temp1 = result.pop();
            result.push(sin(temp1));
        }
        if(step.equals("cos(")){
            //Evaluates unary cosinus operation
            temp1 = result.pop();
            result.push(cos(temp1));
        }
        if(step.equals("abs(")){
            //Evaluates unary absolute operation
            temp1 = result.pop();
            result.push(abs(temp1));
        }
    }

    /**
     * This helper method checks given String is
     * whether a numeric value or not
     * @param str expression
     * @return true if numeric false if not
     */
    private boolean isNumeric(String str){
        try{
            double d = Double.parseDouble(str);
            return true;
        }catch (NumberFormatException | NullPointerException nfe)
        {
            return false;
        }

    }

    /**
     * This is a helper method to check whether given string is an operand or not
     * @return boolean value
     */
    private boolean isOperand(String token){
        boolean expression = token.equals("(") || token.equals("+" )|| token.equals("-" ) || token.equals("*" )||
                token.equals("/") || token.equals("sin(" )|| token.equals("cos(" ) || token.equals("abs(" )|| token.equals(")");

        return !expression;
    }

    /**
     * This method takes an infix expression as String
     * And converts it a postfix equation using stack
     * Uses global postfix StringBuilder Object
     * @param infix as String
     * @return is StringBuilder postfix.toString()
     */

    private String convertToPostfix(String infix){
        String[] sepStr = infix.split(" ");//Finding seperated str

        for(int i = 0; i<sepStr.length; ++i){
            if(isOperand(sepStr[i])){ //Checks whether it is a operand
                postfix.append(sepStr[i] + ",");
                
            }else if(!isOperand(sepStr[i])){ // if it's not operand then it is operator
                processOperator(sepStr[i]); //process operator
            }else{
                throw new InvalidParameterException();
            }
        }

        while(!operators.isEmpty()) // If after all there is still in operators stack then it appends to to postfix exp
            postfix.append(operators.pop()+",");

        return postfix.toString();
    }

    /**
     * This method looks given operator
     * It behaves different according to paranthesis or operators
     * If close paranthesis comes than it looks for any
     * sin,cos,abs or open pharantesis and until find it adds expressions to postfix
     *
     * @param str takes operator as String Object
     */
    private void processOperator(String str) {
        int parCounter = 0;
        if (operators.isEmpty() && !str.equals(")")){ // If stack is empty adds operator directly
            operators.push(str);
        }else if(str.equals("(" )|| str.equals("sin(" ) || str.equals("cos(" ) || str.equals("abs(")){
            // If operator is open paranthesis or sin,cos,abs method than it pushes that in to operator stack
            operators.push(str);
            ++parCounter;
        }else if(str.equals(")")){
            // If operator is close paranthesis than it adds stack elements to postfix
            //Until it finds the opening one
            while(!operators.isEmpty() && (!operators.peek().equals("(")&&!operators.peek().equals("cos(")&& !operators.peek().equals("sin(") &&
                    !operators.peek().equals("abs(") ))
                postfix.append(operators.pop()+",");
            if(!operators.isEmpty() && operators.peek().equals("("))
                operators.pop();
            else if(!operators.isEmpty() && (operators.peek().equals("cos(")) || operators.peek().equals("sin(") || operators.peek().equals("abs("))
                postfix.append(operators.pop()+",");
            parCounter--;
           /* while(!operators.isEmpty()&&(!operators.peek().equals("(" )))
                postfix.append(operators.pop()+" ");
            --parCounter;
           /*if(!operators.isEmpty() && parCounter!=0 &&(operators.peek().equals("(") ))
                operators.pop();*/

        }else if(isPrecedenceGreater(operators.peek(),str)){
            //Checks whether next operators presedence is greater than last operator or not
            operators.push(str);
        }else {
            while(!operators.isEmpty()&&!isPrecedenceGreater(operators.peek(),str)){
                //If next operator is equal or lower than operators stack peek then it adds
                //until top presedence operator becomes at top
                postfix.append(operators.pop()+",");
            }
            operators.push(str);
        }
    }

    /**
     * This helper method gives operators to values
     * According to theis presidence
     * +, - are 1 and *,/ are 2
     * if next(upcoming) operator has greate presidence then older one
     * method returns true else false
     * @param first current operator as String object
     * @param next  upcoming operator as String object
     * @return true if next > current, false if it's not
     */
    private boolean isPrecedenceGreater(String first,String next){
        int f = 0;
        int n = 0;
        //Giving value to first operator
        switch (first) {
            case "+" :
                f = 1;
                break;

            case "-" :
                f = 1;
                break;

            case "/" :
                f = 2;
                break;
            case "*" :
                f = 2;
                break;

        }
        //assigning value to second operator
        switch (next) {
            case "+" :
                n = 1;
                break;

            case "-" :
                n = 1;
                break;

            case "/" :
                n = 2;
                break;
            case "*" :
                n = 2;
                break;


        }
        boolean expression = n>f;
        return n>f; //checks whether upcoming is big or not
    }

    /**
     * This helper method calculates a given numbers given power
     * @param num base number as double
     * @param pow degree as integer
     * @return returns result as double
     */
    private double pow (double num, int pow){
        int i = 1;
        double power = num;
        while(i < pow){
            ++i;
            power = power*num ;
        }
        return power;
    }

    /**
     * This helper method calculates factorial of given number
     * @param number as integer
     * @return  returns result as double
     */
    private double fac(int number){
        int i = 1;
        int result = 1;
        if(number == 0 || number == 1)
            return 1.0;
        if(number < 0)
            throw new InputMismatchException();
        while(i<=number){
            result = result*i;
            ++i;

        }
        return Double.valueOf(result);
    }

    /**
     * This method calculates sinus of given degree
     * Using Taylor - Mc. lauren series
     * @param val degree as double
     * @return result of calculation as double
     */
    private double sin(double val){

        double x = val%360;//Transforms for degrees that over 360
        if(x>180)
            x = 180-x;//takes that degrees negative if it's bigger than 180
        x = x/180.0;  // transforms degree to radian because Taylar series calculates according to radian
        x = x*3.14; // multiplies with Pi to transform radian (sensivity is : 2)
        //Calculates according to Taylor - Mc Lauren Series with 4 element sensivity
        return x - (pow(x,3)/fac(3)) + (pow(x,5)/fac(5)) - (pow(x,7)/fac(7));
    }

    /**
     * This method calculates cosinus of given degree
     * Using Taylor - Mc. lauren series
     * @param val degree as double
     * @return result of calculation as double
     */
    private double cos(double val){

        double x = val%360;//Transforms for degrees that over 360
        if(x>180)
            x = 180-x;//takes that degrees negative if it's bigger than 180
        x = x/180.0; // transforms degree to radian because Taylar series calculates according to radian
        x = x*3.14;// multiplies with Pi to transform radian (sensivity is : 2)
        //Calculates according to Taylor - Mc Lauren Series with 4 element sensivity
        double result = 1 - (pow(x,2)/fac(2)) + (pow(x,4)/fac(4)) - (pow(x,6)/fac(6));
        if(val%360 > 180) // if degree is bigger than 180 than result is negative of calculated value
            result = result*-1;
        return result;
    }

    /**
     * Absolute method looks the given value
     * If it is lower than 0 than it multiplies with -1
     * else it returns directly value
     * @param val value as double
     * @return as double
     */
    private double abs(double val){
        if(val<0)
            return val*(-1);
        else
            return val;
    }
}
