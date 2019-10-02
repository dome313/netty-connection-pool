package se.lolotron.connectionpool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.ChannelPoolHandler;
import se.lolotron.connectionpool.channel.MyChannel;
import se.lolotron.connectionpool.handler.ByteBufToStringInboundHandler;
import se.lolotron.connectionpool.handler.ByteBufToStringOutboundHandler;

class MyChannelPoolHandler implements ChannelPoolHandler {

  @Override
  public void channelReleased(Channel channel) throws Exception {
    System.out.println("Connection released!");
  }

  @Override
  public void channelAcquired(Channel channel) throws Exception {
    ((MyChannel) channel).resetTimer();
  }

  @Override
  public void channelCreated(Channel channel) throws Exception {
    ((MyChannel) channel).startTimer();
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast(new ByteBufToStringOutboundHandler());
    pipeline.addLast(new ByteBufToStringInboundHandler());
  }

}
