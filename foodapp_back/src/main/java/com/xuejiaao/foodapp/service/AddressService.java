package com.xuejiaao.foodapp.service;

import com.xuejiaao.foodapp.entity.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {

    // 获取用户地址列表
    List<Address> getAddressList(Long userId);

    // 获取地址详情
    Address getAddressDetail(Long id, Long userId);

    // 获取默认地址
    Address getDefaultAddress(Long userId);

    // 添加地址
    Long addAddress(Map<String, Object> addressData, Long userId);

    // 更新地址
    boolean updateAddress(Map<String, Object> addressData, Long userId);

    // 删除地址
    boolean deleteAddress(Long id, Long userId);

    // 设置默认地址
    boolean setDefaultAddress(Long id, Long userId);
}