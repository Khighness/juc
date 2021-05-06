package top.parak.aqs;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author KHighness
 * @since 2021-05-05
 */

@Slf4j(topic = "GenericRedisDemo")
public class GenericRedisDemo {
    public static void main(String[] args) {
        GenericCached cached = new GenericCached();
        cached.update(new User(1, "KHighness", "parakovo@gmail.com"));
        cached.update(new User(2, "RubbishK", "rubbish@gmail.com"));
        cached.update(new User(3, "FlowerK", "flower@gmail.com"));
        log.debug(cached.query(User.class, 1).toString());
        log.debug(cached.query(User.class, 1).toString());
        log.debug(cached.query(User.class, 2).toString());
    }
}

class User {
    private Integer id;
    private String name;
    private String email;
    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

interface Generic {
    String update(Object obj);
    Map<String, String> query(Class<?> clazz, Integer id);
}

class GenericRedis implements Generic {
    private final Jedis jedis = new Jedis("127.0.0.1", 6379); {
        jedis.auth("KAG1823");
    }

    public String update(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        Map<String, String> map = new HashMap<>();
        Integer id = null;
        try {
            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            id = (Integer) idField.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jedis.hmset(clazz.getSimpleName() + ":" + id, map);
    }

    public Map<String, String> query(Class<?> clazz, Integer id) {
        return jedis.hgetAll(clazz.getSimpleName() + ":" + id);
    }
}

// 装饰器模式，Java层面做一层缓存
@Slf4j(topic = "GenericCached")
class GenericCached implements Generic {
    private final GenericRedis redis = new GenericRedis();
    private final Map<Key, Map<String, String>> cache = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static class Key {
        Class<?> clazz;
        Integer id;

        public Key(Class<?> clazz, Integer id) {
            this.clazz = clazz;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(clazz, key.clazz) && Objects.equals(id, key.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, id);
        }

        @Override
        public String toString() {
            return "Key[" + "clazz=" + clazz + ", id=" + id + ']';
        }
    }

    // 先更新redis再清除缓存，需要加写锁
    @Override
    public String update(Object obj) {
        String res = null;
        lock.writeLock().lock();
        try {
            // 更新redis
            res = redis.update(obj);
            // 清除缓存
            Field idField = obj.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Key key = new Key(obj.getClass(), (int) idField.get(obj));
            if (cache.remove(key) != null) {
                log.debug("清除缓存: {}", key);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
        return res;
    }

    // 先查询缓存，需要加读锁；
    // 没有则查询redis添加到缓存，需要加写锁
    @Override
    public Map<String, String> query(Class<?> clazz, Integer id) {
        Key key = new Key(clazz, id);
        Map<String, String> res = null;
        lock.readLock().lock();
        // 查询缓存
        try {
            if (cache.containsKey(key)) {
                log.debug("缓存查询: {}", res = cache.get(key));
                return res;
            }
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        // 查询数据库，添加到缓存
        try {
            // 双重检查
            if (cache.containsKey(key)) {
                log.debug("缓存查询: {}", res = cache.get(key));
                return res;
            }
            cache.put(key, res = redis.query(clazz, id));
            log.debug("添加缓存: {}", key);
        } finally {
            lock.writeLock().unlock();
        }
        return res;
    }
}
