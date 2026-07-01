package com.kyh.system.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyh.system.mapper.SyainMapper;
import com.kyh.system.model.Syain;
import com.kyh.system.model.SyainExample;
import com.kyh.system.service.SyainService;

@Service
public class SyainServiceImpl implements SyainService {

    @Autowired
    private SyainMapper syainMapper;

    @Override
    public List<Syain> selectAll() {

        SyainExample example = new SyainExample();

        return syainMapper.selectByExample(example);
    }
}