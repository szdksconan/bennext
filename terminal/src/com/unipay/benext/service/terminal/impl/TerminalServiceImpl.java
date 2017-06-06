package com.unipay.benext.service.terminal.impl;

import com.unipay.benext.mapper.TerminalMapper;
import com.unipay.benext.model.park.TerminalDic;
import com.unipay.benext.service.terminal.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/3/25.
 */
@Service
public class TerminalServiceImpl implements TerminalService{
    @Autowired
    TerminalMapper terminalMapper;

    @Override
    public TerminalDic getValueForKey(TerminalDic terminalDic) {
        return terminalMapper.getValueForKey(terminalDic);
    }

    @Override
    public void saveValueForKey(TerminalDic terminalDic) {
        terminalMapper.saveValueForKey(terminalDic);
    }
}
