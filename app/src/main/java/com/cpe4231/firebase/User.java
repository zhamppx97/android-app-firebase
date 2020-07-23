package com.cpe4231.firebase;

import com.google.firebase.database.IgnoreExtraProperties;
public class User
{
    public String nameTitle;
    public String firstName;
    public String lastName;
    public String age;
    public String studentID;
    public String nationalIDCard;
    public String address;
    public String postCode;
    public String mobilePhoneNumber;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User()
    {
    }

    public User(String nameTitle, String firstName, String lastName, String age, String studentID, String nationalIDCard, String address, String postCode, String mobilePhoneNumber)
    {
        this.nameTitle = nameTitle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.studentID = studentID;
        this.nationalIDCard = nationalIDCard;
        this.address = address;
        this.postCode = postCode;
        this.mobilePhoneNumber = mobilePhoneNumber;
    }
}
