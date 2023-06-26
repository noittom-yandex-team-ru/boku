package com.alexey.transactionsapp.externalapi;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.alexey.transactionsapp.externalapi.WithdrawalService.WithdrawalState.*;

@Service
public class WithdrawalServiceStub implements WithdrawalService<BigDecimal> {
    private final ConcurrentMap<WithdrawalId, Withdrawal> requests = new ConcurrentHashMap<>();

    @Override
    public void requestWithdrawal(WithdrawalId id, Address address, BigDecimal amount) { // Please substitute T with prefered type
        final var existing = requests.putIfAbsent(id, new Withdrawal(finalState(), finaliseAt(), address, amount));
        if (existing != null && !Objects.equals(existing.address, address) && !Objects.equals(existing.amount, amount))
            throw new IllegalStateException("Withdrawal request with id[%s] is already present".formatted(id));
    }

    private WithdrawalState finalState() {
        return ThreadLocalRandom.current().nextBoolean() ? COMPLETED : FAILED;
    }

    private long finaliseAt() {
        return System.currentTimeMillis() + ThreadLocalRandom.current().nextLong(1000, 10000);
    }

    @Override
    public WithdrawalState getRequestState(WithdrawalId id) {
        final var request = requests.get(id);
        if (request == null)
            throw new IllegalArgumentException("Request %s is not found".formatted(id));
        return request.finalState();
    }

    record Withdrawal(WithdrawalState state, long finaliseAt, Address address, BigDecimal amount) {
        public WithdrawalState finalState() {
            return finaliseAt <= System.currentTimeMillis() ? state : PROCESSING;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        WithdrawalServiceStub s = new WithdrawalServiceStub();
        WithdrawalId id = new WithdrawalId(UUID.randomUUID());
        s.requestWithdrawal(id, new Address("a"), BigDecimal.TEN);
        while (s.getRequestState(id) == PROCESSING){
            System.out.println(s.getRequestState(id));
            Thread.sleep(500);
        }
        System.out.println(s.getRequestState(id));
        System.out.println(s.getRequestState(id));
        System.out.println(s.getRequestState(id));



        }




}

