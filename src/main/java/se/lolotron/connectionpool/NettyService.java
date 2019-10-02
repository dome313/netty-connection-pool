package se.lolotron.connectionpool;

import static java.lang.System.lineSeparator;
import static java.time.LocalDateTime.now;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.springframework.stereotype.Service;

@Service
public class NettyService {

  private SimpleChannelPool simpleChannelPool;

  public NettyService(SimpleChannelPool simpleChannelPool) {
    this.simpleChannelPool = simpleChannelPool;
  }

  public void sendMessage(String msg) {
    Future<Channel> future = simpleChannelPool.acquire();

    future.addListener((FutureListener<Channel>) f -> {
      if (f.isSuccess()) {
        Channel ch = f.getNow();

        ChannelFuture channelFuture = ch.writeAndFlush((msg + " " + now() + lineSeparator()));

        channelFuture.addListener(writeFuture -> {
          if (writeFuture.isSuccess()) {
            System.out.println("Channel write successful");
            // how to READ data here?
          }
        });
        // Release back to pool
        simpleChannelPool.release(ch);
      } else {
        System.out.println("Failed to acquire a channel");
      }
    });
  }

}
