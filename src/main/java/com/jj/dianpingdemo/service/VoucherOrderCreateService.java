//package com.jj.dianpingdemo.service;
//
//import com.jj.dianpingdemo.entity.Result;
//import com.jj.dianpingdemo.entity.SeckillVoucher;
//import com.jj.dianpingdemo.entity.VoucherOrder;
//import com.jj.dianpingdemo.mapper.SeckillVoucherMapper;
//import com.jj.dianpingdemo.mapper.VoucherOrderMapper;
//import jakarta.annotation.Resource;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
///**
// * @author: JSY
// * @version: 1.0
// */
//@Service
//public class VoucherOrderCreateService {
//
//    @Resource
//    private SeckillVoucherMapper seckillVoucherMapper;
//    @Resource
//    private VoucherOrderMapper voucherOrderMapper;
//
//    // 独立事务方法，绝对生效！
//    @Transactional
//    public Result createOrder(Long voucherId, Long userId) {
//        // 1. 查询秒杀券
//        SeckillVoucher seckillVoucher = seckillVoucherMapper.selectByVoucherId(voucherId);
//        // 2. 校验时间
//        LocalDateTime now = LocalDateTime.now();
//        if (now.isBefore(seckillVoucher.getBeginTime())) {
//            return Result.fail("秒杀尚未开始");
//        }
//        if (now.isAfter(seckillVoucher.getEndTime())) {
//            return Result.fail("秒杀已经结束");
//        }
//        // 3. 校验库存
//        if (seckillVoucher.getStock() < 1) {
//            return Result.fail("库存不足");
//        }
//
//        // 4. 一人一单查询（事务生效，能读到最新数据）
////        Long orderId = voucherOrderMapper.existsByUserIdAndVoucherId(userId, voucherId);
////        if (orderId != null) {
////            return Result.fail("每人限购一张");
////        }
//        // 🔥 修复：查询数量，判断是否>0
//        int count = voucherOrderMapper.countByUserIdAndVoucherId(userId, voucherId);
//        if (count > 0) {
//            return Result.fail("每人限购一张");
//        }
//
//        // 5. 扣库存（你的乐观锁SQL）
//        int rows = seckillVoucherMapper.deductStock(voucherId);
//        if (rows == 0) {
//            return Result.fail("库存不足");
//        }
//
//        // 6. 创建订单
//        VoucherOrder order = new VoucherOrder();
//        order.setId(System.currentTimeMillis());
//        order.setUserId(userId);
//        order.setVoucherId(voucherId);
//        order.setStatus(1);
//        voucherOrderMapper.insert(order);
//
//        return Result.ok("秒杀成功！订单号：" + order.getId());
//    }
//}
