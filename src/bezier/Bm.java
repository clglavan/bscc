/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Scanner;

/**
 *
 * @author Cristi
 */
public class Bm implements Serializable{

    int number1, number2;

    String intBytes;
    byte[] result;

    String intBytes2;
    byte[] result2;

    String intBytes3;

    String xored = "";
    String plainText = "";
    byte[] plainText_bytes;
    String plainText_bytes_string = "";
    String cryptedText = "";
    String decrypted = "";

    String string_numar1 = "", string_numar2 = "";

    public String crypt_text(String text, int nr1, int nr2) throws UnsupportedEncodingException {
        number1 = nr1;
        number2 = nr2;
        getNumber1();
        getNumber2();
        xorNumber();
        getText(text);
        crypt();

        return cryptedText;
    }

    public String decrypt_text(String text, int nr1, int nr2) {

        number1 = nr1;
        number2 = nr2;
        getNumber1();
        getNumber2();
        xorNumber();
        cryptedText = text;
        decrypt();

        return BinaryToString(decrypted);
    }

    private void getNumber1() {

        string_numar1 = Integer.toBinaryString(number1);
        //  System.out.println("nr1:" + string_numar1);
    }

    private void getNumber2() {

        string_numar2 = Integer.toBinaryString(number2);
        // System.out.println("nr2:" + string_numar2);

    }

    private void getText(String plainText) throws UnsupportedEncodingException {

        plainText_bytes_string = StringToBinary(plainText);
        // System.out.println("text:"+plainText_bytes_string);
    }

    private void xorNumber() {
        int size = 0;
        intBytes3 = "";
        if (string_numar1.length() > string_numar2.length()) {
            size = string_numar2.length();

        } else {
            size = string_numar1.length();
        }

        for (int i = 0; i < size; i++) {
            if (string_numar1.charAt(i) == '0' && string_numar2.charAt(i) == '0' || string_numar1.charAt(i) == '1' && string_numar2.charAt(i) == '1') {
                intBytes3 += '0';
            }
            if (string_numar1.charAt(i) == '0' && string_numar2.charAt(i) == '1' || string_numar1.charAt(i) == '1' && string_numar2.charAt(i) == '0') {
                intBytes3 += '1';
            }
        }

        xored = intBytes3;
        // System.out.println("xored:" + xored);
    }

    private void crypt() {
        int j = 0;
        cryptedText = "";

        for (int i = 0; i < plainText_bytes_string.length(); i++) {
            if (plainText_bytes_string.charAt(i) == '0' && xored.charAt(j) == '0' || plainText_bytes_string.charAt(i) == '1' && xored.charAt(j) == '1') {
                cryptedText += '1';
            }
            if (plainText_bytes_string.charAt(i) == '0' && xored.charAt(j) == '1' || plainText_bytes_string.charAt(i) == '1' && xored.charAt(j) == '0') {
                cryptedText += '0';
            }
            j++;
            if (j == xored.length()) {
                j = 0;
            }

        }

        //   System.out.println("crypt:" + cryptedText);
    }

    private void decrypt() {
        int j = 0;
        decrypted = "";

        for (int i = 0; i < cryptedText.length(); i++) {
            if (cryptedText.charAt(i) == '0' && xored.charAt(j) == '0' || cryptedText.charAt(i) == '1' && xored.charAt(j) == '1') {
                decrypted += '1';
            }
            if (cryptedText.charAt(i) == '0' && xored.charAt(j) == '1' || cryptedText.charAt(i) == '1' && xored.charAt(j) == '0') {
                decrypted += '0';
            }
            j++;
            if (j == xored.length()) {
                j = 0;
            }

        }
        //  System.out.println("decrypted:" + decrypted);

    }

    public String BinaryToString(String s) {

        String output = "";
        for (int i = 0; i <= s.length() - 8; i += 8) {
            int k = Integer.parseInt(s.substring(i, i + 8), 2);
            output += (char) k;
        }
        //   System.out.println("binaryToString:" + output);
        return output;

    }

    public String StringToBinary(String s) throws UnsupportedEncodingException {

        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            //binary.append(' ');
        }
        //   System.out.println("'" + s + "' to binary: " + binary);
        return binary.toString();
    }

   /* public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        Scanner sc = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Bm ceva = new Bm();
        int nr1 = sc.nextInt();
        int nr2 = sc.nextInt();
        ceva.number1 = nr1;
        ceva.number2 = nr2;
        String text = br.readLine();
        System.out.println("Crypt---------------------");
        String temp = ceva.crypt_text(text, ceva.number1, ceva.number2);
        System.out.println("temp:" + temp);
        System.out.println("Decrypt--------------------");

        String output = "";
        for (int i = 0; i <= temp.length() - 8; i += 8) {
            int k = Integer.parseInt(temp.substring(i, i + 8), 2);
            output += (char) k;
        }
        System.out.println("Interceptat:" + output);

        Bm ceva2 = new Bm();
        ceva2.number1=nr1;
        ceva2.number2=nr2;

        System.out.println("decriptat:" + ceva2.decrypt_text(temp, ceva2.number1, ceva2.number2));

    }*/
}
