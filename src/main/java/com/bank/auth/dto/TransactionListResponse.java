package com.bank.auth.dto;

import java.util.List;

public class TransactionListResponse {
    private List<TransactionDto> items;
    private long total;
    private int page;

    public TransactionListResponse(){}
    public TransactionListResponse(List<TransactionDto> items, long total, int page){
        this.items = items; this.total = total; this.page = page;
    }
    public List<TransactionDto> getItems(){ return items; }
    public void setItems(List<TransactionDto> items){ this.items = items; }
    public long getTotal(){ return total; }
    public void setTotal(long total){ this.total = total; }
    public int getPage(){ return page; }
    public void setPage(int page){ this.page = page; }
}
