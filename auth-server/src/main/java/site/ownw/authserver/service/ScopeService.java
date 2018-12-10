package site.ownw.authserver.service;

import org.springframework.data.domain.Page;
import site.ownw.authserver.entity.Scope;
import site.ownw.authserver.model.request.ScopeRequestModel;

import java.util.Optional;

/**
 * @author sofior
 * @date 2018/11/12 14:08
 */
public interface ScopeService {

    /**
     * 分页查询
     *
     * @param page 第几页，从0开始
     * @param size 页面大小
     * @return 分页信息
     */
    Page<Scope> findScopes(Integer page, Integer size);

    /**
     * 新增修改Scope
     *
     * @param scope 需要新增和修改的Scope
     * @return 需要新增和修改后的Scope
     */
    Scope saveScope(ScopeRequestModel scope);

    /**
     * 获取Scope
     *
     * @param scopeName 需要获取的Scope名称
     * @return Scope
     */
    Optional<Scope> loadScopeByScopeName(String scopeName);

    /**
     * 删除Scope
     *
     * @param scopeId 需要删除的Scope的Id
     * @return 被删除的Scope
     */
    Scope delScope(Long scopeId);
}
