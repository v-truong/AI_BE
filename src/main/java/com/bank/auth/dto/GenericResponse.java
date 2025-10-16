package com.bank.auth.dto;

public class GenericResponse {
    private boolean success;
    public GenericResponse(){}
    public GenericResponse(boolean success){ this.success = success; }
    public boolean isSuccess(){ return success; }
    public void setSuccess(boolean success){ this.success = success; }
}
