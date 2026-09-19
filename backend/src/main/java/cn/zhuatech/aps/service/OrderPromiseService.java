/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class OrderPromiseService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(
        @DecimalMin("0.01") BigDecimal requiredHours,
        @DecimalMin("0.0") BigDecimal availableCapacityHours,
        @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal materialReadyRate,
        @DecimalMin("0.0") BigDecimal bottleneckQueueHours,
        @DecimalMin("0.0") BigDecimal alternativeCapacityHours,
        @Min(0) int dueInDays,
        @NotNull Boolean rushOrder
    ) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String status, BigDecimal executableHours, BigDecimal capacityGapHours,
                         BigDecimal promiseConfidence, List<String> actions) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result evaluate(Request request) {
        BigDecimal effectiveCapacity = request.availableCapacityHours()
            .add(request.alternativeCapacityHours())
            .subtract(request.bottleneckQueueHours()).max(BigDecimal.ZERO);
        BigDecimal executable = effectiveCapacity.multiply(request.materialReadyRate())
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal gap = request.requiredHours().subtract(executable).max(BigDecimal.ZERO)
            .setScale(2, RoundingMode.HALF_UP);
        BigDecimal confidence = executable.divide(request.requiredHours(), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100)).min(BigDecimal.valueOf(100))
            .setScale(1, RoundingMode.HALF_UP);

        List<String> actions = new ArrayList<>();
        String status;
        if (gap.signum() == 0 && request.materialReadyRate().compareTo(BigDecimal.valueOf(90)) >= 0) {
            status = "CONFIRM";
            actions.add("锁定承诺日期并冻结关键物料");
        } else if (confidence.compareTo(BigDecimal.valueOf(75)) >= 0 || Boolean.TRUE.equals(request.rushOrder())) {
            status = "EXPEDITE";
            actions.add("启用替代产能并压缩瓶颈队列");
            actions.add("每日跟踪物料齐套率和剩余缺口");
        } else {
            status = "RENEGOTIATE";
            actions.add("重新协商交期或拆分批次交付");
            actions.add("评估外协产能与替代物料方案");
        }
        if (request.dueInDays() <= 2 && gap.signum() > 0) actions.add("触发临期订单升级机制");
        return new Result(status, executable, gap, confidence, List.copyOf(actions));
    }
}
