package service.core;

/**
 * Class to store the quotations returned by the quotation services
 * 
 * @author Rem
 *
 */
public class Quotation {
	public Quotation(String company, String reference, double price) {
		this.company = company;
		this.reference = reference;
		this.price = price;
	}

	public Quotation(){}
	
	private String company;
	private String reference;
	private double price;

	public double getPrice() {
		return price;
	}

	public String getCompany() {
		return company;
	}

	public String getReference() {
		return reference;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
