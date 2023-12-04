package stockListFragment

import android.view.LayoutInflater
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Switch
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetomobiledef.R
import com.example.projetomobiledef.retrofit.SymbolSummary
import com.example.projetomobiledef.SharedPreferencesHelper
import com.squareup.picasso.Picasso

class EditStockListRecyclerAdapter(
    private val dataList: MutableList<SymbolSummary>,
    private val listener: OnSymbolToggleListener
) : RecyclerView.Adapter<EditStockListRecyclerAdapter.ItemViewHolder>() {

    interface OnSymbolToggleListener {
        fun onSymbolToggle(symbol: SymbolSummary, isChecked: Boolean)
    }


    class ItemViewHolder(itemView: View,  ) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: Switch = itemView.findViewById(R.id.button)

        val logoImageView:AppCompatImageView=itemView.findViewById(R.id.ivStocksList_logo)
        val stockName:TextView=itemView.findViewById(R.id.tvSymbol_List)
        val stockPrice:TextView=itemView.findViewById(R.id.tvSymbolPrice_list)
        val stockPercent:TextView=itemView.findViewById(R.id.tvSymbolPercent_List)

        init{
            itemView.setOnClickListener{
            }

        }
    }

    fun setData(data: List<SymbolSummary>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }
    // Inflating the item layout and returning the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stock_card, parent, false)
        return ItemViewHolder(itemView)
    }

    // Binding the data to the views
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        dataList[position].logo_url.let{
            Picasso.get().load(it).into(holder.logoImageView)
        }
        holder.stockName.text = dataList[position].symbol
        holder.stockPrice.text = dataList[position].current_price.toString()
        holder.stockPercent.text = dataList[position].change_percent.toString()

        val isSymbolSaved = SharedPreferencesHelper.loadSymbols(holder.itemView.context).contains(dataList[position])

        holder.nameTextView.setOnCheckedChangeListener(null)

        holder.nameTextView.isChecked = isSymbolSaved

        // Adicione um Handler para postar a notificação fora do ciclo de vida do RecyclerView
        Handler().post {
            holder.nameTextView.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // adicionar o símbolo às preferências compartilhadas enquanto o switch está ativado
                    SharedPreferencesHelper.addSymbol(dataList[position], holder.itemView.context)
                } else {
                    // remover o símbolo das preferências compartilhadas enquanto o switch está desativado
                    SharedPreferencesHelper.removeSymbol(dataList[position], holder.itemView.context)
                    // Notify the listener that the symbol should be removed
                    listener.onSymbolToggle(dataList[position], false)
                }
                notifyItemChanged(position)
            }
        }
    }

    // Return the size of your dataset
    override fun getItemCount(): Int {
        return dataList.size
    }

    interface  onItemClickListener{
        fun onItemClick(position: Int)
    }


}