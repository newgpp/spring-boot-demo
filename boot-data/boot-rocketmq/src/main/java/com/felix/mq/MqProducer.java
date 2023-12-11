package com.felix.mq;

/**
 * @author felix
 * @desc some desc
 */
public interface MqProducer {

    interface Callback {
        void onStatus(boolean succeed);

        void onException(Throwable e);
    }

    void shutdown();

    boolean send(String tag, String key, String body) throws Exception;

    void sendAsync(String tag, String key, String body, Callback callback);
}
