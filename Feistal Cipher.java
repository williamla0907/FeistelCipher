package com.williamla;

import javax.crypto.KeyGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Main {


    public static void main(String[] args) {
        //Generating a keyList of size 16
        KeysGen generator = new KeysGen();
        ArrayList<BigInteger> keyList = generator.getKeyList();

        //Testing


        String s = "he";
        FeistelCipher cipher = new FeistelCipher(s, keyList);
        String encryptedText = cipher.cipher();
        System.out.println(encryptedText);
        BigInteger
        System.out.println("------------------------------------");
        FeistelCipher decipher = new FeistelCipher(encryptedText, keyList);
        String decryptedText = decipher.decipher();
        System.out.println(decryptedText);



    }

}

//Feistel Cipher
class FeistelCipher{

    //left and right
    private BigInteger left;
    private BigInteger right;
    private ArrayList<BigInteger> keyList;

    public FeistelCipher(String text, ArrayList<BigInteger> keyList){
        //split the text to two half and store keyList
        BigInteger b = new BigInteger(text.getBytes());
        String s = b.toString(2);
        if ((s.length() % 2) != 0){
            s= "0" + s.substring(0);
            System.out.println("original string" + s);
        }
        left = new BigInteger(s.substring(0,s.length()/2), 2);
        right = new BigInteger(s.substring(s.length()/2), 2);
        this.keyList = keyList;
    }

    public String cipher(){
        //do encryption
        for( int i = 0; i < keyList.size(); i++){
            alg(i);
        }
        return (getResult());
    }

    public String decipher(){
        // do decrypt
        for( int i = keyList.size()-1; i >= 0 ; i--){
            alg(i);
        }
        return (getResult());
    }

    //Feistel Cipher Algorithm
    private void alg(int position){
        BigInteger token = xorFunc(right, keyList.get(position));
        System.out.println("key " + keyList.get(position).toString(2));
        System.out.println("token " + token.toString(2));
        BigInteger shiftedToken = shiftToken(token);
        System.out.println("shifted token "  + shiftedToken.toString(2));
        System.out.println("old left " + left.toString(2));
        System.out.println("old right " + right.toString(2));
        BigInteger temp = right;
        right = xorFunc(left, shiftedToken);
        left = temp;
        System.out.println("new left " + left.toString(2));
        System.out.println("new right " + right.toString(2));
    }

    //Left circular shift
    private BigInteger shiftToken(BigInteger token){
        String s = token.toString(2);
        String s1 = s.substring(0,1);
        String s2 = s.substring(1);
        s = s2 + s1;
        return (new BigInteger(s,2));
    }

    //Get result
    private String getResult(){
        BigInteger encryptedResult = new BigInteger((right.toString(2) + left.toString(2)), 2);
        System.out.println("encrypted string" + encryptedResult.toString(2));
        String encryptedString = new String(encryptedResult.toByteArray());
        return encryptedString;
    }

    //Exclusive OR function
    private static BigInteger xorFunc(BigInteger first, BigInteger second){
        BigInteger convertedText = first.xor(second);
        return convertedText;
    }

}

//Keys Generator class: Generating 16 keys of type BigInteger
class KeysGen{
    private ArrayList<BigInteger> keyList = new ArrayList<>();

    public KeysGen(){
        Random rnd = new Random();
        for ( int i = 0; i < 1; i++){
            //keyList.add(new BigInteger(1, rnd));
            keyList.add(new BigInteger("1",2));
        }
    }

    public ArrayList<BigInteger> getKeyList(){
        return keyList;
    }
}

