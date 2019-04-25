package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "works_on",
    foreignKeys = {
            @ForeignKey(
                    parentColumns = "Ssn",
                    childColumns = "Essn",
                    entity = EmployeeEntity.class),
            @ForeignKey(
                    childColumns = "Pno",
                    parentColumns = "Pnumber",
                    entity = ProjectEntity.class)
    },
    indices = { @Index("Pno")})
public class WorksOnEntity {
    @PrimaryKey
    @NonNull
    public String Essn;

    public int Pno;
    public int Hours;

    public WorksOnEntity(@NonNull EmployeeEntity emp, @NonNull ProjectEntity proj) {
        Essn = emp.Ssn;
        Pno = proj.Pnumber;
    }

    public WorksOnEntity() {}
}
