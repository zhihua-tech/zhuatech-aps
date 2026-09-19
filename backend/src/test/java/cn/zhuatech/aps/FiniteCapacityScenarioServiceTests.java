/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps;

import cn.zhuatech.aps.service.FiniteCapacityScenarioService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class FiniteCapacityScenarioServiceTests {
    private final FiniteCapacityScenarioService service = new FiniteCapacityScenarioService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void exposesMaterialDrivenShortage() {
        var result = service.evaluate(new FiniteCapacityScenarioService.ScenarioRequest("APS-01", 1000, 1200,
                new BigDecimal("1"), 60, 60, 60, 120, 70, true));
        assertThat(result.materialCapacity()).isEqualTo(700);
        assertThat(result.shortageQuantity()).isEqualTo(300);
        assertThat(result.decision()).isEqualTo("ESCALATE_CAPACITY");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void releasesFeasibleStandardPlan() {
        var result = service.evaluate(new FiniteCapacityScenarioService.ScenarioRequest("APS-02", 500, 900,
                new BigDecimal("1"), 30, 30, 30, 0, 100, false));
        assertThat(result.feasibleQuantity()).isEqualTo(500);
        assertThat(result.decision()).isEqualTo("RELEASE_STANDARD");
    }
}
