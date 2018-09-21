package com.example.maheshpujala.dummy.adapter;

import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maheshpujala.dummy.R;
import com.example.maheshpujala.dummy.model.OrderEntity;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by maheshpujala on 20,July,2018
 */

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<OrderEntity> allOrders;
    private static final int UNSELECTED = -1;
    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;

    public OrdersAdapter(List<OrderEntity> allOrders,RecyclerView recyclerView) {
        this.allOrders = allOrders;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order, viewGroup, false);
        return new OrdersAdapter.OrderItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,int position) {
        final OrderItemHolder orderItemHolder = (OrderItemHolder) viewHolder;
        final OrderEntity singleOrder = allOrders.get(viewHolder.getAdapterPosition());

        orderItemHolder.name.setText(singleOrder.getName());
        orderItemHolder.phone.setText(singleOrder.getPhone());
        orderItemHolder.email.setText(singleOrder.getEmail());
        orderItemHolder.quantity.setText(singleOrder.getQuantity());
        orderItemHolder.total.setText(singleOrder.getTotal());
        orderItemHolder.date.setText(singleOrder.getDate());


        boolean isSelected = viewHolder.getAdapterPosition() == selectedItem;
        orderItemHolder.expandableLayout.setExpanded(isSelected, false);

        orderItemHolder.cardHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderItemHolder  holder = (OrderItemHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandableLayout.collapse();
                }

                int position = viewHolder.getAdapterPosition();
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    orderItemHolder.expandableLayout.expand();
                    selectedItem = position;
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return allOrders.size();
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder {

        TextView name,phone,email,quantity,date,total;
        ExpandableLayout expandableLayout;
        MaterialCardView cardHolder;
        private OrderItemHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            email = view.findViewById(R.id.email);
            quantity = view.findViewById(R.id.quantity);
            date = view.findViewById(R.id.date);
            total = view.findViewById(R.id.total);
            expandableLayout = view.findViewById(R.id.expandable_layout);
            cardHolder = view.findViewById(R.id.card_holder);

        }
    }
}