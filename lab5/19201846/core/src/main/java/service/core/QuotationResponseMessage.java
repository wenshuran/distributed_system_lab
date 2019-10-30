package service.core;

public class QuotationResponseMessage implements java.io.Serializable {
    public long id;
    public Quotation quotation;

    public QuotationResponseMessage(long id, Quotation quotation) {
        this.id = id;
        this.quotation = quotation;
    }
}
