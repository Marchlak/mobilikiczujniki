package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter adapter;

    private boolean subtitleVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensor_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateSubtitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String subtitle = null;
            if (subtitleVisible) {
                subtitle = getString(R.string.sensors_count, sensorList.size());
            }
            actionBar.setSubtitle(subtitle);
        }
    }
    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iconImageView;
        private TextView nameTextView;
        private Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(v -> {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SensorActivity.this);
                dialogBuilder.setMessage(sensor.getVendor() + "\n\n" + sensor.getMaximumRange())
                        .setTitle(R.string.sensor_details_alert_title)
                        .setPositiveButton("Ok", (dialog, id) -> {

                        });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                return true;
            });

            iconImageView = itemView.findViewById(R.id.sensor_icon);
            nameTextView = itemView.findViewById(R.id.sensor_name);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            iconImageView.setImageResource(R.drawable.ic_sensor);
            nameTextView.setText(sensor.getName());
            if(sensor.getType() == Sensor.TYPE_GRAVITY || sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                nameTextView.setTextColor(Color.GREEN);
            }
            else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                nameTextView.setBackgroundColor(Color.GREEN);
            } else {
                nameTextView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onClick(View v) {
            if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                intent.putExtra("sensor_index", sensorList.indexOf(sensor));
                startActivity(intent);
            }
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {

        private List<Sensor> sensors;

        public SensorAdapter(List<Sensor> sensors) {
            this.sensors = sensors;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SensorActivity.this);
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }
}