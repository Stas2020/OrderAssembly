package com.westas.orderassembly.rest_service;

import com.westas.orderassembly.subdivision.ListSubdivision;

public interface TOnResponceSubDivision
{
    void OnSuccess(ListSubdivision list_subdivision);
    void OnFailure(Throwable t);
}
