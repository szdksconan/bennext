package com.unipay.benext.task;

/**
 * Created by LiuY on 2017/3/5 0005.
 */
public class ServiceInfoT{

//    private static Logger logger = LoggerFactory.getLogger(ServiceInfoT.class);
//    long lastUpdateTime = System.currentTimeMillis();
//    ServiceInfoService serviceInfoService;
//
//    public ServiceInfoT(ServiceInfoService serviceInfoService){
//        this.serviceInfoService = serviceInfoService;
//    }
//
//    @Override
//    public void run() {
//        while (true){
//            if(PropertyUtils.getPropertyValue("heartbeat")==null) {
//                try {
//                    Thread.sleep(10000l);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                long heartbeat = Long.parseLong(PropertyUtils.getPropertyValue("heartbeat"));
//                long now = System.currentTimeMillis();
//                if (now - lastUpdateTime >= heartbeat * 1000) {
//                    logger.info("=================send heartbeat================");
//                    List<ServiceInfo> list =serviceInfoService.getServiceInfo(new HashMap());
//                    List idArr =new ArrayList();
//                    for (int i = 0; i < list.size(); i++) {
//                        ServiceInfo serviceInfo = list.get(i);
//                        String url ="http://"+serviceInfo.getIp()+":"+serviceInfo.getPort()+"/park/heartBeat";
//                       try {
//                           logger.info("message send to:"+url);
//                           String rs = HttpUtil.HttpPost(url, null, 2000, 1000);
//                           idArr.add(serviceInfo.getId());
//                      }catch (Exception e){
//                          logger.info(url,e);
//                      }
//                    }
//                    if(idArr.size()>0) serviceInfoService.updateBatch(idArr,new Date(now));
//                    lastUpdateTime = now;
//                } else {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}
