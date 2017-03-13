package com.williamla;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //Text
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(true) {
            System.out.println("Enter a string of binaries, must be more than 8 character: ");
            input = scanner.next();
            if(input.equals("q")) break;
            if(input.length() < 8 ) continue;
            char[] check = input.toCharArray();
            boolean checked = false;
            for (int i = 0; i < check.length; i++) {
                if (check[i] != '0' && check[i] != '1') {
                    checked = true;
                    break;
                }
            }
            if(checked) continue;


            //Generating key list
            KeysGen gen = new KeysGen();
            ArrayList<String> keyList = gen.getKeyList();
            System.out.print("Keylist = [");
            for ( int i = 0; i < keyList.size()-1; i++ ){
                System.out.print("Key#" + i + ":" + keyList.get(i) +", ");
            }
            System.out.println("Key#" + (keyList.size()-1) + ":" + keyList.get(keyList.size()-1) +"]");

            //Cipher
            FeistelCipher cipher = new FeistelCipher(input, keyList, input.length());
            String encryptedText = cipher.encrypt();
            System.out.println("Encrypted text: " + encryptedText);


            //Decipher
            FeistelCipher decipher = new FeistelCipher(encryptedText, keyList, input.length());
            String decryptedText = decipher.decrypt();
            System.out.println("Decrypted text: " + decryptedText);

        }
    }
}

class FeistelCipher{
    private String left;
    private String right;
    private int length;
    private ArrayList<String> keyList;

    public FeistelCipher(String s, ArrayList<String> keyList, int length){
        left = s.substring(0, s.length()/2);
        right = s.substring(s.length()/2);
        int compare = left.length() - right.length();
        if (compare < 0){
            left = fillUp(left, Math.abs(compare));
        }
        else if(compare >0){
            right = fillUp(right, compare);
        }
        this.keyList = keyList;
        this.length = length;
    }

    public String encrypt(){
        for ( int i = 0; i < keyList.size(); i++){
            alg(i);
        }
        return right + left;
    }

    public String decrypt(){
        for ( int i = keyList.size()-1; i >= 0 ; i--){
            alg(i);
        }
        String r = right + left;
        return r.substring(r.length()-length);
    }


    public void alg(int position){
        String token = func(right, keyList.get(position));
        String temp = right;
        right = xor(left, token);
        left = temp;
    }

    public String func(String s, String key){
        String token = xor(s, key);
        String shiftedToken = shiftToken(token);
        return shiftedToken;
    }

    public String shiftToken(String s){
        s = s.substring(1) + s.substring(0,1);
        return s;
    }


    public String xor(String first, String second){
        int compare = first.length() - second.length();
        if (compare < 0){
            first = fillUp(first, Math.abs(compare));
        }
        else if(compare >0){
            second = fillUp(second, compare);
        }
        StringBuilder builder = new StringBuilder();
        char[] fir = first.toCharArray();
        char[] sec = second.toCharArray();
        for ( int i = 0; i < first.length(); i++){
            if(fir[i] == sec[i]){
                builder.append("0");
            }else{
                builder.append("1");
            }
        }

        return builder.toString();
        }


    public String fillUp(String s, int diff){
        for ( int i = 0 ; i < diff; i++){
            s = "0" + s.substring(0);
        }
        return s;
    }

}

class KeysGen{
    private ArrayList<String> keyList = new ArrayList<>();

    public KeysGen(){
        Random rnd = new Random();
        for ( int i = 0; i < 16; i++){
            BigInteger key = new BigInteger(4, rnd);
            keyList.add(key.toString(2));
        }
    }

    public ArrayList<String> getKeyList(){return keyList;}
}