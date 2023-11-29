package com.users.findo.security;

import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    static {
        System.loadLibrary("native-lib");
    }
    String keyString;
    String ivString;
    public native String getKey();
    public native String getIv();
    private static final String ALGO = "AES";
    private static final String ALGO_MOD_PAD = "AES/CBC/PKCS5Padding";

    public Crypto (){
        this.keyString = getKey();
        this.ivString = getIv();
    }

    public String encrypt(String Message){

        try{

            byte [] byte_Key = new byte[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                byte_Key = Base64.getDecoder().decode(this.keyString);
            }
            byte [] byte_IV = new byte[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                byte_IV = Base64.getDecoder().decode(this.ivString);
            }

            // Algorithm Payloads
            SecretKey PayloadKey = new SecretKeySpec(byte_Key,ALGO);
            AlgorithmParameterSpec PayloadIV = new IvParameterSpec(byte_IV);

            Cipher cipher = Cipher.getInstance(ALGO_MOD_PAD);
            cipher.init(Cipher.ENCRYPT_MODE, PayloadKey, PayloadIV);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(cipher.doFinal(Message.getBytes(StandardCharsets.UTF_8)));
            }
            return "";
        }
        catch (Exception e){
            return "ERROR : " + e.getMessage();
        }

    }

    public String decrypt(String Message){
        try{

            byte [] byte_Key = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                byte_Key = Base64.getDecoder().decode(this.keyString);
            }
            byte [] byte_IV = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                byte_IV = Base64.getDecoder().decode(this.ivString);
            }

            // Algorithm Payloads
            SecretKey PayloadKey = new SecretKeySpec(byte_Key,ALGO);
            AlgorithmParameterSpec PayloadIV = new IvParameterSpec(byte_IV);

            Cipher cipher = Cipher.getInstance(ALGO_MOD_PAD);
            cipher.init(Cipher.DECRYPT_MODE, PayloadKey, PayloadIV);

            byte[] t = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                t = Base64.getDecoder().decode(Message);
            }

            byte[] k = cipher.doFinal(t);
            return new String(k);

        }
        catch (Exception e){
            return "ERROR : " + e.getMessage();
        }

    }

}

