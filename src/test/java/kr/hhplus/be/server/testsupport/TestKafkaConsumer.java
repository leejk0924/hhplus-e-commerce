package kr.hhplus.be.server.testsupport;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@TestComponent
public class TestKafkaConsumer {
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @KafkaListener(
            topics = "test-topic",
            groupId = "test-group",
            properties = {
                    "auto.offset.reset=earliest"
            }
    )
    public void listen(String message) {
        messageQueue.offer(message);
    }

    public String getReceivedMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return messageQueue.poll(timeout, unit);
    }

    public void clearMessages() {
        messageQueue.clear();
    }
}
