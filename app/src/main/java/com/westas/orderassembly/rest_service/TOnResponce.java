package com.westas.orderassembly.rest_service;


public interface TOnResponce {
    void OnSuccessResponce(TResponce responce);
    void OnFailureResponce(Throwable t);
}
