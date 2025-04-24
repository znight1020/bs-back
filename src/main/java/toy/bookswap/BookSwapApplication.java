package toy.bookswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BookSwapApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookSwapApplication.class, args);
  }
}
