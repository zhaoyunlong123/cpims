package com.javakc.springbootcpims.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Component("JsonWebToken")
// springboot:读取配置文件的信息
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JsonWebToken {
    private String header;
    private String prefix;
    private String secret;
    //颁发者身份标识
    private String issuer;
    //过期时间 默认1小时 3600秒
    private long expiration;
    //记住我 过期时间7天
    private long remember;
    //Claims权限标示符
    private String claimsAuthorities;

    /**
     * 生成认证token
     * @param claims    权限集合
     * @param username  账号
     * @param time      时长
     * @return 认证token
     * createToken：创建一个token
     */
    public String createToken(String username, long time)
    {
        return Jwts.builder()
                //签发标识
                .setIssuer(issuer)
                //权限集合
                //.setClaims(claims)
                //签发账号
                .setSubject(username)
                //设置签发时间
                .setIssuedAt(new Date())
                //设置签名秘钥
                .signWith(SignatureAlgorithm.HS512, secret)
                //设置签发过期时间
                .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                .compact();
    }

    /**
     * 根据token获取账号信息
     * @param token 请求token
     * @return 账号
     */
    public String getUsername(String token){
        return getTokenClaims(token).getSubject();
    }

    /**
     * 根据token验证是否过期
     * @param token 请求token
     * @return 是否过期
     */
    public boolean isExpiration(String token){
        return getTokenClaims(token).getExpiration().before(new Date());
    }

    /**
     * 验证令牌（是不是发送请求的用户的）
     * @param token 令牌
     * @param username 账号
     * @return 验证结果
     */
    public Boolean validateToken(String token, String username) {
        String userName = getUsername(token);
        return (userName.equals(username) && !isExpiration(token));
    }

    /**
     * 根据token获取签署认证信息
     * @param token 请求token
     * @return 签署认证信息
     */
    public Claims getTokenClaims(String token){
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }

    /**
     * 检验令牌
     * @param request 请求参数
     * @param response 响应参数
     * @return 令牌
     */
    public Authentication getAuthenticationFromToken(HttpServletRequest request, HttpServletResponse response)
    {
        Authentication authentication = null;
        String token = getToken(request);
        if(token != null) {
            // 请求令牌不能为空
            if(getAuthentication() == null) {
                // 上下文中Authentication为空
                //Claims在token中包含了用户自定义的信息(权限) 账号, 过期时间
                Claims claims = getTokenClaims(token);
                if(claims == null) {
                    return null;
                }
                String username = claims.getSubject();
                if(username == null) {
                    return null;
                }
                //验证token是否过期
                //添加redis查询, 是否存在
                if(isExpiration(token)) {
//                    String refreshToken = request.getHeader("refresh_token");
//                    if(StringUtils.isEmpty(refreshToken) || isExpiration(refreshToken))
//                    {
                        return null;
//                    }
//                    else
//                    {
//                        createToken( username, expiration);
//                        response.addHeader(header, token);
//                    }
                }
                //从Claims中获取用户权限
//                Object authors = claims.get("authorities");
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                if (!StringUtils.isEmpty(authors) && authors instanceof List) {
//                    for (Object object : (List) authors) {
//                        authorities.add(new SimpleGrantedAuthority((String) object));
//                    }
//                }
                authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            } else {
                if(validateToken(token, getUsername(token))) {
                    // 如果上下文中Authentication非空，且请求令牌合法，直接返回当前登录认证信息
                    authentication = getAuthentication();
                }
            }
        }
        return authentication;
    }

    /**
     * 获取请求token
     * @param request 请求
     * @return 令牌
     */
    public String getToken(HttpServletRequest request) {
        //从请求头中获取header（yml文件中的header: Authorization:获取认证）
        String token = request.getHeader(header);
        //如果获取token不存在，则更换获取的名字
        if(StringUtils.isEmpty(token)) {
            token = request.getHeader("token");
            //contains:是否包含prefix（yml文件中的prefix: "Bearer "）
        } else if(token.contains(prefix)){
            //将多余的删除
            token = token.substring(prefix.length());
        }
        //如果为空返回null，否则返回token
        if("".equals(token)) {
            token = null;
        }
        return token;
    }

    /**
     * 获取当前登录信息
     * @return 认证对象
     */
    public Authentication getAuthentication() {
        if(StringUtils.isEmpty(SecurityContextHolder.getContext())) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
