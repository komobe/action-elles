package ci.komobe.actionelle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@SpringBootApplication
public class ActionelleApplication {
  public static void main(String[] args) {
    SpringApplication.run(ActionelleApplication.class, args);
  }
}
