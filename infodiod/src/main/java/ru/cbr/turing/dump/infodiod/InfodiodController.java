package ru.cbr.turing.dump.infodiod;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cbr.turing.dump.infodiod.client.InfodiodServiceClient;

import java.util.List;

@RestController
public class InfodiodController {

    @Autowired private Application.Config config;
    @Autowired private StorageService storageService;

    @RequestMapping("/")
    public String index(){
        return config.getVersion() + " Fake infodiod ready to serve!";
    }

    @RequestMapping(value = "/dumpCreated", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public InfodiodServiceClient.SendDumpByInfodiodResponse dumpCreated(@RequestBody InfodiodServiceClient.Order order){
        String message = "created dump sended to infodiod service, order num: " + order.getOrderNum();
        List<String> fileNames = storageService.filesCreatedByOrder(order.getOrderNum());
        return new InfodiodServiceClient.SendDumpByInfodiodResponse.DumpSendedResponse(message, fileNames);
    }
}
