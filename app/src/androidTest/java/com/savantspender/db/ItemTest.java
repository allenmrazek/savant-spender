package com.savantspender.db;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.ItemDao;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ItemTest extends DefaultDatabaseTest {
    private static final String TestInstitutionId = "test_inst_id";
    private static final String TestItemId = "test_item_id";
    private static final String TestItemAccess = "access_token";

    private ItemDao mItems;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    @Override
    public void createDb() {
        super.createDb();
        mItems = mDatabase.itemDao();

        mDatabase.institutionDao().insert(new InstitutionEntity(TestInstitutionId, "test institution"));
        mDatabase.itemDao().insert(new ItemEntity(TestItemId, TestInstitutionId, TestItemAccess));
    }

    @Test
    public void insert_with_valid_institution() {
        ItemEntity e = new ItemEntity("itemid", TestInstitutionId, "access");

        mItems.insert(e);
    }

    @Test
    public void insert_then_read() throws InterruptedException {
        List<ItemEntity> items = LiveDataTestUtil.getValue(mItems.getItems());

        int count = items.size();

        ItemEntity e = new ItemEntity("itemid", TestInstitutionId, "access");

        mItems.insert(e);

        items = LiveDataTestUtil.getValue(mItems.getItems());

        assertThat(items.size(), equalTo(count + 1));
    }

    @Test
    public void insert_with_invalid_institution() {
        ItemEntity e = new ItemEntity("itemid", "invalid_id", "access");

        thrown.expect(SQLiteConstraintException.class);
        thrown.expectMessage("FOREIGN KEY constraint failed");

        mItems.insert(e);
    }

    @Test
    public void insert_with_duplicate_id() {
        ItemEntity e = new ItemEntity(TestItemId, TestInstitutionId, "access");

        thrown.expect(SQLiteConstraintException.class);
        thrown.expectMessage("UNIQUE constraint failed");
        mItems.insert(e);
    }

    @Test
    public void read() throws InterruptedException {
        List<ItemEntity> items = LiveDataTestUtil.getValue(mItems.getItems());

        assertThat(items.size(), is(1));

        ItemEntity e = items.get(0);

        assertThat(e.id, equalTo(TestItemId));
        assertThat(e.institutionId, equalTo(TestInstitutionId));
        assertThat(e.access_token, equalTo(TestItemAccess));
    }

    @Test
    public void delete_existing() throws InterruptedException {
        List<ItemEntity> items = LiveDataTestUtil.getValue(mItems.getItems());

        assertThat(items.size(), is(1));

        mItems.delete(items.get(0));

        items = LiveDataTestUtil.getValue(mItems.getItems());

        assertThat(items.size(), is(0));
    }

    @Test
    public void delete_nonexisting() throws InterruptedException {
        ItemEntity doesntExist = new ItemEntity("nonexistingid", TestInstitutionId, "access");

        mItems.delete(doesntExist);
    }


    @Test
    public void test_insert_read_delete() throws InterruptedException {
        //super.preliminary_loader();
        mDatabase.institutionDao().insert(new InstitutionEntity("instid-123", "institution name here"));
        ItemEntity e = new ItemEntity("654321", "instid-123", "123456");

        mItems.insert(e);

        //List<ItemEntity> entities = LiveDataTestUtil.getValue(mItems.getItems());

       // assertThat(entities.size(), is(1));

       // entities = LiveDataTestUtil.getValue(mItems.getItems());

       // assertThat(entities.size(), is(1));

       // ItemEntity retrieved = entities.get(0);

       // assertThat(retrieved, equalTo(e));

       // entities = LiveDataTestUtil.getValue(mItems.getItems());

       // assertThat(entities.size(), is(0));

      //  mItems.delete(retrieved);

        //entities = LiveDataTestUtil.getValue(mItems.getItems());

       // assertThat(entities.size(), is(0));
    }
}
