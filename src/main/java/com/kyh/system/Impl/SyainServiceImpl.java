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

    @Override
    public List<Syain> search(
            Integer companyId,
            String employeeName,
            Integer jobKind,
            boolean working,
            boolean retired) {
        SyainExample example = new SyainExample();
        SyainExample.Criteria criteria = example.createCriteria();
        
        // 削除データ除外
        criteria.andDeleteFlagEqualTo(0);
        
        // 所属会社：「全て」以外の場合
        if (companyId != null) {
            criteria.andSyozokuKaisyaEqualTo(companyId);
        }

        // 職業種類
        if (jobKind != null) {
            criteria.andSyokugyoKindEqualTo(jobKind);
        }

        // 在籍・非在籍
        if (working && !retired) {
            criteria.andTaisyaDateIsNull();
        } else if (!working && retired) {
            criteria.andTaisyaDateIsNotNull();
        }

        // 社員名 LIKE検索
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            String keyword = "%" + employeeName.trim() + "%";
            SyainExample nameExample = new SyainExample();
            SyainExample.Criteria c1 = nameExample.createCriteria();
            c1.andDeleteFlagEqualTo(0);
            if (companyId != null) c1.andSyozokuKaisyaEqualTo(companyId);
            if (jobKind != null) c1.andSyokugyoKindEqualTo(jobKind);
            if (working && !retired) c1.andTaisyaDateIsNull();
            else if (!working && retired) c1.andTaisyaDateIsNotNull();
            c1.andFirstNameKanjiLike(keyword);
            SyainExample.Criteria c2 = nameExample.or();
            c2.andDeleteFlagEqualTo(0);
            if (companyId != null) c2.andSyozokuKaisyaEqualTo(companyId);
            if (jobKind != null) c2.andSyokugyoKindEqualTo(jobKind);
            if (working && !retired) c2.andTaisyaDateIsNull();
            else if (!working && retired) c2.andTaisyaDateIsNotNull();
            c2.andLastNameKanjiLike(keyword);
            nameExample.setOrderByClause("first_name_kanji ASC, last_name_kanji ASC");
            return syainMapper.selectByExample(nameExample);
        }
        
        example.setOrderByClause("first_name_kanji ASC, last_name_kanji ASC");
        return syainMapper.selectByExample(example);
    }
    
 // 社員削除
    @Override
    public void delete(Integer syainId) {
        Syain syain = new Syain();
        syain.setSyainId(syainId);
        syain.setDeleteFlag(1);
        syainMapper.updateByPrimaryKeySelective(syain);
    }
    
    // 社員追加
    @Override
    public void insert(Syain syain) {
        syainMapper.insertSelective(syain);
    }
    
    // 社員更新
    @Override
    public Syain selectByPrimaryKey(Integer syainId) {
        return syainMapper.selectByPrimaryKey(syainId);
    }
    @Override
    public void update(Syain syain) {
        syainMapper.updateByPrimaryKeySelective(syain);
    }
}