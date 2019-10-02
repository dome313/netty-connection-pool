package se.lolotron.connectionpool.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;

public class ByteBufToStringInboundHandler extends ChannelInboundHandlerAdapter {

  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf in = (ByteBuf) msg;
    try {
      String response = in.toString(Charset.defaultCharset());
      System.out.println(ctx.toString() + " : " + response);
    } finally {
    }
  }

}
