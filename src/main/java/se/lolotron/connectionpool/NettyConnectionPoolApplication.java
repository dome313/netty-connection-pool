package se.lolotron.connectionpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NettyConnectionPoolApplication {

  @Autowired
  private NettyService nettyService;

  public static void main(String[] args) {
    SpringApplication.run(NettyConnectionPoolApplication.class, args);
  }

  @RequestMapping("/test/{msg}")
  public void test(@PathVariable String msg) throws Exception {
    nettyService.sendMessage(msg);
  }

}
