package site.ownw.authserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * @author sofior
 * @date 2018/8/16 23:41
 */
@Data
@Cacheable
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class BaseEntity implements Auditable<Long, Long, LocalDateTime>, Serializable {

    private static final long serialVersionUID = 9029988945001067700L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable
    Long id;

    @JsonIgnore
    private @Nullable
    Long createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private @Nullable
    Date createdDate;

    @JsonIgnore
    private @Nullable
    Long lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private @Nullable
    Date lastModifiedDate;

    @Override
    public Optional<Long> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Optional<LocalDateTime> getCreatedDate() {
        return null == createdDate ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(createdDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = Date.from(createdDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Optional<Long> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return null == lastModifiedDate ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(lastModifiedDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = Date.from(lastModifiedDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }
}
