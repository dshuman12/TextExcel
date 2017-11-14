/*
 * Cell is a unit of the spreadsheet so that is handles all the changes to the cell values according to the location. 
 */
public class Cell
{ 
    private String printLocation;
    private int[] locationArray;
    private String printValue;  
    private double doubleValue; //stays null until parsed
    private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private String inputValue;
    public Cell(int r, int c) 
    {   
        printLocation = Character.toString(alphabet[c]) + (r+1);
        locationArray = new int[2];
        locationArray[0] = r;
        locationArray[1] = c;
        printValue = "<empty>"; 
        inputValue = "<empty>";
    }
    public void setPrintLocation(String location)
    {
        printLocation = location;
    } 
    public String getLocationString()
    {
        return printLocation;
    }
    public double getDoubleValue()
    {
        return doubleValue;
    }
    public String getValue()
    {
        return printValue;
    }
    public char[] getAlphabet()
    {
        return alphabet;
    }
    public String getInputValue()
    {
        return inputValue;
    }
    public void setValue(double input)
    {
        doubleValue = input; 
    }
    public void setValue(String input)
    {
        printValue = input;
    }
    public void setInput(String input)
    {
        inputValue = input;
    }
    public void setCellIndividual(String input) throws NumberFormatException
    {
        inputValue = input;
        try{
            doubleValue = Double.parseDouble(input);
            printValue = input;
        }
        catch (NumberFormatException error)
        {
            printValue = input;
            if (input.charAt(0) == '\"')
                printValue = input.substring(1, input.length());
            if (input.charAt(input.length()-1) == '\"')
                printValue = printValue.substring(0, printValue.length()-1);
            inputValue = "\"" + printValue + "\"";
        }
    }
    public void clear()
    {
        printValue = "<empty>";
        inputValue = "<empty>";
        doubleValue = 0.0;
    }
}