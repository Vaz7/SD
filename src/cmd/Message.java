package cmd;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * MAX 1000 BYTES POR MENSAGEM -> HARDCODED PARA N PASSAR OS 1500 MTU
 *
 * TIPOS DE MENSAGEM:
 * 0. Desconexão
 * 1. Autenticação
 * 2. Registo
 * 3. Enviar código
 * 4. Autenticação errada
 * 5. Autenticação efetuada
 * 6. Username já utilizado
 * 7. Username registado
 * 8. Resultado FaaS
 * 9. Erro na execução
 * 10. Demasiada memória necessária
 */


public class Message implements Serializable {
    /**
     * byte[] para transportar dados
     */
    private byte[] data;
    /**
     * length do byte[] para fazer deserialize
     */
    private int length; // 4 bytes
    /**
     * enviar memória necessária para aquele código
     */
    private int num; // 4 bytes
    /**
     * identifica a mensagem enviada
     */
    private byte msg; // 1 byte

    public Message(byte[] data, byte msg, int num) {
        this.data = Arrays.copyOf(data, data.length);
        this.length = data.length;
        this.msg = msg;
        this.num = num;
    }

    public Message(byte msg){
        this.data = new byte[0];
        this.msg = msg;
        this.length = 0;
        this.num = 0;
    }

    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    public int getLength() {
        return length;
    }

    public int getNum() {
        return num;
    }

    public byte getMsg() {
        return msg;
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeInt(this.length);
        dos.write(this.data);
        dos.writeInt(this.num);
        dos.writeByte(this.msg);
    }

    public static Message deserialize(DataInputStream dis) throws IOException {
        int len = dis.readInt();
        byte[] data = new byte[len];
        dis.read(data, 0, len);
        int num = dis.readInt();
        byte msg = dis.readByte();
        return new Message(data, msg, num);
    }


}
