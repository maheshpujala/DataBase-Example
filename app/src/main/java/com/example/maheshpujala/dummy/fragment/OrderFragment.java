package com.example.maheshpujala.dummy.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.maheshpujala.dummy.R;
import com.example.maheshpujala.dummy.adapter.OrdersAdapter;
import com.example.maheshpujala.dummy.database.OrderDatabase;
import com.example.maheshpujala.dummy.model.OrderEntity;
import com.example.maheshpujala.dummy.network.RequestModel;
import com.example.maheshpujala.dummy.network.ResponseModel;
import com.example.maheshpujala.dummy.network.ServerRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by maheshpujala on 19,July,2018
 */
public class OrderFragment extends Fragment {

    View rootView;
    private int URL_ONE = 1;
    RecyclerView recyclerView;
    public OrderFragment() {
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);
        getOrdersList();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rootView = view.findViewById(android.R.id.content);
        recyclerView = view.findViewById(R.id.recycler_view_orders);

    }

    private void setRecyclerView(List<OrderEntity> allOrders) {
        if(allOrders.size() > 0){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new OrdersAdapter(allOrders,recyclerView));
        Log.e("setRecyclerView", "=="+allOrders.size());

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && gestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildAdapterPosition(child);

                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
        }else{
            Snackbar.make(rootView,getString(R.string.connect_to_internet),Snackbar.LENGTH_LONG).show();
        }
    }

    private void getOrdersList() {
        RequestModel requestModel = new RequestModel(getString(R.string.url_one),"", Request.Method.GET);
        new ServerRequest(getActivity()).sendRequest(requestModel,URL_ONE,true,"Loading");
    }

    @Subscribe
    public void ordersFetched(ResponseModel responseModel) {
        //Write code to perform action after event is received.
        if(responseModel.getRequestCallBack() == URL_ONE){
            if(responseModel.getStatusCode() == 200 || responseModel.getStatusCode() == 555){
                PopulateDbAsync task = new PopulateDbAsync(OrderFragment.this,responseModel.getPayload());
                task.execute();
            }else{
                Snackbar.make(rootView,getString(R.string.error_unknown),Snackbar.LENGTH_LONG).show();
            }
        }
    }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            menu.clear();
            getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);
            MenuItem mSearchMenuItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        Log.e("onQueryTextChange", "===="+newText);
                        //TODO do search functionality
                    }catch (Exception r) {
                        r.printStackTrace();
                    }
                    return true;
                }
            });
        }



        @Override
        public void onDestroyView() {
            super.onDestroyView();
            EventBus.getDefault().unregister(this);
        }



        private static class PopulateDbAsync extends AsyncTask<Void, Void, List<OrderEntity>> {

            private final OrderDatabase mDb;
            private String payLoad;
            private OrderFragment orderFragment;

            PopulateDbAsync(OrderFragment orderFragment, String payLoad) {
                this.payLoad = payLoad;
                this.orderFragment = orderFragment;
                mDb = OrderDatabase.getInstance(orderFragment.getActivity());
            }

            @Override
            protected List<OrderEntity> doInBackground(final Void... params) {
                try {
                    JSONArray ordersArray = new JSONArray(payLoad);
                    Log.e("doInBackground====", ordersArray.length()+"=="+payLoad);
                    for (int i=0; i < ordersArray.length(); i++) {
                        JSONObject singleOrder =  ordersArray.getJSONObject(i);

                        OrderEntity orderEntity = new OrderEntity();
                        orderEntity.setDate(singleOrder.getString("date"));
                        orderEntity.setEmail(singleOrder.getString("email"));
                        orderEntity.setName(singleOrder.getString("name"));
                        orderEntity.setOrderid(singleOrder.getString("orderid"));
                        orderEntity.setPhone(singleOrder.getString("phone"));
                        orderEntity.setQuantity(singleOrder.getString("quantity"));
                        orderEntity.setTotal(singleOrder.getString("total"));
                        Cursor cursor = mDb.orderDao().selectById(singleOrder.getString("orderid"));
                        if (cursor == null || !cursor.moveToFirst()) {
                            mDb.orderDao().insert(orderEntity);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return mDb.orderDao().getAllOrders();
            }

            @Override
            protected void onPostExecute(List<OrderEntity> allOrderList) {
                super.onPostExecute(allOrderList);
                orderFragment.setRecyclerView(allOrderList);
            }

        }


    }
