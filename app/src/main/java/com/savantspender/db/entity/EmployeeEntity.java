package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "employees")
public class EmployeeEntity {
    @PrimaryKey
    @NonNull
    public String Ssn;

    @NonNull
    public String Fname;

    @NonNull
    public String Lname;

    @NonNull
    public Date Bdate;

    public int Dno;

    public EmployeeEntity(String Ssn, String Fname, String Lname, Date Bdate, int Dno) {
        this.Ssn = Ssn;
        this.Fname = Fname;
        this.Lname = Lname;
        this.Bdate = Bdate;
        this.Dno = Dno;
    }

    @NonNull
    @Override
    public String toString() {
        return "Employee: " + Fname + " " + Lname + " " + Ssn + ", bdate " + Bdate.toString() + ", in department " + Dno;
    }
}
