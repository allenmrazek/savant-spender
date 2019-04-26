package com.savantspender.db;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.PlaidItemDao;
import com.savantspender.db.entity.PlaidItemEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PlaidItemTest extends DefaultDatabaseTest {
    private PlaidItemDao mItems;

    @Before
    @Override
    public void createDb() {
        super.createDb();
        mItems = mDatabase.itemDao();
    }

    @Test
    public void test_insert_read_delete() throws InterruptedException {
        PlaidItemEntity e = new PlaidItemEntity("123456", "sandbox-123456", "instid-123");

        mItems.insert(e);

        List<PlaidItemEntity> entities = LiveDataTestUtil.getValue(mItems.getItems());

        assertThat(entities.size(), is(1));

        entities = LiveDataTestUtil.getValue(mItems.getItemsFromInstitution(e.getInstId()));

        assertThat(entities.size(), is(1));

        PlaidItemEntity retrieved = entities.get(0);

        assertThat(retrieved, equalTo(e));

        entities = LiveDataTestUtil.getValue(mItems.getItemsFromInstitution("none"));

        assertThat(entities.size(), is(0));

        mItems.delete(retrieved);

        entities = LiveDataTestUtil.getValue(mItems.getItems());

        assertThat(entities.size(), is(0));
    }
}
