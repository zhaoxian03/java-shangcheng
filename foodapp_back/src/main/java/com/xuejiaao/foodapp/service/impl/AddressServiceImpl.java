package com.xuejiaao.foodapp.service.impl;

import com.xuejiaao.foodapp.entity.Address;
import com.xuejiaao.foodapp.mapper.AddressMapper;
import com.xuejiaao.foodapp.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    @Override
    public List<Address> getAddressList(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

    @Override
    public Address getAddressDetail(Long id, Long userId) {
        return addressMapper.selectByIdAndUserId(id, userId);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return addressMapper.selectDefaultAddress(userId);
    }

    @Override
    @Transactional
    public Long addAddress(Map<String, Object> addressData, Long userId) {
        Address address = new Address();
        address.setUserId(userId);
        address.setName((String) addressData.get("name"));
        address.setPhone((String) addressData.get("phone"));
        address.setProvince((String) addressData.get("province"));
        address.setCity((String) addressData.get("city"));
        address.setDistrict((String) addressData.get("district"));
        address.setDetail((String) addressData.get("detail"));
        address.setIsDefault((Integer) addressData.getOrDefault("isDefault", 0));
        address.setCreateTime(new Date());
        address.setUpdateTime(new Date());

        // 如果设置为默认地址，先取消其他默认地址
        if (address.getIsDefault() == 1) {
            addressMapper.cancelAllDefault(userId);
        }

        addressMapper.insert(address);
        return address.getId();
    }

    @Override
    @Transactional
    public boolean updateAddress(Map<String, Object> addressData, Long userId) {
        Long id = Long.parseLong(addressData.get("id").toString());

        // 检查地址是否存在
        Address existing = addressMapper.selectByIdAndUserId(id, userId);
        if (existing == null) {
            return false;
        }

        Address address = new Address();
        address.setId(id);
        address.setUserId(userId);
        address.setName((String) addressData.get("name"));
        address.setPhone((String) addressData.get("phone"));
        address.setProvince((String) addressData.get("province"));
        address.setCity((String) addressData.get("city"));
        address.setDistrict((String) addressData.get("district"));
        address.setDetail((String) addressData.get("detail"));
        address.setIsDefault((Integer) addressData.getOrDefault("isDefault", 0));
        address.setUpdateTime(new Date());

        // 如果设置为默认地址，先取消其他默认地址
        if (address.getIsDefault() == 1) {
            addressMapper.cancelAllDefault(userId);
        }

        return addressMapper.update(address) > 0;
    }

    @Override
    public boolean deleteAddress(Long id, Long userId) {
        return addressMapper.delete(id, userId) > 0;
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long id, Long userId) {
        // 先取消所有默认地址
        addressMapper.cancelAllDefault(userId);
        // 设置新的默认地址
        return addressMapper.setDefault(id, userId) > 0;
    }
}