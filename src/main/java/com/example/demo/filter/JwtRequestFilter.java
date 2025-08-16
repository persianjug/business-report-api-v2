// package com.example.demo.filter;

// import java.io.IOException;

// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.example.demo.domain.service.UserDetailsServiceImpl;
// import com.example.demo.util.JwtTokenUtil;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;

// // OncePerRequestFilterを継承することで、リクエストごとに一度だけフィルターが実行
// @Component
// @RequiredArgsConstructor
// public class JwtRequestFilter extends OncePerRequestFilter {
//   private final UserDetailsServiceImpl userDetailsService;
//   private final JwtTokenUtil jwtTokenUtil;

  
//   @Override
//   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//       throws ServletException, IOException {
//     final String requestTokenHeader = request.getHeader("Authorization");

//     String username = null;
//     String jwtToken = null;

//     // リクエストヘッダーからJWTトークンを取得
//     if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//       jwtToken = requestTokenHeader.substring(7);
//       try {
//         username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//       } catch (Exception e) {
//         // トークンが無効な場合は何もしない
//       }
//     } else {
//       logger.warn("JWT Token does not begin with Bearer String");
//     }

//     // トークンから取得したユーザー名が有効かつ、まだ認証されていない場合
//     if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//       UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

//       // トークンが有効な場合、認証情報を設定
//       if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
//         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//             userDetails, null, userDetails.getAuthorities());
//         usernamePasswordAuthenticationToken
//             .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//         // SecurityContextに認証情報を設定
//         SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//       }
//     }
//     filterChain.doFilter(request, response);
//   }

// }
