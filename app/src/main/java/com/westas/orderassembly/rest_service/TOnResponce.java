package com.westas.orderassembly.rest_service;

public interface TOnResponce_<T> {
    void OnSuccess(TResponce_<T> responce);
    void OnFailure(Throwable t);
}
