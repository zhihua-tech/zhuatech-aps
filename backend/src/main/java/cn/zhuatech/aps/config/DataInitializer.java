/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.aps.config;
import cn.zhuatech.aps.model.*; import cn.zhuatech.aps.repository.*; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.*; import org.springframework.security.crypto.password.PasswordEncoder; import java.time.LocalDate; import java.util.List;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Configuration public class DataInitializer {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Bean CommandLineRunner seed(WorkCenterRepository centers,WorkOrderRepository orders,EquipmentRepository equipment,InspectionRepository inspections,UserRepository users,PasswordEncoder encoder){return args->{
        if(centers.count()>0)return;
        WorkCenter assembly=centers.save(new WorkCenter("WC-AS-01","总装一线","装配车间",2400)); WorkCenter machining=centers.save(new WorkCenter("WC-MC-02","精加工二线","机加车间",1800)); WorkCenter testing=centers.save(new WorkCenter("WC-TS-01","功能测试线","检测车间",2200));
        WorkOrder w1=orders.save(new WorkOrder("MO-20260729-018","FG-AX120","智能执行器 AX120",assembly,1200,864,12,LocalDate.now().plusDays(1),WorkOrder.Status.RUNNING,"B260729A"));
        WorkOrder w2=orders.save(new WorkOrder("MO-20260729-021","FG-DR80","精密减速器 DR80",machining,680,272,7,LocalDate.now().plusDays(2),WorkOrder.Status.RUNNING,"B260729C"));
        WorkOrder w3=orders.save(new WorkOrder("MO-20260730-006","FG-IO16","工业采集模块 IO16",assembly,1500,0,0,LocalDate.now().plusDays(3),WorkOrder.Status.RELEASED,"B260730B"));
        WorkOrder w4=orders.save(new WorkOrder("MO-20260728-015","FG-SV40","伺服驱动器 SV40",testing,960,960,9,LocalDate.now(),WorkOrder.Status.COMPLETED,"B260728D"));
        equipment.saveAll(List.of(new Equipment("EQ-AS-011","一号自动装配单元",assembly,Equipment.Status.RUNNING,86),new Equipment("EQ-AS-014","扭矩锁付工作站",assembly,Equipment.Status.RUNNING,91),new Equipment("EQ-MC-027","五轴加工中心",machining,Equipment.Status.ALARM,64),new Equipment("EQ-TS-006","综合性能测试台",testing,Equipment.Status.IDLE,78)));
        inspections.saveAll(List.of(new Inspection("IPQC-260729-032",w1,"工序巡检",20,0,Inspection.Result.PASSED,"周妍"),new Inspection("FAI-260729-011",w2,"首件检验",5,1,Inspection.Result.FAILED,"周妍"),new Inspection("FQC-260729-018",w4,"完工检验",32,0,Inspection.Result.PASSED,"陆承"),new Inspection("FAI-260730-003",w3,"首件检验",5,0,Inspection.Result.PENDING,"陆承")));
        String demo=encoder.encode("Demo@2026");users.saveAll(List.of(new UserAccount("operator",demo,"徐师傅",UserAccount.Role.OPERATOR,"WC-AS-01"),new UserAccount("planner",demo,"沈清和",UserAccount.Role.PLANNER,null),new UserAccount("quality",demo,"周妍",UserAccount.Role.QUALITY,null),new UserAccount("admin",encoder.encode("ZhuaTech@2026"),"系统管理员",UserAccount.Role.ADMIN,null)));
    };}
}
