package myapp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberParser {
    private static final Map<String, Integer> NUMBER_MAP = new HashMap<>();

    static {
        NUMBER_MAP.put("zero", 0);
        NUMBER_MAP.put("one", 1);
        NUMBER_MAP.put("two", 2);
        NUMBER_MAP.put("three", 3);
        NUMBER_MAP.put("four", 4);
        NUMBER_MAP.put("five", 5);
        NUMBER_MAP.put("six", 6);
        NUMBER_MAP.put("seven", 7);
        NUMBER_MAP.put("eight", 8);
        NUMBER_MAP.put("nine", 9);
        NUMBER_MAP.put("ten", 10);
        NUMBER_MAP.put("eleven", 11);
        NUMBER_MAP.put("twelve", 12);
        NUMBER_MAP.put("thirteen", 13);
        NUMBER_MAP.put("fourteen", 14);
        NUMBER_MAP.put("fifteen", 15);
        NUMBER_MAP.put("sixteen", 16);
        NUMBER_MAP.put("seventeen", 17);
        NUMBER_MAP.put("eighteen", 18);
        NUMBER_MAP.put("nineteen", 19);
        NUMBER_MAP.put("twenty", 20);
        NUMBER_MAP.put("thirty", 30);
        NUMBER_MAP.put("forty", 40);
        NUMBER_MAP.put("fifty", 50);
        NUMBER_MAP.put("sixty", 60);
        NUMBER_MAP.put("seventy", 70);
        NUMBER_MAP.put("eighty", 80);
        NUMBER_MAP.put("ninety", 90);
        NUMBER_MAP.put("hundred", 100);
    }

    public Integer NumberConverter(String input)
    {
        String cleanInput = input.toLowerCase().trim();
        String[] words = cleanInput.split("[\\s-]+");

        System.out.println("Words before clean: " + Arrays.toString(words));

        // heres the rule:
        // if a single digit number combes before a double digit number, discard the single digit
        // 0 to 9 is single digit
        // 10 to 28 is double digit
        List<String> keys = new ArrayList<>(NUMBER_MAP.keySet());
        boolean singleDigitFound = false;

        for(int i=0; i < words.length; i++)
        {
            String word = words[i];
            System.out.println("Looking at " + word + " at position " + i);

            if(NUMBER_MAP.containsKey(word))
            {
                int position = keys.indexOf(word); // get the position of word

                if(position >= 0 && position <= 9)
                {
                    // this means its one of the single digits
                    singleDigitFound = true; // set it to true
                }
                else if(position >= 10 && position <= 28)
                {
                    // this means its one of the second digits

                    // this means the previous iteration found a single digit
                    if(singleDigitFound)
                    {
                        List<String> list = new ArrayList<>(Arrays.asList(words));
                        list.remove(i-1);
                        words = list.toArray(new String[0]);
                        singleDigitFound = false; // reset the flag
                    }
                }
            }
        }

        System.out.println("Words after clean: " + Arrays.toString(words));

        boolean foundNumber = false;

        int total = 0;

        for(String word : words)
        {
            if(NUMBER_MAP.containsKey(word))
            {
                total += NUMBER_MAP.get(word);
                foundNumber = true;
            }
            else
            {
                continue;
            }
        }

        if(foundNumber) return total;
        else return -1;
    }
}
