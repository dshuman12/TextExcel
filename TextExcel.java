/* 
 * TextExcel runs all the methods for the user. It asks for user input and delegates the tasks based on that. And it prints what the user wants based on what it has delegated. 
 */ 
import java.util.*;
import java.lang.Character;
import java.io.*;
public class TextExcel
{   
    public static void main(String[] args) throws ArrayIndexOutOfBoundsException, StringIndexOutOfBoundsException
    {
        Scanner input = new Scanner(System.in); 
        System.out.println("Welcome to TextExcel!");
        System.out.print("\nEnter a number of rows (less than or equal to 99): ");
        int rows = input.nextInt();
        while(rows > 99 || rows <= 0)
        {
            System.out.println("Invalid Input");
            System.out.print("\nEnter a number of rows (less than 99): ");
            rows = input.nextInt();
        }
        input.nextLine();
        System.out.print("\nEnter a number of columns (less than or equal to 26): ");
        int columns = input.nextInt();
        while (columns > 26 || columns <= 0)
        {
            System.out.println("Invalid Input");
            System.out.print("\nEnter a number of columns (less than or equal to 26): ");
            columns = input.nextInt();
        }
        input.nextLine();
        SpreadSheet spreadsheet = new SpreadSheet(rows, columns); //first value cant be greater than 26
        System.out.print("\nEnter a Command: ");
        String command = input.nextLine();
        while(!command.equals("exit")) //asks for a command until exit is called
        { 
            if (command.equals("help"))
            { 
                System.out.println("\nprint - displays the entire spreadsheet with updated contents" + 
                                   "\nexit - exits the program" + 
                                   "\nExample: A1 = 10 - sets the cell A1 to 10" +  
                                   "\nExample: A1 - prints the value of A1, which is 10" +
                                   "\nclear - clears all the cells in the spreadsheet" + 
                                   "\nExample: clear A1 - clears cell A1 only\n" +
                                   "\nExample: A1 = ( 3 + 2 ) - sets A1 to 5; all formulas in parenthesis seperated by spaces" + 
                                   "\nsum - adds all the values of the cells between and including the inputs. Example: A1 = ( sum A2 - A3 )" + 
                                   "\navg - takes the avg of all the values of the cells between and including the inputs. Example: A1 = ( avg A2 - A4 )" );
            }
            else if (command.equals("print"))
            {
                System.out.println(); 
                spreadsheet.print(); 
            }
            else if (command.contains("(") && command.contains(")") && command.contains("="))
            {
                try
                {
                    if (command.contains(" sum "))
                        spreadsheet.setSumCell(command);
                    else if (command.contains(" avg "))
                        spreadsheet.setAvgCell(command);
                    else
                        spreadsheet.setFormulaCell(command);
                }
                catch (StringIndexOutOfBoundsException err)
                {
                    System.out.println("Please have spaces between each character or include an operation");
                }
                catch (ArrayIndexOutOfBoundsException err)
                {
                    System.out.println("Please have spaces between each character or include an operation");
                }
            }
            else if ((command.indexOf("=") == 3 || command.indexOf("=") == 4) && spreadsheet.search(command.charAt(0)) != -1 && (command.charAt(1)-48 <= rows || (command.charAt(1)-48)*10 + (command.charAt(2)-48) <= rows))//contains an =
            {
                try 
                {
                    spreadsheet.setCell(command);
                }
                catch(ArrayIndexOutOfBoundsException err)
                {
                      System.out.println("Invalid Input");
                } 
            }
            else if (command.contains("clear"))
            {
                try { //checks for valid number in location
                    if (containsAnyValue(command.substring(command.indexOf("r")+1), spreadsheet.getAlphabet())) //if contains a valid letter
                        spreadsheet.clearIndividual(command.substring(command.indexOf('r') + 2));
                    else
                        spreadsheet.clear();
                }
                catch(ArrayIndexOutOfBoundsException err)
                {
                  System.out.println("Invalid Input");
                }  
            }
            else if (command.indexOf("=") == -1 && spreadsheet.search(command.charAt(0)) != -1) //doesnt contain an equals sign, just looking for value
            {
                try {
                    System.out.println(spreadsheet.getCellValue(command));
                }
                catch(ArrayIndexOutOfBoundsException err)
                {
                    System.out.println("Invalid Input");
                }
            }
            
            else if (!command.equals("exit"))
            {
                System.out.println("Invalid Input");
            }
            System.out.print("Enter a Command: ");
            command = input.nextLine();
        }
        System.out.println("\nGoodbye!");
    }
    
    public static boolean containsAnyValue(String str, char[] array) //used to see if contains any value
    {
        char[] strArray = str.toCharArray();
        for (int i = 0; i < array.length; i++)
        {
            for(int k = 0; k < strArray.length; k++)
            {
                strArray[k] = Character.toUpperCase(strArray[k]);
                if (strArray[k] == array[i])
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static int numOfSpaces(String input)
    {
        int toReturn = 0; 
        for (int i = 0; i < input.length(); i++)
        {
            if (input.charAt(i) == ' ')
                toReturn++;
        }
        return toReturn;
    }
     
}