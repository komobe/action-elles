package ci.komobe.actionelle.infrastructure.views.exceptionhandlers;

import ci.komobe.actionelle.infrastructure.views.dto.ResponseApi;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Moro KONÉ 2025-05-30
 */
@RestControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
      MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType
  ) {
    return !returnType.getParameterType().equals(ResponseApi.class);
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response
  ) {
    if (body instanceof ResponseApi) {
      return body;
    }

    return ResponseApi.success(body);
  }
}

