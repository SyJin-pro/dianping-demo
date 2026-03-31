package com.jj.dianpingdemo.service;

import com.jj.dianpingdemo.entity.*;
import com.jj.dianpingdemo.mapper.SeckillVoucherMapper;
import com.jj.dianpingdemo.mapper.VoucherMapper;
import com.jj.dianpingdemo.mapper.VoucherOrderMapper;
import com.jj.dianpingdemo.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * @author: JSY
 * @version: 1.0
 */
@Service
public class VoucherService {

    private final VoucherMapper voucherMapper;
    private final SeckillVoucherMapper seckillVoucherMapper;

    private final VoucherOrderMapper voucherOrderMapper;

    public VoucherService(VoucherMapper voucherMapper,
                          SeckillVoucherMapper seckillVoucherMapper,
                          VoucherOrderMapper voucherOrderMapper) {
        this.voucherMapper = voucherMapper;
        this.seckillVoucherMapper = seckillVoucherMapper;
        this.voucherOrderMapper = voucherOrderMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addVoucher(VoucherDto dto) {
        // 秒杀券参数校验
        if (Integer.valueOf(1).equals(dto.getType())) {
            Assert.notNull(dto.getStock(), "秒杀券库存不能为空");
            Assert.notNull(dto.getBeginTime(), "秒杀开始时间不能为空");
            Assert.notNull(dto.getEndTime(), "秒杀结束时间不能为空");
        }

        // 1. 组装 Voucher 实体
        Voucher voucher = new Voucher();
        voucher.setShopId(dto.getShopId());
        voucher.setTitle(dto.getTitle());
        voucher.setSubTitle(dto.getSubTitle());
        voucher.setRules(dto.getRules());
        voucher.setPayValue(dto.getPayValue());
        voucher.setActualValue(dto.getActualValue());
        voucher.setType(dto.getType() == null ? 0 : dto.getType());
        voucher.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());

        // ==============================================
        // 🔥 补上创建时间、更新时间（核心修复）
        // ==============================================
        voucher.setCreateTime(LocalDateTime.now());
        voucher.setUpdateTime(LocalDateTime.now());

        // 2. 插入基础优惠券表
        voucherMapper.insert(voucher);

        // 3. 秒杀券额外处理
        if (Integer.valueOf(1).equals(voucher.getType())) {
            SeckillVoucher seckillVoucher = new SeckillVoucher();
            seckillVoucher.setVoucherId(voucher.getId());
            seckillVoucher.setStock(dto.getStock());
            seckillVoucher.setBeginTime(dto.getBeginTime());
            seckillVoucher.setEndTime(dto.getEndTime());

            // ==============================================
            // 🔥 秒杀券也补上创建时间、更新时间
            // ==============================================
            seckillVoucher.setCreateTime(LocalDateTime.now());
            seckillVoucher.setUpdateTime(LocalDateTime.now());

            seckillVoucherMapper.insert(seckillVoucher);
        }

        return Result.ok("新增优惠券成功，ID：" + voucher.getId());
    }

    // ==================== 秒杀抢购核心方法 ====================
    public  Result seckillVoucher(Long voucherId) {
        // 1. 获取当前登录用户（从ThreadLocal）
        UserDto user = UserHolder.getUser();
        if (user == null) {
            return Result.fail("请先登录");
        }
        Long userId = user.getId();

        // 先加锁 → 再执行带事务的核心逻辑
        synchronized (userId.toString().intern()) {
//            return createVoucherOrder(voucherId, userId);
            VoucherService proxy = (VoucherService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId, userId);
        }
    }

    // 🔥 独立的事务方法：先加锁，再开启事务
    @Transactional
    public Result createVoucherOrder(Long voucherId, Long userId) {
        // 2. 查询秒杀券
        SeckillVoucher seckillVoucher = seckillVoucherMapper.selectByVoucherId(voucherId);
        // 3. 校验时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(seckillVoucher.getBeginTime())) {
            return Result.fail("秒杀尚未开始");
        }
        if (now.isAfter(seckillVoucher.getEndTime())) {
            return Result.fail("秒杀已经结束");
        }
        // 4. 校验库存
        if (seckillVoucher.getStock() < 1) {
            return Result.fail("库存不足");
        }

        // 5. 一人一单查询（此时事务刚开，能读到最新数据）
        int count = voucherOrderMapper.countByUserIdAndVoucherId(userId, voucherId);
        if (count > 0) {
            return Result.fail("每人限购一张");
        }

        // 6. 扣库存
        int rows = seckillVoucherMapper.deductStock(voucherId);
        if (rows == 0) {
            return Result.fail("库存不足");
        }

        // 7. 创建订单
        VoucherOrder order = new VoucherOrder();
        order.setId(System.currentTimeMillis());
        order.setUserId(userId);
        order.setVoucherId(voucherId);
        order.setStatus(1);
        voucherOrderMapper.insert(order);

        int i = 1 / 0;

        return Result.ok("秒杀成功！订单号：" + order.getId());
    }
}
