package ci.komobe.actionelle.infrastructure.rest.controllers.advices;

import ci.komobe.actionelle.domain.utils.paginate.PageResponse;
import ci.komobe.actionelle.infrastructure.rest.controllers.responses.ResponseApi;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Wrapper pour standardiser les réponses de l'API
 *
 * @author Moro KONÉ 2025-05-31
 */
@RestControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof byte[]) {
      return body;
    }
    // Ne pas wrapper les réponses déjà au format ResponseApi ou PageResponse
    if (body instanceof ResponseApi<?> || body instanceof PageResponse<?>) {
      return body;
    }

    return ResponseApi.success(body);

  }
}
