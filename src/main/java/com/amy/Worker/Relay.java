package com.amy.Worker;

import static com.amy.Util.EnterUtil.takeIdOf;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
public class Relay {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final WriteLock writeLock = lock.writeLock();

    private final ReadLock readLock = lock.readLock();

    private final List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();

    private volatile boolean canSend;

    public void init() {
        canSend = true;
    }

    public void saveSession(WebSocketSession session) {
//        目前来说，这个方法不加锁也是线程安全的
        sessionList.add(session);
    }

    public void removeSession(WebSocketSession... sessions) {
        writeLock.lock();
        sessionList.removeIf(session -> ArrayUtils.contains(sessions, session));
        writeLock.unlock();
    }

    public void removeSession(String... ids) {
        writeLock.lock();
        sessionList.removeIf(session -> ArrayUtils.contains(ids, takeIdOf(session)));
        writeLock.unlock();
    }


    public void sendTextToExpect(String msg, String... ids) {
        if (Objects.nonNull(msg)) {
            while (!canSend) {
                Thread.onSpinWait();
            }
            readLock.lock();
            sessionList.forEach(
                    session -> {
                        if (ArrayUtils.contains(ids, takeIdOf(session))) {
                            try {
                                log.info("发送{}给{}", msg, ids);
                                session.sendMessage(new TextMessage(msg));
                            } catch (Exception e) {
                                log.info("转发的消息失败:{}", msg);
                                e.printStackTrace();
                            }
                        }
                    }
            );
            readLock.unlock();
        } else {
            log.info("发到{}的消息为空,不发", (Object[]) ids);
        }
    }

    public void sendTextToAllExcept(String msg, String... ids) {
        if (Objects.nonNull(msg)) {
            while (!canSend) {
                Thread.onSpinWait();
            }
            readLock.lock();
            sessionList.forEach((
                    session -> {
                        if (!ArrayUtils.contains(ids, takeIdOf(session))) {
                            try {
                                session.sendMessage(new TextMessage(msg));
                            } catch (IOException e) {
                                log.info("神奇");
                                e.printStackTrace();
                            }
                        }
                    }
            ));
            readLock.lock();
        } else {
            log.info("除了{}，发给其他所有人的消息为空,不发", (Object[]) ids);
        }
    }


}
