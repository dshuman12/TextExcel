/*
 * SpreadSheet class compiles all the Cell values in one spreadsheet for the user to use and keeps track of the Cell values as they apply to the location. 
 */
import java.lang.Character;
import java.lang.String;
public class SpreadSheet
{
    private Cell[][] spreadsheet;
    private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private int rows; //numbers
    private int columns; //alphabet
    public SpreadSheet(int rows, int columns) //constructor makes a 10 by 7 2D array to hold all the Cells in. Creates new cells for each. 
    {
        this.rows = rows;
        this.columns = columns;
        spreadsheet = new Cell[rows][columns];
        for (int r = 0; r < spreadsheet.length; r++)
        { 
            for (int c = 0; c < spreadsheet[r].length; c++)
            {
                spreadsheet[r][c] = new Cell(r, c);
            } 
        }
    }
    public char[] getAlphabet() //alphabet is used for labeling the columns of the spreadhseet when printing. 
    { 
        return alphabet;
    }
    public void print() //print runs through the 2D array and prints, in user friendly format, all the cell values in their location. 
    {
        System.out.print("            |");
        for (int r = 0; r < columns; r++) //prints out the alphabet labels
        {
            System.out.print("     " + alphabet[r]+"      |");
        }
        System.out.println(); 
        for (int r = 0; r < rows; r++) 
        {
            System.out.println(printLine());//"------------+------------+------------+------------+------------+------------+------------+------------+");
            if (r >= 9)
                System.out.print("     " + (r+1)+ "     |");
            else 
                System.out.print("     " + (r+1) + "      |");
            for (int c = 0; c < spreadsheet[r].length; c++)
            {
                Cell cell = spreadsheet[r][c];
                String value = cell.getValue();
                System.out.print(printCell(cell.getValue()));
            }
            System.out.println(); 
        }
        System.out.println(printLine());//"------------+------------+------------+------------+------------+------------+------------+------------+");
        System.out.println();
    }
    private String printCell(String value) //method is used to print the write value for each cell depending on different cases. Handles all of those cases. 
    {
        if (value.length() > 12) 
            return value.substring(0, 11) + ">|";
        if (value.equals("<empty>")) 
            return "            |";
        String toReturn = value;
        for (int i = 0; i < (12-value.length()); i++)
        {
            toReturn += " ";
        }
        return toReturn + "|";
    }
    public void setCell(String input) 
    {
        int index = input.indexOf("=");
        char[] location = input.substring(0, index-1).toCharArray(); //gets a letter and a number
        int letterLocation = search(location[0]); //makes sure within dimensions
        if (letterLocation == -1 || input.substring(input.indexOf('=')+1).length() == 0)// if not a valid letter
        {
            System.out.println("Invalid Input");
        }
        else
        {
            if (location.length > 2 && location[2] != ' ') //if the number is 10 or greater
            {
                int column = (location[1]-48)*10 + location[2]-48;
                Cell toChange = spreadsheet[column-1][letterLocation]; //offset char by the ascii 49 because 1 char is 49 number
                toChange.setCellIndividual(input.substring(index+2));//returns a cellLocation and sets cell using that location in the spreadsheet
                toChange.setPrintLocation(input.substring(0, index-1));
            }
            else
            {
                Cell toChange = spreadsheet[location[1]-49][letterLocation]; //offset char by the ascii 49 because 1 char is 49 number
                toChange.setCellIndividual(input.substring(index+2));//returns a cellLocation and sets cell using that location in the spreadsheet
                toChange.setPrintLocation(input.substring(0, index-1));
            }
        }
    }
    public void setFormulaCell(String input)
    {
        int index = input.indexOf("=");
        char[] location = input.substring(0, index-1).toCharArray(); //gets a letter and a number
        int letterLocation = search(location[0]); //makes sure within dimensions
        String actualInput = input;
        if (letterLocation == -1) // if not a valid letter
        {
            System.out.println("Invalid Input: invalid letter input");
        }
        else
        {
            if (replace(input.substring(input.indexOf("(")+2, input.indexOf(")")-1)).contains("Invalid Input"))
            {
                System.out.println("Invalid Input");
                return;
            }
            input = input.substring(0, input.indexOf("(")+1) + replace(input.substring(input.indexOf("(")+2, input.indexOf(")")-1)) + ")";//gets to actual formula and includes 
            if (location.length > 2 && location[2] != ' ') //if the number is 10 or greater
            {
                int column = (location[1]-48)*10 + location[2]-48;//offset char by the ascii 49 because 1 char is 49 number
                Cell toSet = spreadsheet[column-1][letterLocation];
                CellFormula toChange = new CellFormula(column-1, letterLocation);
                toSet.setCellIndividual(toChange.setCellIndividualFormula(input.substring(index+2)));
                toSet.setPrintLocation(input.substring(0, index-1));
                toSet.setInput(actualInput.substring(index+2));
            }
            else
            {
                Cell toSet = spreadsheet[location[1]-49][letterLocation];
                CellFormula toChange = new CellFormula(location[1]-49, letterLocation);
                toSet.setCellIndividual(toChange.setCellIndividualFormula(input.substring(index+2)));
                toSet.setPrintLocation(input.substring(0, index-1));
                toSet.setInput(actualInput.substring(index+2));
            }
        }
    }
    public int search(char letter)
    {
        letter = Character.toUpperCase(letter);
        for (int i = 0; i < columns; i++)
        {
            if (alphabet[i] == letter)
            {
                return i; 
            }
        }
        return -1;
    } 
    private String replace(String input) throws NumberFormatException //replaces all cell locations with cell values
    {
        String toReturn = " ";
        String[] inputArray = input.split(" ");
        for (int i = 0; i < inputArray.length; i++)
        {
            double finalValue = 0;
            String nextString = inputArray[i];
            try{
                if (canParseDouble(nextString) || isOperation(nextString))//valid number
                {
                    toReturn += nextString + " ";
                }
                else if (!getCellValue(nextString).contains("Invalid Input") && !getCellValue(nextString).contains(" = <empty>") && canParseDouble(getDoubleValue(nextString).substring(getDoubleValue(nextString).indexOf("=")+1))) //if letter location is valid and if double
                {
                    finalValue = Double.parseDouble(getDoubleValue(nextString).substring(getDoubleValue(nextString).indexOf("=")+1));
                    toReturn += finalValue + " ";
                }
                else //not valid at all
                    throw new NumberFormatException();
            }
            catch(NumberFormatException err)
            {
                return "Invalid Input";
            }
        }
        return toReturn;
    }
    public void setSumCell(String input)
    {
        String actualInput = input;
        int index = input.indexOf("sum")+3; //index of space after sum
        int letterLocationOne = 0;
        int rowLocationOne = 0;
        int letterLocationTwo = 0;
        int rowLocationTwo = 0;
        //first location and second location
        for (int i = 0; i < 2; i++)
        {
            input = input.substring(index+1);
            char[] location = input.substring(0, input.indexOf(" ")).toCharArray(); //gets a letter and a number
            int letterLocation = search(location[0]); //makes sure within dimensions
            if (letterLocation == -1 || !canParseDouble("" + location[1]) || location[1]-48 > rows || (location.length > 2 && !canParseDouble("" + location[1] + location[2]) && Double.parseDouble("" + location[1] + location[2]) > rows)) // if not a valid letter
            {    
                System.out.println("Invalid Input: Not valid location");
                actualInput = "<empty>";
                return;
            }
            else if (i == 0)
            {
                letterLocationOne = letterLocation;
                if (location.length > 2 && location[2] != ' ') //if the number is 10 or greater
                    rowLocationOne = ((location[1]-48)*10 + location[2]-48)-1;
                else
                    rowLocationOne = location[1]-48-1;
            }
            else
            {
                letterLocationTwo = letterLocation; 
                if (location.length > 2 && location[2] != ' ') //if the number is 10 or greater
                    rowLocationTwo = ((location[1]-48)*10 + location[2]-48)-1;
                else
                    rowLocationTwo = location[1]-48-1;
            }
            index = input.indexOf("-")+1;
        }
        
        if (rowLocationOne > rowLocationTwo || letterLocationOne > letterLocationTwo || (rowLocationOne == rowLocationTwo && letterLocationOne == letterLocationTwo))
        {
           System.out.println("Invalid Input: Invalid range");
           return;
        }
        double toReturn = 0;
        for (int r = rowLocationOne; r <= rowLocationTwo; r++) {
            for (int c = letterLocationOne; c <= letterLocationTwo; c++)
            {
                Cell toChange = spreadsheet[r][c];
                String value = getCellValue("" + alphabet[c] + (r+1));
                value = value.substring(value.indexOf("=") + 1);
                if(canParseDouble(value))
                    toReturn += Double.parseDouble(value);
                else
                {
                    System.out.println("Invalid Input: Not double input");
                    actualInput = "<empty>";
                    return; 
                }      
            }
        }
        
        index = actualInput.indexOf("=");
        char[] location = actualInput.substring(0, index-1).toCharArray(); //gets a letter and a number
        int letterLocation = search(location[0]); //makes sure within dimensions
        if (letterLocation == -1 || !canParseDouble("" + location[1]) || location[1]-48 > rows || (location.length > 2 && (!canParseDouble("" + actualInput.substring(0, index-1)) || Double.parseDouble("" + actualInput.substring(0, index-1)) > rows)))// if not a valid letter
        {
            System.out.println("Invalid Input: Not valid location");
            actualInput = "<empty>";
            return;
        }
        else
        {
            if (location.length > 2 && location[2] != ' ') //if the number is 10 or greater
            {
                int row = (location[1]-48)*10 + location[2]-48;
                Cell toChange = spreadsheet[row-1][letterLocation]; //offset char by the ascii 49 because 1 char is 49 number
                toChange.setCellIndividual("" + toReturn);//returns a cellLocation and sets cell using that location in the spreadsheet
                toChange.setPrintLocation(actualInput.substring(0, index-1));
                toChange.setInput(actualInput.substring(actualInput.indexOf("=")+2));
            }
            else
            {
                Cell toChange = spreadsheet[location[1]-49][letterLocation]; //offset char by the ascii 49 because 1 char is 49 number
                toChange.setCellIndividual("" + toReturn);//returns a cellLocation and sets cell using that location in the spreadsheet
                toChange.setPrintLocation(actualInput.substring(0, index-1));
                toChange.setInput(actualInput.substring(actualInput.indexOf("=")+2));
            }
        }
    }
    public void setAvgCell(String input)
    {
        String actualInput = input;
        int index = input.indexOf("avg")+3; //index of space after avg
        int letterLocationOne = 0;
        int rowLocationOne = 0;
        int letterLocationTwo = 0;
        int rowLocationTwo = 0;
        //first location and second location
        for (int i = 0; i < 2; i++)
        {
            input = input.substring(index+1);
            char[] location = input.substring(0, input.indexOf(" ")).toCharArray(); //gets a letter and a number
            int letterLocation = search(location[0]); //makes sure within dimensions
            if (letterLocation == -1 || !canParseDouble("" + location[1]) || location[1]-48 > rows || (location.length > 2 && !canParseDouble("" + location[1] + location[2]) && Double.parseDouble("" + location[1] + location[2]) > rows)) // if not a valid letter
            {    
                System.out.println("Invalid Input: Not valid location");
                actualInput = "<empty>";
                return;
            }
            else if (i == 0)
            {
                letterLocationOne = letterLocation;
                rowLocationOne = location[1]-48-1;
            }
            else
            {
                letterLocationTwo = letterLocation; 
                rowLocationTwo = location[1]-48-1;
            }
            index = input.indexOf("-")+1;
        }
        
        if (rowLocationOne > rowLocationTwo || letterLocationOne > letterLocationTwo || (rowLocationOne == rowLocationTwo && letterLocationOne == letterLocationTwo))
        {
           System.out.println("Invalid Input: Invalid range");
           return;
        }
        double toReturn = 0;
        int count = 0;
        for (int r = rowLocationOne; r <= rowLocationTwo; r++) {
            for (int c = letterLocationOne; c <= letterLocationTwo; c++)
            {
                Cell toChange = spreadsheet[r][c];
                String value = getCellValue("" + alphabet[c] + (r+1));
                value = value.substring(value.indexOf("=") + 1);
                if(canParseDouble(value))
                {
                    toReturn += Double.parseDouble(value);
                    count++;
                }
                else
                {
                    System.out.println("Invalid Input: Not double value");
                    actualInput = "<empty>";
                    return; 
                }      
            }
        }
        toReturn /= count;
        
        index = actualInput.indexOf("=");
        char[] location = actualInput.substring(0, index-1).toCharArray(); //gets a letter and a number
        int letterLocation = search(location[0]); //makes sure within dimensions
        if (letterLocation == -1 || !canParseDouble("" + location[1]) || location[1]-48 > rows || (location.length > 2 && (!canParseDouble("" + actualInput.substring(0, index-1)) || Double.parseDouble("" + actualInput.substring(0, index-1)) > rows)))// if not a valid letter
        {
            System.out.println("Invalid Input: Not valid location");
            actualInput = "<empty>";
            return;
        }
        else
        {
            if (location.length > 2 && location[2] != ' ') //if the number is 10 or greater
            {
                int row = (location[1]-48)*10 + location[2]-48;
                Cell toChange = spreadsheet[row-1][letterLocation]; //offset char by the ascii 49 because 1 char is 49 number
                toChange.setCellIndividual("" + toReturn);//returns a cellLocation and sets cell using that location in the spreadsheet
                toChange.setPrintLocation(actualInput.substring(0, index-1));
                toChange.setInput(actualInput.substring(actualInput.indexOf("=")+2));
            }
            else
            {
                Cell toChange = spreadsheet[location[1]-49][letterLocation]; //offset char by the ascii 49 because 1 char is 49 number
                toChange.setCellIndividual("" + toReturn);//returns a cellLocation and sets cell using that location in the spreadsheet
                toChange.setPrintLocation(actualInput.substring(0, index-1));
                toChange.setInput(actualInput.substring(actualInput.indexOf("=")+2));
            }
        }
    }
    private boolean isOperation(String input)
    {
        if (input.length() < 3 && input.contains("+") || input.contains("-") || input.contains("*") || input.contains("/"))
        {
            return true;
        }
        return false;
    }
    private boolean canParseDouble(String input)
    {
        try
        {
            Double.parseDouble(input);
        }
        catch(NumberFormatException err)
        {
            return false;
        }
        return true;
    }
    
