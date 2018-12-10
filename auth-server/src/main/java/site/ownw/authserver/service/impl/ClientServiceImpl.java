package site.ownw.authserver.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import site.ownw.authserver.entity.Client;
import site.ownw.authserver.model.request.ClientRequestModel;
import site.ownw.authserver.model.response.ClientResponseModel;
import site.ownw.authserver.repository.ClientRepository;
import site.ownw.authserver.repository.RoleRepository;
import site.ownw.authserver.repository.ScopeRepository;
import site.ownw.authserver.service.ClientService;
import site.ownw.authserver.util.BeanUtils;
import site.ownw.authserver.util.UUIDUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sofior
 * @date 2018/10/25 16:12
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ScopeRepository scopeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository.findFirstByClientId(clientId).orElseThrow(() -> new RuntimeException(clientId + " Not Found"));
    }

    @Override
    public Page<Client> findClients(Integer page, Integer size) {
        return clientRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public ClientResponseModel saveClient(ClientRequestModel clientRequestModel) {
        ClientResponseModel responseModel = new ClientResponseModel();
        if (clientRequestModel.isNew()) {
            responseModel.setClient(new Client());
            BeanUtils.copyProperties(clientRequestModel, responseModel.getClient());
            responseModel.getClient().setClientId(UUIDUtils.generateUUID());
            String secret = UUIDUtils.generateUUID();
            responseModel.getClient().setClientSecret(encodeSecret(secret));
            responseModel.getClient().setAuthorizedGrantTypes(List.of("authorization_code"));
            responseModel.setSecret(secret);
        } else {
            var optional = clientRepository.findById(clientRequestModel.getId());
            BeanUtils.copyProperties(clientRequestModel, optional.orElseThrow(() -> new RuntimeException("修改失败")), BeanUtils.findNullFields(clientRequestModel));
            responseModel.setClient(optional.get());
        }
        responseModel.getClient().setScopes(new ArrayList<>(clientRequestModel.getScopes().size()));
        for (Long scopeId : clientRequestModel.getScopes()) {
            scopeRepository.findById(scopeId).ifPresent(scope -> responseModel.getClient().getScopes().add(scope));
        }

        responseModel.getClient().setRoles(new ArrayList<>(clientRequestModel.getRoles().size()));
        for (Long roleId : clientRequestModel.getRoles()) {
            roleRepository.findById(roleId).ifPresent(role -> responseModel.getClient().getRoles().add(role));
        }

        responseModel.setClient(clientRepository.save(responseModel.getClient()));
        return responseModel;
    }

    @Override
    public Client delClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("删除失败"));
        clientRepository.deleteById(clientId);
        return client;
    }

    private String encodeSecret(@NonNull final String password) {
        return "{bcrypt}" + passwordEncoder.encode(password);
    }

}
