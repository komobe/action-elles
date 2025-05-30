package ci.komobe.actionelle.infrastructure.hibernatejpa.entities;

import ci.komobe.actionelle.domain.valueobjects.PuissanceFiscale;
import ci.komobe.actionelle.domain.valueobjects.TypeBaseCalcul;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.converters.PuissanceFiscaleConverter;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.embeddables.PrimeEmbeddable;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "garanties")
public class GarantieEntityJpa {

  @Id
  private String id;

  private String libelle;
  private String description;
  private String code;

  @Convert(converter = PuissanceFiscaleConverter.class)
  private PuissanceFiscale puissanceFiscale;

  @Enumerated(EnumType.STRING)
  private TypeBaseCalcul baseDeCalcul;

  @Embedded
  @AttributeOverride(name = "valeur", column = @Column(name = "prime_valeur"))
  @AttributeOverride(name = "type", column = @Column(name = "prime_type"))
  private PrimeEmbeddable prime;

  @Embedded
  @AttributeOverride(name = "valeur", column = @Column(name = "prime_minimum_valeur"))
  @AttributeOverride(name = "type", column = @Column(name = "prime_minimum_type"))
  private PrimeEmbeddable primeMinimum;

  private int maxAge;
  private boolean plafonne;

  @ManyToMany(mappedBy = "garanties", fetch = FetchType.LAZY)
  private List<ProduitEntityJpa> produits = new ArrayList<>();
}

