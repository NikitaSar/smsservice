package smsservice.service;

public interface QueueContainerService {
    void start();
    void stop();
    boolean isStarted();
}
