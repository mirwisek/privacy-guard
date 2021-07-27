package com.fyp.privacyguard.core

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fyp.privacyguard.databinding.ItemGridBinding

class GridAdapter(private val context: Context, private val onClick: (entry: GridEntry) -> Unit) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    private var list: List<GridEntry>? = null

    inner class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemGridBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {

        val binding = ItemGridBinding.inflate(LayoutInflater.from(context), parent, false)
        return GridViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        with(holder) {
            list?.let { items ->
                with(items[position]) {
                    Glide.with(context).load(icon).into(binding.image)
                    binding.title.text = title
                    binding.card.setOnClickListener {
                        onClick.invoke(this)
                    }
                    binding.card.setCardBackgroundColor(Color.parseColor(background))
                }
            }
        }
    }

    fun updateList(list: List<GridEntry>) {
        this.list = list
        notifyDataSetChanged()
    }

}