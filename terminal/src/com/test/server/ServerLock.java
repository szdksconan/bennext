package com.test.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
    public class ServerLock {
        //维护每个连接专有的版本号 <ip,version>
        public static Map LockVersionMap = new HashMap();
        /**
         * 根据连接管理版本号
         * @param ip
         * @return
         */
        public synchronized static int add(String ip) {
            int version = 0;
            if (LockVersionMap.get(ip) == null)
                LockVersionMap.put(ip, version);
            version = (int)LockVersionMap.get(ip)+1;
            LockVersionMap.put(ip,version);
            return version;
        }

        /**
         * 获取版本号
         * @param ip
         * @return
         */
        public static int getVersion(String ip){
            return (int) LockVersionMap.get(ip);
        }

    }


