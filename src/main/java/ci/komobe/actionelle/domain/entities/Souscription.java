package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.utils.IdGenerator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
public class Souscription {

  private static final Random random = new Random();

  private String id;
  private String numero;
  private Assure assure;
  private Vehicule vehicule;

  public static Souscription creer(Assure assure, Vehicule vehicule) {
    return Souscription.builder()
        .id(IdGenerator.generateId())
        .numero(generateNumero())
        .assure(assure)
        .vehicule(vehicule)
        .build();
  }

  private static String generateNumero() {
    String code = "SO";
    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
    int numeroRandom = random.nextInt(1000000000);
    return code + date + numeroRandom;
  }
}
