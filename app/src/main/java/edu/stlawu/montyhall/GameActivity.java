//Anna Breton
//sources for my app:
// https://sproutsocial.com/insights/bounce-animation-for-android/, https://developer.android.com
// https://en.wikipedia.org/wiki/Monty_Hall_problem
//https://www.freesoundeffects.com/free-sounds/
//Images of goats, cars and doors are from google images
// code stared in cs450 with assistance from Sam Emmerson
//https://developer.android.com
package edu.stlawu.montyhall;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.Arrays;
import java.util.Random;

import static edu.stlawu.montyhall.MainFragment.PREF_NAME;

public class GameActivity extends AppCompatActivity {
    //variables for keeping track of everything!
    public Random randomizer = new Random();
    public ImageButton door1 = null;
    public ImageButton door2 = null;
    public ImageButton door3 = null;
    private ImageButton doorWCar = null;
    private ImageButton selectedDoor = null;
    private ImageButton goatDoor = null;
    private TextView prompt = null;
    private TextView again = null;
    private int win = 0;
    private int loss = 0;
    private int choicedoor = -1;
    private int winnerdoor = -1;
    private int opendoor = -1;
    private TextView timesWon = null;
    private TextView timesLost = null;
    public AudioAttributes aa = null;
    private SoundPool soundPool = null;
    private int sound_car = 0;
    private int sound_goat = 0;
    private ImageButton doors[];

    private void save() {
        getPreferences(MODE_PRIVATE)
                .edit()
                .putInt("win_count", win)
                .apply();
        getPreferences(MODE_PRIVATE)
                .edit()
                .putInt("loss_count", loss)
                .apply();
        getPreferences(MODE_PRIVATE)
                .edit()
                .putInt("win_Door", choicedoor)
                .apply();

        getPreferences(MODE_PRIVATE)
                .edit()
                .putInt("goat_door", opendoor)
                .apply();

        getPreferences(MODE_PRIVATE)
                .edit()
                .putInt("selected_door", choicedoor)
                .apply();
    }




    private void doBounceAnimation(View targetView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", 0, 25, 0);
        //animator.setInterpolator(new EasingInterpolator(Ease.ELASTIC_IN_OUT));
        animator.setStartDelay(500);
        animator.setDuration(1500);
        animator.start();
    }
    private ImageButton generate_win() {
        int win = (randomizer.nextInt(3));
        winnerdoor = win;
        return doors[win];
    }

