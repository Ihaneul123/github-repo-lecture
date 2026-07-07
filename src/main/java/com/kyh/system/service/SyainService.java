package com.kyh.system.service;

import java.util.List;

import com.kyh.system.model.Syain;

public interface SyainService {

    List<Syain> selectAll();
    
    List<Syain> search(
    	    Integer companyId,
    	    String employeeName,
    	    Integer jobKind,
    	    boolean working,
    	    boolean retired
    	);

    void delete(Integer syainId);

	void insert(Syain syain);
 
}