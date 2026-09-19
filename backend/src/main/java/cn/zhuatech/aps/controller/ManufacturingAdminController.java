/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.controller; import cn.zhuatech.aps.common.ApiResponse; import cn.zhuatech.aps.dto.ApsDto.*; import cn.zhuatech.aps.service.ApsService; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/admin") @PreAuthorize("hasAnyRole('PLANNER','QUALITY','ADMIN')") public class ManufacturingAdminController {private final ApsService aps;/**
                                                                                                                                                                                * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                */
public ManufacturingAdminController(ApsService aps){this.aps=aps;}/**
                                                                                                                                                                                                                                                  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                  */
@GetMapping("/dashboard") public ApiResponse<Dashboard> dashboard(){return ApiResponse.ok(aps.adminDashboard());}/**
                                                                                                                                                                                                                                                                                                                                                                   * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                                                                                                   */
@GetMapping("/work-orders") public ApiResponse<List<WorkOrderView>> orders(){return ApiResponse.ok(aps.workOrders());}}
