package everTale.everTale_be.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import everTale.everTale_be.auth.service.TokenAuthService;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final TokenAuthService tokenAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException{

        String token = jwtProvider.resolveToken(request);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                tokenAuthService.validateNotBlackListed(token);
                Authentication authentication;

                if (jwtProvider.isTokenContainsProfileId(token)) {
                    // 프로필 정보가 포함된 토큰인 경우
                    authentication = jwtProvider.getAuthenticationWithProfile(token);
                } else {
                    // 로그인만 된 상태의 토큰인 경우
                    authentication = jwtProvider.getAuthentication(token);
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (BadRequestHandler e) {
            setErrorResponse(response, e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void setErrorResponse(HttpServletResponse response, String code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        // ApiResponse 구조랑 맞추기
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(Map.of(
                "isSuccess", false,
                "code", code,
                "message", message
        ));
        response.getWriter().write(json);
    }
}
