package ci.komobe.actionelle.infrastructure.persistences.postgres.mappers;

import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.PageRequest;
import ci.komobe.actionelle.domain.utils.sorting.Sort.Direction;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Sort.Order;

/**
 * Mapper pour convertir entre Spring Data Page et notre Page personnalisé
 * 
 * @author Moro KONÉ 2025-05-31
 */
public final class PageMapper {

  private PageMapper() {
  }

  /**
   * Convertit notre PageRequest en PageRequest de Spring Data
   * 
   * @param pageRequest notre PageRequest
   * @return PageRequest de Spring Data
   */
  public static org.springframework.data.domain.PageRequest toSpringPageRequest(
      PageRequest pageRequest) {
    var springSort = org.springframework.data.domain.Sort.by(
        pageRequest.getSorts().stream()
            .map(s -> new Order(
                s.getDirection() == Direction.ASC
                    ? org.springframework.data.domain.Sort.Direction.ASC
                    : org.springframework.data.domain.Sort.Direction.DESC,
                s.getProperty()))
            .toList());

    return org.springframework.data.domain.PageRequest.of(
        pageRequest.getNumber(),
        pageRequest.getSize(),
        springSort);
  }

  /**
   * Convertit une Page Spring Data en notre Page
   * 
   * @param <T>        type des éléments
   * @param springPage Page Spring Data
   * @return notre Page
   */
  public static <T> Page<T> fromSpringPage(org.springframework.data.domain.Page<T> springPage) {
    return Page.<T>builder()
        .data(springPage.getContent())
        .number(springPage.getNumber())
        .size(springPage.getSize())
        .totalElements(springPage.getTotalElements())
        .totalPages(springPage.getTotalPages())
        .build();
  }

  /**
   * Convertit une Page Spring Data en notre Page avec mapping des éléments
   * 
   * @param <T>        type source
   * @param <R>        type cible
   * @param springPage Page Spring Data
   * @param mapper     fonction de mapping
   * @return notre Page
   */
  public static <T, R> Page<R> fromSpringPage(
      org.springframework.data.domain.Page<T> springPage,
      Function<T, R> mapper) {
    List<R> mappedContent = springPage.getContent().stream()
        .map(mapper)
        .toList();

    return Page.<R>builder()
        .data(mappedContent)
        .number(springPage.getNumber())
        .size(springPage.getSize())
        .totalElements(springPage.getTotalElements())
        .totalPages(springPage.getTotalPages())
        .build();
  }
}