    private void Image_button_clicked(final ImageButton mydoor) {
        if (selectedDoor == null) {
            selectedDoor = mydoor;
            choicedoor = Arrays.asList(doors).indexOf(mydoor);
            getPreferences(MODE_PRIVATE).edit().putInt("selected_door", choicedoor).apply();
            prompt.setText("do you want to choose a different door?");
            mydoor.setImageResource(R.drawable.selected_door);
            int tmp = randomizer.nextInt(3);
            while (doors[tmp] == doorWCar) {
                tmp = randomizer.nextInt(3);
            }

            ImageButton aDoor = doors[tmp];
            aDoor.setImageResource(R.drawable.goat);
            opendoor = tmp;
            goatDoor = aDoor;
            getPreferences(MODE_PRIVATE).edit().putInt("goat_door", opendoor).apply();
        } else {
            selectedDoor.setImageResource(R.drawable.door);

            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    mydoor.setImageResource(R.drawable.three);
                }

                public void onFinish() {
                    BreakIterator mTextField;
                    new CountDownTimer(1000, 100) {
                        public void onTick(long millisUntilFinished) {
                            mydoor.setImageResource(R.drawable.two);
                        }

                        public void onFinish() {

                            BreakIterator mTextField;
                            new CountDownTimer(1000, 100) {

                                public void onTick(long millisUntilFinished) {

                                    mydoor.setImageResource(R.drawable.one);
                                }

                                public void onFinish() {
                                    if (doorWCar == mydoor) {
                                        mydoor.setImageResource(R.drawable.car);
                                        win++;

                                        soundPool.play(sound_car, 1f,
                                                1f, 1, 0, 1f);
                                    } else {
                                        door1.setEnabled(false);
                                        door2.setEnabled(false);
                                        door3.setEnabled(false);
                                        mydoor.setImageResource(R.drawable.goat);
                                        loss++;
                                        doorWCar.setImageResource(R.drawable.car);
                                        soundPool.play(sound_goat, 1f,
                                                1f, 1, 0, 1f);
                                        doBounceAnimation(mydoor);
                                    }
                                    choicedoor = -1;
                                    winnerdoor = -1;
                                    opendoor = -1;
                                    doorWCar = null;
                                    selectedDoor = null;
                                    goatDoor = null;
                                    timesLost.setText(Integer.toString(loss));
                                    timesWon.setText(Integer.toString(win));
                                    again.setText(R.string.again);
                                    again.setEnabled(true);
                                    again.setVisibility(View.VISIBLE);
                                    save();
                                    //start everything!
                                }
                            }.start();
                        }
                    }.start();
                }
            }.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        boolean newGame = getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE).getBoolean("NEWCLICKED", true);

        this.win = 0;
        this.loss = 0;
        this.timesWon = findViewById(R.id.win_text);
        this.timesLost = findViewById(R.id.loss_text);
        this.prompt = findViewById(R.id.prompt);
        this.again = findViewById(R.id.again);

        this.aa = new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        this.soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(aa)
                .build();

        this.sound_car = this.soundPool.load(
                this, R.raw.car_horn, 1);
        this.sound_goat = this.soundPool.load(
                this, R.raw.goat_noise, 1);

        selectedDoor = null;
        door1 = (ImageButton) findViewById(R.id.door1);
        door2 = (ImageButton) findViewById(R.id.door2);
        door3 = (ImageButton) findViewById(R.id.door3);

        doors = new ImageButton[]{door1, door2, door3};
        this.door1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_button_clicked(door1);

            }
        });
        this.door2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_button_clicked(door2);
            }
        });
        this.door3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_button_clicked(door3);
            }
        });


        if (newGame == true) {
            this.win = 0;
            this.loss = 0;
            getPreferences(MODE_PRIVATE).edit().putInt("win_count", 0);
            getPreferences(MODE_PRIVATE).edit().putInt("loss_count", 0);
            this.selectedDoor = null;
            choicedoor = -1;
            doorWCar = generate_win();
            getPreferences(MODE_PRIVATE).edit().putInt("win_Door", -1);
        } else {

            System.out.println(getPreferences(MODE_PRIVATE).contains("win_Door"));
            winnerdoor = getPreferences(MODE_PRIVATE).getInt("win_Door", -1);

            if (winnerdoor == -1) {
                doorWCar = generate_win();
            } else {
                doorWCar = doors[winnerdoor];
            }
            opendoor = getPreferences(MODE_PRIVATE).getInt("goat_door", -1);

            if (opendoor == -1) {
                goatDoor = null;
            } else {
                goatDoor = doors[opendoor];
                goatDoor.setImageResource(R.drawable.goat);
            }

            choicedoor = getPreferences(MODE_PRIVATE).getInt("selected_door", -1);
            if (choicedoor == -1) {
                selectedDoor = null;
            } else {

                selectedDoor = doors[choicedoor];
                selectedDoor.setImageResource(R.drawable.selected_door);
            }

            win = getPreferences(MODE_PRIVATE).getInt("win_count", 0);

            loss = getPreferences(MODE_PRIVATE).getInt("loss_count", 0);

            timesLost.setText(Integer.toString(loss));
            timesWon.setText(Integer.toString(win));

        }

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDoor = null;
                goatDoor = null;
                doorWCar = generate_win();
                opendoor = -1;
                choicedoor = -1;
                winnerdoor = -1;
                door1.setImageResource(R.drawable.door);
                door2.setImageResource(R.drawable.door);
                door3.setImageResource(R.drawable.door);
                again.setText("");
                prompt.setText("Choose a Door");
                door1.setEnabled(true);
                door2.setEnabled(true);
                door3.setEnabled(true);
                again.setVisibility(View.INVISIBLE);
                save(); }}); }

    @Override
    protected void onStart() {
        save();
        super.onStart();
    }

    @Override
    protected void onPause() {
        save();
        super.onPause();
    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        save();
        super.onDestroy();
    }


}

