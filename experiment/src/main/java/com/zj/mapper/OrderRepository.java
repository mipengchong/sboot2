//package com.zj.mapper;
//
//import com.zj.model.OrderInfo;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//
//import javax.transaction.Transactional;
//
///**
// * Created with IntelliJ IDEA.
// * User: SuShaohua
// * Date: 2018/6/24
// * Time: 18:47
// * Description: MAIN
// */
//public interface OrderRepository extends JpaRepository<OrderInfo, Long>, JpaSpecificationExecutor<OrderInfo> {
//
//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query(value = "update order_info set create_time =?1 where order_info_id=?2", nativeQuery = true)
//    int updateOrderStatusById(String orderStatus, String id);
//
//
//}
