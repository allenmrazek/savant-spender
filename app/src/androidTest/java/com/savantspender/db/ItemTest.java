package com.savantspender.db;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.dao.ItemDao;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ItemTest extends DefaultDatabaseTest {
    private ItemDao DOAItems;
    private InstitutionDao DOAInstitution;
    private String IDInstitution = "123456";
    private String ID2Institution = "234567";
    private ItemEntity inputE = new ItemEntity("654321", this.IDInstitution, "access_token", "public_token");
    private ItemEntity inputE2 = new ItemEntity("abcdefg", this.ID2Institution, "access_token2", "public_token2");

    @Before
    @Override
    public void createDb() {
        super.createDb();
        DOAItems = mDatabase.itemDao();
        DOAInstitution = mDatabase.institutionDao();
        //populate nessarcy items in database
        //declare dumby varibles
        InstitutionEntity inputInstE = new InstitutionEntity(this.IDInstitution, "BofA");
        InstitutionEntity inputInstE2 = new InstitutionEntity(this.ID2Institution, "Chase");
        //prepopulate database
        DOAInstitution.insert(inputInstE);
        DOAInstitution.insert(inputInstE2);
        //////////////////////////////
        DOAItems.insert(this.inputE);
        DOAItems.insert(this.inputE2);
    }

    @Test
    public void test_insert() throws InterruptedException {}

    @Test
    public void test_getValue() throws InterruptedException
    {
        List<ItemEntity> outputEs = LiveDataTestUtil.getValue(DOAItems.getItems());
        ItemEntity outputE = outputEs.get(0);

        assertThat(outputE,is(instanceOf(ItemEntity.class)));
        assertThat(outputE.id, equalTo(this.inputE.id));
        assertThat(outputE.institutionId, equalTo(this.inputE.institutionId));
        assertThat(outputE.access_token, equalTo(this.inputE.access_token));
        assertThat(outputE.public_token, equalTo(this.inputE.public_token));
    }

    @Test
    public void test_conflict_resolution() throws InterruptedException
    {
        //testing replacement functionality for collision
        //generate matching enity with change
        String acesstokenALT = "access_tokenALT";
        String public_tokenALT = "public_tokenALT";

        ItemEntity inputEALT = new ItemEntity(this.inputE.id, this.inputE.institutionId, acesstokenALT, public_tokenALT);
        DOAItems.insert(inputEALT);
        List<ItemEntity> outputEs = LiveDataTestUtil.getValue(DOAItems.getItems());
        ItemEntity outputE = outputEs.get(1); // because its now the second item
        /////////////////////////////////
        //checking it has been replaced
        assertThat(outputE.id, equalTo(this.inputE.id));
        assertThat(outputE.institutionId, equalTo(this.inputE.institutionId));
        assertThat(outputE.access_token, equalTo(acesstokenALT));
        assertThat(outputE.public_token, equalTo(public_tokenALT));
    }

    @Test
    public void test_delete() throws InterruptedException {

        DOAItems.delete(this.inputE);
        List<ItemEntity> outputEs = LiveDataTestUtil.getValue(DOAItems.getItems());
        assertThat(outputEs.size(),is(equalTo(1)));

       // entities = LiveDataTestUtil.getValue(DOAItems.getItems());

       // assertThat(entities.size(), is(1));

        //aserting items are identical


        //entities = LiveDataTestUtil.getValue(mItems.getItems());

        //assertThat(entities.size(), is(0));

        //mItems.delete(retrieved);

        //entities = LiveDataTestUtil.getValue(mItems.getItems());

        //assertThat(entities.size(), is(0));

    }
}
