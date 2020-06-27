package cn.liuhp.origin.watch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created by xkorey on 4/10/15.
 */
public class Client implements Watcher, AsyncCallback.StatCallback {

    private ZooKeeper zk;
    private boolean dead;
    private String znode;
    private Stat stat;
    private int index;

    /**
     *
     * 初始化node节点
     * 获取数据配置watcher
     * */
    public void initZkClient(String ipport,int timetout,String znode) {
        try {
            this.znode = znode;
            zk = new ZooKeeper(ipport, timetout, this);
            stat = zk.exists(znode, false);
            if(null == stat){
                System.out.println("创建节点=" + znode);
                zk.create(znode,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }
            zk.getData(znode,this,stat);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * 监听消息的方法
    * */
    public void process(WatchedEvent event) {
        System.out.println("监听事件=" + event);
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    dead = true;
                    break;
            }
        } else {
            if (path != null && path.equals(znode)) {
                System.out.println("处理监听事件=" + znode);
                try {
                    //1 事件没有具体的内容，需要去获取
                    //zk.exists(znode, true, this, null);
                    byte[] b = zk.getData(znode, false, null);
                    System.out.println(new String(b));
                    //2 重复的注册watcher
                    zk.getData(znode,this,stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    * 这个不需要也行
    * */
    public void processResult(int rc, String s, Object o, Stat stat) {
        boolean exists;
        System.out.println("rc=" + rc);
        switch (rc) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists = false;
                break;
            case KeeperException.Code.SessionExpired:
            case KeeperException.Code.NoAuth:
                dead = true;
                return;
            default:
                zk.exists(znode, true, this, null);
                return;
        }

        byte b[] = null;
        if (exists) {
            System.out.println("获取到消息处理。。。。");
            /*try {
                b = zk.getData(znode, false, null);
                System.out.println(new String(b));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }*/
        }
    }

    /**
     * 调整data数据
     * */
    public void dataMaker(int i){
        index=i;
        int max=index+10;
        boolean run=true;
        while(run){
            try {
                zk.setData(znode,((index++)+"").getBytes(), -1);
                Thread.sleep(1000*3);
                if(index>max){
                    run=false;
                    zk.close();
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
