package se.lolotron.nettypool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import java.net.InetSocketAddress;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lolotron.clienthandler.DummyClientHandler;

@SpringBootApplication
@RestController
public class NettyConnectionPoolClientApplication {

  private SimpleChannelPool simpleChannelPool;

  public static void main(String[] args) {
    SpringApplication.run(NettyConnectionPoolClientApplication.class, args);
  }

  @PostConstruct
  public void setup() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();

    bootstrap.group(group);
    bootstrap.channel(NioSocketChannel.class);
    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    bootstrap.remoteAddress(new InetSocketAddress("localhost", 9000));
    bootstrap.handler(new ChannelInitializer<SocketChannel>() {
      protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new DummyClientHandler());
      }
    });
    simpleChannelPool = new SimpleChannelPool(bootstrap, new DummyChannelPoolHandler());
  }

  @RequestMapping("/test/{msg}")
  public void test(@PathVariable String msg) throws Exception {
    Future<Channel> future = simpleChannelPool.acquire();

    future.addListener((FutureListener<Channel>) f -> {
      if (f.isSuccess()) {
        System.out.println("Connected");
        Channel ch = f.getNow();
        ch.writeAndFlush(msg + System.lineSeparator());

        // Release back to pool
        simpleChannelPool.release(ch);
      } else {
        System.out.println("not successful");
      }
    });
  }
}
