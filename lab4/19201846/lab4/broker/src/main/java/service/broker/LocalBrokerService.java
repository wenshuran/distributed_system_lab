package service.broker;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.core.*;

import java.util.*;

@RestController
public class LocalBrokerService {
    private Map<Integer, Quotation> quotations = new HashMap<>();
    private HashMap<Integer, String> urls = new HashMap<>();
    private int auldfellasId = 0;
    private int dodgydriversId = 1;
    private int girlpowerId = 2;
    {
        urls.put(auldfellasId, "http://auldfellas:8080/quotations");
        urls.put(dodgydriversId, "http://dodgydrivers:8081/quotations");
        urls.put(girlpowerId, "http://girlpower:8082/quotations");
    }

    private static int applicationNumber = 0;
    private static HashMap<Integer, ClientApplication> clientApplicationHashMap = new HashMap<>();

    @RequestMapping(value="/applications",method = RequestMethod.POST)
    public ClientApplication getQuotations(@RequestBody ClientInfo info) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ClientInfo> request = new HttpEntity<>(info);
        ArrayList<Quotation> quotations = new ArrayList<>();
        quotations.add(restTemplate.postForObject(urls.get(auldfellasId), request, Quotation.class));
        quotations.add(restTemplate.postForObject(urls.get(dodgydriversId), request, Quotation.class));
        quotations.add(restTemplate.postForObject(urls.get(girlpowerId), request, Quotation.class));
        ClientApplication clientApplication = new ClientApplication(applicationNumber, quotations, info);
        clientApplicationHashMap.put(applicationNumber++, clientApplication);
        return clientApplication;
    }

    @RequestMapping(value="/applications/{application-number}",method=RequestMethod.GET)
    public ClientApplication getResource(@PathVariable("application-number") String applicationNumber) {
        if (clientApplicationHashMap.containsKey(Integer.parseInt(applicationNumber))){
            return clientApplicationHashMap.get(Integer.parseInt(applicationNumber));
        }
        else {
            throw new NoSuchQuotationException();
        }
    }

    @RequestMapping(value="/applications",method=RequestMethod.GET)
    public ArrayList<ClientApplication> listApplications() {
        ArrayList<ClientApplication> list = new ArrayList<>();
        for (ClientApplication value : clientApplicationHashMap.values()){
            list.add(value);
        }
        return list;
    }


}
