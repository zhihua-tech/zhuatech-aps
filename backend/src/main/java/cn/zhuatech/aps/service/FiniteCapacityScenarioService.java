/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业有限产能场景评估，避免无限产能假设直接下发车间。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class FiniteCapacityScenarioService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ScenarioResult evaluate(ScenarioRequest request) {
        int regularMinutes = Math.max(0, request.availableMinutes() - request.maintenanceMinutes()
                - request.frozenCapacityMinutes() - request.changeoverMinutes());
        int regularCapacity = BigDecimal.valueOf(regularMinutes)
                .divide(request.cycleMinutesPerUnit(), 0, RoundingMode.DOWN).intValue();
        int overtimeCapacity = BigDecimal.valueOf(request.overtimeLimitMinutes())
                .divide(request.cycleMinutesPerUnit(), 0, RoundingMode.DOWN).intValue();
        int materialCapacity = request.demandQuantity() * request.materialReadyPercent() / 100;
        int regularFeasible = Math.min(request.demandQuantity(), Math.min(regularCapacity, materialCapacity));
        int feasibleQuantity = Math.min(request.demandQuantity(),
                Math.min(regularCapacity + overtimeCapacity, materialCapacity));
        int shortage = request.demandQuantity() - feasibleQuantity;
        BigDecimal fillRate = BigDecimal.valueOf(feasibleQuantity * 100L)
                .divide(BigDecimal.valueOf(request.demandQuantity()), 2, RoundingMode.HALF_UP);
        String decision = shortage == 0 && feasibleQuantity == regularFeasible ? "RELEASE_STANDARD"
                : shortage == 0 ? "RELEASE_WITH_OVERTIME"
                : request.highPriority() ? "ESCALATE_CAPACITY" : "RESCHEDULE_SHORTAGE";
        List<String> controls = new ArrayList<>();
        controls.add("排程发布前固化场景版本、需求版本和主数据版本");
        if (request.materialReadyPercent() < 100) controls.add("缺料订单进入齐套跟踪，不得提前报工");
        if (feasibleQuantity > regularFeasible) controls.add("加班产能需生产负责人审批");
        if (shortage > 0) controls.add("缺口 " + shortage + " 件需重排或重新承诺交期");
        return new ScenarioResult(request.planCode(), regularCapacity, overtimeCapacity,
                materialCapacity, feasibleQuantity, shortage, fillRate, decision, List.copyOf(controls));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ScenarioRequest(@NotBlank String planCode, @Min(1) int demandQuantity,
                                  @Min(1) int availableMinutes,
                                  @DecimalMin("0.01") BigDecimal cycleMinutesPerUnit,
                                  @Min(0) int changeoverMinutes, @Min(0) int maintenanceMinutes,
                                  @Min(0) int frozenCapacityMinutes, @Min(0) int overtimeLimitMinutes,
                                  @Min(0) @Max(100) int materialReadyPercent,
                                  boolean highPriority) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ScenarioResult(String planCode, int regularCapacity, int overtimeCapacity,
                                 int materialCapacity, int feasibleQuantity, int shortageQuantity,
                                 BigDecimal fillRate, String decision, List<String> controls) {}
}
