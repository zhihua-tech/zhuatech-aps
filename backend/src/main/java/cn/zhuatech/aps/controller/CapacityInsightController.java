/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.controller;

import cn.zhuatech.aps.common.ApiResponse;
import cn.zhuatech.aps.service.CapacityBalanceService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('PLANNER','ADMIN')")
public class CapacityInsightController {
    private final CapacityBalanceService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public CapacityInsightController(CapacityBalanceService service) { this.service = service; }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/capacity-balance")
    public ApiResponse<CapacityBalanceService.Result> analyze(@Valid @RequestBody CapacityBalanceService.Request request) {
        return ApiResponse.ok(service.analyze(request));
    }
}
