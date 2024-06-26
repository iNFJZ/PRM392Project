package com.example.prm392project.presentation.store.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392project.R;
import com.example.prm392project.common.api.ApiService;
import com.example.prm392project.control.SharePreferenceManager;
import com.example.prm392project.databinding.FragmentCheckoutBinding;
import com.example.prm392project.model.Cart;
import com.example.prm392project.model.CartDetail;
import com.example.prm392project.model.ItemCart;
import com.example.prm392project.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutFragment extends Fragment {
    private FragmentCheckoutBinding binding;
    User userLogin;
    private ItemCartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.back.setOnClickListener(it -> getParentFragmentManager().popBackStack());
        List<ItemCart> poList = SharePreferenceManager.getItems(requireContext());
        long total = 0;
        for (ItemCart i : poList
        ) {
            total += i.price * i.quantity;
        }
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("TOTAL_BILL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("totalMoney", String.valueOf(total));
        editor.commit();
        binding.txtTongtien.setText("Tổng tiền: " + String.valueOf(total) + "VND");
        userLogin = getUserLoggedIn();
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferencesPrice = requireActivity().getSharedPreferences("TOTAL_BILL", Context.MODE_PRIVATE);
                String price = sharedPreferences.getString("totalMoney", "0");

                Cart c = new Cart();
                c.setAddress(binding.edtAddress.getText().toString());
                c.setNote(binding.edtNote.getText().toString());
                c.setPhone(binding.edtPhone.getText().toString());
                c.setPrice(Double.valueOf(price));
                //c.setOrderDate(Instant.now());

                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("USER_TOKEN", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                ApiService.apiService.addOrder(c, "Bearer " + token)
                        .enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if(response.body() != null){
                                    Cart cart = response.body();
                                    addOrderDetail(cart);
                                    Toast.makeText(requireContext(), "Đặt hàng thành công.", Toast.LENGTH_SHORT).show();SharePreferenceManager.clearItems(requireContext());
                                    SharePreferenceManager.clearItems(requireContext());
                                    FragmentManager fm = requireActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction = fm.beginTransaction().setCustomAnimations(
                                            R.anim.slide_in,  // enter
                                            R.anim.fade_out,  // exit
                                            R.anim.fade_in,   // popEnter
                                            R.anim.slide_out  // popExit
                                    );
                                    transaction.replace(R.id.wrapper, new CartFragment(), null).addToBackStack(null).commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {
                                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private User getUserLoggedIn() {

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("USER_TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        ApiService.apiService.getLoggedInUserProfile("Bearer " + token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            userLogin = response.body();
                            binding.edtName.setText(userLogin.getUsername());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
        return userLogin;
    }

    private void addOrderDetail(Cart cart){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("USER_TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        List<ItemCart> itemCarts = SharePreferenceManager.getItems(requireContext());
        for (ItemCart i: itemCarts) {
            CartDetail detail = new CartDetail();
            detail.setPrice((int) (i.getPrice() * i.getQuantity()));
            detail.setQuantity(i.getQuantity());

            ApiService.apiService.addCartDetails(detail, cart.getId(), i.ProductId, "Bearer " + token)
                    .enqueue(new Callback<CartDetail>() {
                        @Override
                        public void onResponse(Call<CartDetail> call, Response<CartDetail> response) {

                        }

                        @Override
                        public void onFailure(Call<CartDetail> call, Throwable t) {

                        }
                    });
        }
    }
}
