Netty connection pool is not sending messages

```java
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
```

