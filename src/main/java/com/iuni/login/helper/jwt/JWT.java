package com.iuni.login.helper.jwt;

import com.iuni.login.domain.Authority;
import com.iuni.login.domain.IuniToken;
import com.iuni.login.domain.SignIn;
import com.iuni.login.helper.redis.RedisService;
import com.iuni.login.service.AuthorityService;
import com.iuni.login.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class JWT {
    private static final String SECRET_KEY = "dkdldbslqjtm20231230wjdtlrdhvmsdmfgidgotjekffurksek";


    private final long accessTokenExp = 1000L * 60 * 60;
    //private final long accessTokenExp = 6000;
    private final long refreshExp = 2592000000L;
    private final MemberService memberService;
    private final RedisService redisService;
    private final AuthorityService authorityService;
    public JWT(MemberService memberService,
               AuthorityService authorityService,
               RedisService redisService) {
        this.memberService = memberService;
        this.authorityService = authorityService;
        this.redisService = redisService;
    }

    public IuniToken generate(String email, List<Authority> roles, Long id, String nickName){
        String refreshToken = getRefreshToken(email);
        redisService.setValues(email, id.toString(), Duration.ofMillis(getTokenExpiration(refreshExp).getTime()));

        IuniToken result= new IuniToken();
        result.setRefreshToken(refreshToken);
        result.setAccessToken(getAccessToken(email, roles, id, nickName));
        result.setResult("Success");
        return result;
    }

    public String getIdByRefreshToken(IuniToken data){
        if(validateRefreshToken(data.getRefreshToken())){
            String email = getAccount(data.getRefreshToken());
            return redisService.getValues(email);
        } else {
            return "expired";
        }
    }


    public Date getTokenExpiration(long data){
        Date now = new Date();
        return new Date(now.getTime() + data);
    }

    public String getAccessToken(String email, List<Authority> roles, Long id, String nickName){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", roles);
        claims.put("id", id);
        claims.put("nickName", nickName);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(getTokenExpiration(accessTokenExp))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long getId(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
    }

    public String getRefreshToken(String email){
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(getTokenExpiration(refreshExp))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getAccount(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }



    public Claims getClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuth(String token){
        UserDetails userDetails = memberService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public int getTier(String token){
        String temp = this.getAccount(token);
        return authorityService.getTier("USER");
    }

    public String resolve(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    public boolean validate(String token){
        try{
//            if(!token.substring(0, "Iuni".length()).equalsIgnoreCase("Iuni")){
//                return false;
//            }
//            else {
//                token = token.split(" ")[1].trim();
//            }

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken){
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        }
        catch(Exception e){
            return false;
        }
    }
}