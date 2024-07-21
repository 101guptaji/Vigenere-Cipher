import java.util.*;
import edu.duke.*;
import java.io.File;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        String ans = "";
        for(int i=whichSlice;i<message.length();i+=totalSlices)
        {
            ans = ans + message.charAt(i);
        }
        return ans;
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        for(int i = 0;i<klength;i++)
        {
            String sliceEnc = sliceString(encrypted, i, klength);
            
            CaesarCracker cb = new CaesarCracker(mostCommon);
            key[i] = cb.getKey(sliceEnc);
        }
        return key;
    }

    public void breakVigenere () {
        FileResource fr =new FileResource();
        String enc = fr.asString();
        
        int[] key = tryKeyLength(enc, 4, 'e');
        /*for(int i = 0;i<key.length;i++)
        {
            System.out.print(key[i]+", ");
        }*/
        VigenereCipher vc = new VigenereCipher(key);
        String decrypted = vc.decrypt(enc);
        System.out.println("Decrypted msg: "+decrypted);
    }
    
    //write the public method readDictionary, which has one parameter—a FileResource fr. This method should first make a new HashSet of Strings, then read each line in fr (which should contain exactly one word per line), convert that line to lowercase, and put that line into the HashSet that you created. The method should then return the HashSet representing the words in a dictionary.
    public HashSet<String> readDictionary(FileResource fr)
    {
        HashSet<String> hs = new HashSet<String>();
        for(String word : fr.lines())
        {
            word = word.toLowerCase();
            hs.add(word);
        }
        return hs;
    }
    
    //write the public method countWords, which has two parameters—a String message, and a HashSet of Strings dictionary. This method should split the message into words (use .split(“\\W+”), which returns a String array), iterate over those words, and see how many of them are “real words”—that is, how many appear in the dictionary.
    //This method should return the integer count of how many valid words it found.
    public int countWords(String msg, HashSet<String> dict)
    {
        int count = 0;
        for(String word : msg.split("\\W+"))
        {
            if(dict.contains(word.toLowerCase()))
            {
                count++;
            }
        }
        return count;
    }
    
    //write the public method breakForLanguage, which has two parameters—a String encrypted, and a HashSet of Strings dictionary. This method should try all key lengths from 1 to 100 to obtain the best decryption for each key length in that range. 
    //For each key length, your method should decrypt the message and count how many of the “words” in it are real words in English, based on the dictionary passed in.
    //This method should figure out which decryption gives the largest count of real words, and return that String decryption.
    public String breakForLanguage(String encrypted, HashSet<String> dict)
    {
        String validDecrypt ="";
        int maxCount = 0;
        int[] validKey = new int[100];
        char commonChar = mostCommonCharIn(dict);
        for(int i =1;i<=100;i++)
        {
            int[] key = tryKeyLength(encrypted, i, commonChar);
            VigenereCipher vc = new VigenereCipher(key);
            String decrypted = vc.decrypt(encrypted);
            int count = countWords(decrypted, dict);
            if(maxCount < count)
            {
                maxCount = count;
                validDecrypt = decrypted;
                validKey = key;
            }
        }
        System.out.print(validKey.length+" Keys: {");
        for(int i = 0;i<validKey.length;i++)
        {
            System.out.print(validKey[i]+",");
        }
        System.out.println("}");
        System.out.println("file contains "+maxCount+" valid words out of "+encrypted.split("\\W+").length);
        return validDecrypt;
    }
    
    
    public void breakVigenereUnknownKeyLength () {
        FileResource fr =new FileResource();
        String enc = fr.asString();
        
        FileResource dictFile = new FileResource("dictionaries/English");
        HashSet<String> dictionary = readDictionary(dictFile);
        
        String decrypted = breakForLanguage(enc, dictionary);
        System.out.println("Decrypted msg: "+decrypted);
    }
    
    //write the public method mostCommonCharIn, which has one parameter—a HashSet of Strings dictionary. This method should find out which character, of the letters in the English alphabet, appears most often in the words in dictionary. It should return this most commonly occurring character.
    public char mostCommonCharIn(HashSet<String> dict)
    {
        char mostOccur = 'e';
        int maxCount = 0;
        HashMap <Character, Integer> charCount = new HashMap <Character, Integer>();
        
        for(String word : dict)
        {
            for(int i=0;i<word.length();i++)
            {
                if(!charCount.containsKey(word.charAt(i)))
                {
                    charCount.put(word.charAt(i), 1);
                }
                else
                {
                    charCount.put(word.charAt(i), charCount.get(word.charAt(i))+1);
                }
            }
        }
        //System.out.println(charCount);
        for(char c : charCount.keySet())
        {
            if(charCount.get(c) > maxCount)
            {
                maxCount = charCount.get(c);
                mostOccur = c;
            }
        }
        return mostOccur;
    }
    
    //write the public method breakForAllLangs, which has two parameters—a String encrypted, and a HashMap, called languages, mapping a String representing the name of a language to a HashSet of Strings containing the words in that language. Try breaking the encryption for each language, and see which gives the best results
    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> lang)
    {
        String validLang = "", validDecrypt = "";
        int maxCount = 0;
        for(String l : lang.keySet())
        {
            String decrypted = breakForLanguage(encrypted, lang.get(l));
            int count =  countWords(decrypted, lang.get(l));
            if(count > maxCount)
            {
                validLang = l;
                validDecrypt = decrypted;
                maxCount = count;
            }
        }
        
        System.out.println("Language for decrypt: "+validLang);
        System.out.println("Decrypted message: "+validDecrypt);
        
    }
    
    public void breakVigenereWithManyDict() {
        FileResource fr =new FileResource();
        String enc = fr.asString();
        
        HashMap <String, HashSet<String>> wordDict = new HashMap <String, HashSet<String>>();
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles())
        {
            FileResource dictFile = new FileResource(f);
            HashSet<String> dictionary = readDictionary(dictFile);
            wordDict.put(f.getName(), dictionary);
        }
        
        breakForAllLangs(enc, wordDict);
    }
}









