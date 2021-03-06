package info.xiancloud.cache.service.unit.list;

import info.xiancloud.cache.redis.Redis;
import info.xiancloud.cache.redis.operate.ListCacheOperate;
import info.xiancloud.cache.service.CacheGroup;
import info.xiancloud.plugin.*;
import info.xiancloud.plugin.message.UnitResponse;
import info.xiancloud.plugin.message.UnitRequest;
import info.xiancloud.plugin.support.cache.CacheConfigBean;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 查询 List 指定范围缓存 (注意: 包头包尾)
 *
 * @author John_zero
 */
public class CacheListRangeUnit implements Unit {
    @Override
    public String getName() {
        return "cacheListRange";
    }

    @Override
    public Group getGroup() {
        return CacheGroup.singleton;
    }

    @Override
    public UnitMeta getMeta() {
        return UnitMeta.create("查询 List 指定范围缓存 (注意: 包头包尾)").setPublic(false);
    }

    @Override
    public Input getInput() {
        return new Input()
                .add("key", Object.class, "缓存的关键字", REQUIRED)
                .add("startIndex", Long.class, "起始 index", REQUIRED)
                .add("endIndex", Long.class, "结束 index, 默认: 起始 + 100", NOT_REQUIRED)
                .add("cacheConfig", CacheConfigBean.class, "", NOT_REQUIRED);
    }

    @Override
    public UnitResponse execute(UnitRequest msg) {
        String key = msg.getArgMap().get("key").toString();
        long startIndex = Long.parseLong(msg.getArgMap().get("startIndex").toString());
        Long endIndex = msg.getArgMap().get("endIndex") != null ? Long.parseLong(msg.getArgMap().get("endIndex").toString()) : (startIndex + 100);
        CacheConfigBean cacheConfigBean = msg.get("cacheConfig", CacheConfigBean.class);

        List<String> result = null;
        try (Jedis jedis = Redis.useDataSource(cacheConfigBean).getResource()) {
            result = ListCacheOperate.range(jedis, key, startIndex, endIndex);
        } catch (Exception e) {
            return UnitResponse.exception(e);
        }
        return UnitResponse.success(result);
    }

}
