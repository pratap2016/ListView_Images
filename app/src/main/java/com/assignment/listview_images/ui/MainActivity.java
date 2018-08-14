package com.assignment.listview_images.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import com.assignment.listview_images.R;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.tv_tool_bar_title) AppCompatTextView tv_Heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ui();

    }

    /**
     * Initializing all UI components
     */
    private void ui() {
        initViews();
    }


    /**
     * Initializing all views
     */
    private void initViews() {
        // Setting Toolbar to action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addOrReplaceFragment(R.id.fragment_main, new MainFragment(), MainFragment.class.getName());
    }

    /**
     *  Adding or replacing fragment for UI related operations
     * @param fragment_main
     * @param mainFragment
     * @param name
     */
    private void addOrReplaceFragment(int fragment_main, MainFragment mainFragment, String name) {

        if(getFragmentManager().getBackStackEntryCount() == 0){
            getFragmentManager()
                    .beginTransaction()
                    .add(fragment_main, mainFragment, name)
                    .disallowAddToBackStack()
                    .commit();
        }else{
            getFragmentManager()
                    .beginTransaction()
                    .replace(fragment_main, mainFragment, name)
                    .disallowAddToBackStack()
                    .commit();
        }

    }




    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() >0){
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
