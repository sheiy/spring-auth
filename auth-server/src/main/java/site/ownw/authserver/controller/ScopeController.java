package site.ownw.authserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import site.ownw.authserver.entity.Client;
import site.ownw.authserver.entity.Scope;
import site.ownw.authserver.model.request.ScopeRequestModel;
import site.ownw.authserver.service.ScopeService;

/**
 * @author sofior
 * @date 2018/11/12 14:07
 */
@RestController
@RequestMapping("/scope")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScopeController {

    private final ScopeService scopeService;

    @GetMapping("/findScopes")
    public Page<Scope> findScopes(@RequestParam(required = false, defaultValue = "0") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return scopeService.findScopes(page, size);
    }

    @PostMapping("/saveScope")
    public Scope saveScope(@RequestBody ScopeRequestModel scope) {
        return scopeService.saveScope(scope);
    }

    @DeleteMapping("/{scopeId}")
    public Scope delScope(@PathVariable Long scopeId) {
        return scopeService.delScope(scopeId);
    }
}
