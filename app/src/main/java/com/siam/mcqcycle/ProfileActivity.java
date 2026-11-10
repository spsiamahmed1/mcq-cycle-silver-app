package com.siam.mcqcycle;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.siam.mcqcycle.helper.CycleHelper;
import com.siam.mcqcycle.helper.PrivacyHelper;
import com.siam.mcqcycle.helper.SkillHelper;

public class ProfileActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state){super.onCreate(state);PrivacyHelper.apply(this);setContentView(R.layout.activity_profile);
        SwitchMaterial sound=findViewById(R.id.soundSwitch),vibration=findViewById(R.id.vibrationSwitch),dark=findViewById(R.id.darkSwitch),secure=findViewById(R.id.secureSwitch);
        sound.setChecked(CycleHelper.prefs(this).getBoolean("sound_enabled",true));vibration.setChecked(CycleHelper.prefs(this).getBoolean("vibration_enabled",true));
        dark.setChecked(CycleHelper.prefs(this).getBoolean("dark_mode",false));secure.setChecked(CycleHelper.prefs(this).getBoolean("secure_screen",true));
        sound.setOnCheckedChangeListener((b,c)->CycleHelper.prefs(this).edit().putBoolean("sound_enabled",c).apply());
        vibration.setOnCheckedChangeListener((b,c)->CycleHelper.prefs(this).edit().putBoolean("vibration_enabled",c).apply());
        dark.setOnCheckedChangeListener((b,c)->{CycleHelper.prefs(this).edit().putBoolean("dark_mode",c).apply();AppCompatDelegate.setDefaultNightMode(c?AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO);});
        secure.setOnCheckedChangeListener((b,c)->{CycleHelper.prefs(this).edit().putBoolean("secure_screen",c).apply();PrivacyHelper.apply(this);});
        int total=CycleHelper.total(this);((android.widget.TextView)findViewById(R.id.profileSkill)).setText("মোট MCQ: "+total+"\nবর্তমান Skill: "+SkillHelper.level(total)+"\n"+SkillHelper.remainingLabel(total));
        findViewById(R.id.profileHome).setOnClickListener(v->startActivity(new Intent(this,MainActivity.class)));
    }
}
