package com.pyroarsonistapps.subterreneandescent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private int level = 1;
    private int heroHP;
    private int initMaxHeroHP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startNewGame = (Button) findViewById(R.id.start_new_game);
        startNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, LevelActivity.class);
                myIntent.putExtra("level", level);
                myIntent.putExtra("heroHP", heroHP);
                myIntent.putExtra("initMaxHeroHP", initMaxHeroHP);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

}