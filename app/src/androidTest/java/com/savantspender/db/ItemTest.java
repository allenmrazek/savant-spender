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

public class ItemTest extends InstitutionTest {
    private ItemDao DOAItems;
    private InstitutionDao DOAInstitution;

    @Before
    @Override
    public void createDb() {
        super.createDb();
        DOAItems = mDatabase.itemDao();
        DOAInstitution = mDatabase.institutionDao();
    }

    @Test
    public void test_insert_read_delete() throws InterruptedException {
        //populate nessarcy items in database
        //declare dumby varibles
        String IDInstitution = "123456";
        String ID2Institution = "234567";
        InstitutionEntity inputE = new InstitutionEntity(IDInstitution, "BofA");
        InstitutionEntity expectedE = inputE;
        InstitutionEntity inputE2 = new InstitutionEntity(ID2Institution, "Chase");
        InstitutionEntity expectedE2 = inputE2;
        //prepopulate database
        DOAInstitution.insert(inputE);
        DOAInstitution.insert(inputE2);
        //////////////////////////////
        String IDItem = "654321";
        String ID2Item = "abcdefg";

        ItemEntity inputItemE = new ItemEntity(IDItem, IDInstitution, "access_token", "public_token");
        ItemEntity inputItemE2 = new ItemEntity(ID2Item, ID2Institution, "access_token2", "public_token2");

        DOAItems.insert(inputItemE);
        DOAItems.insert(inputItemE2);

        List<ItemEntity> outputItemEs = LiveDataTestUtil.getValue(DOAItems.getItems());

        assertThat(outputItemEs.size(), is(2));

       // entities = LiveDataTestUtil.getValue(DOAItems.getItems());

       // assertThat(entities.size(), is(1));

        //aserting items are identical
        ItemEntity outputItemE = outputItemEs.get(0);

        assertThat(outputItemE.id, equalTo(inputItemE.id));
        assertThat(outputItemE.institutionId, equalTo(inputItemE.institutionId));
        assertThat(outputItemE.access_token, equalTo(inputItemE.access_token));
        assertThat(outputItemE.public_token, equalTo(inputItemE.public_token));



        //testing replacement functionality for collision
        //generate matching enity with change
        String acesstokenALT = "access_tokenALT";
        String public_tokenALT = "public_tokenALT";

        ItemEntity inputItemE3 = new ItemEntity(IDItem, IDInstitution, acesstokenALT, public_tokenALT);
        DOAItems.insert(inputItemE3);
        List<ItemEntity> newoutputItemEs = LiveDataTestUtil.getValue(DOAItems.getItems());
        ItemEntity newoutputItemE = newoutputItemEs.get(1); // because its now the second item

        //checking it has been replaced
        assertThat(newoutputItemE.id, equalTo(IDItem));
        assertThat(newoutputItemE.institutionId, equalTo(IDInstitution));
        assertThat(newoutputItemE.access_token, equalTo(acesstokenALT));
        assertThat(newoutputItemE.public_token, equalTo(public_tokenALT));

        //entities = LiveDataTestUtil.getValue(mItems.getItems());

        //assertThat(entities.size(), is(0));

        //mItems.delete(retrieved);

        //entities = LiveDataTestUtil.getValue(mItems.getItems());

        //assertThat(entities.size(), is(0));

    }
}
