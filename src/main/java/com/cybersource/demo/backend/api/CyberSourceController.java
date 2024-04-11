package com.cybersource.demo.backend.api;

import com.cybersource.demo.backend.service.CyberSourceService;
import com.cybersource.ws.client.ClientException;
import com.cybersource.ws.client.FaultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CyberSourceController {

    @Autowired
    private CyberSourceService cyberSourceService;

    @RequestMapping(value = "/api/v1/cybersource", method = RequestMethod.GET)
    public String index() {
        return "OK";
    }


    @RequestMapping(value = "/api/v1/cybersource", method = RequestMethod.POST)
    public String stepUpCheck(@RequestParam("TransactionId") String transactionId,
                              @RequestParam("Response") String response,
                              @RequestParam("MD") String md) {
        try {
            return cyberSourceService.doValidate(transactionId);
        } catch (FaultException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(
            value = "/api/v1/cybersource/order", method = RequestMethod.POST
    )
    public String createOrder(@RequestBody Map<String, String> requestData) {
        String cardNumber = requestData.get("cardNumber");
        String year = requestData.get("year");
        String month = requestData.get("month");
        try {
            return cyberSourceService.doSetup(cardNumber, year, month);
        } catch (FaultException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(
            value = "/api/v1/cybersource/enroll", method = RequestMethod.POST
    )
    public String enroll(@RequestBody Map<String, String> requestData) {
        String referenceId = requestData.get("referenceId");
        String cardNumber = requestData.get("cardNumber");
        try {
            return cyberSourceService.doEnroll(cardNumber, referenceId);
        } catch (FaultException | ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
