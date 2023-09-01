package ru.practicum.android.diploma.team.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.practicum.android.diploma.R;

public class TestActivity extends AppCompatActivity implements SwipeStack.SwipeStackListener, View.OnClickListener {

    private Button mButtonLeft, mButtonRight;
    private FloatingActionButton mFab;

    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeStack = findViewById(R.id.swipeStack);
        mFab = findViewById(R.id.fabAdd);

        mFab.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        fillWithTestData();
    }

    private void fillWithTestData() {
        mData.add(getString(R.string.lead));
        mData.add(getString(R.string.oldwise));
        mData.add(getString(R.string.flash));
        mData.add(getString(R.string.meister));
        mData.add(getString(R.string.bitwise));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mButtonLeft)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mButtonRight)) {
            mSwipeStack.swipeTopViewToRight();
        } else if (v.equals(mFab)) {
            mData.add("getString(R.string.dummy_fab)");
            mAdapter.notifyDataSetChanged();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return true;
//    }

    //@Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.menuRes:
//                mSwipeStack.resetStack();
//                Snackbar.make(mFab, "R.string.stack_reset", Snackbar.LENGTH_SHORT).show();
//                return true;
//            case R.id.menuGit:
//                Snackbar.make(mFab, "R.string.stack_reset", Snackbar.LENGTH_SHORT).show();
//                return true;
//            default:
//                Snackbar.make(mFab, "R.string.stack_reset", Snackbar.LENGTH_SHORT).show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, "Like",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, "Like",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(this, "The end", Toast.LENGTH_SHORT).show();
    }

    public class SwipeStackAdapter extends BaseAdapter {

        int[] drawableIds = {
                R.drawable.rectangle1,
                R.drawable.rectangle2,
                R.drawable.rectangle3,
                R.drawable.rectangle4,
                R.drawable.rectangle5,
                R.drawable.rectangle6
        };
        Random random = new Random();
        int randomIndex = random.nextInt(drawableIds.length);
        int randomDrawableId = drawableIds[randomIndex];


        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.card, parent, false);
            }

            TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
            var x = mData.get(position);
            textViewCard.setText(mData.get(position));
            textViewCard.setBackgroundResource(R.drawable.rectangle1);

            return convertView;
        }
    }
}
