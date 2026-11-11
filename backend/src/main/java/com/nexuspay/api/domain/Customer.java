package com.nexuspay.api.domain;
public record Customer(long id,String username,String fullName,String document,String birthDate,String email,String phone,String address,String status) {}
