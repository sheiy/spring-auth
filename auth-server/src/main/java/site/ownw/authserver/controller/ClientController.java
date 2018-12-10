package site.ownw.authserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import site.ownw.authserver.entity.Client;
import site.ownw.authserver.model.request.ClientRequestModel;
import site.ownw.authserver.model.response.ClientResponseModel;
import site.ownw.authserver.service.ClientService;

/**
 * @author sofior
 * @date 2018/11/12 14:07
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/findClients")
    public Page<Client> findClients(@RequestParam(required = false, defaultValue = "0") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        return clientService.findClients(page, size);
    }

    @PostMapping("/saveClient")
    public ClientResponseModel saveClient(@RequestBody ClientRequestModel clientRequestModel) {
        return clientService.saveClient(clientRequestModel);
    }

    @DeleteMapping("/{clientId}")
    public Client delClient(@PathVariable Long clientId) {
        return clientService.delClient(clientId);
    }
}
