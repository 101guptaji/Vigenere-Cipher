import edu.duke.*;
import java.util.*;

/**
 * Write a description of tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class tester {
    
    public void testCaesarCipher()
    {
        FileResource fr = new FileResource();
        String msg = fr.asString();
        
        int key = 17;
        CaesarCipher cc = new CaesarCipher(key);
        String encrypted = cc.encrypt(msg);
        System.out.println("Key: "+key+"\nEncrypted msg: "+encrypted);
        
        String decrypted = cc.decrypt(encrypted);
        System.out.println("Decrypted msg: "+decrypted);
    }
    
    public void testCaesarCracker()
    {
        FileResource fr = new FileResource();
        String enc = fr.asString();
        /*
        //for english, most common letter is 'e'
        CaesarCracker cb = new CaesarCracker();
        String decrypted = cb.decrypt(enc);
        System.out.println("Decrypted msg: "+decrypted);
        */
        //for Portuguese, most common letter is 'a'
        CaesarCracker cb = new CaesarCracker('a');
        String decrypted = cb.decrypt(enc);
        System.out.println("Decrypted msg: "+decrypted);
    }
    
    public void testVigenereCipher()
    {
        FileResource fr = new FileResource();
        String msg = fr.asString();
        
        String keyString = "rome";
        String alph = "abcdefghijklmnopqrstuvwxyz";
        int[] key = new int[keyString.length()];
        for(int i=0;i<key.length;i++)
        {
            int index = alph.indexOf(keyString.charAt(i));
            if(index != -1)
            {
                key[i] = index;
            }
        }
        VigenereCipher vc = new VigenereCipher(key);
        
        String encrypted = vc.encrypt(msg);
        System.out.println("Key: "+keyString+"\nEncrypted msg: "+encrypted);
        
        String decrypted = vc.decrypt(encrypted);
        System.out.println("Decrypted msg: "+decrypted);
    }
    
    public void testVigenereBreaker()
    {
        VigenereBreaker vb = new VigenereBreaker();
        System.out.println(vb.sliceString("abcdefghijklm", 0, 3)); 
        System.out.println(vb.sliceString("abcdefghijklm", 1, 3));
        System.out.println(vb.sliceString("abcdefghijklm", 2, 3));
        System.out.println(vb.sliceString("abcdefghijklm", 0, 4));
        
        FileResource fr = new FileResource();
        String enc = fr.asString();
        System.out.println("Keys: ");
        int[] key = vb.tryKeyLength(enc, 4, 'e');
        for(int i = 0;i<key.length;i++)
        {
            System.out.print(key[i]+",");
        }
        
        //vb.breakVigenere();
    }
    
    public void testVBUnknownKeyLength()
    {
        VigenereBreaker vb = new VigenereBreaker();
        FileResource fr = new FileResource();
        
        HashSet<String> dictionary = vb.readDictionary(fr);
        System.out.println("most Common Char In: "+vb.mostCommonCharIn(dictionary));
        //System.out.println(dictionary.size());
    }
}
