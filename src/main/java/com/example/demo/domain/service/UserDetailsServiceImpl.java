// package com.example.demo.domain.service;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Configuration
// @Service
// public class UserDetailsServiceImpl implements UserDetailsService {

//   @Override
//   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//     // この部分はデータベースからユーザー情報を取得するロジックに置き換える
//     if ("user".equals(username)) {
//       // パスワードをハッシュ化して設定
//       return User.builder()
//           .username("user")
//           .password(passwordEncoder().encode("password"))
//           .roles("USER")
//           .build();
//     }
//     throw new UsernameNotFoundException("User not found: " + username);
//   }

//   // パスワードをハッシュ化
//   @Bean
//   public PasswordEncoder passwordEncoder() {
//     return new BCryptPasswordEncoder();
//   }
// }
