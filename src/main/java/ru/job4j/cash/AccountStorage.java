package ru.job4j.cash;

import java.util.HashMap;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot by null");
        }
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean result = false;
        var fromAccount = accounts.get(fromId);
        var toAccount = accounts.get(toId);
        if (fromAccount != null
                && toAccount != null
                && amount > 0
                && fromAccount.amount() >= amount) {
            accounts.replace(fromId, new Account(fromId, fromAccount.amount() - amount));
            accounts.replace(toId, new Account(toId, toAccount.amount() + amount));
            result = true;
        }
        return result;
    }
}