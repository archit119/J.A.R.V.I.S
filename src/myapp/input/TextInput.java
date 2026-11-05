package myapp.input;

import java.util.Scanner;

public class TextInput {
    @SuppressWarnings("resource")
    public String GetInput()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter prompt:");

        String userInput = input.nextLine();
        return userInput;
    }
}
