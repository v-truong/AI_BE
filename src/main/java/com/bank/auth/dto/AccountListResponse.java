package com.bank.auth.dto;

import java.util.List;

public class AccountListResponse {
    private List<AccountListItemDto> items;
    private long total;
    private int page;
    private int size;

    public AccountListResponse(){}

    public AccountListResponse(List<AccountListItemDto> items, long total, int page, int size){
        this.items = items; this.total = total; this.page = page; this.size = size;
    }

    public List<AccountListItemDto> getItems(){ return items; }
    public void setItems(List<AccountListItemDto> items){ this.items = items; }
    public long getTotal(){ return total; }
    public void setTotal(long total){ this.total = total; }
    public int getPage(){ return page; }
    public void setPage(int page){ this.page = page; }
    public int getSize(){ return size; }
    public void setSize(int size){ this.size = size; }
}
