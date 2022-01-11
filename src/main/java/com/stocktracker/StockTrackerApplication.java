/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stocktracker;

import com.stocktracker.model.Role;
import com.stocktracker.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
public class StockTrackerApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(StockTrackerApplication.class, args);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CommandLineRunner run(UserService userService) {
    return args -> {
      userService.saveRole(new Role(null, "ROLE_USER"));
      userService.saveRole(new Role(null, "ROLE_MANAGER"));
      userService.saveRole(new Role(null, "ROLE_ADMIN"));
      userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
    };
  }

//  @RequestMapping("/")
//  String index(Map<String, Object> model) {
//    model.put("stock", new Stock());
//    return "index";
//  }
//
//  @RequestMapping("/hello")
//  String hello(Map<String, Object> model) {
//    model.put("science", "E=mc^2: 12 GeV = ");
//    return "hello";
//  }
//
//  @RequestMapping("/add-ticker")
//  String saveTicker(@ModelAttribute Stock stock) {
//    AlphaVantageWebClient client = new AlphaVantageWebClient();
//    client.tickerInformation();
//    return "result";
//  }

}
