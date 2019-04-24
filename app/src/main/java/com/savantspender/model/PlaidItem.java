package com.savantspender.model;

import java.util.List;

// Uniquely identifies a Plaid 'Item', a set of credentials for one institution. Multiple accounts
// may be held under one institution
public interface PlaidItem {
    String getPublicToken();
    String getAccessToken();
    String getId();
}
