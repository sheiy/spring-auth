package site.ownw.authserver.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.ownw.authserver.entity.Client;

/**
 * @author Sofior
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseModel {

    private Client client;

    private String secret;

}
