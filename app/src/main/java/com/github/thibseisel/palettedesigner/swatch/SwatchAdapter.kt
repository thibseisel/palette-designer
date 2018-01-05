package com.github.thibseisel.palettedesigner.swatch

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.inflate

class SwatchAdapter(
        private val selectionListener: (Int) -> Unit
) : RecyclerView.Adapter<SwatchAdapter.SwatchHolder>() {

    private val items = ArrayList<LabeledSwatch>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwatchHolder {
        return SwatchHolder(parent).also {
            it.attachListeners(selectionListener)
        }
    }

    override fun onBindViewHolder(holder: SwatchHolder, position: Int) =
            holder.bind(items[position])

    operator fun get(position: Int) = items[position]

    fun updateWith(swatches: List<LabeledSwatch>) {
        val callback = LabeledSwatchDiff(items, swatches)
        val diff = DiffUtil.calculateDiff(callback)
        items.clear()
        items += swatches
        diff.dispatchUpdatesTo(this)
    }

    class SwatchHolder(
            parent: ViewGroup
    ) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_swatch)) {

        private val swatchView = itemView.findViewById<SwatchView>(R.id.swatchView)

        fun attachListeners(listener: (Int) -> Unit) {
            itemView.setOnClickListener { _ ->
                listener(adapterPosition)
            }
        }

        fun bind(swatch: LabeledSwatch) {
            swatchView.setLabel(swatch.label)
            swatchView.setSwatch(swatch.swatch)
        }
    }
}

class LabeledSwatchDiff(
        private var oldItems: List<LabeledSwatch>,
        private var newItems: List<LabeledSwatch>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldItems[oldItemPosition]
        val new = newItems[newItemPosition]
        // If labels are the same, then its the same kind of color swatch
        return old.label == new.label
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldItems[oldItemPosition]
        val new = newItems[newItemPosition]

        val sameLabel = old.label == new.label
        val sameSwatch = old.swatch == new.swatch

        return sameLabel && sameSwatch
    }

}