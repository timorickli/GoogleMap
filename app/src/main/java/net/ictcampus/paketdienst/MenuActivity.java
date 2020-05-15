package net.ictcampus.paketdienst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageButton ib = findViewById(R.id.imageButton);
        TextView spielanleitung = findViewById(R.id.spielanleitung);
        TextView shop = findViewById(R.id.shop);
        TextView inventory = findViewById(R.id.inventory);
        TextView settings = findViewById(R.id.settings);
        ib.setOnClickListener(this);
        spielanleitung.setOnClickListener(this);
        inventory.setOnClickListener(this);
        shop.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.imageButton:
                intent= new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
                break;
            case R.id.spielanleitung:
                break;
            case R.id.shop:
                break;
            case R.id.inventory:
                intent= new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent= new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}