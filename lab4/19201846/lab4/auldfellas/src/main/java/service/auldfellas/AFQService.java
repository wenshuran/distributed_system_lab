package service.auldfellas;

import org.springframework.web.bind.annotation.*;
import service.core.AbstractQuotationService;
import service.core.ClientInfo;
import service.core.NoSuchQuotationException;
import service.core.Quotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
@RestController
public class AFQService extends AbstractQuotationService {
	// All references are to be prefixed with an AF (e.g. AF001000)
	public static final String PREFIX = "AF";
	public static final String COMPANY = "Auld Fellas Ltd.";

	private Map<String, Quotation> quotations = new HashMap<>();

	@RequestMapping(value="/quotations",method= RequestMethod.POST)
	public Quotation createQuotation(@RequestBody ClientInfo info) {
		Quotation quotation = generateQuotation(info);
		quotations.put(quotation.getReference(), quotation);
		return quotation;
	}

	@RequestMapping(value="/quotations/{reference}",method=RequestMethod.GET)
	public Quotation getResource(@PathVariable("reference") String reference) {
		Quotation quotation = quotations.get(reference);
		if (quotation == null) throw new NoSuchQuotationException();
		return quotation;
	}

	@RequestMapping(value="/quotations",method=RequestMethod.GET)
	public ArrayList<Quotation> listQuotations() {
		ArrayList<Quotation> list = new ArrayList<>();
		for(Quotation quotation: quotations.values())
			list.add(quotation);
		return list;
	}

	/**
	 * Quote generation:
	 * 30% discount for being male
	 * 2% discount per year over 60
	 * 20% discount for less than 3 penalty points
	 * 50% penalty (i.e. reduction in discount) for more than 60 penalty points 
	 */
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 600 and 1200
		double price = generatePrice(600, 600);
		
		// Automatic 30% discount for being male
		int discount = (info.getGender() == ClientInfo.MALE) ? 30:0;
		
		// Automatic 2% discount per year over 60...
		discount += (info.getAge() > 60) ? (2*(info.getAge()-60)) : 0;
		
		// Add a points discount
		discount += getPointsDiscount(info);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getPointsDiscount(ClientInfo info) {
		if (info.getPoints() < 3) return 20;
		if (info.getPoints() <= 6) return 0;
		return -50;
		
	}

}
