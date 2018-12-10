package site.ownw.authserver.service;


import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import site.ownw.authserver.entity.Client;
import site.ownw.authserver.model.request.ClientRequestModel;
import site.ownw.authserver.model.response.ClientResponseModel;

/**
 * @author sofior
 * @date 2018/10/25 16:12
 */
public interface ClientService extends ClientDetailsService {

    /**
     * 分页查询
     *
     * @param page 第几页，从0开始
     * @param size 页面大小
     * @return 分页信息
     */
    Page<Client> findClients(Integer page, Integer size);

    /**
     * 新增修改Client
     *
     * @param clientRequestModel 需要新增和修改的Client
     * @return 需要新增和修改后的Client
     */
    ClientResponseModel saveClient(ClientRequestModel clientRequestModel);

    /**
     * 删除Client
     *
     * @param clientId 要删除的Client的ID
     * @return 被删除的Client
     */
    Client delClient(Long clientId);
}
