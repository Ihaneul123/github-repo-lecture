package com.kyh.system.service;

import java.util.List;
import java.util.Map;

import com.kyh.system.model.Setting;

public interface SettingService {
    List<Setting> getSettingsByCategory1(Integer category1);
    List<Setting> getSettingsByCategory1AndCategory3(Integer category1, Integer category3);
    List<Setting> getSettingsByCategory1AndCategory2(Integer category1, Integer category2);
    Map<Integer, String> getSettingMapByCategory1AndCategory3(Integer category1, Integer category3);
    Map<Integer, String> getSettingMapByCategory1AndCategory2(Integer category1, Integer category2);
}