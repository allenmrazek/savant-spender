package com.savantspender.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.response.TransactionsGetResponse;
import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.TransactionEntity;
import com.savantspender.util.PlaidUtil;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class DownloadTransactionsWorker extends Worker {
    private static final int NUM_TRANSACTIONS_DOWNLOAD_PER_CALL = 50;

    private PlaidClient mClient;
    private AppDatabase mDatabase;


    public DownloadTransactionsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }



    @NonNull
    @Override
    public Result doWork() { // already on a bg thread, make synchronous calls
        try {
            SavantSpender spender = (SavantSpender) getApplicationContext();
            mDatabase = spender.getDatabase();

            List<AccountEntity> accounts = mDatabase.accountDao().getAccountsSync();

            mClient = PlaidUtil.build();

            for (AccountEntity account : accounts) {
                Log.i("Spender", "Downloading transaction details for " + account.name);

                downloadTransactions(account);

                // todo: do something with the transactions

                Log.i("Spender", "Finished download transaction details for " + account.name);
            }
            // todo: determine success

            Log.i("Spender", "finished downloading transaction data for " + accounts.size() + " accounts");

            return Result.success();
        } catch (Exception e) {
            Log.e("Spender", "failed to download transactions: " + e.getMessage());

            // todo: handle this better
            return Result.failure(
                    new Data.Builder().putString("message", e.getMessage()).build());
        }
    }



    private List<TransactionEntity> downloadTransactions(AccountEntity account) throws IOException { // todo: better exception handling
        String accessToken = mDatabase.itemDao().getAccessTokenSync(account.itemId);
        List<TransactionEntity> transactions = new ArrayList<>(NUM_TRANSACTIONS_DOWNLOAD_PER_CALL);

        TransactionsGetRequest request =
                new TransactionsGetRequest(accessToken, getStartDate(), getEndDate())
                    .withAccountIds(Arrays.asList(account.id))
                    .withCount(NUM_TRANSACTIONS_DOWNLOAD_PER_CALL);

        Log.e("Spender", "StartDate: " + getStartDate().toString());
        Log.e("Spender", "EndDate: " + getEndDate().toString());

        int offset = 0;
        int totalCount = 0;

        do {
            Response<TransactionsGetResponse> response = mClient.service()
                    .transactionsGet(request).execute(); // sync


            if (!response.isSuccessful()) {
                // todo: better error reporting
                Log.e("Spender", "failed to update account " + account.name + ": " + response.code() + ": " + response.message());

                return new ArrayList<>(); // don't modify any transactions
            }

            List<TransactionsGetResponse.Transaction> downloadedTransactions = response.body().getTransactions();

            totalCount = response.body().getTotalTransactions();
            offset += downloadedTransactions.size();

            Log.v("Spender", "downloaded " + offset + " of " + totalCount + " transactions");

            request.withOffset(offset);

            // convert into TransactionEntities
            for (TransactionsGetResponse.Transaction t : downloadedTransactions) {
                // if (t.amount is negative ignore ?)
//                transactions.add(
//                        new TransactionEntity(
//                                t.getTransactionId(),
//                                t.getAccountId(),
//                                account.itemId,
//                                t.getName(),
//                                t.getAmount(),
//                                t.getPending(),
//                                t.getDate()));
                    Log.w("Spender", "Transaction: " + t.getAccountId() + " : " + t.getName() + ", " + t.getTransactionId());

            }
        } while (offset < totalCount);

        return null; // todo
    }


    private Date getStartDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);

        return calendar.getTime();
    }


    private Date getEndDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }


    private Date getDateFromString(@NonNull String date) {
        //DateTimeFormatter formatter = new DateTimeFormatter();

        return null;
    }
}
