package com.alexey.transactionsapp.service;

import com.alexey.transactionsapp.model.Address;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Service
public class AccountLockService {
    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public record Result<T>(boolean status, T result){};
    @SneakyThrows
    public <T> Result<T> lockAndExecute(Address add1, Address add2, long millis, Supplier<T> action) {
        ReentrantLock reentrantLock1 = lockMap.computeIfAbsent(add1.address(), a -> new ReentrantLock());
        ReentrantLock reentrantLock2 = lockMap.computeIfAbsent(add2.address(), a -> new ReentrantLock());
        if (add1.address().compareTo(add2.address()) > 0) {
            ReentrantLock reentrantLock = reentrantLock1;
            reentrantLock1 = reentrantLock2;
            reentrantLock2 = reentrantLock;
        }

        boolean b1 = reentrantLock1.tryLock(millis / 2, TimeUnit.MILLISECONDS);
        if (!b1)
            return new Result<>(false, null);

        boolean b2 = reentrantLock2.tryLock(millis / 2, TimeUnit.MILLISECONDS);
        if (!b2) {
            reentrantLock1.unlock();
            return new Result<>(false, null);
        }

        try {
            T res = action.get();
            return new Result<>(true, res);
        } finally {
            reentrantLock1.unlock();
            reentrantLock2.unlock();
        }

    }

    @SneakyThrows
    public <T> Result<T> lockAndExecute(Address add1, long millis, Supplier<T> action) {
        ReentrantLock reentrantLock1 = lockMap.computeIfAbsent(add1.address(), a -> new ReentrantLock());

        boolean b1 = reentrantLock1.tryLock(millis / 2, TimeUnit.MILLISECONDS);
        if (!b1)
            return  new Result<>(false, null);;

        try {
            T res = action.get();
            return new Result<>(true, res);
        } finally {
            reentrantLock1.unlock();
        }

    }
}
