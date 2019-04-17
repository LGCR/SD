package Sockets.Model.Mensagem;

import java.io.*;
import java.security.PublicKey;

public class Mensagem implements Serializable {

    private Long tempo;
    private PublicKey chavePublica;

    public Mensagem(Long tempo) {
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

    public Long getTempo() {
        return tempo;
    }

    public void setTempo(Long tempo) {
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
