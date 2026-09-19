/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aps;

import cn.zhuatech.aps.service.OrderPromiseService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class OrderPromiseServiceTests {
    private final OrderPromiseService service = new OrderPromiseService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void confirmsFeasibleOrder() {
        var result = service.evaluate(new OrderPromiseService.Request(
            bd("80"), bd("100"), bd("95"), bd("5"), bd("0"), 7, false));
        assertThat(result.status()).isEqualTo("CONFIRM");
        assertThat(result.capacityGapHours()).isZero();
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void renegotiatesSevereCapacityGap() {
        var result = service.evaluate(new OrderPromiseService.Request(
            bd("120"), bd("60"), bd("70"), bd("15"), bd("0"), 5, false));
        assertThat(result.status()).isEqualTo("RENEGOTIATE");
        assertThat(result.capacityGapHours()).isPositive();
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private BigDecimal bd(String value) { return new BigDecimal(value); }
}
