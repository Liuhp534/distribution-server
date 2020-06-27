package cn.liuhp.origin.watch;

/**
 * Created by xkorey on 4/11/15.
 */
public class Zkclient1 {
    public static void main(String[]args) throws InterruptedException {

        Client client = new Client();
        client.initZkClient("192.168.0.116:2181",30000,"/zka");
        Thread.sleep(Integer.MAX_VALUE);
        client.dataMaker(0);

    }
}