    public String getCellValue(String input) throws NumberFormatException
    { 
        try{ //checks if not a cell location available
            int row = Integer.parseInt(input.substring(1));
            if (search(input.charAt(0)) == -1)
                return "Invalid Input";
            Cell toPrint = spreadsheet[row-1][search(input.charAt(0))];
            return toPrint.getLocationString() + " = " + toPrint.getInputValue();
        }
        catch(NumberFormatException err)
        {
            return "Invalid input";
        }
    }  
    public String getDoubleValue(String input)
    {
        try{ //checks if not a cell location available
            int row = Integer.parseInt(input.substring(1));
            if (search(input.charAt(0)) == -1)
                return "Invalid Input";
            Cell toPrint = spreadsheet[row-1][search(input.charAt(0))];
            return toPrint.getLocationString() + " = " + toPrint.getDoubleValue();
        }
        catch(NumberFormatException err)
        {
            return "Invalid Input";
        }
    }
    
    public void clearIndividual(String input)
    {
        int column = input.charAt(1)-48; //char to integer, ascii
        char letter = input.charAt(0); //get letter
        if (column == 1 && input.length() > 2 && input.charAt(2)-48 == 0) //ten
            spreadsheet[9][search(letter)].clear();
        else if (input.length() == 2)
            spreadsheet[column-1][search(letter)].clear();
        else
            System.out.println("Invalid Input");
    }
    
