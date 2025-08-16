// package com.example.demo.app.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.app.payload.JwtResponse;
// import com.example.demo.app.payload.LoginRequest;
// import com.example.demo.domain.service.UserDetailsServiceImpl;
// import com.example.demo.util.JwtTokenUtil;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/api/auth")
// @RequiredArgsConstructor
// public class AuthenticationController {
//   private final AuthenticationManager authenticationManager;
//   private final JwtTokenUtil jwtTokenUtil;
//   private final UserDetailsServiceImpl userDetailsService;

//   @PostMapping("/login")
//   public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest)
//       throws AuthenticationException {
//     // ユーザー認証
//     authenticationManager
//         .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

//     // ユーザー詳細をロード
//     final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

//     // JWTトークンを生成
//     final String token = jwtTokenUtil.generateToken(userDetails);

//     // トークンをレスポンスとして返す
//     return ResponseEntity.ok(new JwtResponse(token));
//   }

// }
