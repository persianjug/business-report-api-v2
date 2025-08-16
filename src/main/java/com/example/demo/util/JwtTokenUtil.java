// package com.example.demo.util;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Component;

// import java.security.Key;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.function.Function;

// @Component
// public class JwtTokenUtil {
//   // JWTの署名に使用する秘密鍵
//   @Value("${jwt.secret}")
//   private String secret;

//   // JWTの有効期限（ミリ秒）
//   @Value("${jwt.expiration}")
//   private long expiration;

//   // ユーザー名を取得
//   public String getUsernameFromToken(String token) {
//     return getClaimFromToken(token, Claims::getSubject);
//   }

//   // トークンの有効期限を取得
//   public Date getExpirationDateFromToken(String token) {
//     return getClaimFromToken(token, Claims::getExpiration);
//   }

//   public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//     final Claims claims = getAllClaimsFromToken(token);
//     return claimsResolver.apply(claims);
//   }

//   // トークンからすべてのクレームを取得
//   private Claims getAllClaimsFromToken(String token) {
//     return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
//   }

//   // トークンの有効性をチェック
//   private Boolean isTokenExpired(String token) {
//     final Date expiration = getExpirationDateFromToken(token);
//     return expiration.before(new Date());
//   }

//   // トークンの生成
//   public String generateToken(UserDetails userDetails) {
//     Map<String, Object> claims = new HashMap<>();
//     return doGenerateToken(claims, userDetails.getUsername());
//   }

//   private String doGenerateToken(Map<String, Object> claims, String subject) {
//     return Jwts.builder()
//         .setClaims(claims)
//         .setSubject(subject)
//         .setIssuedAt(new Date(System.currentTimeMillis()))
//         .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
//         .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//         .compact();
//   }

//   // トークンの検証
//   public Boolean validateToken(String token, UserDetails userDetails) {
//     final String username = getUsernameFromToken(token);
//     return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//   }

//   // 秘密鍵を生成
//   private Key getSigningKey() {
//     byte[] keyBytes = Decoders.BASE64.decode(this.secret);
//     return Keys.hmacShaKeyFor(keyBytes);
//   }

// }
