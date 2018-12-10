package site.ownw.authserver.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author sofior
 * @date 2018/9/6 11:10
 */
@Data
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Log implements Serializable {

    private static final long serialVersionUID = -7302648324366410547L;

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 访问的URL
     */
    private String path;

    /**
     * 访问者IP
     */
    private String ip;

    /**
     * 访问时间
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessTime;

    /**
     * 处理请求的方法
     */
    private String handler;

    /**
     * 请求参数
     */
    @Lob
    @Column(columnDefinition="mediumtext")
    private String request;

    /**
     * 响应内容
     */
    @Lob
    @Column(columnDefinition="mediumtext")
    private String response;

    /**
     * 处理耗时,单位毫秒
     */
    private Long processTime;
}
