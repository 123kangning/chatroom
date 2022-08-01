package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import message.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        //1.4 字节,魔数
        byteBuf.writeBytes(new byte[]{9,2,6,4});
        //2.1 字节,序列化算法方式 0-->jdk ，1-->json
        byteBuf.writeByte(0);
/*        //3.1 字节,指令类型
        byteBuf.writeByte(message.getMessageType());*/
        //4.4 字节,请求序号（为了双工通信，提高异步能力）
        byteBuf.writeInt(message.getSequenceId());

        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes=bos.toByteArray();
        //5.4 字节，消息长度
        byteBuf.writeInt(bytes.length);
        //log.info("encode length={}",bytes.length);
        //6.2字节，备用位
        byteBuf.writeShort(0xffff);
        //6.获取内容的字节数组
        byteBuf.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magicNum=byteBuf.readInt();
        byte serializerType=byteBuf.readByte();
/*        byte messageType=byteBuf.readByte();*/
        int sequenceId=byteBuf.readInt();
        int length=byteBuf.readInt();
        //log.info("decode length={}",length);
        byteBuf.readByte();
        byteBuf.readByte();
        byte[] bytes=new byte[length];
        byteBuf.readBytes(bytes,0,length);
        if(serializerType==0){
            //使用jdk序列化
            ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(bytes));
            Message message=(Message) ois.readObject();
            /*log.info("{}, {}, {}, {}, {}",magicNum,serializerType,messageType,sequenceId,length);
            log.info("{}",message);*/
            list.add(message);
        }

    }
}
