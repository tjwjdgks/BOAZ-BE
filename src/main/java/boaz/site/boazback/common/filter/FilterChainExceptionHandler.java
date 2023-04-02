package boaz.site.boazback.common.filter;

import boaz.site.boazback.common.exception.ErrorCode;
import boaz.site.boazback.common.exception.ErrorResponse;
import boaz.site.boazback.common.exception.GeoIpException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(value = Integer.MIN_VALUE)
@RequiredArgsConstructor
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (GeoIpException ex){
            log.error("GeoIpException handler filter");
            setErrorResponse(response,ex);
        }catch (RuntimeException ex){
            log.error("runtime exception exception handler filter");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,response,ex);
        }
    }

    public void setErrorResponse(HttpServletResponse response,GeoIpException ex){
        ErrorCode errorCode = ex.getErrorCode();
        response.setStatus(errorCode.getStatusCode());
        response.setContentType("application/json");
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        try{
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private ErrorResponse getErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.create()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatusCode());
    }


    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex){
        response.setStatus(status.value());
        response.setContentType("application/json");
        ErrorResponse errorResponse = getErrorResponse(ErrorCode.SEVER_ERROR);
        errorResponse.message(ex.getMessage());
        try{
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }
}
