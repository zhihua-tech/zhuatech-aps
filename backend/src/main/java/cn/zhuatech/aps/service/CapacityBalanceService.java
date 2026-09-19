/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class CapacityBalanceService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result analyze(Request request) {
        double effectiveHours = Math.max(.1, request.availableHours() - request.maintenanceHours());
        double demandHours = request.plannedHours() + request.changeoverHours();
        double loadPercent = Math.round(demandHours / effectiveHours * 10000.0) / 100.0;
        double overloadHours = Math.round(Math.max(0, demandHours - effectiveHours) * 100.0) / 100.0;
        String status = loadPercent >= 120 ? "CRITICAL" : loadPercent > 100 ? "OVERLOADED" : loadPercent >= 85 ? "TIGHT" : "BALANCED";
        List<String> actions = new ArrayList<>();
        if (overloadHours > 0) actions.add("拆分超负荷工单并评估替代工作中心");
        if (request.changeoverHours() >= effectiveHours * .1) actions.add("合并相同工艺批次以减少换型时间");
        if (request.urgentOrders() > 0) actions.add("锁定急单产能并复核普通订单承诺");
        if (actions.isEmpty()) actions.add("保持当前排程并监控负荷变化");
        return new Result(request.workCenter(), effectiveHours, loadPercent, overloadHours, status, actions);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String workCenter, @Positive double availableHours,
                          @Min(0) double plannedHours, @Min(0) int urgentOrders,
                          @Min(0) double changeoverHours, @Min(0) double maintenanceHours) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String workCenter, double effectiveHours, double loadPercent,
                         double overloadHours, String status, List<String> actions) {}
}
