package se.lolotron.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.net.InetSocketAddress;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.lolotron.clienthandler.DummyClientHandler;

@SpringBootApplication
@RestController
public class SimpleNettyClientApplication {

  private EventLoopGroup group;
  private ChannelFuture channelFuture;
  private Bootstrap clientBootstrap;

  public static void main(String[] args) {
    SpringApplication.run(SimpleNettyClientApplication.class, args);
  }

  @PostConstruct
  public void setup() throws Exception {
    group = new NioEventLoopGroup();
    clientBootstrap = new Bootstrap();

    clientBootstrap.group(group);
    clientBootstrap.channel(NioSocketChannel.class);
    clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    clientBootstrap.remoteAddress(new InetSocketAddress("localhost", 9000));
    clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
      protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new DummyClientHandler());
      }
    });
    channelFuture = clientBootstrap.connect().sync();
  }

  @PreDestroy
  public void cleanUp() throws Exception {
    group.shutdownGracefully().sync();
  }

  @RequestMapping("/test/{msg}")
  public void test(@PathVariable String msg) throws Exception {
    if (!channelFuture.channel().isOpen()) {
      channelFuture = clientBootstrap.connect().sync();
    }
    channelFuture.channel().writeAndFlush(msg + System.lineSeparator());
  }

}
