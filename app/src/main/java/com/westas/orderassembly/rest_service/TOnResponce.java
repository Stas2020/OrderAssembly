package com.westas.orderassembly.rest_service;


public interface TOnResponce<T> {
    void OnSuccess(TResponce<T> responce);
    void OnFailure(Throwable t);
}
