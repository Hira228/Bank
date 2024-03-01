package org.example;

import org.example.entity.BankAccountEntity;
import org.example.entity.TransactionEntity;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionEntityTest {

    @Test
    public void testCreateTransactionEntity() {
        BankAccountEntity sender = new BankAccountEntity();
        sender.setId(1L);

        BankAccountEntity recipient = new BankAccountEntity();
        recipient.setId(2L);

        TransactionEntity transaction = TransactionEntity.builder()
                .sender(sender)
                .recipient(recipient)
                .amount(BigDecimal.valueOf(100))
                .timestamp(LocalDateTime.now())
                .build();

        Assert.assertEquals(sender, transaction.getSender());
        Assert.assertEquals(recipient, transaction.getRecipient());
        Assert.assertEquals(BigDecimal.valueOf(100), transaction.getAmount());
        Assert.assertNotNull(transaction.getTimestamp());
    }

    @Test
    public void testTransactionEntityId() {
        UUID id = UUID.randomUUID();
        TransactionEntity transaction = TransactionEntity.builder()
                .id(id)
                .sender(new BankAccountEntity())
                .recipient(new BankAccountEntity())
                .amount(BigDecimal.TEN)
                .timestamp(LocalDateTime.now())
                .build();

        Assert.assertEquals(id, transaction.getId());
    }
}
