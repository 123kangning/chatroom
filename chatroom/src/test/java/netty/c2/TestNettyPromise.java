package netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.准备EvenLoop对象
        NioEventLoopGroup group=new NioEventLoopGroup();
        EventLoop eventLoop=group.next();
        //2.主动创建promise,结果容器
        DefaultPromise<Integer> promise=new DefaultPromise<>(eventLoop);

        new Thread(()->{
            //3.任意一个线程执行计算，执行完毕
            log.info("开始计算");
            try {
                int i=1/0;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //promise.setFailure(e);
            }
            promise.setSuccess(66);

        }).start();
        //4.接受结果
        log.info("等待结果");
        log.info("结果是{}",promise.get());

    }
}
