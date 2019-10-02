package se.lolotron.connectionpool.channel;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MyChannel extends NioSocketChannel {

  private ScheduledExecutorService scheduledExecutorService;
  private ScheduledFuture<?> scheduledFuture;

  MyChannel(ScheduledExecutorService scheduledExecutorService) {
    this.scheduledExecutorService = scheduledExecutorService;
  }

  public void startTimer() {
    scheduledFuture = scheduledExecutorService
        .scheduleAtFixedRate(() -> writeAndFlush(
            "KEEP ALIVE" + LocalDateTime.now() + System.lineSeparator()),
            10000, 10000, TimeUnit.MILLISECONDS);
  }

  @Override
  public ChannelFuture writeAndFlush(Object msg) {
    return super.writeAndFlush(msg);
  }

  public void resetTimer() {
    scheduledFuture.cancel(true);
    startTimer();
  }
}
