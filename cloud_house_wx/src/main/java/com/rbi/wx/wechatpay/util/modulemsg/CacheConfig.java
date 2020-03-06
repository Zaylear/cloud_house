package com.rbi.wx.wechatpay.util.modulemsg;

public abstract class CacheConfig <T>{
    /**
     * 缓存服务对象
     */
    private T cacheServer;

    public CacheConfig(){

    }

    public CacheConfig(T cacheServer) {
        this.cacheServer = cacheServer;
    }

    /**
     * 得到缓存服务也可以不写
     * @return
     */
    protected T getCacheServer() {
        return cacheServer;
    }

    /**
     * 设置换缓存服务也可以不写
     * @param cacheServer
     */

    /**
     * 缓存Accese的方法
     * @param Accese
     */
  public abstract void cacheAccese(String Accese);

    /**
     * 得到Accese的方法
     * 如果过期了就返回NULL
     * @return
     */
  public abstract String getAccese();
}
