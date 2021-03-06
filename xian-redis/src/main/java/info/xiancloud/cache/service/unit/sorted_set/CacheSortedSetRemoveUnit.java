package info.xiancloud.cache.service.unit.sorted_set;

import info.xiancloud.cache.redis.Redis;
import info.xiancloud.cache.redis.util.FormatUtil;
import info.xiancloud.cache.service.CacheGroup;
import info.xiancloud.plugin.*;
import info.xiancloud.plugin.message.UnitResponse;
import info.xiancloud.plugin.message.UnitRequest;
import info.xiancloud.plugin.support.cache.CacheConfigBean;

import java.util.Iterator;
import java.util.Set;

/**
 * Sorted Set Remove
 * <p>
 * http://doc.redisfans.com/sorted_set/zrem.html
 *
 * @author John_zero
 */
public class CacheSortedSetRemoveUnit implements Unit {
    @Override
    public String getName() {
        return "cacheSortedSetRemove";
    }

    @Override
    public Group getGroup() {
        return CacheGroup.singleton;
    }

    @Override
    public UnitMeta getMeta() {
        return UnitMeta.create("Sorted Set Remove").setPublic(false);
    }

    @Override
    public Input getInput() {
        return new Input()
                .add("key", String.class, "", REQUIRED)
                .add("member", String.class, "", NOT_REQUIRED)
                .add("members", Set.class, "", NOT_REQUIRED)
                .add("cacheConfig", CacheConfigBean.class, "", NOT_REQUIRED);
    }

    @Override
    public UnitResponse execute(UnitRequest msg) {
        String key = msg.get("key", String.class);
        Object member = msg.get("member", Object.class);
        Set members = (Set) msg.getArgMap().get("members");
        CacheConfigBean cacheConfigBean = msg.get("cacheConfig", CacheConfigBean.class);

        try {
            Long result = Redis.call(cacheConfigBean, jedis ->
            {
                if (members != null && !members.isEmpty()) {
                    String[] _members = new String[members.size()];
                    Iterator<Object> iterator = members.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        _members[i] = FormatUtil.formatValue(iterator.next());
                        i++;
                    }
                    return jedis.srem(key, _members);
                } else if (member != null && !"".equals(member)) {
                    return jedis.srem(key, FormatUtil.formatValue(member));
                } else {
                    return -1L;
                }
            });
            return UnitResponse.success(result);
        } catch (Throwable e) {
            return UnitResponse.exception(e);
        }
    }

}
