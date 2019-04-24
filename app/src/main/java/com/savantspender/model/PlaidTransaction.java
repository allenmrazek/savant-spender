package com.savantspender.model;

import java.util.List;

public interface PlaidTransaction {
    String getTransactionId();
    List<Tag> getTags();
    float getAmount();

    PlaidAccount getAccount();
}
