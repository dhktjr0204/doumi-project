package com.example.doumiproject.entity;

import lombok.*;

@Data
public class User {

  private long id;
  private String nickName;
  private String password;
  private String role;
}
