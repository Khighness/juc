package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KHighness
 * @since 2021-04-11
 */

@Slf4j(topic = "GuardedObject")
public class GuardedObjectDemo {

    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        // 获取结果线程
        new Thread(() -> {
            log.debug("等待结果");
            List<String> response = (List<String>) guardedObject.getResponse(5000);
            if (response != null) {
                log.debug("结果大小：{}", response.size());
            } else {
                log.debug("下载失败");
            }
        }, "receive").start();
        // 下载内容线程
        new Thread(() -> {
            log.debug("执行下载");
            List<String> download = ParaKDownloader.download("https://www.parak.top");
            guardedObject.setResponse(download);
        }, "download").start();
    }
}

class GuardedObject {
    // 结果
    private Object response;

    // 获取结果
    public Object getResponse(long timeout) {
        synchronized (this) {
            // 开始时间
            long start = System.currentTimeMillis();
            // 经历时间
            long pass = 0;
            // 没有结果
            while (response == null) {
                // 此轮循环应该等待的时间
                long wait = timeout - pass;
                if (wait <= 0) {
                    break;
                }
                try {
                    this.wait(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pass = System.currentTimeMillis() - start;
            }
            return response;
        }
    }

    // 产生结果
    public void setResponse(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}

class ParaKDownloader {
    public static List<String> download(String url) {
        HttpURLConnection connection = null;
        List<String> res = new ArrayList<>();
        String line = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                res.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
