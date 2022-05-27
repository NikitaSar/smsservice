package smsservice.mocks;

import smsservice.service.QueueContainerService;

public class FakeQueueContainerService implements QueueContainerService {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }
}
