package se.lolotron.nettypool;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;

public class DummyChannelPoolHandler implements ChannelPoolHandler {

  @Override
  public void channelReleased(Channel channel) throws Exception {
    System.out.println("channelReleased");
  }

  @Override
  public void channelAcquired(Channel channel) throws Exception {
    System.out.println("channelAcquired");
  }

  @Override
  public void channelCreated(Channel channel) throws Exception {
    System.out.println("channelCreated");
  }

}
