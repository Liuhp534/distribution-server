package cn.liuhp.origin;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @description:
 * @author: liuhp534
 * @create: 2020-06-26 18:50
 */
public class ZKClient {


    public static void main(String[] args) {
        fun1();
    }

    private static void fun1() {
        // 创建一个与服务器的连接 需要(服务端的 ip+端口号)(session过期时间)(Watcher监听注册)
        try {
            ZooKeeper zk = new ZooKeeper("192.168.0.116:2181", 3000, new Watcher()
            {
                // 监控所有被触发的事件
                public void process(WatchedEvent event)
                {
                    System.out.println(event.toString());
                }
            });
            // 创建一个目录节点
            /**
             * CreateMode:
             *       PERSISTENT (持续的，相对于EPHEMERAL，不会随着client的断开而消失)
             *       PERSISTENT_SEQUENTIAL（持久的且带顺序的）
             *       EPHEMERAL (短暂的，生命周期依赖于client session)
             *       EPHEMERAL_SEQUENTIAL  (短暂的，带顺序的)
             */
            zk.create("/path01", "data01".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("OK!");
    }





}
