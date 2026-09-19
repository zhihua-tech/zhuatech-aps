/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.service;
import cn.zhuatech.aps.common.BusinessException; import cn.zhuatech.aps.dto.ApsDto.*; import cn.zhuatech.aps.model.*; import cn.zhuatech.aps.repository.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service @Transactional(readOnly=true) public class ApsService {
    private final WorkOrderRepository orders; private final OperationReportRepository reports; private final EquipmentRepository equipment; private final InspectionRepository inspections; private final CurrentUserService current;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ApsService(WorkOrderRepository orders,OperationReportRepository reports,EquipmentRepository equipment,InspectionRepository inspections,CurrentUserService current){this.orders=orders;this.reports=reports;this.equipment=equipment;this.inspections=inspections;this.current=current;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Dashboard shopfloorDashboard(){String center=current.get().getWorkCenterCode();List<WorkOrder> list=center==null?orders.findAllByOrderByDueDateAsc():orders.findByWorkCenterCodeOrderByDueDateAsc(center);return dashboard(list);}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Dashboard adminDashboard(){return dashboard(orders.findAllByOrderByDueDateAsc());}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public List<WorkOrderView> workOrders(){return orders.findAllByOrderByDueDateAsc().stream().map(this::view).toList();}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional public ReportResult report(Long id,ReportRequest request){WorkOrder order=orders.findById(id).orElseThrow(()->new BusinessException("计划订单不存在"));if(order.getStatus()==WorkOrder.Status.COMPLETED)throw new BusinessException("已完工工单不能继续反馈");if(order.getCompletedQty()+request.goodQty()>order.getPlannedQty())throw new BusinessException("合格数量不能超过工单剩余数量");order.report(request.goodQty(),request.defectQty());reports.save(new OperationReport(order,request.operationName(),request.goodQty(),request.defectQty(),current.get().getFullName(),request.remark()));return new ReportResult(order.getOrderNo(),order.getCompletedQty(),order.getDefectQty(),progress(order),order.getStatus().name());}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private Dashboard dashboard(List<WorkOrder> list){int planned=list.stream().mapToInt(WorkOrder::getPlannedQty).sum(),done=list.stream().mapToInt(WorkOrder::getCompletedQty).sum(),defects=list.stream().mapToInt(WorkOrder::getDefectQty).sum();int rate=planned==0?0:Math.round(done*100f/planned);List<Metric> metrics=List.of(new Metric("今日计划",String.format("%,d",planned),list.size()+" 张计划订单","blue"),new Metric("生产达成率",rate+"%",String.format("%,d / %,d",done,planned),"green"),new Metric("一次合格率",String.format("%.1f%%",done+defects==0?100d:done*100d/(done+defects)),defects+" 件不良","warn"),new Metric("资源异常",equipment.countByStatus(Equipment.Status.ALARM)+"",inspections.countByResult(Inspection.Result.PENDING)+" 项待检","red"));return new Dashboard(metrics,list.stream().map(this::view).toList(),equipment.findAllByOrderByCodeAsc().stream().map(e->new EquipmentView(e.getCode(),e.getName(),e.getWorkCenter().getName(),e.getStatus().name(),e.getOee(),e.getLastHeartbeat())).toList(),inspections.findTop10ByOrderByIdDesc().stream().map(i->new InspectionView(i.getInspectionNo(),i.getWorkOrder().getOrderNo(),i.getWorkOrder().getProductName(),i.getInspectionType(),i.getSampleQty(),i.getDefectQty(),i.getResult().name(),i.getInspector())).toList());}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private WorkOrderView view(WorkOrder o){return new WorkOrderView(o.getId(),o.getOrderNo(),o.getProductCode(),o.getProductName(),o.getWorkCenter().getName(),o.getWorkCenter().getWorkshop(),o.getPlannedQty(),o.getCompletedQty(),o.getDefectQty(),o.getDueDate(),o.getStatus().name(),o.getBatchNo(),progress(o));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private int progress(WorkOrder o){return o.getPlannedQty()==0?0:Math.min(100,Math.round(o.getCompletedQty()*100f/o.getPlannedQty()));}
}
