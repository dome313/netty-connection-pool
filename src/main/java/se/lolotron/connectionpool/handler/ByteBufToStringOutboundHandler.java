package se.lolotron.connectionpool.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.nio.charset.Charset;

public class ByteBufToStringOutboundHandler extends ChannelOutboundHandlerAdapter {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
    System.out.println(ctx.toString() + " : writing...");
    ByteBuf encoded = ctx.alloc().buffer(4);
    encoded.writeCharSequence((CharSequence) msg, Charset.defaultCharset());
    ctx.write(encoded, promise);
  }

}
