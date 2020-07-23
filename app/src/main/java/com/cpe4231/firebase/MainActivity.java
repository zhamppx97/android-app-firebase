package com.cpe4231.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputNameTitle, inputFirstName, inputLastName, inputAge, inputStudentID, inputNationalIDCard, inputAddress, inputPostCode, inputMobilePhoneNumber;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        txtDetails = (TextView)findViewById(R.id.txt_user);

        inputNameTitle = (EditText)findViewById(R.id.nametitle);
        inputFirstName = (EditText)findViewById(R.id.firstname);
        inputLastName = (EditText)findViewById(R.id.lastName);
        inputAge = (EditText)findViewById(R.id.age);
        inputStudentID = (EditText)findViewById(R.id.studentid);
        inputNationalIDCard = (EditText)findViewById(R.id.nationalidcard);
        inputAddress = (EditText)findViewById(R.id.address);
        inputPostCode = (EditText)findViewById(R.id.postCode);
        inputMobilePhoneNumber = (EditText)findViewById(R.id.mobilephonenumber);

        btnSave = (Button)findViewById(R.id.btn_save);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");
        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");
                String appTitle = dataSnapshot.getValue(String.class);
                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTitle = inputNameTitle.getText().toString();
                String firstName = inputFirstName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String age = inputAge.getText().toString();
                String studentID = inputStudentID.getText().toString();
                String nationalIDCard = inputNationalIDCard.getText().toString();
                String address = inputAddress.getText().toString();
                String postCode = inputPostCode.getText().toString();
                String mobilePhoneNumber = inputMobilePhoneNumber.getText().toString();

                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(nameTitle, firstName, lastName, age, studentID, nationalIDCard, address, postCode, mobilePhoneNumber);
                } else {
                    updateUser(nameTitle, firstName, lastName, age, studentID, nationalIDCard, address, postCode, mobilePhoneNumber);
                }
            }
        });
        toggleButton();
    }

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Update");
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String nameTitle, String firstName, String lastName, String age, String studentID, String nationalIDCard, String address, String postCode, String mobilePhoneNumber) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        User user = new User(nameTitle, firstName, lastName, age, studentID, nationalIDCard, address, postCode, mobilePhoneNumber);
        mFirebaseDatabase.child(userId).setValue(user);
        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }
                Log.e(TAG, "User data is changed!" + user.firstName + " " + user.lastName);
                // Display newly updated name and email
                //txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                inputNameTitle.setText("");
                inputFirstName.setText("");
                inputLastName.setText("");
                inputAge.setText("");
                inputStudentID.setText("");
                inputNationalIDCard.setText("");
                inputAddress.setText("");
                inputPostCode.setText("");
                inputMobilePhoneNumber.setText("");
                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String nameTitle, String firstName, String lastName, String age, String studentID, String nationalIDCard, String address, String postCode, String mobilePhoneNumber) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(nameTitle))
            mFirebaseDatabase.child(userId).child("nameTitle").setValue(nameTitle);
        if (!TextUtils.isEmpty(firstName))
            mFirebaseDatabase.child(userId).child("firstName").setValue(firstName);
        if (!TextUtils.isEmpty(lastName))
            mFirebaseDatabase.child(userId).child("lastName").setValue(lastName);
        if (!TextUtils.isEmpty(age))
            mFirebaseDatabase.child(userId).child("age").setValue(age);
        if (!TextUtils.isEmpty(studentID))
            mFirebaseDatabase.child(userId).child("studentID").setValue(studentID);
        if (!TextUtils.isEmpty(nationalIDCard))
            mFirebaseDatabase.child(userId).child("nationalIDCard").setValue(nationalIDCard);
        if (!TextUtils.isEmpty(address))
            mFirebaseDatabase.child(userId).child("address").setValue(address);
        if (!TextUtils.isEmpty(postCode))
            mFirebaseDatabase.child(userId).child("postCode").setValue(postCode);
        if (!TextUtils.isEmpty(mobilePhoneNumber))
            mFirebaseDatabase.child(userId).child("mobilePhoneNumber").setValue(mobilePhoneNumber);
    }
}
