/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
 *
 * @author thomas
 */
public class SaveFileDAO
{

    public SaveFileDAO(String fileName)
    {

    }

    public void saveLogin(String userName, String passWord) throws IOException
    {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("LoginData.txt")))
        {
            userName = encrypt(userName);
            passWord = encrypt(passWord);
            bw.append(userName
                    + ", " + passWord);

        }
    }

    public String[] loadLogin() throws IOException
    {
//        ArrayList<String> userData = new ArrayList<>();
        String[] loadedArray = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader("LoginData.txt")))
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
