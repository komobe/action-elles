package ci.komobe.actionelle.domain.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Génère une référence de devis (format : QT + 12 caractères)
 *
 * @author Moro KONÉ 2025-05-30
 */
public class QuoteReferenceGenerator {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHm");
  private static final String PREFIX = "QT";
  private static final Random RANDOM = new Random();

  private QuoteReferenceGenerator() {
  }

  public static String generate() {
    String timestamp = LocalDateTime.now().format(FORMATTER);
    var randomString = "";
    if (timestamp.length() > 9) {
      int randomNextInt = 10 + RANDOM.nextInt(90);
      randomString = String.format("%02d", randomNextInt);
    } else {
      int randomNextInt = 100 + RANDOM.nextInt(900);
      randomString = String.format("%03d", randomNextInt);
    }

    return PREFIX + timestamp + randomString;
  }
}
