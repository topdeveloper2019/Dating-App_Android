package com.example.drago.vetroapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drago.vetroapp.adapters.CountriesListAdapter;

public class CountryFilterActivity extends AppCompatActivity implements View.OnClickListener {
    private  ListView listView = null;
    private ImageView navigationImage;
    private TextView doneText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_filter);
        Toolbar toolbar = findViewById(R.id.filter_toolbar);
        String[] recourseList=this.getResources().getStringArray(R.array.CountryCodes);

        navigationImage = findViewById(R.id.country_nativigation);
        doneText = findViewById(R.id.doneTextView);
        listView = (ListView) findViewById(R.id.countryListView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ModeCallback());
        listView.setAdapter(new CountriesListAdapter(this, recourseList));
        navigationImage.setOnClickListener(this);
        doneText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.country_nativigation:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.doneTextView:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
            default:
                break;
        }
    }

    private class ModeCallback implements ListView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
            final int checkedCount = listView.getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Select Items");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.share:
                    Toast.makeText(CountryFilterActivity.this, "Shared " + listView.getCheckedItemCount() +
                            " items", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    break;
                default:
                    Toast.makeText(CountryFilterActivity.this, "Clicked " + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {

        }
    }
}
