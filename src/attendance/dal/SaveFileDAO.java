package attendance.dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *This class handles the saved the file with Login data.
 * @author Simon Birkedal, Stephan Fuhlendorff, Thomas Hansen & Jacob Enemark
 */
public class SaveFileDAO
{
    String fileName;
    
    /**
     * The contructor.
     * @param fileName 
     */
    public SaveFileDAO(String fileName)
    {
        this.fileName = fileName;
    }
/**
 * Writes the Login data to the txt.file. 
 * @param userName String, the username to be saved.
 * @param passWord String, the password to be saved.
 * @throws IOException 
 */
    public void saveLogin(String userName, String passWord) throws IOException
    {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName)))
        {
            userName = encrypt(userName);
            passWord = encrypt(passWord);
            bw.append(userName
                    + ", " + passWord);

        }
    }
/**
 * Reads the login data from the saved file.
 * @return a String array with the Login data
 * @throws IOException 
 */
    public String[] loadLogin() throws IOException
    {
        String[] loadedArray = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            Scanner scanner = new Scanner(br);
            while (scanner.hasNext())
            {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                loadedArray[0] = fields[0].trim();
                loadedArray[1] = fields[1].trim();

            }
        }
        if (!"".equals(loadedArray[0]) && !"".equals(loadedArray[1]))
        {
            loadedArray[0] = decrypt(loadedArray[0]);
            loadedArray[1] = decrypt(loadedArray[1]);
        }
        return loadedArray;
    }
/**
 * Encrypts the login data.
 * @param text to be encrypted.
 * @return the encrypted String.
 */
    public static String encrypt(String text)
    {

        try
        {
            String key = "Bar12345Bar12345";
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            Base64.Encoder encoder = Base64.getEncoder();
            String encryptedString = encoder.encodeToString(encrypted);
            return encryptedString;
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidKeyException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (BadPaddingException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
/**
 * Decrypts the login data.
 * @param encryptedString to be decrypted.
 * @return decrypted login data.
 */
    public static String decrypt(String encryptedString)
    {
        try
        {
            String key = "Bar12345Bar12345";
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            Base64.Decoder decoder = Base64.getDecoder();
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(decoder.decode(encryptedString)));
            return decrypted;
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidKeyException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (BadPaddingException ex)
        {
            Logger.getLogger(SaveFileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