    public void clear()
    {
        for (int r = 0; r < spreadsheet.length; r++)
        {
            for (int c = 0; c < spreadsheet[r].length; c++)
            {
                clearIndividual("" + alphabet[c] + (r+1));
            }
        }
    }
    
    public String printLine()
    {
        String toReturn = "";
        for(int i = 0; i < columns+1; i++)
        {
            toReturn += "------------+";
        }
        return toReturn;
    }
    
    public Cell findCell(String location)
    {
        int row = location.charAt(1)-48; //gets number
        int column = search(location.charAt(0)); //gets number value of letter
        if (location.length() > 2 && location.charAt(2) != ' ') //if the number is 10 or greater
        {
            row = (location.charAt(1)-48)*10 + location.charAt(2)-48;
            return spreadsheet[row-1][column]; //offset char by the ascii 49 because 1 char is 49 number
        }
        return spreadsheet[row-1][column]; //offset char by the ascii 49 because 1 char is 49 number
    }
    
    public boolean validFormula(String input) throws NumberFormatException //include parenthesis and = and location
    {
        String[] inputArray = input.split(" ");
        if (getCellValue(inputArray[0]).equals("Invalid Input") || !inputArray[1].equals("=") || !inputArray[2].equals("(") || !inputArray[inputArray.length-1].equals(")"))
            return false;
        String[] formulaArray = input.substring(input.indexOf("(")).split(" ");
        for (int i = 1; i < formulaArray.length; i++)
        {
            if(i%2 == 0 && input.charAt(i) != '+' && input.charAt(i) != '/' && input.charAt(i) != '*' && (input.indexOf('-') == -1 || input.charAt(input.indexOf('-') + 1) != ' '))//gets rid of posibility of negatives
                return false;
            if (i%2 == 1)
            {
                try
                {
                    Double.parseDouble(formulaArray[i]);
                }
                catch (NumberFormatException err)
                {
                    return false;
                }
            }            
        }
        return true;
    }
}