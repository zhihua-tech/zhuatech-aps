/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.controller;

import cn.zhuatech.aps.service.FiniteCapacityScenarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业级决策 API，调用方应保存请求、响应与审批审计轨迹。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/planning")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class FiniteCapacityScenarioController {
    private final FiniteCapacityScenarioService service;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public FiniteCapacityScenarioController(FiniteCapacityScenarioService service) {
        this.service = service;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/finite-capacity")
    public FiniteCapacityScenarioService.ScenarioResult decide(@Valid @RequestBody FiniteCapacityScenarioService.ScenarioRequest request) {
        return service.evaluate(request);
    }
}
