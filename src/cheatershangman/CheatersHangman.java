/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cheatershangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author konatsu
 */
public class CheatersHangman 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // 1. Read the dictionary.txt and store it in your program at runtime.
        String fileName = "words.txt";
        HashMap<String, Integer> words = new HashMap<>();

        try
        {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) 
            {
                String word = scanner.nextLine().toLowerCase();
                words.put(word, word.length());
            }
            scanner.close();

        } 
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
            
        boolean isGameOn = true;
        while(isGameOn)
        {
            // 2. Ask the user to choose the size of the hidden word they want to try to
            // guess. Keep asking them if there are no words of that length.
            Scanner kb = new Scanner(System.in);
            int wordLen = 0;
            while(true)
            {
                System.out.print("Enter the word length: ");
                wordLen = kb.nextInt();
                if(words.containsValue(wordLen))
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid length.");
                }
            }
            
            // 3. Ask the user to choose how many wrong guesses they get to have before
            // they lose.
            System.out.print("Enter how many lives you want to have: ");
            int life = kb.nextInt();
            kb.nextLine();
            
            // 4. Create an initial list of hidden words using the dictionary and choosing
            // all the words that have the length the using asked for.
            List<String> wordsList = new ArrayList<>();
            for(String word : words.keySet())
            {
                int length = words.get(word);
                if(length == wordLen)
                {
                    wordsList.add(word);
                }
            }
            System.out.println(wordsList);
            
            // 5. Play hangman using our cheating algorithm:
            Set<Character> guessedLetters = new HashSet<>();    
            String currentWord = "_".repeat(wordLen); 
            
            while(life > 0)
            {
                // (a) Print out the revealed letters, their wrong guesses, and the remaining
                // number of wrong guesses   
                System.out.println("Current word: " + currentWord);
                System.out.println("Guessed letters: " + guessedLetters);
                System.out.println("Lives left: " + life);                
                System.out.println(wordsList);
                
                System.out.print("Guess a letter: ");
                char guess = kb.next().toLowerCase().charAt(0);
                kb.nextLine();
        
                // (b) Get the user’s new guess and be nice and ask them to reenter their
                // letter if they already guessed it.
                if(guessedLetters.contains(guess))
                {
                    System.out.println("Letter already guessed, please try again.");
                    continue;
                }
                
                guessedLetters.add(guess);
                
                // (c) Separate your list of hidden words into word families based on the
                // input.
                Map<String, List<String>> wordFamilies = new HashMap<>();
                for(String word : wordsList)
                {
                    String family = "";
                    for(char c : word.toCharArray())
                    {
                        if(c == guess)
                        {
                            family += c;
                        }
                        else
                        {
                            family += "_";
                        }
                    }
                    if(!wordFamilies.containsKey(family))
                    {
                        wordFamilies.put(family, new ArrayList<>());
                    }
                    wordFamilies.get(family).add(word);
                }
                
                // (d) Choose a word family using some strategy (I used the word family
                // with the most words) and make that the new hidden word list. If this
                // reveals letters to the user, reveal letters, otherwise the player loses a
                // wrong guess.
                List<String> largestFamilyList = new ArrayList<>();
                String largestFamily = "";
                for(String family : wordFamilies.keySet())
                {
                    if(wordFamilies.get(family).size() > largestFamilyList.size())
                    {
                        largestFamily = family;
                        largestFamilyList = wordFamilies.get(family);
                    }
                }
                wordsList = largestFamilyList;
                String newCurrentWord = "";
                for(int i = 0; i < wordLen; i++)
                {
                    if(largestFamily.charAt(i) != '_')
                    {
                        newCurrentWord += largestFamily.charAt(i);
                    } 
                    else if(!currentWord.substring(i, i+1).equals("_"))
                    {
                        newCurrentWord += currentWord.charAt(i);
                    } 
                    else 
                    {
                        newCurrentWord += "_";
                    }
                }
                currentWord = newCurrentWord;
                if(largestFamily.contains("" + guess))
                {
                    System.out.println("Correct guess!");
                }
                else 
                {
                    System.out.println("Wrong guess!");
                    life--;
                }
                boolean isWordGuessed = !currentWord.contains("_");
                if(isWordGuessed)
                {
                    System.out.println("Correct! The word is " + currentWord + ".");
                    System.out.println("You win!");
                    break;
                }
                
                // (e) Keep going until:
                // (f) If all the letters are revealed, the player wins. If they player is out of
                // wrong guesses, randomly pick a hidden word from the hidden word
                // list and reveal it to the user, pretending that was your hidden word
                // all along.
                if(life == 0)
                {
                    System.out.println("You lose!");
                    Object[] wordsArray = wordsList.toArray();
                    int randomIndex = (int)(Math.random() * wordsArray.length);
                    System.out.println("The word was " + wordsArray[randomIndex] + ".");
                }
                
            }
            
            
            // 6. Ask if they want to play again.
            System.out.print("Play again? (Y/N): ");
            String playAgain = kb.nextLine();
            if(playAgain.toLowerCase().charAt(0) != 'y')
            {
                isGameOn = false;
            }
         

        }
    }

}
