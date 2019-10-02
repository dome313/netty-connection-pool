package se.lolotron.connectionpool.channel;

import io.netty.channel.ChannelFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.stereotype.Component;

@Component
public class MyChannelFactory implements ChannelFactory<MyChannel> {

  private ScheduledExecutorService scheduledExecutorService;

  public MyChannelFactory() {
    this.scheduledExecutorService = Executors.newScheduledThreadPool(5);
  }

  @Override
  public MyChannel newChannel() {
    return new MyChannel(scheduledExecutorService);
  }

}
