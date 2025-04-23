package com.conference.interceptor;

import com.conference.utils.JwtUtil;
import com.conference.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 令牌验证，测试时需要将token放入请求头
        String token = request.getHeader("Authorization");
        // 验证token
        try {
            // 验证成功
            Map<String, Object> claims = JwtUtil.parseToken(token);

            // 将业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            // 验证失败
            // http状态码为401
            e.printStackTrace();
            response.setStatus(401);
            return false;
        }
    }

    // 响应完成后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在该方法中清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
