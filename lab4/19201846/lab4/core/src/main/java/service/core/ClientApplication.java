package service.core;

import java.util.ArrayList;

public class ClientApplication {
    private int applicationNumber;
    private ClientInfo info;
    private ArrayList<Quotation> quotations;

    public ClientApplication(){}

    public ClientApplication(int applicationNumber, ArrayList<Quotation> quotations, ClientInfo info){
        this.info = info;
        this.quotations = quotations;
        this.applicationNumber = applicationNumber;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.info = clientInfo;
    }

    public void setId(int id) {
        this.applicationNumber = id;
    }

    public void setQuotations(ArrayList<Quotation> quotations) {
        this.quotations = quotations;
    }

    public ArrayList<Quotation> getQuotations() {
        return quotations;
    }

    public ClientInfo getClientInfo() {
        return info;
    }

    public int getId() {
        return applicationNumber;
    }
}

