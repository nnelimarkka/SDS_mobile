package com.example.quicklauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        Button secondActivityBtn = (Button) findViewById(R.id.secondActivityBtn);
        Button googleBtn = (Button) findViewById(R.id.googleButton);
        Button callButton = (Button) findViewById(R.id.callButton);
        Button toastButton = (Button) findViewById(R.id.toastButton);

        secondActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);

                //passing information to new activity
                startIntent.putExtra("message", "Hello World!");

                startActivity(startIntent);
            }
        });

        //launching an outside app
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String google = "http://www.google.com";
                Uri webUrl = Uri.parse(google);

                Intent goToGoogle = new Intent(Intent.ACTION_VIEW, webUrl);

                //check that an application was found for viewing
                /*if(goToGoogle.resolveActivity(getPackageManager()) != null) {
                    startActivity(goToGoogle);
                }
                This did not work so I had to research a bit. Luckily Android developers has great documentation
                */

                //Let's use try catch instead
                try {
                    startActivity(goToGoogle);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Default browser was not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //going to phone dialer
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = "tel:1234";
                Uri telUrl = Uri.parse(tel);

                Intent goToDial = new Intent(Intent.ACTION_VIEW, telUrl);

                //check that an application was found for viewing
                /*if (goToDial.resolveActivity(getPackageManager()) != null) {
                    startActivity(goToDial);
                }
                This did not work so I had to research a bit. Luckily Android developers has great documentation
                */

                try {
                    startActivity(goToDial);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Phone dialer was not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //toasting for no reason
        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "This is a toast", Toast.LENGTH_SHORT).show();
            }
        });
    }
}