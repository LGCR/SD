package Sockets.Util;

import Sockets.View.Tela;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.stream.StreamSupport;

public class EncriptaDecripta {


    /**
     * Criptografa o texto puro usando chave pública.
     */
    public static byte[] criptografa(byte[] texto, PublicKey chave) {
        byte[] cipherText = null;

        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            // Criptografa o texto puro usando a chave Púlica
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            cipherText = cipher.doFinal(texto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipherText;
    }

    /**
     * Decriptografa o texto puro usando chave privada.
     */
    public static byte[] decriptografa(byte[] texto, PrivateKey chave) {
        byte[] dectyptedText;

        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            // Decriptografa o texto puro usando a chave Privada
            cipher.init(Cipher.DECRYPT_MODE, chave);
            dectyptedText = cipher.doFinal(texto);

        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            System.out.println("Erro ao decriptografar mensagem");
            return null;
        }

        assert dectyptedText != null;
        return dectyptedText;
    }

    /**
     * Gera a chave que contém um par de chave Privada e Pública usando 1025
     * bytes. Armazena o conjunto de chaves nas variáveis chavePrivade
     * e chavePublica
     */
    public static KeyPair geraChave() {
        final KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Falha ao gerar par de chaves! Finalizando processo.");
            System.exit(1);
        }
        return null;
    }

}
