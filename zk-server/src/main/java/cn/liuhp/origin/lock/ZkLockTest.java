package cn.liuhp.origin.lock;

import org.apache.zookeeper.KeeperException;

/**
 * @description:
 * @author: liuhp534
 * @create: 2020-06-26 20:58
 */
public class ZkLockTest {


    private static int count = 0;

    public static void main(String[] args) throws Exception {
        fun1();
    }

    private static void fun1() throws Exception {
        for (int i = 0; i < 10; i ++) {
            Thread t = new Thread(() -> {
                try {
                    ZkLock zkLock = new ZkLock();
                    zkLock.acquireLock();
                    count ++;
                    zkLock.releaseLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }
}
