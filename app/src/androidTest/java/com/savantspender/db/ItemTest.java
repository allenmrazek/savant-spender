package com.savantspender.db;

import android.database.sqlite.SQLiteConstraintException;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.dao.ItemDao;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import retrofit2.http.HEAD;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ItemTest extends DefaultDatabaseTest {

    private static final String TestInstitutionId = "test_inst_id";
    private static final String TestItemId = "test_item_id";
    private static final String TestItemAccess = "access_token";

    private ItemDao mItems;


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
    }


}
