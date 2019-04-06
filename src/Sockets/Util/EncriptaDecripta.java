package Sockets.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EncriptaDecripta {

    private static final String ALGORITHM = "RSA";
    private PrivateKey chavePrivada;
    private PublicKey chavePublica;

    public EncriptaDecripta() {
        try {
            KeyPair temp = this.geraChave();
            this.chavePrivada = temp.getPrivate();
            this.chavePublica = temp.getPublic();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncriptaDecripta.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    /**
     * Criptografa o texto puro usando chave pública.
     */
    public static byte[] criptografa(String texto, PublicKey chave) {
        byte[] cipherText = null;

        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Criptografa o texto puro usando a chave Púlica
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            cipherText = cipher.doFinal(texto.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipherText;
    }

    /**
     * Decriptografa o texto puro usando chave privada.
     */
    public static String decriptografa(byte[] texto, PrivateKey chave) {
        byte[] dectyptedText;

        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Decriptografa o texto puro usando a chave Privada
            cipher.init(Cipher.DECRYPT_MODE, chave);
            dectyptedText = cipher.doFinal(texto);

        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            System.out.println("Erro ao decriptografar mensagem");
            return null;
        }

        assert dectyptedText != null;
        return new String(dectyptedText);
    }

    /**
     * Gera a chave que contém um par de chave Privada e Pública usando 1025
     * bytes. Armazena o conjunto de chaves nas variáveis chavePrivade
     * e chavePublica
     */
    private KeyPair geraChave() throws NoSuchAlgorithmException {

        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(1024);
        return keyGen.generateKeyPair();
    }

    public PrivateKey getChavePrivada() {
        return chavePrivada;
    }

    public PublicKey getChavePublica() {
        return chavePublica;
    }


    /*
      Testa o Algoritmo

      public static void main(String[] args) {

      try {

      // Verifica se já existe um par de chaves, caso contrário gera-se as
      chaves.. if (!verificaSeExisteChavesNoSO()) { // Método responsável por
      gerar um par de chaves usando o algoritmo RSA e // armazena as chaves nos
      seus respectivos arquivos. geraChave(); }

      final String msgOriginal = "Exemplo de mensagem"; ObjectInputStream
      inputStream = null;

      // Criptografa a Mensagem usando a Chave Pública inputStream = new
      ObjectInputStream(new FileInputStream(PATH_CHAVE_PUBLICA)); final
      PublicKey chavePublica = (PublicKey) inputStream.readObject(); final
      byte[] textoCriptografado = criptografa(msgOriginal, chavePublica);

      // Decriptografa a Mensagem usando a Chave Pirvada inputStream = new
      ObjectInputStream(new FileInputStream(PATH_CHAVE_PRIVADA)); final
      PrivateKey chavePrivada = (PrivateKey) inputStream.readObject(); final
      String textoPuro = decriptografa(textoCriptografado, chavePrivada);

      // Imprime o texto original, o texto criptografado e // o texto
      descriptografado. System.out.println("Mensagem Original: " +
      msgOriginal); System.out.println("Mensagem Criptografada: "
      +textoCriptografado.toString()); System.out.println("Mensagem
      Decriptografada: " + textoPuro);

      } catch (Exception e) { e.printStackTrace(); } }
     */
}
