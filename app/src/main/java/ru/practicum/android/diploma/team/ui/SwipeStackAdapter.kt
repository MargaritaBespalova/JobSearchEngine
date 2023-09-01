package ru.practicum.android.diploma.team.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ru.practicum.android.diploma.R

class SwipeStackAdapter(
    private val mData: List<String>
) : BaseAdapter() {

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): String {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cardView = convertView ?: inflater.inflate(R.layout.card, parent, false)
        val textViewCard = cardView.findViewById<TextView>(R.id.textViewCard)
        textViewCard.text = mData[position]

        return cardView
    }
}
