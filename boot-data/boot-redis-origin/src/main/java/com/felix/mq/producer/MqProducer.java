package com.felix.mq.producer;

/**
 * @author felix
 * @desc some desc
 */
public interface MqProducer {

    interface Callback {
        void onSuccess();

        void onFail(Throwable e, String topic, String tag, String key, String body);
    }

    void shutdown();

    boolean send(String key, String body) throws Exception;

    boolean send(String tag, String key, String body) throws Exception;

    void sendAsync(String tag, String key, String body, Callback callback);

    void sendAsync(String key, String body, Callback callback);
}
