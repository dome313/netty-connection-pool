package se.lolotron.connectionpool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.SimpleChannelPool;
import java.net.InetSocketAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.lolotron.connectionpool.channel.MyChannelFactory;

/**
 * https://netty.io/news/2015/05/07/4-0-28-Final.html
 */
@Configuration
public class NettyPoolConfiguration {

  @Bean
  public SimpleChannelPool simpleChannelPool(MyChannelFactory myChannelFactory) throws Exception {

    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap clientBootstrap = new Bootstrap();

    clientBootstrap.channelFactory(myChannelFactory);

    clientBootstrap.group(group);

    clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    clientBootstrap.remoteAddress(new InetSocketAddress("localhost", 9000));

    return new SimpleChannelPool(clientBootstrap, new MyChannelPoolHandler());
  }

}
