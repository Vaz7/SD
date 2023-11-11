package cmd;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 */


public class Message implements Serializable {
    /**
     * byte[] para transportar dados
     */
    private byte[] data; // MAX 990 bytes
    /**
     * length do byte[] para fazer deserialize
     */
    private int length; // 4 bytes
    /**
     * indica se o chunk corresponde ao último
     */
    private boolean last; // 1 byte
    /**
     * enviar memória necessária para aquele código
     */
    private int num; // 4 bytes
    /**
     * identifica a mensagem enviada
     */
    private byte msg; // 1 byte

    public Message(byte[] data, boolean last, byte msg, int num) {
        this.data = Arrays.copyOf(data, data.length);
        this.length = data.length;
        this.last = last;
        this.msg = msg;
        this.num = num;
    }

    public Message(byte msg){
        this.data = new byte[0];
        this.msg = msg;
        this.length = 0;
        this.last = true;
        this.num = 0;
    }

    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    public int getLength() {
        return length;
    }

    public boolean isLast() {
        return last;
    }

    public int getNum() {
        return num;
    }

    public byte getMsg() {
        return msg;
    }

    /**
     * Método que dá Serialize de uma Message
     * @param message
     * @return
     */
    public static byte[] serializeMessage(Message message) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(message);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método que dá deserialize de uma Message
     * @param data
     * @return
     */
    public static Message deserializeMessage(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método que converte byte[] > 990 len numa Lista de Messages
     * @param data
     * @param msg
     * @param num
     * @return
     */
    public static List<Message> createMessagesFromByteArray(byte[] data, byte msg, int num) {
        List<Message> messages = new ArrayList<>();

        int chunkSize = 990;
        int offset = 0;

        while (offset < data.length) {
            int length = Math.min(chunkSize, data.length - offset);
            byte[] chunk = new byte[length];
            System.arraycopy(data, offset, chunk, 0, length);
            boolean isLastChunk = (offset + length) == data.length;
            Message message = new Message(chunk, isLastChunk, msg, num);

            messages.add(message);

            offset += length;
        }

        return messages;
    }

    /**
     * Método que agrega toda a data dentro do byte[] de cada Mensagem em apenas um byte[]
     * @param messages
     * @return
     */
    public static byte[] convertMessagesToByteArray(List<Message> messages) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            for (Message message : messages) {
                // Get the data from each Message and write it to the ByteArrayOutputStream
                byte[] messageData = message.getData(); // Assuming you have a getData() method in your Message class
                bos.write(messageData);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] serializeMessageList(List<Message> messageList) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            for (Message message : messageList) {
                // Serialize each Message and append to the ByteArrayOutputStream
                byte[] serializedMessage = serializeMessage(message);
                byteArrayOutputStream.write(serializedMessage);
            }

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0]; // Return an empty byte array in case of an exception
        }
    }
}
