package com.darwinbox.core.company.objects;

import java.util.HashMap;

public class Ledger {
    private String id;


    private String ledgerCode;
    private String ledgerDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLedgerCode() {
        return ledgerCode;
    }

    public void setLedgerCode(String ledgerCode) {
        this.ledgerCode = ledgerCode;
    }

    public String getLedgerDescription() {
        return ledgerDescription;
    }

    public void setLedgerDescription(String ledgerDescription) {
        this.ledgerDescription = ledgerDescription;
    }



    public HashMap<String,String> toMap(){
        HashMap<String,String> map= new HashMap<>();

        map.put("TenantLedger[ledger_code]",getLedgerCode());
        map.put("TenantLedger[ledger_desc]",getLedgerDescription());

        return map;
    }
}
