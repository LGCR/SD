package Sockets.Model.Mensagem;

import Sockets.Model.PacoteMensagem;

import java.io.*;
import java.security.PublicKey;

public class Mensagem implements Serializable {

    private int tempo;
    private PublicKey chavePublica;

    public Mensagem(int tempo) {
        this.tempo = tempo;
    }

    public Mensagem(PublicKey chavePublica) {
        this.chavePublica = chavePublica;
    }

    public PublicKey getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(PublicKey chavePublica) {
        this.chavePublica = chavePublica;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public static Mensagem converteArrayBytesParaMensagem(byte[] array) throws IOException, ClassNotFoundException {
        ByteArrayInputStream input = new ByteArrayInputStream(array);
        ObjectInputStream objectInput = new ObjectInputStream(input);
        return  (Mensagem) objectInput.readObject();
    }

    public static byte[] converteMensagemParaArrayBytes( Mensagem pacote) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(output);
        objectOutput.writeObject(pacote);
        return output.toByteArray();
    }
}
