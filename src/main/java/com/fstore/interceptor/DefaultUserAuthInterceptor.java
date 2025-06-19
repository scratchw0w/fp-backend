package com.fstore.interceptor;

import com.fstore.model.UserAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.fstore.service.utility.JwtUtils.getJwtFromCookies;
import static com.fstore.service.utility.JwtUtils.getUsernameFromJwtToken;

@Slf4j
public class DefaultUserAuthInterceptor implements UserAuthInterceptor {
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        String jwt = getJwtFromCookies(request);

        if (Objects.nonNull(jwt) && !jwt.isEmpty()) {
            String userId = getUsernameFromJwtToken(jwt);

            log.debug("userId: {}", userId);
            request.setAttribute("auth", new UserAuth(userId));
        }

        return true;
    }
}
