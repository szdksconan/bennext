package com.unipay.benext.controller.park;

import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.model.park.TerminalDic;
import com.unipay.benext.service.terminal.TerminalService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2017/3/25.
 */
@Controller
@RequestMapping("terminalController")
public class TerminalController {
    private Log log = LogFactory.getLog(this.getClass());

    private static final String terminalIdKey = "terminalId";

    @Autowired
    private TerminalService terminalService;

    @RequestMapping("initSetTerminalId")
    public ModelAndView initSetTerminalId(){
        TerminalDic dic = new TerminalDic();
        dic.setKey(terminalIdKey);
        ModelAndView modelAndView = new ModelAndView("park/setTerminalId");
        modelAndView.addObject("dic",terminalService.getValueForKey(dic));
        return modelAndView;
    }

    @RequestMapping("saveTerminalId")
    @ResponseBody
    public Json saveTerminalId(TerminalDic dic){
        log.warn("修改终端编号开始=======");
        dic.setKey(terminalIdKey);
        Json json = new Json();
        try {
            terminalService.saveValueForKey(dic);
            log.warn("修改终端编号成功=======");
            json.setSuccess(true);
            json.setMsg("修改成功！");
        }catch (Exception e){
            log.warn("修改终端编号失败=======",e);
            json.setSuccess(false);
            json.setMsg("修改失败！");
        }
        return json;
    }
}
