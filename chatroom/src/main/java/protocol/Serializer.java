package protocol;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface Serializer {
    <T> T deserialize(Class<T> cla,byte[] bytes);
    <T> byte[] serialize(T object);

    enum Algorithm implements Serializer{
        Java {
            @Override
            public <T> T deserialize(Class<T> cla, byte[] bytes) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException("反序列化时出错",e);
                }
            }

            @Override
            public <T> byte[] serialize(T object) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("序列化失败",e);
                }
            }
        },
        Json {
            @Override
            public <T> T deserialize(Class<T> cla, byte[] bytes) {
                String json=new String(bytes,StandardCharsets.UTF_8);
                return new Gson().fromJson(json,cla);
            }

            @Override
            public <T> byte[] serialize(T object) {
                String json=new Gson().toJson(object);
                return json.getBytes(StandardCharsets.UTF_8);
            }
        }
    }
}
