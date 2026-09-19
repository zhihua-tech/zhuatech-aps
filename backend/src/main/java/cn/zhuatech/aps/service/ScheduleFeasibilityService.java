/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class ScheduleFeasibilityService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result evaluate(Request request) {
        int netAvailable = Math.max(0, request.availableMinutes() - request.maintenanceMinutes());
        int totalDemand = request.requiredProductionMinutes() + request.changeoverMinutes();
        int shortage = Math.max(0, totalDemand - netAvailable);
        String decision = shortage == 0 ? "FEASIBLE"
            : shortage <= request.overtimeLimitMinutes() ? "USE_OVERTIME" : "RESCHEDULE";
        int projectedDelayMinutes = Math.max(0, shortage - request.overtimeLimitMinutes());
        List<String> actions = new ArrayList<>();
        if (request.changeoverMinutes() > netAvailable * .15) actions.add("合并相同物料或工艺订单以减少换型损失");
        if ("USE_OVERTIME".equals(decision)) actions.add("批准不超过 " + shortage + " 分钟的加班产能");
        if ("RESCHEDULE".equals(decision)) actions.add("转移订单至替代工作中心或调整交期承诺");
        if ("FEASIBLE".equals(decision)) actions.add("锁定排程并下发物料齐套检查");
        return new Result(request.planCode(), netAvailable, totalDemand, shortage,
            projectedDelayMinutes, decision, actions);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String planCode, @Min(0) int requiredProductionMinutes,
                          @Min(1) int availableMinutes, @Min(0) int changeoverMinutes,
                          @Min(0) int maintenanceMinutes, @Min(0) int overtimeLimitMinutes,
                          @Min(1) int dueInHours) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String planCode, int netAvailableMinutes, int totalDemandMinutes,
                         int capacityShortageMinutes, int projectedDelayMinutes,
                         String decision, List<String> actions) {}
}
