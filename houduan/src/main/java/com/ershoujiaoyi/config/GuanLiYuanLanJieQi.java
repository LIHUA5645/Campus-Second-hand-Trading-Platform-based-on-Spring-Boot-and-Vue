// 作者：肖沐樑 QQ：3387432690
package com.ershoujiaoyi.config;

import com.ershoujiaoyi.entity.YongHu;
import com.ershoujiaoyi.repository.YongHuRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员接口统一鉴权：除登录接口 /admin/denglu 外，所有 /admin/** 请求都必须携带合法的管理员身份
 * （请求头 GuanLiYuanId 对应一个 guanLiYuan=true 的账号），否则一律拒绝。
 * 这样即使前端被绕过（如手动伪造 localStorage），服务端也不会放行任何管理操作。
 */
@Component
public class GuanLiYuanLanJieQi implements HandlerInterceptor {

    @Autowired
    private YongHuRepository yongHuRepository;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String luJing = request.getRequestURI();
        // 登录接口放行；预检请求放行
        if ("OPTIONS".equals(request.getMethod()) || "/admin/denglu".equals(luJing)) {
            return true;
        }
        if (!luJing.startsWith("/admin")) {
            return true;
        }
        String guanLiYuanId = request.getHeader("GuanLiYuanId");
        Long id = null;
        if (guanLiYuanId != null && !guanLiYuanId.isEmpty()) {
            try {
                id = Long.parseLong(guanLiYuanId.trim());
            } catch (NumberFormatException e) {
                id = null;
            }
        }
        YongHu yh = id == null ? null : yongHuRepository.findById(id).orElse(null);
        if (yh == null || yh.getGuanLiYuan() == null || !yh.getGuanLiYuan()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> jieGuo = new HashMap<>();
            jieGuo.put("chengGong", false);
            jieGuo.put("xinXi", "无权限，请使用管理员账号登录");
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(jieGuo));
            return false;
        }
        return true;
    }
}
