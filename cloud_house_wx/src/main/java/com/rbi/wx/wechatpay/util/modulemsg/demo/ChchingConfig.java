package com.rbi.wx.wechatpay.util.modulemsg.demo;

import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.util.modulemsg.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


public class ChchingConfig extends CacheConfig<RedisUtil>{
    @Override
    protected RedisUtil getCacheServer() {
        return super.getCacheServer();
    }


    public ChchingConfig() {
    }

    public ChchingConfig(RedisUtil cacheServer) {
        super(cacheServer);
    }

    @Override

    public void cacheAccese(String Accese) {
        this.getCacheServer().set("accese",Accese,7200);
    }

    @Override
    public String getAccese() {
        if (this.getCacheServer().hasKey("accese")){
            return this.getCacheServer().get("accese").toString();
        }
        else{
            return null;
        }
    }
}

