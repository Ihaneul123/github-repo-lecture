package com.kyh.system.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyh.system.mapper.SettingMapper;
import com.kyh.system.model.Setting;
import com.kyh.system.model.SettingExample;
import com.kyh.system.service.SettingService;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

    @Override
    public List<Setting> getSettingsByCategory1(Integer category1) {
        SettingExample example = new SettingExample();
        example.createCriteria()
               .andCategory1EqualTo(category1)
               .andDeleteFlagEqualTo(0);

        example.setOrderByClause("display_order ASC");

        return settingMapper.selectByExample(example);
    }
    
    @Override
    public List<Setting> getSettingsByCategory1AndCategory3(Integer category1, Integer category3) {
        SettingExample example = new SettingExample();
        example.createCriteria()
                .andCategory1EqualTo(category1)
                .andCategory3EqualTo(category3)
                .andDeleteFlagEqualTo(0);

        example.setOrderByClause("display_order ASC");

        return settingMapper.selectByExample(example);
    }

    @Override
    public List<Setting> getSettingsByCategory1AndCategory2(Integer category1, Integer category2) {
        SettingExample example = new SettingExample();
        example.createCriteria()
                .andCategory1EqualTo(category1)
                .andCategory2EqualTo(category2)
                .andDeleteFlagEqualTo(0);

        example.setOrderByClause("display_order ASC");

        return settingMapper.selectByExample(example);
    }
    
    @Override
    public Map<Integer, String> getSettingMapByCategory1AndCategory3(Integer category1, Integer category3) {
        List<Setting> list = getSettingsByCategory1AndCategory3(category1, category3);

        Map<Integer, String> map = new HashMap<>();
        for (Setting setting : list) {
            map.put(setting.getCategory2(), setting.getValue1());
        }
        return map;
    }

    @Override
    public Map<Integer, String> getSettingMapByCategory1AndCategory2(Integer category1, Integer category2) {
        List<Setting> list = getSettingsByCategory1AndCategory2(category1, category2);

        Map<Integer, String> map = new HashMap<>();
        for (Setting setting : list) {
            map.put(setting.getCategory3(), setting.getValue1());
        }
        return map;
    }
}