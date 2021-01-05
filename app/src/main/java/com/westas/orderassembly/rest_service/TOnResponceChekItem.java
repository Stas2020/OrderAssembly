package com.westas.orderassembly.rest_service;

public interface TOnResponceChekItem {
    void OnSuccessResponce(TResponceOfChekItem responce);
    void OnFailureResponce(Throwable t);
}
