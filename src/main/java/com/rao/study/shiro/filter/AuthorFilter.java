package com.rao.study.shiro.filter;

import com.rao.study.shiro.domain.UserToken;
import com.rao.study.shiro.sql.SqlOperation;
import com.rao.study.shiro.utils.HttpUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 自定义过滤器,使用shiro提供的过滤器可以对指定只有设置是否允许直接访问,自定义的过滤器需要添加到Shiro的ShiroFilterFactoryBean中
 * shiro采用的是servlet中的filter
 *
 * 下面使用filter实现一个权限过滤器
 *
 * 注意:加了/**过滤器后,先经过过滤器,过滤器通过了才会进行@RequiresPermissions 这些权限注解的判断,因为这只是判断用户的角色的权限是否有操作某资源的权限
 */
public class AuthorFilter extends AuthenticatingFilter{ //extends AuthorizationFilter {

    /**
     * 表示指定请求是否允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();
        if(uri.contains("/login")){//表示uri为/login的可以直接访问
            return true;
        }else{

            //可以在这里实现权限的判断,是否有访问的权限的判断,已登录,或者有权限则返回true,否则返回false
//            return super.isAccessAllowed(request, response, mappedValue);//默认是只要没设置的都可以访问,即返回true
            if(NoNeedAuthUtils.hasNoAuthentication(httpServletRequest)){//如果有不需要权限验证的,则直接通过
                return true;
            }else{ //否则需要进行权限验证,通过token进行验证
                return HttpUtils.isValidateToken(httpServletRequest);
            }
        }
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        String token = HttpUtils.getTokenFromRequest(httpServletRequest);
//        return new AuthenticationUserToken(token);
        return null;
    }

    /**
     * 在拒绝访问的时候做处理,比如跳转到指定页面
     * 当isAccessAllowed方法返回false时,执行该方法,对拒绝后的处理
     * 当重启服务,导致会话失效,退出登录时调用@RequiresPermissions处理被拒绝的,也会进入到这个访问被拒绝的处理方法中
     *
     * 这个方法就是用来处理所有被拒绝后的操作
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        //比如拒绝后抛出无权限信息,拒绝后重定向到某一个页面
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setStatus(401);

        httpResponse.getWriter().print("无权访问");
//        return true;//这里返回true的话,则表示在前面拦截后,又给放行了,所以相当于没有拦截,默认为false
        return false;
    }

}
