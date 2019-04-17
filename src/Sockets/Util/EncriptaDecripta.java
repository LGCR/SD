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
     * Criptografa o texto puro usando chave privada.
     */
    public static Signature assina(byte[] texto, PrivateKey chave) {
        Signature signature = null;

        try {
            signature = Signature.getInstance("SHA256withDSA");
            signature.initSign(chave);
            signature.update(texto);
            signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }

    /**
     * Decriptografa o texto puro usando chave publica.
     */
    public static boolean verificaAssinatura(byte[] dados, PublicKey chavePublica, byte[] assinatura) {
        Signature signature;
        boolean compativel = false;

        try {
            signature = Signature.getInstance("SHA256withDSA");
            signature.initVerify(chavePublica);
            signature.update(dados);
            compativel = signature.verify(assinatura);

        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException ex) {
            System.out.println("Erro ao verificar mensagem");
        }
        return compativel;
    }

    /**
     * Gera a chave que contém um par de chave Privada e Pública usando 2048
     * bytes. Armazena o conjunto de chaves nas variáveis chavePrivade
     * e chavePublica
     */
    public static KeyPair geraChave() {
        final KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("DSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Falha ao gerar par de chaves! Finalizando processo.");
            System.exit(1);
        }
        return null;
    }

}
