package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        textView = findViewById(R.id.sensor_info);
        textView.setTextColor(Color.BLACK);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getSensorList(Sensor.TYPE_ALL).get(getIntent().getIntExtra("sensor_index", -1));
        if (sensor != null) {
            switch (sensor.getType()) {
                case Sensor.TYPE_GRAVITY: {
                    textView.setText(getResources().getString(R.string.gravity_sensor_label));
                    break;
                }
                case Sensor.TYPE_GAME_ROTATION_VECTOR: {
                    textView.setText(getResources().getString(R.string.game_rotation_vector_sensor_label));
                    break;
                }
                case Sensor.TYPE_LIGHT: {
                    textView.setText(getResources().getString(R.string.light_sensor_label));
                    break;
                }
                case Sensor.TYPE_PRESSURE: {
                    textView.setText(getResources().getString(R.string.pressure_sensor_label));
                    break;
                }
                case Sensor.TYPE_AMBIENT_TEMPERATURE: {
                    textView.setText(getResources().getString(R.string.ambient_temperature_sensor_label));
                    break;
                }
                case Sensor.TYPE_RELATIVE_HUMIDITY: {
                    textView.setText(getResources().getString(R.string.humidity_sensor_label));
                    break;
                }
                case Sensor.TYPE_STEP_COUNTER: {
                    textView.setText(getResources().getString(R.string.step_counter_sensor_label));
                    break;
                }
                case Sensor.TYPE_ACCELEROMETER: {
                    textView.setText(getResources().getString(R.string.accelerometer_sensor_label));
                    break;
                }
                case Sensor.TYPE_STEP_DETECTOR: {
                    textView.setText(getResources().getString(R.string.step_detector_sensor_label));
                    break;
                }
                case Sensor.TYPE_ORIENTATION: {
                    textView.setText(getResources().getString(R.string.orientation_sensor_label));
                    break;
                }
                case Sensor.TYPE_GYROSCOPE: {
                    textView.setText(getResources().getString(R.string.gyroscope_sensor_label));
                    break;
                }
                case Sensor.TYPE_PROXIMITY: {
                    textView.setText(getResources().getString(R.string.proximity_sensor_label));
                    break;
                }
                case Sensor.TYPE_LINEAR_ACCELERATION: {
                    textView.setText(getResources().getString(R.string.linear_acceleration_sensor_label));
                    break;
                }
                default: {
                    sensor = null;
                    textView.setText(R.string.missing_sensor);
                    break;
                }
            }
        } else {
            textView.setText(R.string.missing_sensor);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensor != null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        switch (sensorType) {
            case Sensor.TYPE_GRAVITY: {
                textView.setText(getResources().getString(R.string.gravity_sensor_label, currentValue));
                textView.setBackgroundColor(Color.rgb(0, (255*(int)currentValue/20), 0));
                textView.setTextColor(Color.WHITE);
                break;
            }
            case Sensor.TYPE_GAME_ROTATION_VECTOR: {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                textView.setText(getResources().getString(R.string.game_rotation_vector_sensor_label, x, y, z));
                textView.setBackgroundColor(Color.rgb(0, 0, (255*(int)(Math.abs(x+y+z)/3))));
                textView.setTextColor(Color.WHITE);
                break;
            }
            case Sensor.TYPE_LIGHT: {
                textView.setText(getResources().getString(R.string.light_sensor_label, currentValue));
                textView.setBackgroundColor(Color.rgb((255*(int)currentValue/40000), (255*(int)currentValue/40000), (255*(int)currentValue/40000)));
                if(currentValue < 20000) {
                    textView.setTextColor(Color.WHITE);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                break;
            }
            case Sensor.TYPE_PRESSURE: {
                textView.setText(getResources().getString(R.string.pressure_sensor_label, currentValue));
                textView.setBackgroundColor(Color.rgb((255*(int)currentValue/1100), 0, 0));
                textView.setTextColor(Color.WHITE);
                break;
            }
            case Sensor.TYPE_AMBIENT_TEMPERATURE: {
                float temp = sensorEvent.values[0];
                textView.setText(getResources().getString(R.string.ambient_temperature_sensor_label, temp));
                break;
            }
            case Sensor.TYPE_RELATIVE_HUMIDITY: {
                float humidity = sensorEvent.values[0];
                textView.setText(getResources().getString(R.string.humidity_sensor_label, humidity));
                break;
            }
            case Sensor.TYPE_STEP_COUNTER: {
                int steps = (int) sensorEvent.values[0];
                String text = getResources().getString(R.string.step_counter_sensor_label, steps);
                textView.setText(text);
                break;
            }
            case Sensor.TYPE_ACCELEROMETER: {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                textView.setText(getResources().getString(R.string.accelerometer_sensor_label, x, y, z));
                break;
            }
            case Sensor.TYPE_STEP_DETECTOR: {
                int steps = (int) sensorEvent.values[0];
                textView.setText(getResources().getString(R.string.step_detector_sensor_label, steps));
                break;
            }
            case Sensor.TYPE_ORIENTATION: {
                float azimuth = sensorEvent.values[0]; // Azimut
                float pitch = sensorEvent.values[1];   // Nachylenie
                float roll = sensorEvent.values[2];    // Rolowanie
                textView.setText(getResources().getString(R.string.orientation_sensor_label, azimuth, pitch, roll));
                break;
            }
            case Sensor.TYPE_GYROSCOPE: {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                textView.setText(getResources().getString(R.string.gyroscope_sensor_label, x, y, z));
                break;
            }
            case Sensor.TYPE_PROXIMITY: {
                float distance = sensorEvent.values[0];
                textView.setText(getResources().getString(R.string.proximity_sensor_label, distance));
                break;
            }
            case Sensor.TYPE_LINEAR_ACCELERATION: {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                textView.setText(getResources().getString(R.string.linear_acceleration_sensor_label, x, y, z));
                break;
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("onAccuracyChanged");
    }
}
