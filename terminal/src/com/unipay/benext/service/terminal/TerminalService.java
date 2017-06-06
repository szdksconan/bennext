package com.unipay.benext.service.terminal;

import com.unipay.benext.model.park.TerminalDic;

/**
 * Created by Administrator on 2017/3/25.
 */
public interface TerminalService {
    public TerminalDic getValueForKey(TerminalDic terminalDic);

    public void saveValueForKey(TerminalDic terminalDic);
}
