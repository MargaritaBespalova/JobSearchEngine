package ru.practicum.android.diploma.team.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.practicum.android.diploma.R

class TestActivity : AppCompatActivity(), SwipeStack.SwipeStackListener, View.OnClickListener {
    private var mButtonLeft: Button? = null
    private var mButtonRight: Button? = null
    private var mFab: FloatingActionButton? = null
    private var mData: ArrayList<String>? = null
    private var mSwipeStack: SwipeStack? = null
    private var mAdapter: SwipeStackAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSwipeStack = findViewById<SwipeStack>(R.id.swipeStack)
        mButtonLeft = findViewById<Button>(R.id.buttonSwipeLeft)
        mButtonRight = findViewById<Button>(R.id.buttonSwipeRight)
        mFab = findViewById<FloatingActionButton>(R.id.fabAdd)
        mButtonLeft!!.setOnClickListener(this)
        mButtonRight!!.setOnClickListener(this)
        //mFab.setOnClickListener { }
        mData = ArrayList()
        mAdapter = SwipeStackAdapter(mData!!)
        mSwipeStack!!.adapter = mAdapter
        mSwipeStack!!.setListener(this)
        fillWithTestData()
    }

    private fun fillWithTestData() {
        for (x in 0..4) {
            mData!!.add("getString(R.string.dummy_text)" + " " + (x + 1))
        }
    }

    override fun onClick(v: View) {
        if (v == mButtonLeft) {
            mSwipeStack!!.swipeTopViewToLeft()
        } else if (v == mButtonRight) {
            mSwipeStack!!.swipeTopViewToRight()
        } else if (v == mFab) {
            mData!!.add("getString(R.string.dummy_fab)")
            mAdapter!!.notifyDataSetChanged()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = getMenuInflater()
//        inflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menuReset -> {
//                mSwipeStack!!.resetStack()
//                Snackbar.make(mFab, R.string.stack_reset, Snackbar.LENGTH_SHORT).show()
//                return true
//            }
//
//            R.id.menuGitHub -> {
//                val browserIntent = Intent(
//                    Intent.ACTION_VIEW, Uri.parse("https://github.com/flschweiger/SwipeStack")
//                )
//                startActivity(browserIntent)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onViewSwipedToRight(position: Int) {
        val swipedElement = mAdapter!!.getItem(position)
        Toast.makeText(
            this, "getString(R.string.view_swiped_right, swipedElement)",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onViewSwipedToLeft(position: Int) {
        val swipedElement = mAdapter!!.getItem(position)
        Toast.makeText(
            this, "getString(R.string.view_swiped_left, swipedElement)",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStackEmpty() {
        Toast.makeText(this, "R.string.stack_empty", Toast.LENGTH_SHORT).show()
    }

    inner class SwipeStackAdapter(private val mData: List<String>) :
        BaseAdapter() {
        override fun getCount(): Int {
            return mData.size
        }

        override fun getItem(position: Int): String {
            return mData[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var convertView: View? = convertView
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.card, parent, false)
            }
            val textViewCard = convertView!!.findViewById<View>(R.id.textViewCard) as TextView
            textViewCard.text = mData[position]
            return convertView!!
        }
    }
}