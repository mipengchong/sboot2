//package com.zj.service.impl;
//
//import com.zj.mapper.OrderRepository;
//import com.zj.model.OrderInfo;
//import com.zj.model.OrderItem;
//import com.zj.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Join;
//import javax.persistence.criteria.JoinType;
//import javax.persistence.criteria.Path;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Created with IntelliJ IDEA.
// * User: SuShaohua
// * Date: 2018/6/24
// * Time: 18:59
// * Description: MAIN
// */
//@Service
//public class OrderServiceImpl implements OrderService {
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    public Page<OrderInfo> findBySepc(int page, int size){
//        PageRequest pageReq = this.buildPageRequest(page, size);
//        Page<OrderInfo> tasks = orderRepository.findAll(new MySpec(), pageReq);
//        return tasks;
//
//    }
//
//    private PageRequest buildPageRequest(int page, int size) {
//        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
//        return PageRequest.of(page,size, sort);
//    }
//
//
//    /**
//     * 建立查询条件
//     */
//    private class MySpec implements Specification<OrderInfo> {
//
//        @Override
//        public Predicate toPredicate(Root<OrderInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//
//            //1.混合条件查询
//          Path<String> exp1 = root.get("taskName");
//            Path<Date>  exp2 = root.get("createTime");
//            Path<String> exp3 = root.get("taskDetail");
//            Predicate predicate = cb.and(cb.like(exp1, "%taskName%"),cb.lessThan(exp2, new Date()));
//            cb.or(predicate,cb.equal(exp3, "kkk"));
//
//            /*类似的sql语句为:
//            Hibernate:
//                select
//                    count(task0_.id) as col_0_0_
//                from
//                    tb_task task0_
//                where
//                    (
//                        task0_.task_name like ?
//                    )
//                    and task0_.create_time<?
//                    or task0_.task_detail=?
//            */
//
//            //2.多表查询
//        Join<OrderInfo, OrderItem> join = root.join("project", JoinType.INNER);
//            Path<String> exp4 = join.get("projectName");
//            return cb.like(exp4, "%projectName%");
//
////            Hibernate:
////            select
////                count(task0_.id) as col_0_0_
////            from
////                tb_task task0_
////            inner join
////                tb_project project1_
////                    on task0_.project_id=project1_.id
////            where
////                project1_.project_name like ?
//
//        }
//    }
//
//    @Override
//    public int saveOrderInfo(OrderInfo oreder) {
//        return 0;
//    }
//
//
//
//    private final Map<String, OrderInfo> data = new ConcurrentHashMap<>();
//
//    @Override
//    public Flux<OrderInfo> list() {
//        return Flux.fromIterable(this.data.values());
//    }
//
//
//    Flux<OrderInfo> getById(final Flux<String> ids) {
//        return ids.flatMap(id -> Mono.justOrEmpty(this.data.get(id)));
//    }
//
//    @Override
//    public Mono<OrderInfo> getById(final String id) {
//        return Mono.justOrEmpty(this.data.get(id))
//                .switchIfEmpty(Mono.error(new Exception()));
//    }
//
//    @Override
//    public Mono<OrderInfo> createOrUpdate(final OrderInfo user) {
//        this.data.put(user.getOrderId(), user);
//        return Mono.just(user);
//    }
//
//    @Override
//    public Mono<OrderInfo> delete(final String id) {
//        return Mono.justOrEmpty(this.data.remove(id));
//    }
//
//}
