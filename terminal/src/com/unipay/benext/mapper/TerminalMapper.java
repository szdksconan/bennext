package com.unipay.benext.mapper;

import com.unipay.benext.model.park.TerminalDic;

/**
 * Created by Administrator on 2017/3/25.
 */
public interface TerminalMapper {

    public TerminalDic getValueForKey(TerminalDic terminalDic);

    public void saveValueForKey(TerminalDic terminalDic);
}
