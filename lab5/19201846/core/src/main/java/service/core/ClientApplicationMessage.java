package service.core;

import java.util.ArrayList;

public class ClientApplicationMessage implements java.io.Serializable{
    public long clientID;
    public ClientInfo clientInfo;
    public ArrayList<Quotation> quotations = new ArrayList<>();
    public ClientApplicationMessage(long clientID, ClientInfo clientInfo, Quotation quotation){
        this.clientID = clientID;
        this.clientInfo = clientInfo;
        this.quotations.add(quotation);
    }

    public ClientApplicationMessage(){}

    public void addQuotation(Quotation quotation){
        this.quotations.add(quotation);
    }
}
