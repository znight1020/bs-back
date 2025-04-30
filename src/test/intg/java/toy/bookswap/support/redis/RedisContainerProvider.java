package toy.bookswap.support.redis;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import java.time.Duration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class RedisContainerProvider {

  public static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:7.2.4")
      .withExposedPorts(6379)
      .waitingFor(Wait.forListeningPort())
      .withStartupTimeout(Duration.ofSeconds(10))
      .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
          cmd.getHostConfig().withPortBindings(
              new PortBinding(Ports.Binding.bindPort(6380), new ExposedPort(6379))
          )
      ))
      .withReuse(true);

  static {
    REDIS_CONTAINER.start();
  }
}