/*
 * Cell is a unit of the spreadsheet so that is handles all the changes to the cell values according to the location. 
 */
public class CellFormula extends Cell
{ 
    public CellFormula(int r, int c) 
    {   
        super(r, c);
    }
    public String setCellIndividualFormula(String input) throws NumberFormatException, StringIndexOutOfBoundsException
    {
        input = input.substring(2, input.length()-1); //gets rid of partentheses and spaces
        String[] inputArray = input.split(" ");
        if (numOfOperation(input) == 0)
        {
            System.out.println("Invalid Input: Not a valid Formula. Doesnt have enough spaces or only one operation.");
            return "<empty>";
        }
        try 
        {
            String nextString = input.substring(0, input.indexOf(" "));
            double finalValue = finalValue = Double.parseDouble(input.substring(0, input.indexOf(" ")));
            while(input.length() > 1)
            {
                char operator = input.charAt(input.indexOf(" ") + 1);
                input = input.substring(input.indexOf(operator)+2);
                double nextValue = Double.parseDouble(input.substring(0, input.indexOf(" ")));
                input = input.substring(input.indexOf(" "));
                if(operator == '+')
                {
                    finalValue += nextValue;
                }
                else if (operator == '-' && input.charAt(input.indexOf(operator) + 1) == ' ')
                {
                    finalValue -= nextValue;
                }
                else if (operator == '*')
                {
                    finalValue *= nextValue;
                }
                else if (operator == '/')
                {
                    finalValue /= nextValue;
                }
                else
                {
                    System.out.println("Invalid Input: Invalid operation"); //not a valid operator or forgot an operator
                    return "<empty>";
                }
            }
            String finalValueString = "" + finalValue;
            return finalValueString;
        }
        catch(NumberFormatException err)
        {
            System.out.println("Invalid Input: Not every value is a valid number");
            return "<empty>";
        }
        catch(StringIndexOutOfBoundsException err)
        {
            System.out.println("Invalid Input");
            return "<empty>";
        }
    }
    private int numOfOperation(String input)
    {
        int toReturn = 0; 
        for (int i = 0; i < input.length(); i++)
        {
            if(input.charAt(i) == '+' || input.charAt(i) == '/' || input.charAt(i) == '*' || (input.indexOf('-') != -1 && input.charAt(input.indexOf('-') + 1) == ' '))//gets rid of posibility of negatives
            {
                toReturn++;
            }
        }
        return toReturn;
    }
}