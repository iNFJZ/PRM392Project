package com.example.prm392project.presentation.store.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392project.R;
import com.example.prm392project.control.SharePreferenceManager;
import com.example.prm392project.model.ItemCart;

import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.ViewHolder> {

    private Context context;
    private List<ItemCart> poArrayList;
    private OnClickListener onClickListener;
    private IOnClickQuantity iOnClickQuantity;

    public ItemCartAdapter(Context context, List<ItemCart> poArrayList, IOnClickQuantity iOnClickQuantity) {
        this.context = context;
        this.poArrayList = poArrayList;
        this.iOnClickQuantity = iOnClickQuantity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.item_produtc_cart, parent, false);
        return new ViewHolder(productView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCart p = poArrayList.get(position);
        Glide.with(context).load(p.getImg()).into(holder.poImage);
        holder.poName.setText(p.getName());
        holder.poPrice.setText(String.valueOf(p.getPrice()));
        holder.poNumber.setText(String.valueOf(p.getQuantity()));
        holder.ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                p.setQuantity(p.getQuantity() - 1);
                if(p.getQuantity() < 1){
                    p.setQuantity(1);
                }
                holder.poNumber.setText(String.valueOf(p.getQuantity()));

                List<ItemCart> poList = SharePreferenceManager.getItems(context);

                for (ItemCart i : poList
                ) {
                    if (p.ProductId == i.ProductId) {
                        i.setQuantity(p.getQuantity());
                    }
                }
                SharePreferenceManager.saveItems(context, poList);
                iOnClickQuantity.onClickListener();
            }
        });

        holder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setQuantity(p.getQuantity() + 1);

                holder.poNumber.setText(String.valueOf(p.getQuantity()));

                List<ItemCart> poList = SharePreferenceManager.getItems(context);

                for (ItemCart i : poList
                ) {
                    if (p.ProductId == i.ProductId) {
                        i.setQuantity(p.getQuantity());
                    }
                }
                SharePreferenceManager.saveItems(context, poList);
                iOnClickQuantity.onClickListener();
            }
        });

    }

    public interface OnClickListener {
        //void onClick(int position, ProductOrder po);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return poArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poImage;
        private final TextView poName;
        private final TextView poPrice;
        private final ImageButton ibMinus;
        private final ImageButton ibPlus;
        private EditText poNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poImage = itemView.findViewById(R.id.po_image);
            poName = itemView.findViewById(R.id.po_name);
            poPrice = itemView.findViewById(R.id.po_price);
            poNumber = itemView.findViewById(R.id.po_count);
            ibMinus = itemView.findViewById(R.id.minus_po);
            ibPlus = itemView.findViewById(R.id.bonus_po);

        }
    }
}